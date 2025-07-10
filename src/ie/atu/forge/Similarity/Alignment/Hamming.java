package ie.atu.forge.Similarity.Alignment;

public class Hamming {
    /*
        Hamming distance is the number of substitutions needed to transfrom one string to another.
        ONLY FOR STRING OF IDENTICAL LENGTH
    */ 
    // If the user wants to pass in strings or char[], we can handle both.
    public static int distance(String s1, String s2) {
        return distance(s1.toCharArray(), s2.toCharArray());
    }

    public static int distance(char[] s1, char[] s2) {
        // Strings must be of equal length.
        if(s1.length != s2.length) return -1;


        int dist = 0;
        // Check how many characters in the strings do not match.
        for(int i = 0; i < s1.length; i++) {
            if(s1[i] != s2[i]) dist++;
        }

        return dist;
    }
}
