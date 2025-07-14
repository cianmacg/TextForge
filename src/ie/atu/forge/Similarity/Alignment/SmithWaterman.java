package ie.atu.forge.Similarity.Alignment;

// Local alignment algorithm
public class SmithWaterman {
    private static final int MATCH = 2;
    private static final int MISMATCH = -1;
    private static final int GAP = -2;

    public static String[] align(String s1, String s2) {
        char[][] alignments = align(s1.toCharArray(), s2.toCharArray());

        // If the user inputted strings, give them strings back.
        return new String[]{ new String(alignments[0]), new String(alignments[1]) };
    }

    public static char[][] align(char[] s1, char[] s2) {
        int col = s1.length;
        int row = s2.length;

        int[][] scores = new int[col+1][row+1];

        // Fill in first row/column with 0
        for(int i = 0; i <= col; i++) {
            scores[i][0] = 0;
        }

        for(int j = 0; j <= row; j++) {
            scores[0][j] = 0;
        }

        // To keep track of the max value in the matrix
        int max = 0, maxi = 0, maxj = 0;

        // Fill in the rest of the scores matrix
        for(int i = 1; i <= col; i++) {
            for(int j = 1; j <= row; j++) {
                // Match or Mismatch?
                scores[i][j] = Math.max(Math.max(
                        0,
                        // Match/Mismatch?
                        scores[i - 1][j - 1] + (s1[i - 1] == s2[j - 1] ? MATCH : MISMATCH)),
                        // Gap in s1 (insertion)
                        Math.max(scores[i][j-1] + GAP,
                        // Gap in s2 (deletion)
                        scores[i - 1][j] + GAP
                ));

                // Tracking max score
                if(scores[i][j] > max) {
                    max = scores[i][j];
                    maxi = i;
                    maxj = j;
                }
            }
        }


        // After filling out the matrix of scores, it's time to perform the traceback from the max score.

        // This is the maximum possible length the alignments could be.
        int index = maxi + maxj;
        char[] maxAlignA = new char[index];
        char[] maxAlignB = new char[index];


        // Instead of creating a new variable, I'm just going to reuse index here for the traceback. It needs to be 1 less to be used for accessing array elements.
        index--;
        int size = 0; // Tracks the actual size of the alignment
        while(maxi > 0 && maxj > 0 && scores[maxi][maxj] > 0) {
            // If diagonal
            if(scores[maxi][maxj] == scores[maxi - 1][maxj - 1] + (s1[maxi - 1] == s2[maxj - 1] ? MATCH : MISMATCH)) {
                maxAlignA[index] = s1[maxi - 1];
                maxAlignB[index] = s2[maxj - 1];

                maxi--;
                maxj--;
                index--;
            }
            // If deletion
            else if(scores[maxi][maxj] == scores[maxi - 1][maxj] + GAP) {
                maxAlignA[index] = s1[maxi - 1];
                maxAlignB[index] = '-';
                maxi--;
                index--;
            }
            // If insertion
            else {
                maxAlignA[index] = '-';
                maxAlignB[index] = s2[maxj - 1];
                maxj--;
                index--;
            }

            size++;
        }

        // Create new arrays with only the alignment characters (i.e. no empty indices).
        char[] actualAlignA = new char[size];
        char[] actualAlignB = new char[size];
        System.arraycopy(maxAlignA, index + 1, actualAlignA, 0, actualAlignA.length);
        System.arraycopy(maxAlignB, index + 1, actualAlignB, 0, actualAlignB.length);

        return new char[][]{ actualAlignA, actualAlignB };
    }
}
