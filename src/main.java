import ie.atu.forge.Stemmers.Porter;

public class main {
    public static void main(String[] args) {
        String[] input = {
                "control",
                "controlling",
                "roll",
                "rolling"
        };

        String[] stems = Porter.stem(input);

        for(String stem : stems) {
            System.out.println(stem);
        }

    }
}