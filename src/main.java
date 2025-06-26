import ie.atu.forge.Stemmers.Porter;
import ie.atu.forge.Tokenisers.BPE;

public class main {
    public static void main(String[] args) {
        String input = "Verbosity";

        String stemmed_input = Porter.stem(input);
        System.out.println(stemmed_input);
    }
}