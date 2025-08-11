package ie.atu.forge.Similarity.AlignmentFree;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Generalizes set similarity by weighting the importance of common and differing elements using adjustable parameters α and β.<br><br>
 * α controls the weight (or penalty) assigned to features that are in the first set but not in the second set.
 * β controls the weight assigned to features that are in the second set but not in the first set.
 *
 * <a href="https://psycnet.apa.org/record/1978-09287-001">Original Paper.</a>
 */
public class Tversky {
    // Tversky can generalise both Jaccard and Sorensen-Dice with 'alpha' (a) and 'beta' (b) values.

    /**
     * Generalises set similarity by weighting the importance of common and differing elements using adjustable parameters α and β.<br><br>
     * Special Cases:<br>
     * α = β = 1: Generalises to Jaccard Similarity.
     * α = β = 0.5: Generalises to Sørensen–Dice Similarity.
     *
     * @param s1 The first set.
     * @param s2 The second set.
     * @param a Controls the weight assigned to features that are in the first set but not in the second set.
     * @param b Controls the weight assigned to features that are in the second set but not in the first set.
     * @return The Tversky similarity between the 2 sets.
     * @param <T> The shared type between the 2 sets.
     */
    public static <T> double similarity(Set<T> s1, Set<T> s2, double a, double b) {
        if(s1.isEmpty() && s2.isEmpty()) return 1.0d;
        if(s1.isEmpty() || s2.isEmpty()) return 0.0d;

        Set<T> setA = new HashSet<T>(s1);
        Set<T> setB = new HashSet<T>(s2);
        Set<T> aNotB = new HashSet<T>(setA);
        Set<T> bNotA = new HashSet<T>(setB);
        aNotB.removeAll(setB);
        bNotA.removeAll(setA);

        Set<T> inter = new HashSet<T>(setA);
        inter.retainAll(setB);

        return (double) inter.size() / (inter.size() + a*(aNotB.size()) + b*(bNotA.size()));
    }

    /**
     * Generalises set similarity by weighting the importance of common and differing elements using adjustable parameters α and β. This function simply takes the key sets from the maps and measures the similarity.<br><br>
     * Special Cases:<br>
     * α = β = 1: Generalises to Jaccard Similarity.
     * α = β = 0.5: Generalises to Sørensen–Dice Similarity.
     *
     * @param s1 The first map.
     * @param s2 The second map.
     * @param a Controls the weight assigned to features that are in the first set but not in the second set.
     * @param b Controls the weight assigned to features that are in the second set but not in the first set.
     * @return The Tversky similarity between the 2 sets.
     */
    public static double similarity(Map<String, Integer> s1, Map<String, Integer> s2, double a, double b) {
        return similarity(s1.keySet(), s2.keySet(), a, b);
    }

    /**
     * The distance is simply 1 - the similarity.
     * Special Cases:<br>
     * α = β = 1: Generalises to Jaccard distance.
     * α = β = 0.5: Generalises to Sørensen–Dice distance.
     *
     * @param s1 The first set.
     * @param s2 The second set.
     * @param a Controls the weight assigned to features that are in the first set but not in the second set.
     * @param b Controls the weight assigned to features that are in the second set but not in the first set.
     * @return The Tversky distance between the 2 sets.
     * @param <T> The shared type between the 2 sets.
     */
    public static <T> double distance(Set<T> s1, Set<T> s2, double a, double b) {
        return 1 - similarity(s1, s2, a, b);
    }

    /**
     * The distance is simply 1 - the similarity.
     * Special Cases:<br>
     * α = β = 1: Generalises to Jaccard distance.
     * α = β = 0.5: Generalises to Sørensen–Dice distance.
     *
     * @param s1 The first map.
     * @param s2 The second map.
     * @param a Controls the weight assigned to features that are in the first set but not in the second set.
     * @param b Controls the weight assigned to features that are in the second set but not in the first set.
     * @return The Tversky similarity between the 2 sets.
     */
    public static double distance(Map<String, Integer> s1, Map<String, Integer> s2, double a, double b) {
        return 1 - similarity(s1, s2, a, b);
    }
}
