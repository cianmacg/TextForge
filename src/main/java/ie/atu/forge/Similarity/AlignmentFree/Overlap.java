package main.java.ie.atu.forge.Similarity.AlignmentFree;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Measures the size of the intersection of two sets divided by the size of the smaller set.
 */
public class Overlap {
    // The size of the intersection divided by the size of the smaller of the 2 sets.

    /**
     * Measures the distance of the sets based on their intersection divided by the size of the smaller set.
     *
     * @param s1 The first set.
     * @param s2 The second set.
     * @return The Overlap distance between the 2 sets.
     * @param <T> The shared type of both sets.
     */
    public static <T> double distance(Set<T> s1, Set<T> s2) {
        if(s1.isEmpty() && s2.isEmpty()) return 0.0d;
        if(s1.isEmpty() || s2.isEmpty()) return 1.0d;

        Set<T> inter = new HashSet<T>(s1);
        inter.retainAll(s2);

        double divider = Math.min(s1.size(), s2.size());

        return 1 - (double) inter.size() / divider;
    }

    /**
     * Measures the distance of the sets based on their intersection divided by the size of the smaller set.
     * This function will simply take the key set from the maps and calculate the distance between them.
     *
     * @param s1 The first map containing Strings.
     * @param s2 The second map containing Strings.
     * @return The Overlap distance between the 2 maps.
     */
    public static double distance(Map<String, Integer> s1, Map<String, Integer> s2) {
        return distance(s1.keySet(), s2.keySet());
    }
}