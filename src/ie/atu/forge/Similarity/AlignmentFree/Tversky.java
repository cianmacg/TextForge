package ie.atu.forge.Similarity.AlignmentFree;

import ie.atu.forge.Vectorisers.BagOfWords;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Tversky {
    // Tversky can generalise both Jaccard and Sorensen-Dice with 'alpha' (a) and 'beta' (b) values.
    public static double similarity(Map<String, Integer> s1, Map<String, Integer> s2, double a, double b) {
        if(s1.isEmpty() || s2.isEmpty()) {
            return 0.0d;
        }

        Set<String> setA = new HashSet<String>(s1.keySet());
        Set<String> setB = new HashSet<String>(s2.keySet());
        Set<String> aNotB = new HashSet<String>(setA);
        Set<String> bNotA = new HashSet<String>(setB);
        aNotB.removeAll(setB);
        bNotA.removeAll(setA);

        Set<String> inter = new HashSet<String>(setA);
        inter.retainAll(setB);

        return (double) inter.size() / (inter.size() + a*(aNotB.size()) + b*(bNotA.size()));
    }

    public static double distance(Map<String, Integer> s1, Map<String, Integer> s2, double a, double b) {
        return 1 - similarity(s1, s2, a, b);
    }
}
