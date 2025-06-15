package ie.atu.forge.Tokenisers;

// Word level N-gram tokenisation. For character N-grams, use Ngram.
public class Shingle {
    public static String[] tokenise(String input, int window) {
        String[] words = input.split("\\s+");
        String[] tokens = new String[words.length - window + 1];

        if(words.length <= window) {
            return tokens;
        }

        int i = 0;
        while(i <= words.length - window) {
            StringBuilder sb = new StringBuilder();

            for(int j = 0; j < (window - 1); j++) {
                sb.append(words[i+j]);
                sb.append(" ");
            }
            sb.append(words[i+window - 1]);
            tokens[i] = sb.toString();
            i++;
        }
        return tokens;
    }
}
