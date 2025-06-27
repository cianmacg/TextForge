import ie.atu.forge.Stemmers.Porter;

public class main {
    public static void main(String[] args) {
        String[] input = {
                "relational",
                "conditional",
                "rational",
                "valenci",
                "hesitanci",
                "digitizer",
                "digitiser",
                "conformabli",
                "radicalli",
                "differentli",
                "vileli",
                "analogousli",
                "vietnamization",
                "vietnamisation",
                "predication",
                "operator",
                "feudalism",
                "decisiveness",
                "hopefulness",
                "callousness",
                "formaliti",
                "sensitiviti",
                "sensibiliti"
        };

        for(String i : input) {
            String stemmed_input = Porter.stem(i);
            System.out.println(stemmed_input);
        }

    }
}