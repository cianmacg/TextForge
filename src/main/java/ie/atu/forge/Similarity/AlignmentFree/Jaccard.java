package main.java.ie.atu.forge.Similarity.AlignmentFree;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Measures the overlap between two sets as the size of their intersection divided by the size of their union.
 *
 * <a href="https://cir.nii.ac.jp/crid/1573387450552842240">Original Paper.</a>
 */
public class Jaccard {
    // The size of the intersection divided by the size of the union of the sets.

    /**
     * Measures the similarity between 2 sets based on the division of their intersection by their union.
     *
     * @param s1 The first set.
     * @param s2 The second set.
     * @return The Jaccard Similarity of both sets (from 0.0 to 1.0).
     * @param <T> The shared type of both sets.
     */
    public static <T> double similarity(Set<T> s1, Set<T> s2) {
        if(s1.isEmpty() && s2.isEmpty()) return 1.0d;
        if(s1.isEmpty() || s2.isEmpty()) return 0.0d;
        
        Set<T> inter = new HashSet<T>(s1);
        inter.retainAll(s2);
        
        return (double) inter.size() / (s1.size() + s2.size() - inter.size());
    }


    // Generalised Jaccard takes counts into consideration. I.e. duplicate elements are allowed. Multi-set
    /**
     * Measures the similarity between two multisets based on the ratio of the sum of
     * the minimum counts of each element to the sum of the maximum counts of each element.
     * <p>
     * This is a generalised form of Jaccard similarity that accounts for element multiplicities.
     * A value of {@code 1.0} indicates identical multisets, and {@code 0.0} indicates no overlap.
     *
     * @param s1 The first multiset, represented as a mapping from element to count
     * @param s2 The second multiset, represented as a mapping from element to count
     * @return The generalised Jaccard similarity of both multisets (from 0.0 to 1.0)
     */
    public static double generalisedSimilarity(Map<String, Integer> s1, Map<String, Integer> s2) {
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

    /**
     * The Jaccard distance is 1 - the Jaccard similarity.
     *
     * @param s1 The first set.
     * @param s2 The second set.
     * @return The Jaccard Distance of both sets (from 0.0 to 1.0).
     * @param <T> The shared type of both sets.
     */
    public static <T> double distance(Set<T> s1, Set<T> s2) {
        return 1 - similarity(s1, s2);
    }

    /**
     * The Generalised (Multiset) Jaccard distance is 1 - the Generalised Jaccard similarity.
     *
     * @param s1 The first multiset, represented as a mapping from element to count
     * @param s2 The second multiset, represented as a mapping from element to count
     * @return The generalised Jaccard distance of both multisets (from 0.0 to 1.0)
     */
    public static double generalisedDistance(Map<String, Integer> s1, Map<String, Integer> s2) {
        return 1 - generalisedSimilarity(s1, s2);
    }
}
