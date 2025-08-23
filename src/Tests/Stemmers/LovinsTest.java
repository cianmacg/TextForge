package Tests.Stemmers;

import ie.atu.forge.Stemmers.Lovins;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;

public class LovinsTest {

    @ParameterizedTest
    @DisplayName("Test Lovins stemming with various words")
    @CsvSource({
            "magnesia, magnes",
            "magnesite, magnes",
            "magnesian, magnes",
            "magnesium, magnes",
            "magnet, magnet",
            "magnetic, magnet",
            "magneto, magnet",
            "magnetically, magnet",
            "magnetism, magnet",
            "magnetite, magnet",
            "magnetitic, magnet",
            "magnetizable, magnet",
            "magnetization, magnet",
            "magnetize, magnet",
            "magnetometer, magnetometer",
            "magnetometric, magnetometer",
            "magnetometry, magnetometer",
            "magnetomotive, magnetomot",
            "magneton, magnet",
            "magnetostriction, magnetostrict",
            "magnetostrictive, magnetostrict",
            "magnetron, magnetron",
            "metal, metal",
            "metallic, metal",
            "metallically, metal",
            "metalliferous, metallifer",
            "metallize, metal",
            "metallurgical, metallurg",
            "metallurgy, metallurg",
            "induction, induc",
            "inductance, induc",
            "induced, induc",
            "angular, angl",
            "angle, angl"
    })
    public void testLovinsStemming(String input, String expected) {
        assertEquals(expected, Lovins.stem(input),
                "Failed to stem word: " + input);
    }

    @Test
    @DisplayName("Test magnes-related words stemming")
    public void testMagnesWordFamily() {
        String expectedStem = "magnes";
        assertEquals(expectedStem, Lovins.stem("magnesia"));
        assertEquals(expectedStem, Lovins.stem("magnesite"));
        assertEquals(expectedStem, Lovins.stem("magnesian"));
        assertEquals(expectedStem, Lovins.stem("magnesium"));
    }

    @Test
    @DisplayName("Test magnet-related words stemming")
    public void testMagnetWordFamily() {
        String expectedStem = "magnet";
        assertEquals(expectedStem, Lovins.stem("magnet"));
        assertEquals(expectedStem, Lovins.stem("magnetic"));
        assertEquals(expectedStem, Lovins.stem("magneto"));
        assertEquals(expectedStem, Lovins.stem("magnetically"));
        assertEquals(expectedStem, Lovins.stem("magnetism"));
        assertEquals(expectedStem, Lovins.stem("magnetite"));
        assertEquals(expectedStem, Lovins.stem("magnetitic"));
        assertEquals(expectedStem, Lovins.stem("magnetizable"));
        assertEquals(expectedStem, Lovins.stem("magnetization"));
        assertEquals(expectedStem, Lovins.stem("magnetize"));
        assertEquals(expectedStem, Lovins.stem("magneton"));
    }

    @Test
    @DisplayName("Test magnetometer-related words stemming")
    public void testMagnetometerWordFamily() {
        String expectedStem = "magnetometer";
        assertEquals(expectedStem, Lovins.stem("magnetometer"));
        assertEquals(expectedStem, Lovins.stem("magnetometric"));
        assertEquals(expectedStem, Lovins.stem("magnetometry"));
    }

    @Test
    @DisplayName("Test magnetostriction-related words stemming")
    public void testMagnetostrictionWordFamily() {
        String expectedStem = "magnetostrict";
        assertEquals(expectedStem, Lovins.stem("magnetostriction"));
        assertEquals(expectedStem, Lovins.stem("magnetostrictive"));
    }

    @Test
    @DisplayName("Test metal-related words stemming")
    public void testMetalWordFamily() {
        String expectedStem = "metal";
        assertEquals(expectedStem, Lovins.stem("metal"));
        assertEquals(expectedStem, Lovins.stem("metallic"));
        assertEquals(expectedStem, Lovins.stem("metallically"));
        assertEquals(expectedStem, Lovins.stem("metallize"));
    }

    @Test
    @DisplayName("Test metallurgy-related words stemming")
    public void testMetallurgyWordFamily() {
        String expectedStem = "metallurg";
        assertEquals(expectedStem, Lovins.stem("metallurgical"));
        assertEquals(expectedStem, Lovins.stem("metallurgy"));
    }

