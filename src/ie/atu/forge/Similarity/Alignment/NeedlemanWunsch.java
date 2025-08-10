package ie.atu.forge.Similarity.Alignment;

import ie.atu.forge.ToolBox.MatrixLoader;

import java.io.IOException;
import java.util.Map;

/**
 * Global alignment algorithm that aligns entire sequences filling in gaps where necessary.
 * Uses Match, Mismatch, and Gap scores to calculate alignment scores.
 * Alternatively, a scoring matrix can be used for biological sequences.<br><br>
 *
 * <a href="https://www.sciencedirect.com/science/article/abs/pii/0022283670900574">Original Paper.</a><br>
 *
 * Scoring Matrices can be found <a href="https://ftp.ncbi.nih.gov/blast/matrices/">here</a>.
 */
public class NeedlemanWunsch {
    private int MATCH = 1;
    private int MISMATCH = -1;
    private int GAP = -1;
    private Map<String, Integer> scoringMatrix = null;

    /**
     *  Finds the optimal global alignment between 2 strings (or character arrays).
     *  If a scoring matrix has been set, it will be used by default. Otherwise, Match, Mismatch, and Gap scores will be used.
     *  Use the parameter "useScoringMatrix" to choose whether to use ht scoring matrix or not.
     *
     * @param s1 The subject string (or character array).
     * @param s2 The query string (or character array).
     * @return The optimal alignments for both the subject and query strings.
     */
    public String[] align(String s1, String s2) {
        if(scoringMatrix == null) return alignWithoutMatrix(s1, s2);
        else return alignWithMatrix(s1, s2);
    }

    /**
     *  Finds the optimal global alignment between 2 strings (or character arrays).
     *  If a scoring matrix has been set, it will be used by default. Otherwise, Match, Mismatch, and Gap scores will be used.
     *  Use the parameter "useScoringMatrix" to choose whether to use ht scoring matrix or not.
     *
     * @param s1 The subject string (or character array).
     * @param s2 The query string (or character array).
     * @return The optimal alignments for both the subject and query strings.
     */
    public char[][] align(char[] s1, char[] s2) {
        if(scoringMatrix == null) return alignWithoutMatrix(s1, s2);
        else return alignWithMatrix(s1, s2);
    }

    /**
     *  Finds the optimal global alignment between 2 strings (or character arrays).
     *  If a scoring matrix has been set, it will be used by default. Otherwise, Match, Mismatch, and Gap scores will be used.
     *  Use the parameter "useScoringMatrix" to choose whether to use ht scoring matrix or not.
     *
     * @param s1 The subject string (or character array).
     * @param s2 The query string (or character array).
     * @param useScoringMatrix Controls use of Scoring Matrix or Match, Mismatch, and Gap scores.
     * @return The optimal alignments for both the subject and query strings.
     */
    public String[] align(String s1, String s2, boolean useScoringMatrix) {
        if(!useScoringMatrix) return alignWithoutMatrix(s1, s2);
        else {
            if(scoringMatrix == null) {
                System.out.println("Tried to use Scoring Matrix, but it is null. Defaulting to non-matrix alignment.");
                return alignWithoutMatrix(s1, s2);
            }

            return alignWithMatrix(s1, s2);
        }
    }

    /**
     *  Finds the optimal global alignment between 2 strings (or character arrays).
     *  If a scoring matrix has been set, it will be used by default. Otherwise, Match, Mismatch, and Gap scores will be used.
     *  Use the parameter "useScoringMatrix" to choose whether to use ht scoring matrix or not.
     *
     * @param s1 The subject string (or character array).
     * @param s2 The query string (or character array).
     * @param useScoringMatrix Controls use of Scoring Matrix or Match, Mismatch, and Gap scores.
     * @return The optimal alignments for both the subject and query strings.
     */
    public char[][] align(char[] s1, char[] s2, boolean useScoringMatrix) {
        if(!useScoringMatrix) return alignWithoutMatrix(s1, s2);
        else {
            if(scoringMatrix == null) {
                System.out.println("Tried to use Scoring Matrix, but it is null. Defaulting to non-matrix alignment.");
                return alignWithoutMatrix(s1, s2);
            }

            return alignWithMatrix(s1, s2);
        }
    }

