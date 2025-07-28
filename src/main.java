import java.io.IOException;

import ie.atu.forge.Tokenisers.BPE;

public class main {
    public static void main(String[] args) throws IOException {
        BPE tokens = new BPE();
        String corpus = "This is a corpus. This is going to be a bit larger to allow for better training. I think.";
        String test = "And here I am testing the encoding with a sample string.";
        tokens.train(corpus, 50);
        int[] encoding = tokens.encode(test);
        System.out.println(tokens.decode(encoding));
        tokens.vocabToJson();
    }
}