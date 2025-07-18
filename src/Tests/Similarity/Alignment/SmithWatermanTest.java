package Tests.Similarity.Alignment;

import ie.atu.forge.Similarity.Alignment.SmithWaterman;

import ie.atu.forge.ToolBox.MatrixLoader;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.*;

public class SmithWatermanTest {

    // Default scores (used when no matrix is passed)
    private static final int MATCH = 2;
    private static final int MISMATCH = -1;
    private static final int GAP = -2;

    // --- Tests for default scoring ---
    @Test
    public void testPerfectMatchDefault() {
        String s1 = "GATTACA";
        String s2 = "GATTACA";
        String[] result = SmithWaterman.align(s1, s2);
        assertEquals("GATTACA", result[0]);
        assertEquals("GATTACA", result[1]);
    }

    @Test
    public void testSingleMismatchDefault() {
        String s1 = "GATTACA";
        String s2 = "GACTACA";  // mismatch at position 2
        String[] result = SmithWaterman.align(s1, s2);
        assertEquals(result[0].length(), result[1].length());
        assertEquals(1, countMismatches(result[0], result[1]));
    }

    @Test
    public void testGapIntroductionDefault() {
        String s1 = "GATTACA";
        String s2 = "GATACA";  // one 'T' missing
        String[] result = SmithWaterman.align(s1, s2);
        assertEquals(result[0].length(), result[1].length());
        assertTrue(result[0].contains("-") || result[1].contains("-"));
    }

    @Test
    public void testPartialLocalAlignmentDefault() {
        String s1 = "ACGTGGG";
        String s2 = "TTACGT";
        String[] result = SmithWaterman.align(s1, s2);
        // Should align "ACGT" locally, not entire strings
        assertTrue(result[0].contains("ACGT"));
        assertTrue(result[1].contains("ACGT"));
        assertEquals(result[0].length(), result[1].length());
    }

    // --- Tests for substitution matrix (BLOSUM45) ---
    @Test
    public void testBlosumPerfectMatch() throws IOException {
        String s1 = "MEEPQSDPSV";
        String s2 = "MEEPQSDPSV";
        Map<String, Integer> scores = loadBlosum45Stub();
        String[] result = SmithWaterman.align(s1, s2, scores);
        assertEquals(s1, result[0]);
        assertEquals(s2, result[1]);
    }

    @Test
    public void testBlosumWithMismatch() throws IOException {
        String s1 = "MEEPQSDPSV";
        String s2 = "MEEPRSDPSV";  // one amino acid mismatch
        Map<String, Integer> scores = loadBlosum45Stub();
        String[] result = SmithWaterman.align(s1, s2, scores);
        assertEquals(result[0].length(), result[1].length());
        int mismatches = countMismatches(result[0], result[1]);
        assertTrue(mismatches >= 1 && mismatches <= 3);
    }

    @Test
    public void testBlosumPartialOverlap() throws IOException {
        String s1 = "VVVVVME";
        String s2 = "MEVVVVV";
        Map<String, Integer> scores = loadBlosum45Stub();
        String[] result = SmithWaterman.align(s1, s2, scores);

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
        return MatrixLoader.load("./src/ScoringMatrices/BLOSUM45.txt");
    }
}

