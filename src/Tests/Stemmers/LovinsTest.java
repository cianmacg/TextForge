package Tests.Stemmers;

import static org.junit.jupiter.api.Assertions.*;
import ie.atu.forge.Stemmers.Lovins;

public class LovinsTest {
    public static void main(String[] args) {
        String[] inputs = {
                "magnesia",
                "magnesite",
                "magnesian",
                "magnesium",
                "magnet",
                "magnetic",
                "magneto",
                "magnetically",
                "magnetism",
                "magnetite",
                "magnetitic",
                "magnetizable",
                "magnetization",
                "magnetize",
                "magnetometer",
                "magnetometric",
                "magnetometry",
                "magnetomotive",
                "magneton",
                "magnetostriction",
                "magnetostrictive",
                "magnetron",
                "metal",
                "metallic",
                "metallically",
                "metalliferous",
                "metallize",
                "metallurgical",
                "metallurgy",
                "induction",
                "inductance",
                "induced",
                "angular",
                "angle"
        };

        //        String[] inputs = {
        //                "believable",
        //                //"movement",
        //                //"happiness",
        //                //"national",
        //                "activation",
        //                "registration",
        //                "kilometer",
        //                "conductive",
        //                "assumption",
        //                //"corruptive",
        //                "cursiveness",
        //                "planned",
        //                "stopped",
        //                "running",
        //                "friendliness"
        //        };

        String[] expected = {
                "magnes",
                "magnes",
                "magnes",
                "magnes",
                "magnet",
                "magnet",
                "magnet",
                "magnet",
                "magnet",
                "magnet",
                "magnet",
                "magnet",
                "magnet",
                "magnet",
                "magnetometer",
                "magnetometer",
                "magnetometer",
                "magnetomot",
                "magnet",
                "magnetostrict",
                "magnetostrict",
                "magnetron",
                "metal",
                "metal",
                "metal",
                "metallifer",
                "metal",
                "metallurg",
                "metallurg",
                "induc",
                "induc",
                "induc",
                "angl",
                "angl"
        };

//        String[] expected = {
//                "belief",
//                //"move",
//                //"happy",
//                //"nation",
//                "activ",
//                "register",
//                "kilometer",
//                "conduc",
//                "assum",
//                //"corrub",
//                "cur",
//                "plan",
//                "stop",
//                "run",
//                "friend"
//        };

        for (int i = 0; i < inputs.length; i++) {
            String input = inputs[i];
            String expectedStem = expected[i];
            assertEquals(expectedStem, Lovins.stem(input), "Failed at index " + i + " for word: " + input);
        }
    }
}
