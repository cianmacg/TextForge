package main.java.ie.atu.forge.Similarity.AlignmentFree;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/**
 * A weighted version of Manhattan distance. Sums the absolute differences between coordinates divided by their summed absolute values.
 *
 * <a href="https://garfield.library.upenn.edu/classics1980/A1980HX93800001.pdf">Original Paper.</a>
 */
public class Canberra {
    /**
     * Calculates the Canberra distance between 2 vectors. Vectors must be of equal length.
     *
     * @param v1 An integer vector.
     * @param v2 An integer vector.
     * @return The Canberra distance between both vectors.
     */
    public static double distance(int[] v1, int[] v2) {
        if (v1.length != v2.length) throw new IllegalArgumentException("Vectors must be the same length: v1.length = " + v1.length + "; v2.length = " + v2.length);

        double result = 0.0d;

        for (int i = 0; i < v1.length; i++) {
            int d1 = v1[i];
            int d2 = v2[i];

            result += (double) Math.abs(d1 - d2) / (Math.abs(d1) + Math.abs(d2));
        }

        return result;
    }

    /**
     * Calculates the Canberra distance between 2 vectors. Vectors must be of equal length.
     *
     * @param v1 A double vector.
     * @param v2 A double vector.
     * @return The Canberra distance between both vectors.
     */
    public static double distance(double[] v1, double[] v2) {
        if (v1.length != v2.length) throw new IllegalArgumentException("Vectors must be the same length: v1.length = " + v1.length + "; v2.length = " + v2.length);

        double result = 0.0d;

        for (int i = 0; i < v1.length; i++) {
            double d1 = v1[i];
            double d2 = v2[i];

            result += Math.abs(d1 - d2) / (Math.abs(d1) + Math.abs(d2));
        }

        return result;
    }


    /**
     * Calculates the Canberra distance between 2 vectors. This function will create count vectors from the provided maps.
     *
     * @param m1 A mapping of Strings to their counts.
     * @param m2 A mapping of Strings to their counts.
     * @return The Canberra distance between both maps.
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
