import  ie.atu.forge.Similarity.Alignment.NeedlemanWunsch;
import ie.atu.forge.Similarity.Alignment.SeedAndExtend;
import ie.atu.forge.Similarity.Alignment.SmithWaterman;

public class main {
    public static void main(String[] args) {
        String query   = "AGTCGA";
        String subject = "TTAGTCGATT";
        int k = 3;

        String[] alignments = SeedAndExtend.align(subject, query, k);

        if(alignments == null) {
            System.out.println("Nada");
        } else {
            for(String align: alignments) {
                System.out.println(align);
            }

        }

    }
}