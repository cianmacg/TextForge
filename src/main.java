import ie.atu.forge.Stemmers.Porter;

public class main {
    public static void main(String[] args) {
        String[] input = {
                "revival",
                "allowance",
                "inference",
                "airliner",
                "gyroscopic",
                "adjustable",
                "defensible",
                "irritant",
                "replacement",
                "adjustment",
                "dependent",
                "adoption",
                "homologou",
                "communism",
                "activate",
                "angulariti",
                "homologous",
                "effective",
                "bowdlerize"
        };

        for(String i : input) {
            String stemmed_input = Porter.stem(i);
            System.out.println(stemmed_input);
        }

    }
}