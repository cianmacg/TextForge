import ie.atu.forge.Stemmers.Porter;

public class main {
    public static void main(String[] args) {
        String[] input = {
                "control",
                "controlling",
                "roll",
                "rolling"
        };

        for(String i : input) {
            String stemmed_input = Porter.stem(i);
            System.out.println(stemmed_input);
        }

    }
}