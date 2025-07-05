import ie.atu.forge.Stemmers.Lancaster;
import ie.atu.forge.Stemmers.Lovin;
import ie.atu.forge.Stemmers.Porter;

public class main {
    public static void main(String[] args) {
        String[] input = {
                "skies"
        };

/*        String stem = Lovin.stem("rubbing");
        System.out.println(stem);*/
        String[] stems = Lancaster.stem(input);

        for(String stem : stems) {
            System.out.println(stem);
        }

    }
}