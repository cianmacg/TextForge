package ie.atu.forge.Tokenisers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.StructuredTaskScope;

public class Unigram {
    private boolean trained = false;
    private int max_token_size = 1;
    private Set<String> tokens;
    private final Map<String, Integer> token_counts = new HashMap<>();
    private Map<String, Double> probs; // Token probabilities p(w)

    public Unigram(int token_limit) {
        max_token_size = token_limit;
    }

    public void train(String training_text, int max_vocab_size) {
        if(trained) return;

        tokens = new HashSet<>();
        String[] training_sentences = training_text.replaceAll(" ", "_").toLowerCase().split("\n"); // 'space' to '_' seems to be a convention.
        collect_tokens(training_sentences, max_token_size);

        // Initialize probabilities uniformly
        double initProb = 1.0 / tokens.size();
        probs = new HashMap<>();
        for (String token : tokens) {
            probs.put(token, initProb);
        }

//        while(tokens.size() > max_vocab_size) {
//            eStep(training_sentences);
//            mStep();
//            prune(training_sentences);
//        }
        trained = true;
    }

    // Should only ever be called once (during training).
    private void collect_tokens(String[] text, int max_token_size) {
        Map<String, Integer> tokens = new ConcurrentHashMap<>();

        try(var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            for(String sentence: text) {
                for(int i = 1; i <= max_token_size; i++) {
                    int token_size = i;
                    scope.fork(() -> {
                        Map<String, Integer> sentence_tokens = tokenise(sentence, token_size);
                        sentence_tokens.entrySet().forEach(entry -> tokens.merge(entry.getKey(), entry.getValue(), Integer::sum));
                        return null;
                    });
                }
            }

            scope.join();
            scope.throwIfFailed();

            token_counts.putAll(tokens);
        }   catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Map<String, Integer> tokenise(String text, int token_size) {
        Map<String, Integer> sentence_tokens = new HashMap<>();
        int text_len = text.length();

        int i = 0;
        while(i < text_len - token_size) {
            sentence_tokens.merge(text.substring(i, i+token_size), 1, Integer::sum);
            i++;
        }

        return sentence_tokens;
    }

//    private void eStep(String[] sentences) {
//        expectedCounts = new HashMap<>();
//    }
//
//    private void mStep() {
//
//    }
//
//    private void prune(String[] training_sentences) {
//
//    }
//
//    // Log-likelihood loss
//    private double calc_loss() {
//        double loss = 0.0;
//        return loss;
//    }
}
