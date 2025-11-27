package main.java.ie.atu.forge.Similarity.Alignment;

// I used this for reference: https://www.geeksforgeeks.org/dsa/introduction-to-levenshtein-distance/

/**
 * Levenshtein distance includes substitutions, insertions and deletions.
 * Can handle variable string lengths.
 *
 * <a href="https://ui.adsabs.harvard.edu/abs/1966SPhD...10..707L/abstract">Original Paper.</a>
 */
public class Levenshtein {

    /**
     *  Measures the minimum Edit Distance between 2 Strings. Handles insertions, deletions, and substitutions.
     *
     *
     * @param s1 The subject string
     * @param s2 The query string
     * @return The distance between s1 and s2 using Levenshtein Distance.
     */
    public static int distance(String s1, String s2) {
        if(s1 == null || s2 == null) throw new IllegalArgumentException("At least one of the provided strings is null.");

        return distance(s1.toCharArray(), s2.toCharArray());
    }

    /**
     *  Measures the minimum Edit Distance between 2 Strings. Handles insertions, deletions, and substitutions.
     *
     *
     * @param s1 The subject string (character array)
     * @param s2 The query string (character array)
     * @return The distance between s1 and s2 using Levenshtein Distance.
     */
    /*
     * I'm using the character array representations of the strings here, as there will be many function calls to .charAt otherwise. I am assuming this improves performance.
     * There is another possible implementation which reduces space complexity by only tracking the current and previous rows (since anything else is never really used).
     * May change to this later.
     */
    public static int distance(char[] s1 , char[] s2) {        
        int m = s1.length;
        int n = s2.length;

        // If either string is empty, return the length of the other string.
        if(m == 0) return n;
        if(n == 0) return m;

        int[][] matrix = new int[m + 1][n + 1];

        // Initialize first row and column
        for(int i = 0; i <= m; i++) {
            matrix[i][0] = i; // cost of deleting all characters from s1
        }

        for(int j = 0; j <= n; j++) {
            matrix[0][j] = j; // cost of inserting all characters of s2
        }

        // Fill in the table
        for(int i = 1; i <= m; i++) {
            for(int j = 1; j <= n; j++) {
                if(s1[i-1] == s2[j-1]) {
                    matrix[i][j] = matrix[i-1][j-1]; // no cost
                } else {
                    matrix[i][j] = 1 + Math.min(
                            matrix[i-1][j],     // deletion
                            Math.min(
                                    matrix[i][j-1], // insertion
                                    matrix[i-1][j-1] // substitution
                            )
                    );
                }
            }
        }

        return matrix[m][n];
    }
}
