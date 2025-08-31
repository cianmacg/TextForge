package ie.atu.forge.Similarity.Alignment;
/**
 * A similarity measure that returns a range (0 - 1). Hangles matches and transpositions.
 *
 * <a href="https://www.jstor.org/stable/2289924">Original Paper.</a>
 */
public class Jaro {
    /**
     * Measures how similar two strings are based on matching characters within a certain distance and the number of transpositions.
     *
     * @param s1 The subject string (character array).
     * @param s2 The query string (character array).
     * @return The similarity ranging from 0 (completely dissimilar) to 1 (identical).
     */
    public static double similarity(char[] s1, char[] s2) {
        if (s1.length == 0 && s2.length == 0) return 1.0; // If both are empty, they are a perfect match.
        if (s1.length == 0 || s2.length == 0) return 0.0; // If only 1 is empty, they cannot have any matches.

        int range = Math.max((int) Math.floor((Math.max(s1.length, s2.length) / 2.0d) - 1), 0);

        int[] matched_s1 = new int[s1.length], matched_s2 = new int[s2.length];

        // Count all matches within range.
        int matches = 0;
        for(int i = 0; i < s1.length; i++) {
            char c1 = s1[i];
            int end = Math.min(i + range + 1, s2.length);
            for(int j = Math.max(i - range, 0); j < end; j ++) {
                if(matched_s2[j] == 1) continue;

                if(s2[j] == c1) {
                    matches++;

                    matched_s1[i] = 1;
                    matched_s2[j] = 1;

                    break;
                }
            }
        }

        int transpositions = 0;
        int j = 0;
        // If the next appearing match is not of the same character as the index in s1, this means the characters are not in order and therefore a transposition.
        for(int i = 0; i < s1.length; i++) {
            if(matched_s1[i] == 1) {
                while(matched_s2[j] == 0) j++;

                if(s1[i] != s2[j]) transpositions++;

                j++;
            }
        }

        transpositions /= 2;
        if(matches == 0) return 0;

        return ( (matches / (double) s1.length) +  (matches / (double) s2.length) +  ((matches - transpositions) / (double) matches)) / 3.0d;
    }

    /**
     * Measures how similar two strings are based on matching characters within a certain distance and the number of transpositions.
     *
     * @param s1 The subject string.
     * @param s2 The query string.
     * @return The similarity ranging from 0 (completely dissimilar) to 1 (identical).
     */
    public static double similarity(String s1, String s2) {
        return similarity(s1.toCharArray(), s2.toCharArray());
    }

    /**
     * Measures how distant two strings are based on matching characters within a certain distance and the number of transpositions.
     *
     * @param s1 The subject string (character array).
     * @param s2 The query string (character array).
     * @return The distance ranging from 0 (completely dissimilar) to 1 (identical).
     */
    public static double distance(char[] s1, char[] s2) {
        return 1 - similarity(s1, s2);
    }

    /**
     * Measures how distant two strings are based on matching characters within a certain distance and the number of transpositions.
     *
     * @param s1 The subject string.
     * @param s2 The query string.
     * @return The distance ranging from 0 (completely dissimilar) to 1 (identical).
     */
    public static double distance(String s1, String s2) {
        return 1 - similarity(s1, s2);
    }
}
