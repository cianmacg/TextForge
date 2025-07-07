package ie.atu.forge.Similarity.AlignmentFree;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Overlap {
    // The size of the intersection divided by the size of the smaller of the 2 sets.
    public static double distance(Map<String, Integer> s1, Map<String, Integer> s2) {
        if(s1.isEmpty() || s2.isEmpty()) {
            return 0.0d;
        }

        Set<String> inter = new HashSet<String>(s1.keySet());
        inter.retainAll(s2.keySet());

        double divider = Math.min(s1.size(), s2.size());

        return 1 - (double) inter.size() / divider;
    }

}
