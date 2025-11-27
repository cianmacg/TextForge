package ie.atu.forge.Similarity.Alignment;

/**
 *  Hamming distance is the number of substitutions needed to transform one string to another.
 *  Strings must be of identical lengths.<br><br>
 *
 *  <a href="https://ieeexplore.ieee.org/document/6772729/">Original Paper.</a>
 */
public class Hamming {
    /**
     *  Measures the number of substitutions between 2 strings.
     *  Strings must be of identical lengths.
     *
     * @param s1 The subject string
     * @param s2 The query string
     * @return The distance between the 2 strings using Hamming Distance.
     */
    public static int distance(String s1, String s2) {
        return distance(s1.toCharArray(), s2.toCharArray());
    }

    /**
     *  Measures the number of substitutions between 2 strings.
     *  Strings must be of identical lengths.
     *
     * @param s1 The subject string (character array)
     * @param s2 The query string (character array)
     * @return The distance between the 2 strings using Hamming Distance.
     */
    public static int distance(char[] s1, char[] s2) {
        // Strings must be of equal length.
        if(s1.length != s2.length) throw new IllegalArgumentException("Both strings must be of equal length.");


        int dist = 0;
        // Check how many characters in the strings do not match.
        for(int i = 0; i < s1.length; i++) {
            if(s1[i] != s2[i]) dist++;
        }

        return dist;
    }
}
