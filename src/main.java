import ie.atu.forge.Stemmers.Lovin;
import ie.atu.forge.Stemmers.Porter;

public class main {
    public static void main(String[] args) {
        String[] input = {
                "control",
                "controlling",
                "roll",
                "rolling"
        };

        String stem = Lovin.stem("automatically");

        System.out.println(stem);
    }
}