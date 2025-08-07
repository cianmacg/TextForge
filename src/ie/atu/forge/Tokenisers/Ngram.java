package ie.atu.forge.Tokenisers;

// Character level N-gram tokenisation. For word N-grams, use Shingle.
public class Ngram {
    public static String[] tokenise(String input, int window) {
        String[] tokens = new String[input.length() - window + 1];

        if(input == null || input.length() < window || window <= 0) return new String[0];

        for (int i = 0; i <= input.length() - window; i++) {
            tokens[i] = input.substring(i, i + window);
        }

        return tokens;
    }
}