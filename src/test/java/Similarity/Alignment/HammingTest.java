package test.java.Similarity.Alignment;

import main.java.ie.atu.forge.Similarity.Alignment.Hamming;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class HammingTest {

    @Test
    public void testExactMatch() {
        assertEquals(0, Hamming.distance("karolin", "karolin"));
        assertEquals(0, Hamming.distance("", ""));  // Both empty
    }

    @Test
    public void testSimpleDifferences() {
        assertEquals(3, Hamming.distance("karolin", "kathrin"));
        // Positions differing: 3 ('r'->'t', 'o'->'h', 'l'->'r')

        assertEquals(2, Hamming.distance("1011101", "1001001"));
        // Bits differing: positions 2, 4, 6
    }

    @Test
    public void testAllDifferent() {
        assertEquals(7, Hamming.distance("1010101", "0101010"));
        assertEquals(4, Hamming.distance("AAAA", "TTTT"));
    }

    @Test
    public void testEdgeCases() {
        assertEquals(0, Hamming.distance("A", "A"));
        assertEquals(1, Hamming.distance("A", "B"));
    }

    @Test
    public void testUnequalLength() {
        assertThrows(IllegalArgumentException.class, () -> {
            Hamming.distance("ABC", "AB");
        });
    }

    @Test
    public void testLongStrings() {
        String s1 = "A".repeat(1000);
        String s2 = "A".repeat(999) + "B";  // Only last char differs
        assertEquals(1, Hamming.distance(s1, s2));
    }
}

