package ie.atu.forge.SetSimilarity.AlignmentFree;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Euclidean {
    public static double distance(Map<String, Integer> s1, Map<String, Integer> s2) {
        Set<String> vocabulary = new HashSet<>(s1.keySet());
        vocabulary.addAll(s2.keySet());

        double result = 0;

        for(String token: vocabulary) {
            int d1 = s1.getOrDefault(token, 0);
            int d2 = s2.getOrDefault(token, 0);

            result += (d1 - d2) * (d1 - d2);
        }

        return Math.sqrt(result);
    }
}
