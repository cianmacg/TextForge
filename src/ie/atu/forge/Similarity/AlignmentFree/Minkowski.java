package ie.atu.forge.Similarity.AlignmentFree;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Minkowski {
    // Minkowshki takes an additional parameter 'p'. When 'p' is 1, the distance is Manhattan, 2 is Euclidean, and as it approaches infinity it becomes Chebyshev.
    public static double distance(Map<String, Integer> s1, Map<String, Integer> s2, double p) {
        if(p == 0.0d) {
            return -1d;
        }

        Set<String> vocabulary = new HashSet<>(s1.keySet());
        vocabulary.addAll(s2.keySet());

        double result = 0;

        for(String token: vocabulary) {
            int d1 = s1.getOrDefault(token, 0);
            int d2 = s2.getOrDefault(token, 0);

            result += Math.pow(Math.abs(d1 - d2), p);
        }

        result = Math.pow(result, 1/p);

        return result;
    }
}
