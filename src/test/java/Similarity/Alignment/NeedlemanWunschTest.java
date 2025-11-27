package test.java.Similarity.Alignment;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import main.java.ie.atu.forge.Similarity.Alignment.NeedlemanWunsch;
import main.java.ie.atu.forge.ToolBox.MatrixLoader;

import java.io.IOException;
import java.util.*;

public class NeedlemanWunschTest {

    private Map<String, Integer> identityMatrix(int match, int mismatch) {
        Map<String, Integer> map = new HashMap<>();
        char[] bases = {'A', 'C', 'G', 'T'};
        for (char a : bases) {
            for (char b : bases) {
                map.put("" + a + b, (a == b) ? match : mismatch);
            }
        }
        map.put("**", mismatch);
        return map;
    }

    @Test
    public void testPerfectMatch() {
        NeedlemanWunsch nw = new NeedlemanWunsch();
        nw.setScoringMatrix(identityMatrix(2, -1));
        String[] result = nw.align("ACGT", "ACGT", true);
        assertEquals("ACGT", result[0]);
        assertEquals("ACGT", result[1]);
    }

    @Test
    public void testSingleMismatch() {
        NeedlemanWunsch nw = new NeedlemanWunsch();
        nw.setScoringMatrix(identityMatrix(1, -1));
        String[] result = nw.align("ACGT", "AGGT", true);
        assertEquals(result[0].length(), result[1].length());
        assertEquals(4, result[0].length());
        assertEquals(1, countMismatches(result[0], result[1]));
    }

    @Test
    public void testGapAlignment() {
        NeedlemanWunsch nw = new NeedlemanWunsch();
        nw.setScoringMatrix(identityMatrix(1, -1));
        String s1 = "ACGT";
        String s2 = "AGT";
        String[] result = nw.align(s1, s2, true);
        assertEquals(result[0].length(), result[1].length());
        assertTrue(result[0].contains("-") || result[1].contains("-"));
    }

    @Test
    public void testEmptyInput() {
        NeedlemanWunsch nw = new NeedlemanWunsch();
        nw.setScoringMatrix(identityMatrix(1, -1));
        String[] result = nw.align("", "ACGT", true);
        assertEquals("----", result[0]);
        assertEquals("ACGT", result[1]);
    }

    @Test
    public void testRealisticBlosumExample() throws IOException {
        NeedlemanWunsch nw = new NeedlemanWunsch();
        nw.setScoringMatrix(loadBlosum45Stub());
        String[] result = nw.align("MEEPQSDPSV", "MEEPRSDPSV", true);
        assertEquals(result[0].length(), result[1].length());
        int mismatches = countMismatches(result[0], result[1]);
        assertTrue(mismatches >= 1 && mismatches <= 3);
    }

    @Test
    public void testPerfectMatchDefault() {
        NeedlemanWunsch nw = new NeedlemanWunsch();
        String[] result = nw.align("GATTACA", "GATTACA");
        assertEquals("GATTACA", result[0]);
        assertEquals("GATTACA", result[1]);
    }

    @Test
    public void testSingleMismatchDefault() {
        NeedlemanWunsch nw = new NeedlemanWunsch();
        String[] result = nw.align("GATTACA", "GACTACA");
        assertEquals(result[0].length(), result[1].length());
        assertEquals(1, countMismatches(result[0], result[1]));
    }

    @Test
    public void testInsertionDefault() {
        NeedlemanWunsch nw = new NeedlemanWunsch();
        String[] result = nw.align("GATTACA", "GATACA");
        assertEquals(result[0].length(), result[1].length());
        assertTrue(result[0].contains("-") || result[1].contains("-"));
        assertEquals(7, result[0].length());
    }

    @Test
    public void testDeletionDefault() {
        NeedlemanWunsch nw = new NeedlemanWunsch();
        String[] result = nw.align("GATACA", "GATTACA");
        assertEquals(result[0].length(), result[1].length());
        assertTrue(result[0].contains("-") || result[1].contains("-"));
        assertEquals(7, result[0].length());
    }

    @Test
    public void testEmptyVsSequenceDefault() {
        NeedlemanWunsch nw = new NeedlemanWunsch();
        String[] result = nw.align("", "ACGT");
        assertEquals("----", result[0]);
        assertEquals("ACGT", result[1]);
    }

    @Test
    public void testTwoCompletelyDifferentStrings() {
        NeedlemanWunsch nw = new NeedlemanWunsch();
        String[] result = nw.align("AAAA", "TTTT");
        assertEquals(result[0].length(), result[1].length());
        assertEquals(4, countMismatches(result[0], result[1]));
    }

    private int countMismatches(String a, String b) {
        int mismatches = 0;
        for (int i = 0; i < a.length(); i++) {
            if (a.charAt(i) != b.charAt(i)) mismatches++;
        }
        return mismatches;
    }

    private Map<String, Integer> loadBlosum45Stub() throws IOException {
        return MatrixLoader.load("./ScoringMatrices/BLOSUM45.txt");
    }
}
