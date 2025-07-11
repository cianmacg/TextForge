package ie.atu.forge.Similarity.Alignment;

// Global alignment algorithm that aligns entire sequences filling in gaps where necessary.
public class NeedlemanWunsch {
    private static final int MATCH = 1;
    private static final int MISMATCH = -1;
    private static final int GAP = -2;


    public static String[] align(String s1, String s2) {
        char[][] alignments = align(s1.toCharArray(), s2.toCharArray());

        // If the user inputted strings, give them strings back.
        return new String[]{ new String(alignments[0]), new String(alignments[1]) };
    }


    public static char[][] align(char[] s1, char[] s2) {
        int m = s1.length;
        int n = s2.length;

        int[][] scores = new int[m+1][n+1];

        // First, we need to fill in the rows and columns with cap penalties.
        for(int i = 0; i <= m; i++) {
            scores[i][0] = i * GAP;
        }

        for(int j = 0; j <= n; j++) {
            scores[0][j] = j * GAP;
        }

        // Fill in the rest of the scores matrix
        for(int i = 1; i <= m; i++) {
            for(int j = 1; j <= n; j++) {
                // Match or Mismatch?
                scores[i][j] = Math.max(Math.max(
                        // Match/Mismatch?
                        scores[i - 1][j - 1] + (s1[i - 1] == s2[j - 1] ? MATCH : MISMATCH),
                        // Gap in s1 (insertion)
                        scores[i][j-1] + GAP),
                        // Gap in s2 (deletion)
                        scores[i - 1][j] + GAP
                );
            }
        }

        // Traceback through matrix to find optimal alignment (From scores[m][n] to scores[0][0]).

        // Since the user input character arrays here, I want to return the same datatype.
        int index = m + n;
        char[] maxAlignA = new char[index];
        char[] maxAlignB = new char[index];

        // Instead of creating a new variable, I'm just going to reuse index here for the traceback. It needs to be 1 less to be used for accessing array elements.
        index--;

        // Again, reusing m and n so I don't have to store additional values.
        while(m > 0 || n > 0) {
            // If diagonal.
            if(m > 0 && n > 0 && scores[m][n] == scores[m - 1][n - 1] + (s1[m - 1] == s2[n - 1] ? MATCH : MISMATCH)) {
                maxAlignA[index] = s1[m - 1];
                maxAlignB[index] = s2[n - 1];

                m--;
                n--;
                index--;
            }
            // If deletion
            else if(m > 0 && scores[m][n] == scores[m - 1][n] + GAP) {
                maxAlignA[index] = s1[m - 1];
                maxAlignB[index] = '-';
                m--;
                index--;
            }
            // If insertion
            else {
                maxAlignA[index] = '-';
                maxAlignB[index] = s2[n - 1];
                n--;
                index--;
            }
        }

        // Create new arrays with only the alignment characters (i.e. no empty indices).
        int len = s1.length + s2.length - index - 1;
        char[] actualAlignA = new char[len];
        char[] actualAlignB = new char[len];
        System.arraycopy(maxAlignA, index + 1, actualAlignA, 0, actualAlignA.length);
        System.arraycopy(maxAlignB, index + 1, actualAlignB, 0, actualAlignB.length);

        return new char[][]{ actualAlignA, actualAlignB };
    }

}
