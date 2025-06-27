import ie.atu.forge.Stemmers.Porter;
import ie.atu.forge.Tokenisers.BPE;

public class main {
    public static void main(String[] args) {
        String[] input = {
                "feed",
                "agreed",
                "plastered",
                "bled",
                "motoring",
                "sing"
        };

        for(String i : input) {
            String stemmed_input = Porter.stem(i);
            System.out.println(stemmed_input);
        }

    }
}