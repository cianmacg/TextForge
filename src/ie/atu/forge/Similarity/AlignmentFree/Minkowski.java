package ie.atu.forge.Similarity.AlignmentFree;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A generalised distance metric that encompasses Manhattan, Euclidean, and Chebyshev distances as special cases.
 * Computes the p-th root of the sum of the absolute differences between coordinates to the power p.
 *
 * <a href="http://arxiv.org/abs/1605.04146">Original Paper.</a>
 */
public class Minkowski {
    /**
     * Calculates the Minkowski distance between 2 vectors. <br><br>
     * Special cases:<br>
     * p = 1: Manhattan Distance.<br>
     * p = 2: Euclidean Distance.<br>
     * p = ∞: Chebyshev Distance (Achieved in this case by setting p = -1).
     *
     * @param v1 An integer vector.
     * @param v2 An integer vector.
     * @param p Controls the degree of emphasis on larger versus smaller coordinate differences (large p-value increases the importance of the largest coordinates).
     * @return The Minkowski distance between both vectors.
     */
    public static double distance(int[] v1, int[] v2, double p) {
        if(v1.length != v2.length) throw new IllegalArgumentException("Vectors must be the same length: v1.length = " + v1.length + "; v2.length = " + v2.length);

        // A p value of 0 will cause a division by 0 error.
        if(p == 0.0d) {
            throw new IllegalArgumentException("P cannot be 0. Leads to division by 0 error.");
        }

        // If p is infinity, the similarity becomes Chebyshev. Since it is not possible to pass infinity, -1 will represent it.
        if(p == -1.0d) {
            return Chebyshev.distance(v1, v2);
        }

        double result = 0.0d;

        for(int i = 0; i < v1.length; i++) {
            result += Math.pow(Math.abs(v1[i] - v2[i]), p);
        }

        result = Math.pow(result, 1/p);

        return result;
    }
    /**
     * Calculates the Minkowski distance between 2 vectors. This function will create count vectors from the provided maps. <br><br>
     * Special cases:<br>
     * p = 1: Manhattan Distance.<br>
     * p = 2: Euclidean Distance.<br>
     * p = ∞: Chebyshev Distance (Achieved in this case by setting p = -1).
     *
     * @param m1 A mapping of Strings to their counts.
     * @param m2 A mapping of Strings to their counts.
     * @param p Controls the degree of emphasis on larger versus smaller coordinate differences (large p-value increases the importance of the largest coordinates).
     * @return The Minkowski distance between both vectors.
     */
    public static double distance(Map<String, Integer> m1, Map<String, Integer> m2, double p) {
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

        return distance(v1, v2, p);
    }
}
