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

        int max_iter = 100;
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
                    int start = 0;

                    while(start<text.length() - 1) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(text.charAt(start));
                        while(vocabulary.contains(sb.toString()) && start < text.length() - 1) {
                            start++;
                            sb.append(text.charAt(start));
                        }

                        candidates.put(sb.toString(), candidates.computeIfAbsent(sb.toString(), k -> 0) + 1);
                    }


                    return null;
                });
            }

            scope.join();
            scope.throwIfFailed();
        } catch (Exception e) {
            System.err.println("Error: " + e);
        }

        vocabulary.add(Collections.max(candidates.entrySet(), Map.Entry.comparingByValue()).getKey());
    }

    public Set<String> getVocabulary() {
        return vocabulary;
    }

    public String[] tokenise(String text) {
        ArrayList<String> tokens = new ArrayList<>();

        int pos = 0;
        while(pos < text.length() - 1) {
            StringBuilder sb = new StringBuilder();
            sb.append(text.charAt(pos));

            while(vocabulary.contains(sb.toString() + text.charAt(pos + 1)) && pos < text.length() - 1) {
                pos++;
                sb.append(text.charAt(pos));
            }

            tokens.add(sb.toString());
            pos++;
        }

        return tokens.toArray(new String[tokens.size()]);
    }
}
