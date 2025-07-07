import ie.atu.forge.Normalisation.Scrubber;
import ie.atu.forge.SetSimilarity.AlignmentFree.Cosine;
import ie.atu.forge.Tokenisers.Ngram;
import ie.atu.forge.Vectorisers.BagOfWords;

public class main {
    public static void main(String[] args) {
        String s1 = "Hey, this is a sample sentence that is going to be used to test my similarity measures";
        String s2 = "Well, this is another sample sentence that also will be used for testing.";
        String s3 = "Very different sentence from the other 2 which should have little in common.";

        String[] t1 = Scrubber.scrub(s1).split(" ");
        String[] t2 = Scrubber.scrub(s2).split(" ");
        String[] t3 = Scrubber.scrub(s3).split(" ");

        BagOfWords b1 = new BagOfWords();
        BagOfWords b2 = new BagOfWords();
        BagOfWords b3 = new BagOfWords();

        b1.addBulk(t1);
        b2.addBulk(t2);
        b3.addBulk(t3);


        double similarity = Cosine.similarity(b1.getBag(), b3.getBag());

        System.out.println(similarity);
    }
}