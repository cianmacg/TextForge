package test.java.Stemmers;

import main.java.ie.atu.forge.Stemmers.Porter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;

public class PorterTest {

    @ParameterizedTest
    @DisplayName("Test Porter stemming with various words")
    @CsvSource({
            "caresses, caress",
            "ponies, poni",
            "ties, ti",
            "caress, caress",
            "cats, cat",
            "feed, feed",
            "agreed, agre",
            "plastered, plaster",
            "bled, bled",
            "motoring, motor",
            "sing, sing",
            "conflated, conflat",
            "troubled, troubl",
            "sized, size",
            "hopping, hop",
            "tanned, tan",
            "falling, fall",
            "hissing, hiss",
            "filing, file",
            "happy, happi",
            "sky, sky",
            "relational, relat",
            "conditional, condit",
            "rational, ration",
            "valency, valenc",
            "hesitancy, hesit",
            "digitizer, digit",
            "conformity, conform",
            "radically, radic",
            "different, differ",
            "vile, vile",
            "argue, argu",
            "hopeful, hope",
            "goodness, good"
    })
    public void testPorterStemming(String input, String expected) {
        assertEquals(expected, Porter.stem(input),
                "Failed to stem word: " + input);
    }

    @Test
    @DisplayName("Test Step 1a - plural removal")
    public void testStep1aPluralRemoval() {
        // Test -es removal
        assertEquals("caress", Porter.stem("caresses"));

        // Test -ies → -i
        assertEquals("poni", Porter.stem("ponies"));
        assertEquals("ti", Porter.stem("ties"));

        // Test -s removal
        assertEquals("cat", Porter.stem("cats"));

        // Test no change
        assertEquals("caress", Porter.stem("caress"));
    }

    @Test
    @DisplayName("Test Step 1b - past tense and -ing removal")
    public void testStep1bPastTenseAndIng() {
        // Test -ed removal
        assertEquals("agre", Porter.stem("agreed"));
        assertEquals("plaster", Porter.stem("plastered"));
        assertEquals("conflat", Porter.stem("conflated"));
        assertEquals("troubl", Porter.stem("troubled"));
        assertEquals("size", Porter.stem("sized"));

        // Test -ing removal
        assertEquals("motor", Porter.stem("motoring"));
        assertEquals("fall", Porter.stem("falling"));
        assertEquals("hiss", Porter.stem("hissing"));
        assertEquals("file", Porter.stem("filing"));

        // Test double consonant removal
        assertEquals("hop", Porter.stem("hopping"));
        assertEquals("tan", Porter.stem("tanned"));

        // Test no change
        assertEquals("feed", Porter.stem("feed"));
        assertEquals("bled", Porter.stem("bled"));
        assertEquals("sing", Porter.stem("sing"));
    }

    @Test
    @DisplayName("Test Step 1c - y→i conversion")
    public void testStep1cYtoI() {
        assertEquals("happi", Porter.stem("happy"));
        assertEquals("sky", Porter.stem("sky")); // Short word unchanged
    }

    @Test
    @DisplayName("Test Step 2 - suffix replacement")
    public void testStep2SuffixReplacement() {
        // Test -ational → -ate
        assertEquals("relat", Porter.stem("relational"));

        // Test -al removal
        assertEquals("condit", Porter.stem("conditional"));
        assertEquals("ration", Porter.stem("rational"));

        // Test -ency → -ence
        assertEquals("valenc", Porter.stem("valency"));

        // Test -ancy → -ant
        assertEquals("hesit", Porter.stem("hesitancy"));

        // Test -izer → -ize
        assertEquals("digit", Porter.stem("digitizer"));

        // Test -ally → -al
        assertEquals("radic", Porter.stem("radically"));

        // Test -ent removal
        assertEquals("differ", Porter.stem("different"));
    }

    @Test
    @DisplayName("Test Step 4 - suffix removal")
    public void testStep4SuffixRemoval() {
        // Test -ity removal
        assertEquals("conform", Porter.stem("conformity"));

        // Test -ful removal
        assertEquals("hope", Porter.stem("hopeful"));

        // Test -ness removal
        assertEquals("good", Porter.stem("goodness"));
    }

    @Test
    @DisplayName("Test words that remain unchanged")
    public void testUnchangedWords() {
        assertEquals("caress", Porter.stem("caress"));
        assertEquals("feed", Porter.stem("feed"));
        assertEquals("bled", Porter.stem("bled"));
        assertEquals("sing", Porter.stem("sing"));
        assertEquals("sky", Porter.stem("sky"));
        assertEquals("vile", Porter.stem("vile"));
    }

