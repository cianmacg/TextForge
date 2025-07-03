import ie.atu.forge.Stemmers.Lovin;
import ie.atu.forge.Stemmers.Porter;

public class main {
    public static void main(String[] args) {
/*        String[] input = {
                "rubbing",
                "believe",
                "induction",
                "comsumption",
                "absorption",
                "recursive",
                "administrate",
                "parametric",
                "dissolved",
                "angular",
                "vibex",
                "index",
                "apex",
                "cortex",
                "anthrax",
                "matrix",
                "persuade",
                "evade",
                "decide",
                "elide",
                "deride",
                "expand",
                "defend",
                "respond",
                "collude",
                "obtrude",
                "adhere",
                "remit",
                "extent",
                "converted",
                "parenthetic",
                "analytic",
                "analyzed"
        };*/
        String[] input = {
                "movement"
        };

/*        String stem = Lovin.stem("rubbing");
        System.out.println(stem);*/
        String[] stems = Lovin.stem(input);

        for(String stem : stems) {
            System.out.println(stem);
        }

    }
}