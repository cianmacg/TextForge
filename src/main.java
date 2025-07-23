import java.io.IOException;

import  ie.atu.forge.Tokenisers.Unigram;

public class main {
    public static void main(String[] args) throws IOException {
        Unigram tokens = new Unigram(5);
        String corpus = "This is a corpus. This is going to be a bit larger to allow for better training. I think.";
        tokens.train(corpus, 200);
    }
}