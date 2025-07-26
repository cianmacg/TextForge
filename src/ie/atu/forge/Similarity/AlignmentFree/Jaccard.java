package ie.atu.forge.Similarity.AlignmentFree;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Jaccard {
    // The size of the intersection divided by the size of the union of the sets.
    public static <T> double similarity(Set<T> s1, Set<T> s2) {
        if(s1.isEmpty() && s2.isEmpty()) return 1.0d;
        if(s1.isEmpty() || s2.isEmpty()) return 0.0d;
        
        Set<T> inter = new HashSet<T>(s1);
        inter.retainAll(s2);
        
        return (double) inter.size() / (s1.size() + s2.size() - inter.size());
    }


    // Generalised Jaccard takes counts into consideration. I.e. duplicate elements are allowed. Multi-set
    public static double generalised_similarity(Map<String, Integer> s1, Map<String, Integer> s2) {
        if(s1.isEmpty() && s2.isEmpty()) return 1.0d;
        if(s1.isEmpty() || s2.isEmpty()) return 0.0d;

        long min_count = 0, max_count = 0;

        Set<String> union = new HashSet<>(s1.keySet());
        union.addAll(s2.keySet());

        for(String key: union) {
            int v1 = s1.getOrDefault(key, 0), v2 = s2.getOrDefault(key, 0);
            min_count += Math.min(v1, v2);
            max_count += Math.max(v1, v2);
        }

        if(max_count == 0) return 1.0d; // If every key in both sets has a value of 0, they are identical.

        return (double) min_count / max_count;
    }

    public static <T> double distance(Set<T> s1, Set<T> s2) {
        return 1 - similarity(s1, s2);
    }

    public static double distance(Map<String, Integer> s1, Map<String, Integer> s2) {
        return 1 - generalised_similarity(s1, s2);
    }
}
