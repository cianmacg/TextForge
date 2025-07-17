package ie.atu.forge.Similarity.Alignment;

import ie.atu.forge.ToolBox.MatrixLoader;

import java.io.IOException;
import java.util.Map;

// Global alignment algorithm that aligns entire sequences filling in gaps where necessary.
public class NeedlemanWunsch {
    private static Map<String, Integer> scoring_matrix;
    private static final int MATCH = 1;
    private static final int MISMATCH = -1;
    private static final int GAP = -1;


    public static String[] align(String s1, String s2) {
        char[][] alignments = align(s1.toCharArray(), s2.toCharArray());

        // If the user inputted strings, give them strings back.
        return new String[]{ new String(alignments[0]), new String(alignments[1]) };
    }

    public static char[][] align(char[] s1, char[] s2) {
        int col = s1.length;
        int row = s2.length;

        int[][] scores = new int[col+1][row+1];

        // First, we need to fill in the rows and columns with cap penalties.
        for(int i = 0; i <= col; i++) {
            scores[i][0] = i * GAP;
        }

        for(int j = 0; j <= row; j++) {
            scores[0][j] = j * GAP;
        }

        // Fill in the rest of the scores matrix
        for(int i = 1; i <= col; i++) {
            for(int j = 1; j <= row; j++) {
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

        // Traceback through matrix to find optimal alignment (From scores[col][row] to scores[0][0]).

        // Since the user input character arrays here, I want to return the same datatype.
        int index = col + row;
        char[] maxAlignA = new char[index];
        char[] maxAlignB = new char[index];

        // Instead of creating a new variable, I'm just going to reuse index here for the traceback. It needs to be 1 less to be used for accessing array elements.
        index--;

        // Again, reusing col and row so I don't have to store additional values.
        while(col > 0 || row > 0) {
            // If diagonal.
            if(col > 0 && row > 0 && scores[col][row] == scores[col - 1][row - 1] + (s1[col - 1] == s2[row - 1] ? MATCH : MISMATCH)) {
                maxAlignA[index] = s1[col - 1];
                maxAlignB[index] = s2[row - 1];

                col--;
                row--;
                index--;
            }
            // If deletion
            else if(col > 0 && scores[col][row] == scores[col - 1][row] + GAP) {
                maxAlignA[index] = s1[col - 1];
                maxAlignB[index] = '-';
                col--;
                index--;
            }
            // If insertion
            else {
                maxAlignA[index] = '-';
                maxAlignB[index] = s2[row - 1];
                row--;
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

    public static char[][] align(char[] s1, char[] s2, Map<String, Integer> scoring_matrix) {
        return null;
    }

    public static String[] align(String s1, String s2, Map<String, Integer> scoring_matrix) {
        return null;
    }

    public static char[][] align(char[] s1, char[] s2, String matrix_path) throws IOException {
        Map<String, Integer> scoring_matrix = MatrixLoader.load(matrix_path);

        return align(s1, s2, scoring_matrix);
    }

    public static String[] align(String s1, String s2, String matrix_path) {
        return null;
    }
}
