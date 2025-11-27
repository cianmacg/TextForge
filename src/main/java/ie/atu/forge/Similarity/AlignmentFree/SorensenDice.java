package ie.atu.forge.Similarity.AlignmentFree;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Measures the overlap between two sets as twice the size of their intersection divided by the sum of their sizes.
 *
 * <a href="https://www.scirp.org/reference/ReferencesPapers?ReferenceID=2200146">Sørensen Index paper.</a>
 * <a href="https://www.jstor.org/stable/1932409">Dice Coefficient paper.</a>
 */
public class SorensenDice {
    // 2 * the size of the intersection divided by the sum of the set sizes.

    /**
     * Measures the similarity between two sets as twice the size of their intersection divided by the sum of their sizes.
     *
     * @param s1 The first set.
     * @param s2 The second set.
     * @return The Sørensen–Dice similarity between the 2 sets.
     * @param <T> The shared type of both sets.
     */
    public static <T> double similarity(Set<T> s1, Set<T> s2) {
        if(s1.isEmpty() && s2.isEmpty()) return 1.0d;
        if(s1.isEmpty() || s2.isEmpty()) return 0.0d;

        Set<T> inter = new HashSet<T>(s1);
        inter.retainAll(s2);

        return (double) (2 * inter.size()) / (s1.size() + s2.size());
    }

    /**
     * Measures the similarity between two sets as twice the size of their intersection divided by the sum of their sizes.
     * This function will simply take the key set from the maps and calculate the distance between them.
     *
     * @param s1 The first map containing Strings.
     * @param s2 The second map containing Strings.
     * @return The Sørensen–Dice similarity between the 2 sets.
     */
    public static double similarity(Map<String, Integer> s1, Map<String, Integer> s2) {
        return similarity(s1.keySet(), s2.keySet());
    }

    /**
     * The distance is simply 1 - the similarity.
     *
     * @param s1 The first set.
     * @param s2 The second set.
     * @return The Sørensen–Dice distance between the 2 sets.
     * @param <T> The shared type of both sets.
     */
    public static <T> double distance(Set<T> s1, Set<T> s2) {
        return 1 - similarity(s1, s2);
    }

    /**
     * The distance is simply 1 - the similarity.
     * This function will simply take the key set from the maps and calculate the distance between them.
     *
     * @param s1 The first map containing Strings.
     * @param s2 The second map containing Strings.
     * @return The Sørensen–Dice distance between the 2 sets.
     */
    public static double distance(Map<String, Integer> s1, Map<String, Integer> s2) {
        return 1 - similarity(s1.keySet(), s2.keySet());
    }
}
