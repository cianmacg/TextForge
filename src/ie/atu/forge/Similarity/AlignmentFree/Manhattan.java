package ie.atu.forge.Similarity.AlignmentFree;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * The orthogonal distance between 2 points on a grid. Sums the absolute differences between the coordinates of the points.
 */
public class Manhattan {
    /**
     * Calculates the Manhattan distance between 2 vectors. Both vectors must be of the same length.
     *
     * @param v1 An integer vector.
     * @param v2 An integer vector.
     * @return The Manhattan distance between both vectors.
     */
    public static int distance(int[] v1, int[] v2) {
        if(v1.length != v2.length) throw new IllegalArgumentException("Vectors must be the same length: v1.length = " + v1.length + "; v2.length = " + v2.length);

        int result = 0;

        for(int i = 0; i < v1.length; i++) {
            result += Math.abs(v1[i] - v2[i]);
        }

        return result;
    }

    /**
     * Calculates the Manhattan distance between 2 vectors. Both vectors must be of the same length.
     *
     * @param v1 A double vector.
     * @param v2 A double vector.
     * @return The Manhattan distance between both vectors.
     */
    public static double distance(double[] v1, double[] v2) {
        if(v1.length != v2.length) throw new IllegalArgumentException("Vectors must be the same length: v1.length = " + v1.length + "; v2.length = " + v2.length);

        double result = 0;

        for(int i = 0; i < v1.length; i++) {
            result += Math.abs(v1[i] - v2[i]);
        }

        return result;
    }

    /**
     * Calculates the Manhattan distance between 2 vectors. This function will create count vectors from the provided maps.
     *
     * @param m1 A mapping of Strings to their counts.
     * @param m2 A mapping of Strings to their counts.
     * @return The Manhattan distance between both maps.
     */
    public static int distance(Map<String, Integer> m1, Map<String, Integer> m2) {
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
