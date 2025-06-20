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
        while(vocabulary.size() < vocab_size && iter < max_iter) {
            expand(corpus);
            iter++;
        }

        trained = true;
    }

    private void expand(String[] corpus) {
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
    }

    public Set<String> getVocabulary() {
        return vocabulary;
    }

    public String[] tokenise(String text) {
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


        return tokens.toArray(new String[tokens.size()]);
    }
}
