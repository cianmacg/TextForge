import ie.atu.forge.Tokenisers.BPE;

public class main {
    public static void main(String[] args) {
        String s1 = "hello world this is a new attempt at stuff.";
        String s2 = "and here is another one for good measure.";
        String s3 = "let's continue with another sentence. This time it has some numbers as well. 1 2 3 4 5, the first 5 numbers. Then we also have 6 7 8 9. We mustn't forget to include 0.";
        String[] corpus = new String[3];
        corpus[0] = s1;
        corpus[1] = s2;
        corpus[2] = s3;
        BPE tokeniser = new BPE();
        tokeniser.train(corpus, 60);

        String s = "A simple test of tokenisation. Ideally we should see some tokens here with a length greater than 1. There should really be many.";

        String[] output = tokeniser.tokenise(s);

        for(String token: output) {
            System.out.println(token);
        }

        System.out.println(tokeniser.getVocabulary());
    }
}