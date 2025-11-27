package test.java.Similarity.Alignment;

import ie.atu.forge.Similarity.Alignment.JaroWinkler;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class JaroWinklerTest {

    // Use p = 0.1 (Winkler's recommended scaling factor)
    private static final double P = 0.1;
    private static final double EPSILON = 1e-6;

    @Test
    public void testExactMatch() {
        assertEquals(1.0, JaroWinkler.similarity("MARTHA", "MARTHA", P), EPSILON);
        assertEquals(1.0, JaroWinkler.similarity("", "", P), EPSILON);
    }

    @Test
    public void testCompletelyDifferent() {
        assertEquals(0.0, JaroWinkler.similarity("ABC", "XYZ", P), EPSILON);
        assertEquals(0.0, JaroWinkler.similarity("", "ABC", P), EPSILON);
    }

    @Test
    public void testStandardExamples() {
        // These examples are from Winkler’s published values

        // "MARTHA" vs "MARHTA"
        // Jaro ≈ 0.944444, l = 3 (MAR), p = 0.1
        // JW = 0.944444 + (3 * 0.1 * (1 - 0.944444)) ≈ 0.961111
        assertEquals(0.961111, JaroWinkler.similarity("MARTHA", "MARHTA", P), 1e-5);

        // "DWAYNE" vs "DUANE"
        // Jaro ≈ 0.822222, l = 1 (D), JW ≈ 0.840000
        assertEquals(0.84, JaroWinkler.similarity("DWAYNE", "DUANE", P), 1e-5);

        // "CRATE" vs "TRACE"
        // Jaro ≈ 0.733333, l = 0, JW = same as Jaro
        assertEquals(0.733333, JaroWinkler.similarity("CRATE", "TRACE", P), 1e-5);
    }

    @Test
    public void testPrefixImpact() {
        // Same Jaro base score but different prefixes
        String s1 = "ABCXXXX";
        String s2 = "ABCYYYY";
        // They share "ABC" (l = 3)
        double jw = JaroWinkler.similarity(s1, s2, P);
        assertTrue(jw > 0.5);  // Should be boosted compared to plain Jaro

        String s3 = "XYZXXXX";
        String s4 = "XYZYYYY";
        // Same idea but "XYZ" prefix, also l = 3, so same boost
        double jw2 = JaroWinkler.similarity(s3, s4, P);
        assertEquals(jw, jw2, EPSILON);
    }

    @Test
    public void testSymmetry() {
        assertEquals(JaroWinkler.similarity("MARTHA", "MARHTA", P),
                JaroWinkler.similarity("MARHTA", "MARTHA", P),
                EPSILON);
    }

    @Test
    public void testEdgeCases() {
        assertEquals(0.0, JaroWinkler.similarity("A", "", P), EPSILON);
        assertEquals(0.0, JaroWinkler.similarity("", "A", P), EPSILON);
        assertEquals(1.0, JaroWinkler.similarity("A", "A", P), EPSILON);
    }
}

