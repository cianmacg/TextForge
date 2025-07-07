package ie.atu.forge.SetSimilarity.AlignmentFree;

import ie.atu.forge.Vectorisers.BagOfWords;

import java.util.*;

public class Cosine {
    /*
    Unsure what I should be taking in here. A mapping of tokens to count of each text is a good starting point.
    I can easily create a vector representation from this, and the user can determine what the tokens are (words, ngrams, etc).
     */
    public static double distance(Map<String, Integer> b1, Map<String, Integer> b2) {


        return 0.0d;
    }

    public static double similarity(Map<String, Integer> b1, Map<String, Integer> b2) {
        return 1 - distance(b1, b2);
    }

    private static Integer[][] vectorise(Map<String, Integer> b1, Map<String, Integer> b2) {
        // Add every key from both maps to the vocabulary set.
        Set<String> vocabulary = b1.keySet();
        vocabulary.addAll(b2.keySet());

        // We need to create a vector representation of each map with regards to the complete vocabulary.
        List<Integer> v1 = new ArrayList<Integer>();
        List<Integer> v2 = new ArrayList<Integer>();

        // If the token from the set exists in the map, add the corresponding value as the dimensional value, otherwise add a 0.
        for(String word: vocabulary.stream().toList()) {
            Integer val1 = b1.get(word);
            Integer val2 = b2.get(word);

            if(val1 != null) {
                v1.add(val1);
            } else {
                v1.add(0);
            }

            if(val2 != null) {
                v2.add(val2);
            } else {
                v2.add(0);
            }

        }

        // Return our 2 vectors in a single Integer array.
        Integer[][] vectors = new Integer[2][v1.size()];
        vectors[0] = v1.toArray(new Integer[0]);
        vectors[1] = v2.toArray(new Integer[0]);

        return vectors;
    }
}
