package ie.atu.forge.Similarity.AlignmentFree;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Overlap {
    // The size of the intersection divided by the size of the smaller of the 2 sets.
    public static <T> double distance(Set<T> s1, Set<T> s2) {
        if(s1.isEmpty() && s2.isEmpty()) return 0.0d;
        if(s1.isEmpty() || s2.isEmpty()) return 1.0d;

        Set<T> inter = new HashSet<T>(s1);
        inter.retainAll(s2);

        double divider = Math.min(s1.size(), s2.size());

        return 1 - (double) inter.size() / divider;
    }

    public static double distance(Map<String, Integer> s1, Map<String, Integer> s2) {
        return distance(s1.keySet(), s2.keySet());
    }
}