package ie.atu.forge.Similarity.Alignment;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

// Global alignment algorithm that aligns entire sequences filling in gaps where necessary.
public class NeedlemanWunsch {
    private static Map<String, Integer> scoring_matrix = new TreeMap<>();
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

    // Loads in a given substitution matrix (from path specified by user) to be used instead of Match, Mismatch, and Gap scores.
    public static void loadMatrix(String matrix_path) throws IOException {
        // Make sure the scoring matrix is empty.
        scoring_matrix.clear();



        // I chose this method of file reading so I can read line-by-line.
        try(BufferedReader reader = new BufferedReader(new FileReader(matrix_path))) {
            String line;

            // First, I want to dump and comment lines.
            while((line = reader.readLine()) != null) {
                if(!(line.charAt(0) == '#')) break;
            }

            // The next line that appears is the column header line. There should be 24 columns (including '*').
            // Most lines have many spaces, I'll remove them.
            line = line.replace(" ", "");

            char[] col_names = line.toCharArray();

            // Now we have the column names, lets loop over the rest of the rows and add the combination of the column name + row name as a key to the map, with the value being the value.
            while((line = reader.readLine()) != null) {
                String[] row = line.split(" ");
                int col = 0; // Keep track of the column we are working with.

                for(int i = 1; i < row.length; i++) {
                    StringBuilder key = new StringBuilder();
                    key.append(col_names[col]);
                    key.append(row[0]); // row[0] is the row name.

                    // sometimes after the split, the value is "". We don't want this, so skip over it.
                    if(row[i] == "" || row[i] == null) continue;

                    int value = Integer.parseInt(row[i]);
                    scoring_matrix.put(key.toString(), value);

                    col++;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(scoring_matrix);

    }

}