    private String[] alignWithoutMatrix(String s1, String s2) {
        char[][] alignments = align(s1.toCharArray(), s2.toCharArray());

        // If the user inputted strings, give them strings back.
        return new String[]{ new String(alignments[0]), new String(alignments[1]) };
    }

    private char[][] alignWithoutMatrix(char[] s1, char[] s2) {
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
        return traceback(scores, s1, s2);
    }

    private String[] alignWithMatrix(String s1, String s2) {
        char[][] alignments = alignWithMatrix(s1.toCharArray(), s2.toCharArray());

        return new String[]{new String(alignments[0]), new String(alignments[1])};
    }

    private char[][] alignWithMatrix(char[] s1, char[] s2) {
        if(scoringMatrix == null) {
            throw new IllegalStateException("Trying to use Scoring Matrix while none is set.");
        }

        try {
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
                char c1 = s1[i - 1];

                for(int j = 1; j <= row; j++) {
                    char c2 = s2[j - 1];

                    // This is the difference between the non-score matrix version and this version
                    // Try to get the score. If no score is found, the default MISMATCH score is applied.
                    int match_score = getScore(c1, c2, scoringMatrix);

                    // Match or Mismatch?
                    scores[i][j] = Math.max(Math.max(
                                    // Match/Mismatch?
                                    scores[i - 1][j - 1] + match_score,
                                    // Gap in s1 (insertion)
                                    scores[i][j-1] + GAP),
                            // Gap in s2 (deletion)
                            scores[i - 1][j] + GAP
                    );
                }
            }

            return traceback(scoringMatrix,scores, s1, s2);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private char[][] traceback(int[][] scores, char[] s1, char[] s2) {
        int col = s1.length;
        int row = s2.length;

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

    private char[][] traceback(Map<String, Integer> scoring_matrix, int[][] scores, char[] s1, char[] s2) {
        int col = s1.length;
        int row = s2.length;

        int index = col + row;
        char[] maxAlignA = new char[index];
        char[] maxAlignB = new char[index];

        // Instead of creating a new variable, I'm just going to reuse index here for the traceback. It needs to be 1 less to be used for accessing array elements.
        index--;

        // Again, reusing col and row so I don't have to store additional values.
        while (col > 0 || row > 0) {
            // If diagonal
            if (col > 0 && row > 0) {
                char c1 = s1[col - 1];
                char c2 = s2[row - 1];
                int match_score = getScore(c1, c2, scoring_matrix);

                if (scores[col][row] == scores[col - 1][row - 1] + match_score) {
                    maxAlignA[index] = c1;
                    maxAlignB[index] = c2;
                    col--;
                    row--;
                    index--;
                    continue; // Skip to next iteration since we’ve already handled this step
                }
            }

            // If deletion
            if (col > 0 && scores[col][row] == scores[col - 1][row] + GAP) {
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
                    return scoring_matrix.get("**");
                }
            }
        }
    }

    // Getters and Setters below
    /**
     * Sets the Match score.
     * @param newValue The new score for matches.
     */
    public void setMATCH(int newValue) { MATCH = newValue; }

    /**
     * Sets the Mismatch score.
     * @param newValue The new score for mismatches.
     */
    public void setMISMATCH(int newValue) { MISMATCH = newValue; }

    /**
     * Sets the Gap score.
     * @param newValue The new score for gaps.
     */
    public void setGAP(int newValue) { GAP = newValue; }

    /**
     * Gets the Match score.
     * @return The current score value for Matches.
     */
    public int getMATCH() { return MATCH; }

    /**
     * Gets the Mismatch score.
     * @return The current score value for Mismatches.
     */
    public int getMISMATCH() { return MISMATCH; }

    /**
     * Gets the Gap score.
     * @return The current score value for Gaps.
     */
    public int getGAP() { return GAP; }

    /**
     * Sets the scoring matrix to a map provided by the user.
     * @param matrix A map of character pairs (as a string) and the associated score for each pair.
     */
    public void setScoringMatrix(Map<String, Integer> matrix) {
        scoringMatrix = matrix;
    }

    /**
     * Loads a scoring matrix from a text file.
     * Scoring matrices can be found <a href="https://ftp.ncbi.nih.gov/blast/matrices/">here</a>.
     * @param path The path to the text file containing the scoring matrix.
     * @throws IOException
     */
    public void loadScoringMatrix(String path) throws IOException {
        scoringMatrix = MatrixLoader.load(path);
    }
}
