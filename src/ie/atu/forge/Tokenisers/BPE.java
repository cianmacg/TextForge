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
    private Map<ByteSequence, Integer> inverse_vocab = new HashMap<>();
    private int token_count = 256; // We will always initialise with 256 tokens.
    private final Map<Pair, Integer> pair_freq = new HashMap<>();

    public BPE() {
        // Initialise vocabulary with all byte representations of characters (UTF-8).
        for(int i = 0; i < 256; i++) {
            ByteSequence token = new ByteSequence(new byte[]{(byte) i});
            inverse_vocab.put(token, i);
            vocab.put(i, token);
        }
    }

    public int[] encode(String text) {
        byte[] byte_text = text.getBytes();
        int[] encoding = new int[byte_text.length]; // Maximum possible size

        int start = 0, token_count = 0; // Keep track of count to reduce 'encoding' size later.
        while (start < byte_text.length) {
            int end = start + 1;
            ByteSequence good_bs = new ByteSequence(new byte[]{byte_text[start]}); // at least 1 byte always valid

            // Explore longer matches
            while (end <= byte_text.length) {
                byte[] slice = Arrays.copyOfRange(byte_text, start, end);
                ByteSequence candidate = new ByteSequence(slice);

                if (inverse_vocab.containsKey(candidate)) {
                    good_bs = candidate;  // Update the best valid match
                    end++;
                } else {
                    break;
                }
            }

            // Output the best match found
            encoding[token_count++] = inverse_vocab.get(good_bs);

            // Move start forward by the *length of the match*
            start += good_bs.bytes().length;  // Assuming ByteSequence exposes the underlying byte[]
        }

        int[] fitted_encoding = new int[token_count];
        System.arraycopy(encoding, 0, fitted_encoding, 0, fitted_encoding.length);

        return fitted_encoding;
    }

    public String decode(int[] tokens) {
        List<Byte> token_bytes = new ArrayList<>();

        for(int token: tokens) {
            ByteSequence bs = vocab.get(token);

            for(byte b: bs.bytes()) {
                token_bytes.add(b);
            }
        }

        byte[] string_bytes = new byte[token_bytes.size()];

        for(int i = 0; i < string_bytes.length; i++) {
            string_bytes[i] = token_bytes.get(i);
        }

        return new String(string_bytes, java.nio.charset.StandardCharsets.UTF_8);
    }

    // Should only ever be called once.
    // Vocabulary size will be the added to the size of the base vocabulary.
    public void train(String corpus, int vocab_size) {
        if(trained) return;

        int[] token_corpus = encode(corpus);

        // Initialise pair frequency counts.
        countPairs(token_corpus);

        int count = vocab.size();
        int max_iter = vocab_size + count; // 256 (count) is the base vocab size (single characters)
        // Now that our vocabulary has been initialised, we can expand it by merging tokens together. Using count in place of vocab.size, as they should be the same anyway.
        while(count < max_iter) {
            Pair best_pair = findBestPair(token_corpus);
            if(best_pair!=null) {
                int left = best_pair.first(), right = best_pair.second();
                int token_id = addToken(left, right);

                // Update the token corpus with the new merged token.
                token_corpus = mergeTokens(token_id, left, right, token_corpus);
                countPair(token_id, left, right, token_corpus);
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
        int corpus_len = corpus.length;

        int i = 0;
        while(i < corpus_len - 1) {
            Pair p = new Pair(corpus[i], corpus[i+1]);
            pair_freq.merge(p, 1, Integer::sum);

            i++;
        }
    }

    // Once a pair has been merged, we can remove it from the pair_freq count, and count for the new token instead.
    private void countPair(int new_token, int old_1, int old_2, int[] corpus) {
        // No longer need the frequency count of the old pair, as we've already merged it.
        pair_freq.remove(new Pair(old_1, old_2));
        int corpus_len = corpus.length;
        if(corpus_len <= 1) return;
        // Count the pair frequencies for the new merged token.
        // Performing a check outside the loop for the first and last tokens can save us doing many checks inside the loop.
        if(corpus[0] == new_token) pair_freq.put(new Pair(new_token, corpus[1]), 1);
        if(corpus[corpus_len - 1] == new_token) pair_freq.merge(new Pair(corpus[corpus_len - 2], new_token), 1, Integer::sum);

        // We've already checked the first and last values, hence i = 1 and corpus_len - 2.
        boolean skip_next = false; // This is to prevent double counting.
        int i = 1;
        while(i < corpus_len - 1) {
            if(corpus[i] == new_token) {
                pair_freq.merge(new Pair(corpus[i - 1], new_token), 1, Integer::sum);
                if(!(corpus[i + 1] == new_token)) pair_freq.merge(new Pair(new_token, corpus[i + 1]), 1, Integer::sum); // We'll catch this on the next loop. Don't want to double count.
            }

            i++;
        }
    }

    private Pair findBestPair(int[] corpus) {
        Pair p = null;
        int count = 0;

        for(Map.Entry<Pair, Integer> pair: pair_freq.entrySet()) {
            if(pair.getValue() > count) {
                p = pair.getKey();
                count = pair.getValue();
            }
        }

        return p;
    }

    // Adds a new token to the maps and returns the integer representation.
    private int addToken(int old_1, int old_2) {
        // Merge the bytes from the old tokens.
        byte[] b1 = vocab.get(old_1).bytes();
        byte[] b2 = vocab.get(old_2).bytes();

        byte[] new_b = new byte[b1.length + b2.length];

        System.arraycopy(b1, 0, new_b, 0, b1.length);
        System.arraycopy(b2, 0, new_b, b1.length, b2.length);

        // Create a new ByteSequence for our new token, from the bytes of the old tokens.
        ByteSequence new_token = new ByteSequence(new_b);

        vocab.put(token_count, new_token);
        inverse_vocab.put(new_token, token_count);
        token_count++;

        return token_count - 1;
    }

    private int[] mergeTokens(int merged_token, int old_1, int old_2, int[] corpus) {
        int corpus_len = corpus.length;
        if(corpus_len == 0) return corpus;

        List<Integer> updated_corpus = new ArrayList<>(corpus_len);

        // Look over the corpus for places to merge the tokens.
        int i = 0;
        while(i < corpus_len - 1){
            if(corpus[i] == old_1 && corpus[i + 1] == old_2) {
                updated_corpus.add(merged_token);
                i+=2;
            }
            else {
                updated_corpus.add(corpus[i]);
                i++;
            }
        }

        // Did we merge the last token? Or does it need to be added?
        if (i < corpus_len) {
            updated_corpus.add(corpus[i]);
        }

        int[] result = new int[updated_corpus.size()];
        for (int j = 0; j < updated_corpus.size(); j++) {
            result[j] = updated_corpus.get(j);
        }

        return result;
    }

    public Map<Integer, ByteSequence> getVocab() {
        return vocab;
    }

    public Map<ByteSequence, Integer> getInverseVocab() {
        return inverse_vocab;
    }


    // Saves the current vocabulary map to a JSON file. Path is the location to save the vocabulary. ByteSequences are represented in Hex.
    public void vocabToJson(String path) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(path + "bpe_vocab.json"));

        writer.write('{');

        boolean first_token = true;
        for (Map.Entry<ByteSequence, Integer> mapping : inverse_vocab.entrySet()) {
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

            if(!first_token) writer.write(",");
            else first_token = false;

            // Write as: HEX_BYTES: VALUE
            writer.newLine();
            writer.write("\t\"" + mapping.getValue() + "\": \"" + sb + "\"");
        }

        writer.newLine();
        writer.write('}');
        writer.close();
    }

    public void vocabToJson() throws IOException {
        vocabToJson("");
    }

    // Saves the current vocabulary (Inverse vocab) map to a JSON file. Path is the location to save the vocabulary. ByteSequences are represented in Hex.
    public void inverseVocabToJson(String path) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(path + "bpe_inverse_vocab.json"));

        writer.write('{');

        boolean first_token = true;
        for (Map.Entry<ByteSequence, Integer> mapping : inverse_vocab.entrySet()) {
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

            if(!first_token) writer.write(",");
            else first_token = false;

            // Write as: HEX_BYTES: VALUE
            writer.newLine();
            writer.write("\t\"" + sb + "\": " + mapping.getValue());
        }

        writer.newLine();
        writer.write('}');
        writer.close();
    }

    public void inverseVocabToJson() throws IOException {
        inverseVocabToJson("");
    }


    // Loads vocab from Json file. Will also populate inverse_vocab. Vocab has integer: hex. Where hex is the byte representation.
    public void loadVocabFromJson(String path) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(path));

        ConcurrentMap<Integer, ByteSequence> vocabulary = new ConcurrentHashMap<>();
        ConcurrentMap<ByteSequence, Integer> inverse_vocabulary = new ConcurrentHashMap<>();

        try(var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            reader.lines().forEach((line) -> {
                if(line.contains("{") || line.contains("}")) return;

                String[] parts = line.replaceAll("[^a-zA-Z0-9 :]", "").split(": ");

                int key = Integer.parseInt(parts[0]);
                ByteSequence bytes = new ByteSequence(parseHexToByte(parts[1]));

                vocabulary.put(key, bytes);
            });

            scope.join().throwIfFailed();
            vocab = new HashMap<>(vocabulary);
            inverse_vocab = new HashMap<>(inverse_vocabulary);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private byte[] parseHexToByte(String hex_codes) {
        String[] hexes = hex_codes.split(" ");
        byte[] bytes = new byte[hexes.length];

        for(int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt(hexes[i]);
        }

        return bytes;
    }
}