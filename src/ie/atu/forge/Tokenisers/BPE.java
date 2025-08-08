package ie.atu.forge.Tokenisers;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.StructuredTaskScope;

record Pair(int first, int second) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pair)) return false;
        Pair other = (Pair) o;
        return (this.first == other.first) && (this.second == other.second);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }}

public class BPE {
    private boolean trained = false;
    private Map<Integer, ByteSequence> vocab = new HashMap<>();
    private Map<ByteSequence, Integer> inverseVocab = new HashMap<>();
    private int tokenCount = 256; // We will always initialise with 256 tokens.
    private final Map<Pair, Integer> pairFreq = new HashMap<>();

    public BPE() {
        // Initialise vocabulary with all byte representations of characters (UTF-8).
        for(int i = 0; i < 256; i++) {
            ByteSequence token = new ByteSequence(new byte[]{(byte) i});
            inverseVocab.put(token, i);
            vocab.put(i, token);
        }
    }

    public int[] encode(String text) {
        byte[] byteText = text.getBytes();
        int[] encoding = new int[byteText.length]; // Maximum possible size

        int start = 0, tokenCounter = 0; // Keep track of count to reduce 'encoding' size later.
        while (start < byteText.length) {
            int end = start + 1;
            ByteSequence goodSequence = new ByteSequence(new byte[]{byteText[start]}); // at least 1 byte always valid

            // Explore longer matches
            while (end <= byteText.length) {
                byte[] slice = Arrays.copyOfRange(byteText, start, end);
                ByteSequence candidate = new ByteSequence(slice);

                if (inverseVocab.containsKey(candidate)) {
                    goodSequence = candidate;  // Update the best valid match
                    end++;
                } else {
                    break;
                }
            }
            if(inverseVocab.get(goodSequence) == null) {
                throw new RuntimeException("Value at key does not exist in inverseVocab.");
            }
            // Output the best match found
            encoding[tokenCounter++] = inverseVocab.get(goodSequence);

            // Move start forward by the *length of the match*
            start += goodSequence.bytes().length;
        }

        int[] fitted_encoding = new int[tokenCounter];
        System.arraycopy(encoding, 0, fitted_encoding, 0, fitted_encoding.length);

        return fitted_encoding;
    }

    public String decode(int[] tokens) {
        List<Byte> tokenBytes = new ArrayList<>();

        for(int token: tokens) {
            byte[] bytes = vocab.get(token).bytes();

            for(byte b: bytes) {
                tokenBytes.add(b);
            }
        }

        byte[] stringBytes = new byte[tokenBytes.size()];

        for(int i = 0; i < stringBytes.length; i++) {
            stringBytes[i] = tokenBytes.get(i);
        }

        return new String(stringBytes, java.nio.charset.StandardCharsets.UTF_8);
    }

    // Should only ever be called once.
    // Vocabulary size will be the added to the size of the base vocabulary.
    public void train(String corpus, int vocabSize) {
        if(trained) return;

        int[] tokenCorpus = encode(corpus);

        // Initialise pair frequency counts.
        countPairs(tokenCorpus);

        int count = vocab.size();
        int maxIter = vocabSize + count; // 256 (count) is the base vocab size (single characters)
        // Now that our vocabulary has been initialised, we can expand it by merging tokens together. Using count in place of vocab.size, as they should be the same anyway.
        while(count < maxIter) {
            Pair best_pair = findBestPair(tokenCorpus);
            if(best_pair!=null) {
                int leftToken = best_pair.first(), rightToken = best_pair.second();
                int tokenId = addToken(leftToken, rightToken);

                // Update the token corpus with the new merged token.
                tokenCorpus = mergeTokens(tokenId, leftToken, rightToken, tokenCorpus);
                countPair(tokenId, leftToken, rightToken, tokenCorpus);
                count++;
            } else {   // If p is null, it means there are no pairs left to merge. We need to end here.
                System.out.println("No pairs left to be merged.");
                return;
            }
        }

        trained = true;
    }

