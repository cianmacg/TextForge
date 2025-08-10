package ie.atu.forge.Similarity.Alignment;

/**
 * Extends Jaro Similarity by boosting scores for strings that share a common prefix.
 * Returns a value ranging from 0 (completely dissimilar) to 1 (identical).
 *
 *  <a href="https://www.researchgate.net/publication/243772975_String_Comparator_Metrics_and_Enhanced_Decision_Rules_in_the_Fellegi-Sunter_Model_of_Record_Linkage">Original Paper.</a>
 */
public class JaroWinkler {
    /**
     * Measures how similar two strings are based on matching characters within a certain distance and the number of transpositions.
     * Common prefixes are rewarded with higher scores. Set scaling factor p to control common prefix reward (defaults to 0.1).
     *
     * @param s1 The subject string (character array).
     * @param s2 The query string (character array).
     * @param p The scaling factor for shared prefixes.
     * @return The similarity ranging from 0 (completely dissimilar) to 1 (Identical)
     */
    public static double similarity(char[] s1, char[] s2, double p) {
        if(s1.length == 0 && s2.length == 0) return 1.0d;
        if(s1.length == 0 || s2.length == 0) return 0.0d;

        double jaro = Jaro.similarity(s1, s2);

        int l = 0;
        int end = Math.min(Math.min(s1.length, s2.length), 4);

        for(int i = 0; i < end; i++) {
            if(s1[i] == s2[i]) l = i + 1;
            else break;
        }

        return jaro + (l * p * (1 - jaro));
    }

    /**
     * Measures how similar two strings are based on matching characters within a certain distance and the number of transpositions.
     * Common prefixes are rewarded with higher scores. Set scaling factor p to control common prefix reward (defaults to 0.1).
     *
     * @param s1 The subject string (character array).
     * @param s2 The query string (character array).
     * @return The similarity ranging from 0 (completely dissimilar) to 1 (Identical)
     */
    public static double similarity(char[] s1 , char[] s2) {
        return similarity(s1, s2, 0.1); // Winkler stated 0.1 is a good default value for p.
    }

    /**
     * Measures how similar two strings are based on matching characters within a certain distance and the number of transpositions.
     * Common prefixes are rewarded with higher scores. Set scaling factor p to control common prefix reward (defaults to 0.1).
     *
     * @param s1 The subject string.
     * @param s2 The query string.
     * @param p The scaling factor for shared prefixes (0 - 1.0).
     * @return The similarity ranging from 0 (completely dissimilar) to 1 (Identical)
     */
    public static double similarity(String s1, String s2, double p) {
        return similarity(s1.toCharArray(), s2.toCharArray(), p);
    }

    /**
     * Measures how similar two strings are based on matching characters within a certain distance and the number of transpositions.
     * Common prefixes are rewarded with higher scores. Set scaling factor p to control common prefix reward (defaults to 0.1).
     *
     * @param s1 The subject string.
     * @param s2 The query string.
     * @return The similarity ranging from 0 (completely dissimilar) to 1 (Identical)
     */
    public static double similarity(String s1, String s2) {
        return similarity(s1.toCharArray(), s2.toCharArray(), 0.1);
    }

}
