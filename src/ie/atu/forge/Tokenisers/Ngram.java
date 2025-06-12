package ie.atu.forge.Tokenisers;

public class Ngram {
    public static String[] tokenise(String input, int window) {
        String[] tokens = new String[input.length() - window + 1];

        int i = 0;
        while (i < input.length() - window) {
            tokens[i++] = input.substring(i, i + window);
        }

        return tokens;
    }
}
