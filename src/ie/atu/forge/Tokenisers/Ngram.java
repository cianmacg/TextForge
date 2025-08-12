package ie.atu.forge.Tokenisers;

// Character level N-gram tokenisation. For word N-grams, use Shingle.'

/**
 * Character level N-gram tokensiation. For word N-gram, use Shingle.
 */
public class Ngram {
    /**
     * Splits text into character N-grams. The size of each N-gram will be the size of the specified window.
     *
     * @param input The text to be tokenised.
     * @param window The size of the N-grams returns
     * @return A String array of N-grams.
     */
    public static String[] tokenise(String input, int window) {
        String[] tokens = new String[input.length() - window + 1];

        if(input == null || input.length() < window || window <= 0) return new String[0];

        for (int i = 0; i <= input.length() - window; i++) {
            tokens[i] = input.substring(i, i + window);
        }

        return tokens;
    }
}