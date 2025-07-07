package ie.atu.forge.Similarity.AlignmentFree;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Chebyshev {
    // Chebyshev only cares about the maximum absolute distance between a dimension.
    public static double distance(Map<String, Integer> s1, Map<String, Integer> s2) {
        Set<String> vocabulary = new HashSet<>(s1.keySet());
        vocabulary.addAll(s2.keySet());

        double result = 0;

        for(String token: vocabulary) {
            int d1 = s1.getOrDefault(token, 0);
            int d2 = s2.getOrDefault(token, 0);

            double difference = Math.abs(d1 - d2);

            // Only if the difference is greater than the current maximum do we care about it.
            if(difference > result) result = difference;
        }

        return result;
    }
}
