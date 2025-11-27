package test.java.Stemmers;

import ie.atu.forge.Stemmers.Lancaster;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;

public class LancasterTest {

    @ParameterizedTest
    @DisplayName("Test Lancaster stemming with various words")
    @CsvSource({
            "running, run",
            "happiness, happy",
            "relational, rel",
            "friendliness, friend",
            "corruptive, corrupt",
            "adjustment, adjust",
            "compute, comput",
            "computed, comput",
            "computing, comput",
            "computer, comput",
            "organization, org",
            "insensitive, insensit",
            "triplicate, triply",
            "triplication, triply",
            "frustrating, frust",
            "frustration, frust",
            "decisive, decid",
            "decision, decid",
            "conclusive, conclud",
            "conclusion, conclud",
            "nationalism, nat",
            "rationalize, rat",
            "rationalization, rat",
            "easily, easy",
            "easier, easy",
            "flying, fly",
            "crying, cry",
            "denied, deny",
            "denial, den",
            "dies, die",
            "dying, dying",
            "sky, sky",
            "skies, sky"
    })
    public void testLancasterStemming(String input, String expected) {
        assertEquals(expected, Lancaster.stem(input),
                "Failed to stem word: " + input);
    }

    @Test
    @DisplayName("Test compute-related words stemming")
    public void testComputeWordFamily() {
        String expectedStem = "comput";
        assertEquals(expectedStem, Lancaster.stem("compute"));
        assertEquals(expectedStem, Lancaster.stem("computed"));
        assertEquals(expectedStem, Lancaster.stem("computing"));
        assertEquals(expectedStem, Lancaster.stem("computer"));
    }

    @Test
    @DisplayName("Test frustration-related words stemming")
    public void testFrustrationWordFamily() {
        String expectedStem = "frust";
        assertEquals(expectedStem, Lancaster.stem("frustrating"));
        assertEquals(expectedStem, Lancaster.stem("frustration"));
    }

    @Test
    @DisplayName("Test decision-related words stemming")
    public void testDecisionWordFamily() {
        String expectedStem = "decid";
        assertEquals(expectedStem, Lancaster.stem("decisive"));
        assertEquals(expectedStem, Lancaster.stem("decision"));
    }

    @Test
    @DisplayName("Test conclusion-related words stemming")
    public void testConclusionWordFamily() {
        String expectedStem = "conclud";
        assertEquals(expectedStem, Lancaster.stem("conclusive"));
        assertEquals(expectedStem, Lancaster.stem("conclusion"));
    }

    @Test
    @DisplayName("Test rationalization-related words stemming")
    public void testRationalizationWordFamily() {
        String expectedStem = "rat";
        assertEquals(expectedStem, Lancaster.stem("rationalize"));
        assertEquals(expectedStem, Lancaster.stem("rationalization"));
    }

    @Test
    @DisplayName("Test triplicate-related words stemming")
    public void testTriplicateWordFamily() {
        String expectedStem = "triply";
        assertEquals(expectedStem, Lancaster.stem("triplicate"));
        assertEquals(expectedStem, Lancaster.stem("triplication"));
    }

    @Test
    @DisplayName("Test easy-related words stemming")
    public void testEasyWordFamily() {
        String expectedStem = "easy";
        assertEquals(expectedStem, Lancaster.stem("easily"));
        assertEquals(expectedStem, Lancaster.stem("easier"));
    }

    @Test
    @DisplayName("Test flying/crying pattern stemming")
    public void testFlyingCryingPattern() {
        assertEquals("fly", Lancaster.stem("flying"));
        assertEquals("cry", Lancaster.stem("crying"));
    }

    @Test
    @DisplayName("Test denial-related words stemming")
    public void testDenialWordFamily() {
        assertEquals("deny", Lancaster.stem("denied"));
        assertEquals("den", Lancaster.stem("denial"));
        assertEquals("die", Lancaster.stem("dies"));
        assertEquals("dying", Lancaster.stem("dying"));
    }

    @Test
    @DisplayName("Test sky-related words stemming")
    public void testSkyWordFamily() {
        assertEquals("sky", Lancaster.stem("sky"));
        assertEquals("sky", Lancaster.stem("skies"));
    }

    @Test
    @DisplayName("Test edge cases")
    public void testEdgeCases() {
        // Test null input - cast to String to resolve ambiguity
        assertThrows(Exception.class, () -> Lancaster.stem((String) null));

        // Test empty string
        assertEquals("", Lancaster.stem(""));

        // Test single character
        assertEquals("a", Lancaster.stem("a"));
        assertEquals("a", Lancaster.stem("A")); // Test uppercase single char

        // Test already stemmed words
        assertEquals("run", Lancaster.stem("run"));
        assertEquals("happy", Lancaster.stem("happy"));
    }

    @Test
    @DisplayName("Test case sensitivity")
    public void testCaseSensitivity() {
        // Test uppercase - stems should always be lowercase
        assertEquals("run", Lancaster.stem("RUNNING"));
        assertEquals("happy", Lancaster.stem("HAPPINESS"));

        // Test mixed case - stems should always be lowercase
        assertEquals("run", Lancaster.stem("Running"));
        assertEquals("happy", Lancaster.stem("Happiness"));
    }

    @Test
    @DisplayName("Test words that should not be stemmed")
    public void testUnchangedWords() {
        String[] unchangedWords = {"sky", "dying"};
        for (String word : unchangedWords) {
            assertEquals(word, Lancaster.stem(word),
                    "Word should remain unchanged: " + word);
        }
    }

    @Test
    @DisplayName("Test aggressive stemming examples")
    public void testAggressiveStemming() {
        // Lancaster is known for aggressive stemming
        assertEquals("org", Lancaster.stem("organization"));
        assertEquals("nat", Lancaster.stem("nationalism"));
        assertEquals("insensit", Lancaster.stem("insensitive"));
    }

    @Test
    @DisplayName("Test performance with multiple calls")
    public void testPerformance() {
        String testWord = "rationalization";
        String expectedStem = "rat";

        // Test multiple calls return consistent results
        for (int i = 0; i < 1000; i++) {
            assertEquals(expectedStem, Lancaster.stem(testWord));
        }
    }
}