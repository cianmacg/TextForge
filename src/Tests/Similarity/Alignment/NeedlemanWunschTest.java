package Tests.Similarity.Alignment;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import ie.atu.forge.Similarity.Alignment.NeedlemanWunsch;
import ie.atu.forge.ToolBox.MatrixLoader;

import java.io.IOException;
import java.util.*;

public class NeedlemanWunschTest {

    // Example helper: identity scoring for testing
    private Map<String, Integer> identityMatrix(int match, int mismatch) {
        Map<String, Integer> map = new HashMap<>();
        char[] bases = {'A', 'C', 'G', 'T'};
        for (char a : bases) {
            for (char b : bases) {
                map.put("" + a + b, (a == b) ? match : mismatch);
            }
        }
        // Optional wildcard fallback
        map.put("**", mismatch);
        return map;
    }

    @Test
    public void testPerfectMatch() {
        String s1 = "ACGT";
        String s2 = "ACGT";
        Map<String, Integer> scores = identityMatrix(2, -1);
        String[] result = NeedlemanWunsch.align(s1, s2, scores);
        assertEquals("ACGT", result[0]);
        assertEquals("ACGT", result[1]);
    }

    @Test
    public void testSingleMismatch() {
        String s1 = "ACGT";
        String s2 = "AGGT";
        Map<String, Integer> scores = identityMatrix(1, -1);
        String[] result = NeedlemanWunsch.align(s1, s2, scores);
        assertEquals(result[0].length(), result[1].length());
        assertEquals(4, result[0].length());
        assertEquals(1, countMismatches(result[0], result[1]));
    }

    @Test
    public void testGapAlignment() {
        String s1 = "ACGT";
        String s2 = "A--GT".replace("-", "");
        Map<String, Integer> scores = identityMatrix(1, -1);
        String[] result = NeedlemanWunsch.align(s1, s2, scores);
        assertEquals(result[0].length(), result[1].length());
        assertTrue(result[0].contains("-") || result[1].contains("-"));
    }

    @Test
    public void testEmptyInput() {
        String s1 = "";
        String s2 = "ACGT";
        Map<String, Integer> scores = identityMatrix(1, -1);
        String[] result = NeedlemanWunsch.align(s1, s2, scores);
        assertEquals("----", result[0]);  // all gaps
        assertEquals("ACGT", result[1]);
    }

    @Test
    public void testRealisticBlosumExample() throws IOException {
        String s1 = "MEEPQSDPSV";
        String s2 = "MEEPRSDPSV";
        Map<String, Integer> scores = loadBlosum45Stub();  // you will implement this
        String[] result = NeedlemanWunsch.align(s1, s2, scores);
        assertEquals(result[0].length(), result[1].length());
        // For sanity, check they have some mismatches but mostly match
        int mismatches = countMismatches(result[0], result[1]);
        assertTrue(mismatches >= 1 && mismatches <= 3);
    }

    /*
     Test for standard, non-substitution matrix algorithm.
     */
    @Test
    public void testPerfectMatchDefault() {
        String s1 = "GATTACA";
        String s2 = "GATTACA";
        String[] result = NeedlemanWunsch.align(s1, s2);
        assertEquals("GATTACA", result[0]);
        assertEquals("GATTACA", result[1]);
    }

    @Test
    public void testSingleMismatchDefault() {
        String s1 = "GATTACA";
        String s2 = "GACTACA";  // 1 mismatch at position 2
        String[] result = NeedlemanWunsch.align(s1, s2);
        assertEquals(result[0].length(), result[1].length());
        assertEquals(1, countMismatches(result[0], result[1]));
    }

    @Test
    public void testInsertionDefault() {
        String s1 = "GATTACA";
        String s2 = "GATACA";  // Missing 'T'
        String[] result = NeedlemanWunsch.align(s1, s2);
        assertEquals(result[0].length(), result[1].length());
        assertTrue(result[0].contains("-") || result[1].contains("-"));
        // The shorter string should have a gap
        assertEquals(result[0].length(), 7);
    }

    @Test
    public void testDeletionDefault() {
        String s1 = "GATACA";
        String s2 = "GATTACA";  // Extra 'T'
        String[] result = NeedlemanWunsch.align(s1, s2);
        assertEquals(result[0].length(), result[1].length());
        assertTrue(result[0].contains("-") || result[1].contains("-"));
        assertEquals(result[0].length(), 7);
    }

    @Test
    public void testEmptyVsSequenceDefault() {
        String s1 = "";
        String s2 = "ACGT";
        String[] result = NeedlemanWunsch.align(s1, s2);
        assertEquals("----", result[0]);
        assertEquals("ACGT", result[1]);
    }

    @Test
    public void testTwoCompletelyDifferentStrings() {
        String s1 = "AAAA";
        String s2 = "TTTT";
        String[] result = NeedlemanWunsch.align(s1, s2);
        assertEquals(result[0].length(), result[1].length());
        assertEquals(4, countMismatches(result[0], result[1]));
    }

    // Helper to count mismatches in aligned sequences
    private int countMismatches(String a, String b) {
        int mismatches = 0;
        for (int i = 0; i < a.length(); i++) {
            if (a.charAt(i) != b.charAt(i)) mismatches++;
        }
        return mismatches;
    }

    // Stub for your real BLOSUM45 map
    private Map<String, Integer> loadBlosum45Stub() throws IOException {
        Map<String, Integer> stub = MatrixLoader.load("./src/ScoringMatrices/BLOSUM45.txt");
        return stub;
    }
}
