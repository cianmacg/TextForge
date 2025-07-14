package ie.atu.forge.Similarity.AlignmentFree;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SorensenDice {
    // 2 * the size of the intersection divided by the sum of the set sizes.
    public static <T> double similarity(Set<T> s1, Set<T> s2) {
        if(s1.isEmpty() || s2.isEmpty()) return 0.0d;

        Set<T> inter = new HashSet<T>(s1);
        inter.retainAll(s2);

        return (double) (2 * inter.size()) / (s1.size() + s2.size());
    }

    public static double similarity(Map<String, Integer> s1, Map<String, Integer> s2) {
        return similarity(s1.keySet(), s2.keySet());
    }

    public static <T> double distance(Set<T> s1, Set<T> s2) {
        return 1 - similarity(s1, s2);
    }

    public static double distance(Map<String, Integer> s1, Map<String, Integer> s2) {
        return 1 - similarity(s1.keySet(), s2.keySet());
    }
}
