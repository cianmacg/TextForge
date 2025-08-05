package ie.atu.forge.Similarity.Alignment;

import ie.atu.forge.ToolBox.MatrixLoader;

import java.io.IOException;
import java.util.Map;

// Local alignment algorithm
public class SmithWaterman {
    private int MATCH = 2;
    private int MISMATCH = -1;
    private int GAP = -2;
    private Map<String, Integer> scoringMatrix = null;

    public String[] align(String s1, String s2) {
        if(scoringMatrix == null) return alignWithoutMatrix(s1, s2);
        else return alignWithMatrix(s1, s2);
    }

    public char[][] align(char[] c1, char[] c2) {
        if(scoringMatrix == null) return alignWithoutMatrix(c1, c2);
        else return alignWithMatrix(c1, c2);
    }

    public String[] align(String s1, String s2, boolean useScoringMatrix) {
        if(!useScoringMatrix) return alignWithoutMatrix(s1, s2);
        else return alignWithMatrix(s1, s2);
    }

    public char[][] align(char[] c1, char[] c2, boolean useScoringMatrix) {
        if(!useScoringMatrix) return alignWithoutMatrix(c1, c2);
        else return alignWithMatrix(c1, c2);
    }

    private String[] alignWithoutMatrix(String s1, String s2) {
        char[][] alignments = alignWithoutMatrix(s1.toCharArray(), s2.toCharArray());

        // If the user inputted strings, give them strings back.
        return new String[]{ new String(alignments[0]), new String(alignments[1]) };
    }

    private char[][] alignWithoutMatrix(char[] s1, char[] s2) {
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
        return traceback(scores, s1, s2, maxi, maxj);
    }

    private String[] alignWithMatrix(String s1, String s2) {
        char[][] alignments = alignWithMatrix(s1.toCharArray(), s2.toCharArray());

        // If the user inputted strings, give them strings back.
        return new String[]{ new String(alignments[0]), new String(alignments[1]) };
    }

    private char[][] alignWithMatrix(char[] s1, char[] s2) {
        if(scoringMatrix == null) throw new IllegalStateException("No scoring matrix set.");

        int col = s1.length;
        int row = s2.length;

        int[][] scores = new int[col+1][row+1];

        // To keep track of the max value in the matrix
        int max = 0, maxi = 0, maxj = 0;

        // Fill in the rest of the scores matrix
        for(int i = 1; i <= col; i++) {
            char c1 = s1[i - 1];
            for(int j = 1; j <= row; j++) {
                char c2 = s2[j - 1];

                // Get the score from the scoring matrix passed by the user.
                int score = getScore(c1, c2, scoringMatrix);
                // Match or Mismatch?
                scores[i][j] = Math.max(Math.max(
                                0,
                                // Match/Mismatch?
                                scores[i - 1][j - 1] + score),
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

        return traceback(scoringMatrix, scores, s1, s2, maxi, maxj);
    }

    private char[][] traceback(int[][] scores, char[] s1, char[] s2, int maxi, int maxj) {
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

    private char[][] traceback(Map<String, Integer> scoring_matrix, int[][] scores, char[] s1, char[] s2, int maxi, int maxj) {
        // This is the maximum possible length the alignments could be.
        int index = maxi + maxj;
        char[] maxAlignA = new char[index];
        char[] maxAlignB = new char[index];


        // Instead of creating a new variable, I'm just going to reuse index here for the traceback. It needs to be 1 less to be used for accessing array elements.
        index--;
        int size = 0; // Tracks the actual size of the alignment
        while(maxi > 0 && maxj > 0 && scores[maxi][maxj] > 0) {
            int score = getScore(s1[maxi - 1], s2[maxj - 1], scoring_matrix);

            // If diagonal
            if(scores[maxi][maxj] == scores[maxi - 1][maxj - 1] + score) {
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

    private int getScore(char c1, char c2, Map<String, Integer> scoring_matrix) {
        // This is the difference between the non-score matrix version and this version
        // Try to get the score
        Integer match_score = scoring_matrix.get("" + c1 + c2);

        // If the above doesn't return a value, it means either c1, c2, or both are not in the map.
        // Try different combinations with the wildcard character (*).
        if(match_score != null) {
            return match_score;
        } else {
            match_score = scoring_matrix.get(""  + '*' + c2);

            if(match_score != null) {
                return match_score;
            } else {
                match_score = scoring_matrix.get("" + c1 + '*');

                if(match_score != null) {
                    return match_score;
                } else {
                    match_score = scoring_matrix.get("**");
                }
            }
        }

        // If nothing has been found at all, i.e., there were no wildcard characters in the map, return the default mismatch.
        return MISMATCH;
    }

    // Getters and Setters below
    public void setMATCH(int new_value) { MATCH = new_value; }

    public void setMISMATCH(int new_value) { MISMATCH = new_value; }

    public void setGAP(int new_value) { GAP = new_value; }

    public int getMATCH() { return MATCH; }

    public int getMISMATCH() { return MISMATCH; }

    public int getGAP() { return GAP; }

    // Sets the scoring matrix to a map provided by the user.
    public void setScoringMatrix(Map<String, Integer> matrix) {
        scoringMatrix = matrix;
    }

    // Loads a scoring matrix from a text file.
    public void loadScoringMatrix(String path) throws IOException {
        scoringMatrix = MatrixLoader.load(path);
    }
}