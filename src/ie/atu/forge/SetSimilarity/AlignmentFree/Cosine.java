package ie.atu.forge.SetSimilarity.AlignmentFree;

import java.util.*;

public class Cosine {
    /*
    Unsure what I should be taking in here. A mapping of tokens to count of each text is a good starting point.
    I can easily create a vector representation from this, and the user can determine what the tokens are (words, ngrams, etc).
     */
    public static double similarity(Map<String, Integer> s1, Map<String, Integer> s2) {
        Set<String> vocabulary = new HashSet<>(s1.keySet());
        vocabulary.addAll(s2.keySet());

        double dotProd = 0.0d;
        double mag1 = 0.0d;
        double mag2 = 0.0d;

        for(String token: vocabulary) {
            int d1 = s1.getOrDefault(token, 0);
            int d2 = s2.getOrDefault(token, 0);

            dotProd += d1 * d2;
            mag1 += d1 * d1;
            mag2 += d2 * d2;
        }

        return dotProd / Math.sqrt(mag1 * mag2);
    }

    public static double distance(Map<String, Integer> b1, Map<String, Integer> b2) {
        // Distance is simply 1 - similarity.
        return 1 - similarity(b1, b2);
    }
}
