package Tests.Stemmers;

import ie.atu.forge.Stemmers.Porter;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PorterTest {
    public static void main(String[] args) {
        String[] inputs = {
                "caresses",   // plural
                "ponies",     // plural + y->i
                "ties",       // plural + y->i
                "caress",     // no stemming
                "cats",       // plural
                "feed",       // step 1b (double e)
                "agreed",     // step 1b
                "plastered",  // step 1b
                "bled",       // irregular
                "motoring",   // step 1b*
                "sing",       // not stemmed
                "conflated",  // step 2
                "troubled",   // step 1b
                "sized",      // step 1b
                "hopping",    // step 1b*
                "tanned",     // step 1b*
                "falling",    // step 1b*
                "hissing",    // step 1b*
                "filing",     // step 1b*
                "happy",      // step 1c
                "sky",        // step 1c (should remain)
                "relational", // step 2
                "conditional",
                "rational",
                "valency",
                "hesitancy",
                "digitizer",
                "conformity",
                "radically",
                "different",
                "vile",
                "argue",
                "hopeful",
                "goodness"
        };

        String[] expected = {
                "caress",   // -es → caress
                "poni",     // -ies → -i
                "ti",       // -ies → -i
                "caress",   // already stemmed
                "cat",      // -s removed
                "feed",     // no change (short word)
                "agre",     // -ed → agre
                "plaster",  // -ed → plaster
                "bled",     // irregular (no change)
                "motor",    // -ing → motor
                "sing",     // no change
                "conflat", // -ed → conflate
                "troubl",   // -ed → troubl
                "size",     // -ed → size
                "hop",      // double p dropped
                "tan",      // double n dropped
                "fall",     // no simplification needed
                "hiss",     // no simplification needed
                "file",     // no simplification
                "happi",    // -y → -i
                "sky",      // short word, unchanged
                "relat",   // -ational → -ate
                "condit",// -al → -ion
                "ration",   // -al → -ion
                "valenc",   // -y → -e, then -ency → -enc
                "hesit",    // -ancy → -ant → - hesit
                "digit",    // -izer → -ize → - digit
                "conform",  // -ity → - conform
                "radic",    // -ally → -al → radic
                "differ",   // -ent → - differ
                "vile",     // already root
                "argu",     // -e remains
                "hope",     // -ful → hope
                "good"      // -ness → good
        };


        for (int i = 0; i < inputs.length; i++) {
            String input = inputs[i];
            String expectedStem = expected[i];
            assertEquals(expectedStem, Porter.stem(input), "Failed at index " + i + " for word: " + input);
        }
    }
}
