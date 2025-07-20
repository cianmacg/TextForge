package ie.atu.forge.Similarity.Alignment;

public class JaroWinkler {
    public static double similarity(char[] c1, char[] c2, double p) {
        if(c1.length == 0 && c2.length == 0) return 1.0d;
        if(c1.length == 0 || c2.length == 0) return 0.0d;

        double jaro = Jaro.similarity(c1, c2);

        int l = 0;
        int end = Math.min(Math.min(c1.length, c2.length), 4);

        for(int i = 0; i < end; i++) {
            if(c1[i] == c2[i]) l = i + 1;
            else break;
        }

        return jaro + (l * p * (1 - jaro));
    }

    public static double similarity(char[] s1 , char[] s2) {
        return similarity(s1, s2, 0.1); // Winkler stated 0.1 is a good default value for p.
    }

    public static double similarity(String s1, String s2, double p) {
        return similarity(s1.toCharArray(), s2.toCharArray(), p);
    }

    public static double similarity(String s1, String s2) {
        return similarity(s1.toCharArray(), s2.toCharArray(), 0.1);
    }

}
