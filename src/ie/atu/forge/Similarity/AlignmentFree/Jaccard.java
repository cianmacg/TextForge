package ie.atu.forge.Similarity.AlignmentFree;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Jaccard {
    // The size of the intersection divided by the size of the union of the sets.
    public static <T> double similarity(Set<T> s1, Set<T> s2) {
        if(s1.isEmpty() || s2.isEmpty()) return 0.0d;
        
        Set<T> inter = new HashSet<T>(s1);
        inter.retainAll(s2);

        Set<T> union = new HashSet<T>(s1);
        union.addAll(s2);
        
        return (double) inter.size() / union.size();
    }

    public static double similarity(Map<String, Integer> s1, Map<String, Integer> s2) {
        return similarity(s1.keySet(), s2.keySet());
    }

    public static <T> double distance(Set<T> s1, Set<T> s2) {
        return 1 - similarity(s1, s2);
    }

    public static double distance(Map<String, Integer> s1, Map<String, Integer> s2) {
        return 1 - similarity(s1, s2);
    }
}