    // Used to count initial pairs
    private void countPairs(int[] corpus) {
        int len = corpus.length;

        int i = 0;
        while(i < len - 1) {
            Pair pair = new Pair(corpus[i], corpus[i+1]);
            pairFreq.merge(pair, 1, Integer::sum);

            i++;
        }
    }

    // Once a pair has been merged, we can remove it from the pair_freq count, and count for the new token instead.
    private void countPair(int newToken, int leftToken, int rightToken, int[] corpus) {
        // No longer need the frequency count of the old pair, as we've already merged it.
        pairFreq.remove(new Pair(leftToken, rightToken));
        int len = corpus.length;
        if(len <= 1) return;
        // Count the pair frequencies for the new merged token.
        // Performing a check outside the loop for the first and last tokens can save us doing many checks inside the loop.
        if(corpus[0] == newToken) pairFreq.put(new Pair(newToken, corpus[1]), 1);
        if(corpus[len - 1] == newToken) pairFreq.merge(new Pair(corpus[len - 2], newToken), 1, Integer::sum);

        // We've already checked the first and last values, hence i = 1 and corpus_len - 2.
        int i = 1;
        while(i < len - 1) {
            if(corpus[i] == newToken) {
                pairFreq.merge(new Pair(corpus[i - 1], newToken), 1, Integer::sum);
                if(!(corpus[i + 1] == newToken)) pairFreq.merge(new Pair(newToken, corpus[i + 1]), 1, Integer::sum); // We'll catch this on the next loop. Don't want to double count.
            }

            i++;
        }
    }

    private Pair findBestPair(int[] corpus) {
        Pair bestPair = null;
        int count = 0;

        for(Map.Entry<Pair, Integer> pair: pairFreq.entrySet()) {
            if(pair.getValue() > count) {
                bestPair = pair.getKey();
                count = pair.getValue();
            }
        }

        return bestPair;
    }

    // Adds a new token to the maps and returns the integer representation.
    private int addToken(int leftToken, int rightToken) {
        // Merge the bytes from the old tokens.
        byte[] leftBytes = vocab.get(leftToken).bytes();
        byte[] rightBytes = vocab.get(rightToken).bytes();

        byte[] mergedBytes = new byte[leftBytes.length + rightBytes.length];

        System.arraycopy(leftBytes, 0, mergedBytes, 0, leftBytes.length);
        System.arraycopy(rightBytes, 0, mergedBytes, leftBytes.length, rightBytes.length);

        // Create a new ByteSequence for our new token, from the bytes of the old tokens.
        ByteSequence newToken = new ByteSequence(mergedBytes);

        vocab.put(tokenCount, newToken);
        inverseVocab.put(newToken, tokenCount);
        tokenCount++;

        return tokenCount - 1;
    }

    // Merge 2 tokens together and update the corpus.
    private int[] mergeTokens(int mergedToken, int leftToken, int rightToken, int[] corpus) {
        int len = corpus.length;
        if(len == 0) return corpus;

        int[] updatedCorpus = new int[corpus.length]; // maximum possible size;
        int outputIndex = 0;
        int i = 0;
        while(i < len - 1) { // -1 ensures we don't get an out-of-bounds exception while checking for rightToken
            if(corpus[i] == leftToken && corpus[i + 1] == rightToken) { // Time to merge. Add the new token in place of the left and right tokens.
                updatedCorpus[outputIndex] = mergedToken; // The position in the new array should be the same as the old array minus the merges up to this point.
                outputIndex++; // We just merged, update.
                i+=2; // Since both tokens are merged, we can skip a position in the corpus.
            } else {    // No merge occurred, add the left token and move on to the next position.
                updatedCorpus[outputIndex] = corpus[i];
                outputIndex++;
                i++;
            }
        }

        // Did we merge the last token? Or does it need to be added?
        if (i < len) {
            updatedCorpus[outputIndex] = corpus[i];
            outputIndex++;
        }

        int[] result = new int[outputIndex];
        System.arraycopy(updatedCorpus, 0, result, 0, result.length);

        return result;
    }

