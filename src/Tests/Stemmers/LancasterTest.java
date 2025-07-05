package Tests.Stemmers;

import ie.atu.forge.Stemmers.Lancaster;
import ie.atu.forge.Stemmers.Lovin;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LancasterTest {
    public static void main(String[] args) {
        String[] inputs = {
                "running",
                "happiness",
                "relational",
                "friendliness",
                "corruptive",
                "adjustment",
                "compute",
                "computed",
                "computing",
                "computer",
                "organization",
                "insensitive",
                "triplicate",
                "triplication",
                "frustrating",
                "frustration",
                "decisive",
                "decision",
                "conclusive",
                "conclusion",
                "nationalism",
                "rationalize",
                "rationalization",
                "easily",
                "easier",
                "flying",
                "crying",
                "denied",
                "denial",
                "dies",
                "dying",
                "sky",
                "skies"
        };

        String[] expected = {
                "run",
                "happy",
                "rel",
                "friend",
                "corrupt",
                "adjust",
                "comput",
                "comput",
                "comput",
                "comput",
                "org",
                "insensit",
                "triply",
                "triply",
                "frust",
                "frust",
                "decid",
                "decid",
                "conclud",
                "conclud",
                "nat",
                "rat",
                "rat",
                "easy",
                "easy",
                "fly",
                "cry",
                "deny",
                "den",
                "die",
                "dying",
                "sky",
                "sky"
        };

        for (int i = 0; i < inputs.length; i++) {
            String input = inputs[i];
            String expectedStem = expected[i];
            assertEquals(expectedStem, Lancaster.stem(input), "Failed at index " + i + " for word: " + input);
        }
    }
}
