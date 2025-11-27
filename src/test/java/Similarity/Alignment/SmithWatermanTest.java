package test.java.Similarity.Alignment;

import ie.atu.forge.Similarity.Alignment.SmithWaterman;
import ie.atu.forge.ToolBox.MatrixLoader;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class SmithWatermanTest {

    // --- Tests for default scoring ---
    @Test
    public void testPerfectMatchDefault() {
        SmithWaterman sw = new SmithWaterman();
        String s1 = "GATTACA";
        String s2 = "GATTACA";
        String[] result = sw.align(s1, s2);
        assertEquals("GATTACA", result[0]);
        assertEquals("GATTACA", result[1]);
    }

    @Test
    public void testSingleMismatchDefault() {
        SmithWaterman sw = new SmithWaterman();
        String s1 = "GATTACA";
        String s2 = "GACTACA";  // mismatch at position 2
        String[] result = sw.align(s1, s2);
        assertEquals(result[0].length(), result[1].length());
        assertEquals(1, countMismatches(result[0], result[1]));
    }

    @Test
    public void testGapIntroductionDefault() {
        SmithWaterman sw = new SmithWaterman();
        String s1 = "GATTACA";
        String s2 = "GATACA";  // one 'T' missing
        String[] result = sw.align(s1, s2);
        assertEquals(result[0].length(), result[1].length());
        assertTrue(result[0].contains("-") || result[1].contains("-"));
    }

    @Test
    public void testPartialLocalAlignmentDefault() {
        SmithWaterman sw = new SmithWaterman();
        String s1 = "ACGTGGG";
        String s2 = "TTACGT";
        String[] result = sw.align(s1, s2);
        assertTrue(result[0].contains("ACGT"));
        assertTrue(result[1].contains("ACGT"));
        assertEquals(result[0].length(), result[1].length());
    }

    // --- Tests for substitution matrix (BLOSUM45) ---
    @Test
    public void testBlosumPerfectMatch() throws IOException {
        SmithWaterman sw = new SmithWaterman();
        sw.setScoringMatrix(loadBlosum45Stub());
        String s1 = "MEEPQSDPSV";
        String s2 = "MEEPQSDPSV";
        String[] result = sw.align(s1, s2);
        assertEquals(s1, result[0]);
        assertEquals(s2, result[1]);
    }

    @Test
    public void testBlosumWithMismatch() throws IOException {
        SmithWaterman sw = new SmithWaterman();
        sw.setScoringMatrix(loadBlosum45Stub());
        String s1 = "MEEPQSDPSV";
        String s2 = "MEEPRSDPSV";  // one amino acid mismatch
        String[] result = sw.align(s1, s2);
        assertEquals(result[0].length(), result[1].length());
        int mismatches = countMismatches(result[0], result[1]);
        assertTrue(mismatches >= 1 && mismatches <= 3);
    }

    @Test
    public void testBlosumPartialOverlap() throws IOException {
        SmithWaterman sw = new SmithWaterman();
        sw.setScoringMatrix(loadBlosum45Stub());
        String s1 = "VVVVVME";
        String s2 = "MEVVVVV";
        String[] result = sw.align(s1, s2);

        assertTrue(result[0].contains("VVVVV") && result[1].contains("VVVVV"));
        assertEquals(result[0].length(), result[1].length());
    }

    // --- Helper methods ---
    private int countMismatches(String a, String b) {
        int mismatches = 0;
        for (int i = 0; i < a.length(); i++) {
            if (a.charAt(i) != b.charAt(i) && a.charAt(i) != '-' && b.charAt(i) != '-') {
                mismatches++;
            }
        }
        return mismatches;
    }

    private Map<String, Integer> loadBlosum45Stub() throws IOException {
        return MatrixLoader.load("./ScoringMatrices/BLOSUM45.txt");
    }
}
