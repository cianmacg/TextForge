package main.java.ie.atu.forge.Similarity.AlignmentFree;

import java.util.*;


/**
 * The cosine of the angle between two n-dimensional vectors in an n-dimensional space.
 *
 * <a href="https://dl.acm.org/doi/10.1145/361219.361220">Original Paper.</a>
 */
public class Cosine {

    /**
     * Calculates the cosine similarity between 2 vectors.
     *
     * @param v1 A subject integer vector.
     * @param v2 A query integer vector.
     * @return The cosine similarity between the 2 vectors (from 0.0 to 1.0).
     */
    public static double similarity(int[] v1, int[] v2) {
        if(v1.length == 0 || v2.length == 0) return 0.0d;

        double dotProd = 0.0d, mag1 = 0.0d, mag2 = 0.0d;

        // Vectors need to be the same length.
        if(v1.length != v2.length) throw new IllegalArgumentException("Vectors must be the same length: v1.length = " + v1.length + "; v2.length = " + v2.length);

        for(int i = 0; i < v1.length; i++) {
            int d1 = v1[i], d2 = v2[i];

            dotProd += d1 * d2;
            mag1 += d1 * d1;
            mag2 += d2 * d2;
        }

        return dotProd / Math.sqrt(mag1 * mag2);
    }

    /**
     * Calculates the cosine similarity between 2 vectors.
     *
     * @param v1 A subject double vector.
     * @param v2 A query double vector.
     * @return The cosine similarity between the 2 vectors (from 0.0 to 1.0).
     */
    public static double similarity(double[] v1, double[] v2) {
        if(v1.length == 0 || v2.length == 0) return 0.0d;

        double dotProd = 0.0d, mag1 = 0.0d, mag2 = 0.0d;

        // Vectors need to be the same length.
        if(v1.length != v2.length) throw new IllegalArgumentException("Vectors must be the same length: v1.length = " + v1.length + "; v2.length = " + v2.length);

        for(int i = 0; i < v1.length; i++) {
            double d1 = v1[i], d2 = v2[i];

            dotProd += d1 * d2;
            mag1 += d1 * d1;
            mag2 += d2 * d2;
        }

        return dotProd / Math.sqrt(mag1 * mag2);
    }

    // From maps, create count vectors.
    /**
     * Calculates the cosine similarity between 2 vectors. This function will create count vectors from the provided maps.
     *
     * @param m1 A mapping of Strings and their counts.
     * @param m2 A mapping of Strings and their counts.
     * @return The cosine similarity between the 2 maps (from 0.0 to 1.0).
     */
    public static double similarity(Map<String, Integer> m1, Map<String, Integer> m2) {
        if(m1.isEmpty() || m2.isEmpty()) return 0.0d;

        Set<String> vocabulary = new HashSet<>(m1.keySet());
        vocabulary.addAll(m2.keySet());

        int[] v1 = new int[vocabulary.size()], v2 = new int[vocabulary.size()];

        int i = 0;
        // Populate the count vectors.
        for(String token: vocabulary) {
            v1[i] = m1.getOrDefault(token, 0);
            v2[i] = m2.getOrDefault(token, 0);
            i++;
        }

        return similarity(v1, v2);
    }

    /**
     * The cosine distance between 2 vectors is simply 1 - their similarity.
     *
     * @param v1 A subject integer vector.
     * @param v2 A query integer vector.
     * @return The cosine distance between the 2 vectors (from 0.0 to 1.0).
     */
    public static double distance(int[] v1, int[] v2) {
        return 1 - similarity(v1, v2);
    }

    /**
     * The cosine distance between 2 vectors is simply 1 - their similarity.
     *
     * @param v1 A subject double vector.
     * @param v2 A query double vector.
     * @return The cosine distance between the 2 vectors (from 0.0 to 1.0).
     */
    public static double distance(double[] v1, double[] v2) {
        return 1 - similarity(v1, v2);
    }

    /**
     * The cosine distance between 2 vectors is simply 1 - their similarity. This function will create count vectors from the provided maps.
     *
     * @param m1 A mapping of Strings and their counts.
     * @param m2 A mapping of Strings and their counts.
     * @return The cosine distance between the 2 maps (from 0.0 to 1.0).
     */
    public static double distance(Map<String, Integer> m1, Map<String, Integer> m2) {
        // Distance is simply 1 - similarity.
        return 1 - similarity(m1, m2);
    }
}
