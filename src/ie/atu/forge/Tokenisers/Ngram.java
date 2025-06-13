package ie.atu.forge.Tokenisers;

// Character level N-gram tokenisation. For word N-grams, use Shingle.
public class Ngram {
    public static String[] tokenise(String input, int window) {
        String[] tokens = new String[input.length() - window + 1];

        int i = 0;
        while (i < input.length() - (window - 1)) {
            tokens[i] = input.substring(i, i + window);
            i++;
        }

        return tokens;
    }
}
