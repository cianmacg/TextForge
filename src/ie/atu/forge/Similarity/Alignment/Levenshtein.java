package ie.atu.forge.Similarity.Alignment;

// I used this for reference: https://www.geeksforgeeks.org/dsa/introduction-to-levenshtein-distance/
public class Levenshtein {
    /*
     * Levenshtein distance includes substitutions, insertions and deletions.
     * Can handle variable string lengths.
     */
    public static int distance(String s1, String s2) {
        return distance(s1.toCharArray(), s2.toCharArray());
    }

    // I'm using the character array representations of the strings here, as there will be many function calls to .charAt otherwise. I am assuming this increases running time.
    public static int distance(char[] s1 , char[] s2) {        
        int m = s1.length;
        int n = s2.length;

        if(m == 0 || n == 0) return 0;

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