    public Map<Integer, ByteSequence> getVocab() {
        return vocab;
    }

    public Map<ByteSequence, Integer> getInverseVocab() {
        return inverseVocab;
    }


    // Saves the current vocabulary map to a JSON file. Path is the location to save the vocabulary. ByteSequences are represented in Hex.
    public void saveVocabToJsonHex(String path) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(path + "bpe_vocab_hex.json"));

        writer.write('{');

        boolean firstToken = true;
        for (Map.Entry<ByteSequence, Integer> mapping : inverseVocab.entrySet()) {
            byte[] bytes = mapping.getKey().bytes();

            // Convert each byte to a two-digit hex string
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02X ", b));  // uppercase hex, space-separated
            }

            // Remove trailing space
            if (!sb.isEmpty()) {
                sb.setLength(sb.length() - 1);
            }

            if(!firstToken) writer.write(",");
            else firstToken = false;

            // Write as: HEX_BYTES: VALUE
            writer.newLine();
            writer.write("\t\"" + mapping.getValue() + "\": \"" + sb + "\"");
        }

        writer.newLine();
        writer.write('}');
        writer.close();
    }

    public void saveVocabToJsonHex() throws IOException {
        saveVocabToJsonHex("");
    }

    public void saveVocabToJsonASCII(String path) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(path + "bpe_vocab_ascii.json"));
        writer.write('{');

        boolean firstToken = true;
        for (Map.Entry<ByteSequence, Integer> mapping : inverseVocab.entrySet()) {
            byte[] bytes = mapping.getKey().bytes();

            // Convert bytes directly to characters
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append((char) (b & 0xFF)); // Use unsigned byte value
            }

            if (!firstToken) writer.write(",");
            else firstToken = false;

            writer.newLine();
            writer.write("\t\"" + mapping.getValue() + "\": \"" + escapeJsonString(sb.toString()) + "\"");
        }

        writer.newLine();
        writer.write('}');
        writer.close();
    }
// Helper method to escape special JSON characters
    private static String escapeJsonString(String input) {
        return input.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t")
                .replace("\b", "\\b")
                .replace("\f", "\\f");
    }

    public void saveVocabToJsonASCII() throws IOException {
        saveVocabToJsonASCII("");
    }

    // Loads vocab from Json file. Will also populate inverse_vocab. Vocab has integer: hex. Where hex is the byte representation.
    public void loadVocabFromJsonHex(String path) throws IOException {
        if(trained) {
            System.out.println("Already trained.");
            return;
        }
        BufferedReader reader = new BufferedReader(new FileReader(path));

        ConcurrentMap<Integer, ByteSequence> vocabulary = new ConcurrentHashMap<>();
        ConcurrentMap<ByteSequence, Integer> inverseVocabulary = new ConcurrentHashMap<>();

        try(var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            reader.lines().forEach((line) -> {
                if(line.contains("{") || line.contains("}")) return;

                String[] parts = line.replaceAll("[^a-zA-Z0-9 :]", "").split(": ");

                int key = Integer.parseInt(parts[0]);
                ByteSequence bytes = new ByteSequence(parseHexToByte(parts[1]));

                vocabulary.put(key, bytes);
                inverseVocabulary.put(bytes, key); // Simply the other way around. Used for encoding.
            });

            scope.join().throwIfFailed();
            vocab = new HashMap<>(vocabulary);
            inverseVocab = new HashMap<>(inverseVocabulary);

            trained = true;
            reader.close();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private byte[] parseHexToByte(String hexCodes) {
        String[] hexes = hexCodes.split(" ");
        byte[] bytes = new byte[hexes.length];

        for(int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt(hexes[i], 16);
        }

        return bytes;
    }
}