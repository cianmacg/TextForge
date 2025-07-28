package ie.atu.forge.Tokenisers;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.*;

record ByteSequence(byte[] bytes) {
    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(!(o instanceof ByteSequence)) return false;
        ByteSequence other = (ByteSequence) o;
        return Arrays.equals(this.bytes, other.bytes);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(bytes);
    }
}

public class BPE {
    private boolean trained = false;
    private final Map<Integer, ByteSequence> vocab = new HashMap<>();
    private final Map<ByteSequence, Integer> inverse_vocab = new HashMap<>();
    private int token_count = 256; // We will always initialise with 256 tokens.

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

        int count = vocab.size();
        int max_iter = vocab_size + 256 + 50; // 256 is the base vocab size (single characters), and the 50 is just to be safe.
        // Now that our vocabulary has been initialised, we can expand it by merging tokens together. Using count in place of vocab.size, as they should be the same anyway.
        while(count < max_iter) {
            int[] best_pair = find_best_pair(token_corpus);
            int token_id = add_token(best_pair);

            // Update the token corpus with the new merged token.
            token_corpus = merge_tokens(token_id, best_pair, token_corpus);
            count++;
        }

        trained = true;
    }

    private int[] find_best_pair(int[] corpus) {
        Map<Integer[], Integer> candidates = new HashMap<>();

        for(int i = 0; i < corpus.length - 1; i++) {
            candidates.merge(new Integer[]{corpus[i], corpus[i + 1]}, 1, Integer::sum);
        }


        if (candidates.isEmpty()) {
            // Nothing to compare, so return empty result
            return new int[0];
        }

        Integer[] tokens = Collections.max(candidates.entrySet(), Map.Entry.comparingByValue()).getKey();
        return Arrays.stream(tokens).mapToInt(Integer::intValue).toArray();
    }

    // Adds a new token to the maps and returns the integer representation.
    private int add_token(int[] old_tokens) {
        List<ByteSequence> new_bs = new ArrayList<>();
        int bs_length = 0;
        for (Integer token : old_tokens) {
            ByteSequence bs = vocab.get(token);
            new_bs.add(bs);
            bs_length += bs.bytes().length;
        }

        // Add all bytes from the merged tokens to a new ByteSequence to represent the new token.
        byte[] token_bytes = new byte[bs_length];
        int count = 0;
        for (ByteSequence bs : new_bs) {
            for (byte b : bs.bytes()) {
                token_bytes[count] = b;
                count++;
            }
        }

        ByteSequence new_token = new ByteSequence(token_bytes);

        vocab.put(token_count, new_token);
        inverse_vocab.put(new_token, token_count);
        token_count++;

        return token_count - 1;
    }

    private int[] merge_tokens(int merged_token, int[] old_tokens, int[] corpus) {
        int corpus_len = corpus.length;

        if(corpus_len == 0) return corpus;

        List<Integer> updated_corpus = new ArrayList<>();

        // Look over the corpus for places to merge the tokens.
        int i = 0;
        while(i < corpus_len - 1){
            if(corpus[i] == old_tokens[0] && corpus[i + 1] == old_tokens[1]) {
                updated_corpus.add(merged_token);
                i+=2;
            }
            else {
                updated_corpus.add(corpus[i]);
                i++;
            }
        }

        // Did we merge the last token? Or does it need to be added?
        if(updated_corpus.isEmpty() || !(updated_corpus.getLast() == merged_token)) updated_corpus.add(corpus[i]);

        return updated_corpus.stream().mapToInt(Integer::intValue).toArray();
    }

    // Saves the current vocabulary (Inverse vocab) map to a JSON file. Path is the location to save the vocabulary.
    public void save_Json(String path) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(path + "bpe_vocab.json"));

        writer.write('{');
        writer.newLine();

        for(Map.Entry<ByteSequence, Integer> mapping: inverse_vocab.entrySet()) {
            ByteBuffer byteBuffer = ByteBuffer.wrap(mapping.getKey().bytes());
            CharBuffer charBuffer = StandardCharsets.UTF_8.decode(byteBuffer);
            char[] key = new char[charBuffer.remaining()];
            charBuffer.get(key);
            System.out.println(key);
            writer.write("'" + Arrays.toString(key) + "'"); // Write the key
            writer.write(':');
            writer.write(' ');
            writer.write(String.valueOf(mapping.getValue())); // Write the value
            writer.write(',');
            writer.newLine();
        }

        writer.write('}');
        writer.close();
    }

    public void save_Json() throws IOException {
        save_Json("");
    }
}