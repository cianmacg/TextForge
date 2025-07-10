package ie.atu.forge.Similarity.Alignment;

public class Levenshtein {
    /*
     * Levenshtein distance includes substitutions, insertions and deletions.
     * Can handle variable string lengths.
     */
    public static int distance(String s1, String s2) {
        return distance(s1.toCharArray(), s2.toCharArray());
    }

    public static int distance(char[] s1 , char[] s2) {        
        if(s1.length == 0 || s2.length == 0) return 0;

        int[][] matrix = new int[s1.length][s2.length];

        

        return 0;
    }
}
