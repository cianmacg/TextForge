import  ie.atu.forge.Similarity.Alignment.NeedlemanWunsch;

public class main {
    public static void main(String[] args) {
        String s1 = "AC";
        String s2 = "AGC";

        String[] alignment = NeedlemanWunsch.align(s1, s2);

        System.out.println(alignment[0]);
        System.out.println(alignment[1]);
    }
}