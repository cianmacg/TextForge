package main.java.ie.atu.forge.Tokenisers;

import java.util.Arrays;

// Word level N-gram tokenisation. For character N-grams, use Ngram.

/**
 * Word level N-gram tokensiation. For character N-grams, use Ngram.
 */
public class Shingle {
    /**
     * Splits text into word N-grams. The number of words in each N-gram will be the size of the specified window.
     * @param input The text to be tokenised.
     * @param window The number of words to be in each token.
     * @return A String array containing the tokens.
     */
    public static String[] tokenise(String input, int window) {
        if(input == null || input.length() <= window || window <= 0) return new String[0];

        String[] words = input.strip().split("\\s+"); // Remove leading/trailing whitespace before splitting

        if(words.length < window) return new String[0];

        String[] tokens = new String[words.length - window + 1];

        for (int i = 0; i <= words.length - window; i++) {
            tokens[i] = String.join(" ", Arrays.copyOfRange(words, i, i + window));
        }

        return tokens;
    }
}