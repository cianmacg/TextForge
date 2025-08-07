package ie.atu.forge.Tokenisers;

import java.util.Arrays;

// Word level N-gram tokenisation. For character N-grams, use Ngram.
public class Shingle {
    public static String[] tokenise(String input, int window) {
        String[] words = input.split("\\s+");


        if(words.length <= window) {
            return new String[0];
        }

        String[] tokens = new String[words.length - window + 1];

        for (int i = 0; i <= words.length - window; i++) {
            tokens[i] = String.join(" ", Arrays.copyOfRange(words, i, i + window));
        }

        return tokens;
    }
}