    @Test
    @DisplayName("Test Porter algorithm steps progression")
    public void testPorterStepsProgression() {
        // Test complex words that go through multiple steps
        assertEquals("relat", Porter.stem("relational")); // Step 2: -ational → -ate
        assertEquals("hesit", Porter.stem("hesitancy"));   // Step 2: -ancy → -ant
        assertEquals("digit", Porter.stem("digitizer"));   // Step 2: -izer → -ize
        assertEquals("radic", Porter.stem("radically"));   // Step 2: -ally → -al
    }

    @Test
    @DisplayName("Test consonant doubling rules")
    public void testConsonantDoublingRules() {
        // Words where double consonants are reduced
        assertEquals("hop", Porter.stem("hopping"));
        assertEquals("tan", Porter.stem("tanned"));

        // Words where double consonants are preserved
        assertEquals("fall", Porter.stem("falling"));
        assertEquals("hiss", Porter.stem("hissing"));
        assertEquals("file", Porter.stem("filing"));
    }

    @Test
    @DisplayName("Test edge cases")
    public void testEdgeCases() {
        // Test null input - returns empty string
        assertEquals("", Porter.stem((String) null));

        // Test empty string
        assertEquals("", Porter.stem(""));

        // Test single character
        assertEquals("a", Porter.stem("a"));
        assertEquals("a", Porter.stem("A")); // Test uppercase single char

        // Test very short words
        assertEquals("i", Porter.stem("is")); // This is following the exact rules of porter stemmer.
        assertEquals("be", Porter.stem("be"));
        assertEquals("it", Porter.stem("it"));
    }

    @Test
    @DisplayName("Test case sensitivity")
    public void testCaseSensitivity() {
        // Test uppercase - stems should always be lowercase
        assertEquals("motor", Porter.stem("MOTORING"));
        assertEquals("happi", Porter.stem("HAPPY"));

        // Test mixed case - stems should always be lowercase
        assertEquals("motor", Porter.stem("Motoring"));
        assertEquals("happi", Porter.stem("Happy"));
    }

    @Test
    @DisplayName("Test Porter measure concept")
    public void testPorterMeasure() {
        // Porter algorithm uses "measure" (m) to determine stemming
        // Test words with different measures

        // m=0 words (short words, often unchanged)
        assertEquals("sky", Porter.stem("sky"));
        assertEquals("feed", Porter.stem("feed"));

        // m≥1 words (longer words, more likely to be stemmed)
        assertEquals("motor", Porter.stem("motoring"));
        assertEquals("relat", Porter.stem("relational"));
        assertEquals("digit", Porter.stem("digitizer"));
    }

    @Test
    @DisplayName("Test vowel-consonant patterns")
    public void testVowelConsonantPatterns() {
        // Test words with different vowel-consonant patterns
        assertEquals("argu", Porter.stem("argue"));   // ends with vowel
        assertEquals("hope", Porter.stem("hopeful")); // consonant-vowel pattern
        assertEquals("good", Porter.stem("goodness")); // consonant ending
    }

    @Test
    @DisplayName("Test performance with multiple calls")
    public void testPerformance() {
        String testWord = "relational";
        String expectedStem = "relat";

        // Test multiple calls return consistent results
        for (int i = 0; i < 1000; i++) {
            assertEquals(expectedStem, Porter.stem(testWord));
        }
    }

    @Test
    @DisplayName("Test Porter vs other algorithms characteristics")
    public void testPorterCharacteristics() {
        // Porter is moderate - less aggressive than Lancaster, more than basic

        // Should stem common suffixes
        assertEquals("motor", Porter.stem("motoring"));
        assertEquals("happi", Porter.stem("happy"));
        assertEquals("relat", Porter.stem("relational"));

        // But preserves some structure
        assertEquals("plaster", Porter.stem("plastered"));
        assertEquals("conflat", Porter.stem("conflated"));

        // Handles irregular cases
        assertEquals("bled", Porter.stem("bled")); // irregular past tense
    }

    @Test
    @DisplayName("Test common English word patterns")
    public void testCommonEnglishPatterns() {
        // Test how Porter handles common English morphological patterns

        // -ing removal
        assertEquals("motor", Porter.stem("motoring"));
        assertEquals("fall", Porter.stem("falling"));

        // -ed removal
        assertEquals("agre", Porter.stem("agreed"));
        assertEquals("size", Porter.stem("sized"));

        // -ly removal (through -ally)
        assertEquals("radic", Porter.stem("radically"));

        // -ness removal
        assertEquals("good", Porter.stem("goodness"));

        // -ful removal
        assertEquals("hope", Porter.stem("hopeful"));
    }
}