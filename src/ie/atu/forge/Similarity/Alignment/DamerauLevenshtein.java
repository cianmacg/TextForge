package ie.atu.forge.Similarity.Alignment;

// Extension of Levenshtein distance that also measures transpositions. A transposition occurs between adjacent characters.
public class DamerauLevenshtein {
    public static int distance(String s1, String s2) {
        return distance(s1.toCharArray(), s2.toCharArray());
    }

    public static int distance(char[] s1 , char[] s2) {
        int m = s1.length;
        int n = s2.length;

        // If either string is null, return -1, as no distance can be calculated.
        if(s1 == null || s2 == null) return -1;

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
                if(s1[i - 1] == s2[j - 1]) {
                    matrix[i][j] = matrix[i - 1][j - 1]; // no cost
                } else {
                    matrix[i][j] = 1 + Math.min(
                            matrix[i - 1][j],     // deletion
                            Math.min(
                                    matrix[i][j - 1], // insertion
                                    matrix[i - 1][j - 1] // substitution
                            )
                    );

                    // Transpositions are checked after performing the normal deletion/insertion/substitution phase
                    if((i > 1) && (j > 1) && (s1[i - 1] == s2[j - 2]) && (s1[i - 2] == s2[j - 1])) {
                        matrix[i][j] = Math.min(
                                matrix[i][j],
                                matrix[i - 2][j - 2] + 1
                        );
                    }
                }
            }
        }

        return matrix[m][n];
    }
}
