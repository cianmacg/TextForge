package ie.atu.forge.Tokenisers;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.StructuredTaskScope;

// Byte-pair encoding tokenisation. Begins with training of a corpus to develop a vocabulary.
public class BPE {
    Set<String> vocabulary;
    boolean trained = false;

    public void train(String[] corpus, int vocab_size) {
        vocabulary = new HashSet<>();

        for(String text: corpus) {
            char[] chars = text.toCharArray();

            for(char c: chars) {
                vocabulary.add(c+"");
            }
        }

        int max_iter = vocab_size + 100;
        int iter = 0;
        trained = true; // Putting this here for now, as otherwise exceptions are thrown during training, need to find a better solution.

        while(vocabulary.size() < vocab_size && iter < max_iter) {
            expand(corpus);
            iter++;
        }


    }

    private void expand(String[] corpus) {
        Map<String, Integer> candidates = new HashMap<>();

        for(String text: corpus) {
            String[] tokens = tokenise(text);

            for(int i = 0; i < tokens.length - 1; i++) {
                candidates.merge(tokens[i] + tokens[i + 1], 1, Integer::sum);
            }
        }

        if(candidates.isEmpty()) return;

        System.out.println("Attempting to add: " + Collections.max(candidates.entrySet(), Map.Entry.comparingByValue()).getKey());
        vocabulary.add(Collections.max(candidates.entrySet(), Map.Entry.comparingByValue()).getKey());
    }

    /*private void expand(String[] corpus) {
        ConcurrentMap<String, Integer> candidates = new ConcurrentHashMap<>();

        try(var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            for(String text: corpus) {
                scope.fork(() -> {
                    String[] tokens = tokenise(text);
                    for(int i = 0; i<tokens.length - 1; i++) {
                        candidates.merge(tokens[i] + tokens[i+1], 1, Integer::sum);
                    }
                    return null;
                });
            }

            scope.join();
            scope.throwIfFailed();
        } catch (Exception e) {
            System.err.println("Error: " + e);
        }

        if (candidates.isEmpty()) {
            return;
        }

        System.out.println("Attempting to add: " + Collections.max(candidates.entrySet(), Map.Entry.comparingByValue()).getKey());
        vocabulary.add(Collections.max(candidates.entrySet(), Map.Entry.comparingByValue()).getKey());
    }*/

    public Set<String> getVocabulary() {
        return vocabulary;
    }

    /*
    In my mind, searching for tokens for left to right (i.e. expanding a substring from the left to right) would be the quickest way to parse a string into tokens.
    However, I couldn't get this to work. It would generally fail to find tokens greater than 2 characters.
    As a result, this implementation starts with a substring the size of the full string, and removes a character from the right (shrinking the substring) until the substring appears
    in the token list (i.e. the substring is a token).
    It then repeats, with the start of the string now being the position after the end of the last token, until all tokens have been found.
     */
    public String[] tokenise(String text) {
        if(!trained) throw new IllegalStateException("Not trained.");

        ArrayList<String> tokens = new ArrayList<>();
        int start = 0;

        while (start < text.length()) {
            int end = text.length();

            while (end > start && !vocabulary.contains(text.substring(start, end))) {
                end--;
            }

            if (end == start) {
                end = start + 1;
            }

            tokens.add(text.substring(start, end));
            start = end;
        }


        return tokens.toArray(new String[0]);
    }
}
