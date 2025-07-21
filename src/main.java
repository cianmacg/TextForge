import java.io.IOException;

import  ie.atu.forge.Tokenisers.BPE2;

public class main {
    public static void main(String[] args) throws IOException {
        BPE2 tokens = new BPE2();
        String corpus = "Hey this is a corpus boy whaddup.";
        tokens.train(corpus, 200);
        int[] new_tok = tokens.encode("whaddup");
        String decoded = tokens.decode(new_tok);
        System.out.println(decoded);
    }
}