package ie.atu.forge.Similarity.AlignmentFree;

import java.util.*;

public class Cosine {

    // Overloading similarity function to also allow uses to pass a pre-made count vector
    public static double similarity(int[] v1, int[] v2) {
        double dotProd = 0.0d, mag1 = 0.0d, mag2 = 0.0d;

        // Vectors need to be the same length.
        if(v1.length != v2.length) return -1.0d;

        for(int i = 0; i < v1.length; i++) {
            int d1 = v1[i], d2 = v2[i];

            dotProd += d1 * d2;
            mag1 += d1 * d1;
            mag2 += d2 * d2;
        }

        return dotProd / Math.sqrt(mag1 * mag2);
    }

    // From maps, create count vectors.
    public static double similarity(Map<String, Integer> s1, Map<String, Integer> s2) {
        Set<String> vocabulary = new HashSet<>(s1.keySet());
        vocabulary.addAll(s2.keySet());

        int[] v1 = new int[vocabulary.size()], v2 = new int[vocabulary.size()];

        int i = 0;
        // Populate the count vectors.
        for(String token: vocabulary) {
            v1[i] = s1.getOrDefault(token, 0);
            v2[i] = s2.getOrDefault(token, 0);
        }

        return similarity(v1, v2);
    }

    public static double distance(int[] v1, int[] v2) {
        return 1 - similarity(v1, v2);
    }

    public static double distance(Map<String, Integer> b1, Map<String, Integer> b2) {
        // Distance is simply 1 - similarity.
        return 1 - similarity(b1, b2);
    }
}
