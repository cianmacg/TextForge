package ie.atu.forge.Similarity.AlignmentFree;

import ie.atu.forge.Vectorisers.BagOfWords;
public class Tversky {
    public static double distance(BagOfWords b1, BagOfWords b2) {
        return 0.0d;
    }

    public static double similarity(BagOfWords b1, BagOfWords b2) {
        return 1 - distance(b1, b2);
    }
}
