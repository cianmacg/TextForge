package ie.atu.forge.ToolBox;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

public class MatrixLoader {
    // Loads in a given substitution matrix (from path specified by user) to be used instead of Match, Mismatch, and Gap scores.
    // Returns a map of character pairs (String) to score value (Integer).
    public static Map<String, Integer> load(String matrix_path) throws IOException {
        // Make sure the scoring matrix is empty.
        Map<String, Integer> scoring_matrix = new TreeMap<>();

        // I chose this method of file reading so I can read line-by-line.
        try(BufferedReader reader = new BufferedReader(new FileReader(matrix_path))) {
            String line;

            // First, I want to dump and comment lines.
            while((line = reader.readLine()) != null) {
                if(!(line.charAt(0) == '#')) break;
            }

            // The next line that appears is the column header line. There should be 24 columns (including '*').
            // Most lines have many spaces, I'll remove them.
            if(line != null) {
                line = line.replace(" ", "");

                char[] col_names = line.toCharArray();


                // Now we have the column names, lets loop over the rest of the rows and add the combination of the column name + row name as a key to the map, with the value being the value.
                while((line = reader.readLine()) != null) {
                    String[] row = line.split(" ");
                    int col = 0; // Keep track of the column we are working with.

                    for(int i = 1; i < row.length; i++) {
                        String key = col_names[col] + row[0]; // row[0] is the row name.

                        // sometimes after the split, the value is "". We don't want this, so skip over it.
                        if(row[i].isEmpty() || row[i] == null) continue;

                        int value = Integer.parseInt(row[i]);
                        scoring_matrix.put(key, value);

                        col++;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return scoring_matrix;
    }
}
