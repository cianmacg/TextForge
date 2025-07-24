package ie.atu.forge.Similarity.AlignmentFree;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Tversky {
    // Tversky can generalise both Jaccard and Sorensen-Dice with 'alpha' (a) and 'beta' (b) values.
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

    public static double similarity(Map<String, Integer> s1, Map<String, Integer> s2, double a, double b) {
        return similarity(s1.keySet(), s2.keySet(), a, b);
    }

    public static <T> double distance(Set<T> s1, Set<T> s2, double a, double b) {
        return 1 - similarity(s1, s2, a, b);
    }

    public static double distance(Map<String, Integer> s1, Map<String, Integer> s2, double a, double b) {
        return 1 - similarity(s1, s2, a, b);
    }
}
