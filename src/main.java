import ie.atu.forge.Stemmers.Porter;

public class main {
    public static void main(String[] args) {
        String[] input = {
                "triplicate",
                "formative",
                "formalize",
                "formalise",
                "electriciti",
                "electrical",
                "hopeful",
                "goodness"
        };

        for(String i : input) {
            String stemmed_input = Porter.stem(i);
            System.out.println(stemmed_input);
        }

    }
}