    @Test
    @DisplayName("Test induction-related words stemming")
    public void testInductionWordFamily() {
        String expectedStem = "induc";
        assertEquals(expectedStem, Lovins.stem("induction"));
        assertEquals(expectedStem, Lovins.stem("inductance"));
        assertEquals(expectedStem, Lovins.stem("induced"));
    }

    @Test
    @DisplayName("Test angle-related words stemming")
    public void testAngleWordFamily() {
        String expectedStem = "angl";
        assertEquals(expectedStem, Lovins.stem("angular"));
        assertEquals(expectedStem, Lovins.stem("angle"));
    }

    @Test
    @DisplayName("Test words that remain unchanged")
    public void testUnchangedWords() {
        // Words that should remain the same after stemming
        assertEquals("magnetron", Lovins.stem("magnetron"));
        assertEquals("metal", Lovins.stem("metal"));
        assertEquals("magnet", Lovins.stem("magnet"));
    }

    @Test
    @DisplayName("Test special compound words")
    public void testCompoundWords() {
        assertEquals("magnetomot", Lovins.stem("magnetomotive"));
        assertEquals("metallifer", Lovins.stem("metalliferous"));
        assertEquals("magnetometer", Lovins.stem("magnetometer"));
    }

    @Test
    @DisplayName("Test edge cases")
    public void testEdgeCases() {
        // Test null input - cast to String to resolve ambiguity
        assertThrows(Exception.class, () -> Lovins.stem((String) null));

        // Test empty string
        assertEquals("", Lovins.stem(""));

        // Test single character
        assertEquals("a", Lovins.stem("a"));
        String res = Lovins.stem("A");
        assertEquals("a", Lovins.stem("A")); // Test uppercase single char

        // Test short words
        assertEquals("cat", Lovins.stem("cat"));
        assertEquals("dog", Lovins.stem("dog"));
    }

    @Test
    @DisplayName("Test case sensitivity")
    public void testCaseSensitivity() {
        // Test uppercase - stems should always be lowercase
        assertEquals("magnet", Lovins.stem("MAGNETIC"));
        assertEquals("metal", Lovins.stem("METALLIC"));

        // Test mixed case - stems should always be lowercase
        assertEquals("magnet", Lovins.stem("Magnetic"));
        assertEquals("metal", Lovins.stem("Metallic"));
    }

    @Test
    @DisplayName("Test Lovins algorithm characteristics")
    public void testLovinsCharacteristics() {
        // Lovins algorithm is known for being less aggressive than Lancaster
        // Test some characteristic behaviors
        assertEquals("magnetometer", Lovins.stem("magnetometer")); // Long technical terms often preserved
        assertEquals("magnetron", Lovins.stem("magnetron")); // Specific technical terms preserved

        // But still stems common suffixes
        assertEquals("magnet", Lovins.stem("magnetic"));
        assertEquals("metal", Lovins.stem("metallic"));
    }

    @Test
    @DisplayName("Test performance with multiple calls")
    public void testPerformance() {
        String testWord = "magnetization";
        String expectedStem = "magnet";

        // Test multiple calls return consistent results
        for (int i = 0; i < 1000; i++) {
            assertEquals(expectedStem, Lovins.stem(testWord));
        }
    }

    @Test
    @DisplayName("Test scientific terminology handling")
    public void testScientificTerminology() {
        // Test how Lovins handles scientific/technical terms
        assertEquals("magnes", Lovins.stem("magnesia"));
        assertEquals("magnes", Lovins.stem("magnesium"));
        assertEquals("induc", Lovins.stem("induction"));
        assertEquals("induc", Lovins.stem("inductance"));
        assertEquals("angl", Lovins.stem("angular"));
    }

    @Test
    @DisplayName("Test suffix removal patterns")
    public void testSuffixRemovalPatterns() {
        // Test various suffix removal patterns characteristic of Lovins
        assertEquals("metal", Lovins.stem("metallic")); // -ic removal
        assertEquals("metal", Lovins.stem("metallically")); // -ically removal
        assertEquals("magnet", Lovins.stem("magnetism")); // -ism removal
        assertEquals("magnet", Lovins.stem("magnetize")); // -ize removal
        assertEquals("induc", Lovins.stem("induced")); // -ed removal
        assertEquals("induc", Lovins.stem("induction")); // -tion removal
    }
}