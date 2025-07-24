package ie.atu.forge.Similarity.AlignmentFree;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class Minkowski {
    // Minkowshki takes an additional parameter 'p'. When 'p' is 1, the distance is Manhattan, 2 is Euclidean, and as it approaches infinity it becomes Chebyshev.
    public static double distance(int[] v1, int[] v2, double p) {
        if(v1.length != v2.length) return -1.0d;
        
        // A p value of 0 will cause a division by 0 error. Returning -1 to indicate an issue. SHOULD HANDLE THIS BETTER.
        if(p == 0.0d) {
            return -1.0d;
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
    
    public static double distance(Map<String, Integer> s1, Map<String, Integer> s2, double p) {
        Set<String> vocabulary = new HashSet<>(s1.keySet());
        vocabulary.addAll(s2.keySet());

        int[] v1 = new int[vocabulary.size()], v2 = new int[vocabulary.size()];

        int i = 0;
        // Populate the count vectors.
        for(String token: vocabulary) {
            v1[i] = s1.getOrDefault(token, 0);
            v2[i] = s2.getOrDefault(token, 0);
            i++;
        }

        return distance(v1, v2, p);
    }
}
