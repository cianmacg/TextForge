package ie.atu.forge.Similarity.AlignmentFree;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Manhattan {
    public static double distance(int[] v1, int[] v2) {
        if(v1.length != v2.length) return -1.0d;

        double result = 0.0d;

        for(int i = 0; i < v1.length; i++) {
            result += Math.abs(v1[i] - v1[i]);
        }

        return result;
    }


    public static double distance(Map<String, Integer> s1, Map<String, Integer> s2) {
        Set<String> vocabulary = new HashSet<>(s1.keySet());
        vocabulary.addAll(s2.keySet());

        int[] v1 = new int[vocabulary.size()], v2 = new int[vocabulary.size()];

        int i = 0;
        // Populate the count vectors.
        for(String token: vocabulary) {
            v1[i] = s1.getOrDefault(token, 0);
            v2[i] = s2.getOrDefault(token, 0);
        }

        return distance(v1, v2);
    }
}
