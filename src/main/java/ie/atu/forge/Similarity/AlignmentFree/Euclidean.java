package main.java.ie.atu.forge.Similarity.AlignmentFree;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * The straight-line distance between 2 points in any dimension. The square root of the sum of squared differences between the coordinates of the points. <br><br>
 * Applied on strings by converting them to vectors.
 */
public class Euclidean {
    /**
     * Calculates the straight line distance between 2 vectors.
     *
     * @param v1 An integer vector.
     * @param v2 An integer vector.
     * @return The distance between the provided vectors using Euclidean distance.
     */
    public static double distance(int[] v1, int[] v2) {
        if (v1.length != v2.length) throw new IllegalArgumentException("Vectors must be the same length: v1.length = " + v1.length + "; v2.length = " + v2.length);

        double result = 0.0d;

        for (int i = 0; i < v1.length; i++) {
            int d1 = v1[i];
            int d2 = v2[i];

            result += (d1 - d2) * (d1 - d2);
        }

        return Math.sqrt(result);
    }

    /**
     * Calculates the straight line distance between 2 vectors.
     *
     * @param v1  double vector.
     * @param v2 A double vector.
     * @return The distance between the provided vectors using Euclidean distance.
     */
    public static double distance(double[] v1, double[] v2) {
        if (v1.length != v2.length) throw new IllegalArgumentException("Vectors must be the same length: v1.length = " + v1.length + "; v2.length = " + v2.length);

        double result = 0.0d;

        for (int i = 0; i < v1.length; i++) {
            double d1 = v1[i];
            double d2 = v2[i];

            result += (d1 - d2) * (d1 - d2);
        }

        return Math.sqrt(result);
    }

    /**
     * Calculates the straight line distance between 2 vectors. This function will create count vectors from the provided maps.
     *
     * @param m1 A mapping of Strings and their counts.
     * @param m2 A mapping of Strings and their counts.
     * @return The distance between the created vectors using Euclidean distance.
     */
    public static double distance(Map<String, Integer> m1, Map<String, Integer> m2) {
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

        return distance(v1, v2);
    }
}
