import ie.atu.forge.Stemmers.Lovin;
import ie.atu.forge.Stemmers.Porter;

public class main {
    public static void main(String[] args) {
        String[] input = {
                "rubbing",
                "embedded",
                "believe",
                "induction",
                "consumption"
        };

/*        String stem = Lovin.stem("rubbing");
        System.out.println(stem);*/
        String[] stems = Lovin.stem(input);

        for(String stem : stems) {
            System.out.println(stem);
        }

    }
}