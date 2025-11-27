package test.java.Similarity.Alignment;

import ie.atu.forge.Similarity.Alignment.Jaro;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class JaroTest {

    @Test
    public void testExactMatch() {
        assertEquals(1.0, Jaro.similarity("MARTHA", "MARTHA"), 1e-6);
        assertEquals(1.0, Jaro.similarity("", ""), 1e-6);
    }

    @Test
    public void testCompletelyDifferent() {
        assertEquals(0.0, Jaro.similarity("ABC", "XYZ"), 1e-6);
        assertEquals(0.0, Jaro.similarity("", "ABC"), 1e-6);
    }

    @Test
    public void testPartialMatchExamples() {
        // Standard Jaro example (from Winkler’s papers)
        // Jaro("MARTHA", "MARHTA") ≈ 0.944444
        assertEquals(0.944444, Jaro.similarity("MARTHA", "MARHTA"), 1e-5);

        // Another common case (transpositions)
        // Jaro("DWAYNE", "DUANE") ≈ 0.822222
        assertEquals(0.822222, Jaro.similarity("DWAYNE", "DUANE"), 1e-5);

        // Another with minor differences
        // Jaro("CRATE", "TRACE") ≈ 0.733333
        assertEquals(0.733333, Jaro.similarity("CRATE", "TRACE"), 1e-5);
    }

    @Test
    public void testSymmetry() {
        assertEquals(Jaro.similarity("MARTHA", "MARHTA"),
                Jaro.similarity("MARHTA", "MARTHA"), 1e-12);
        assertEquals(Jaro.similarity("DWAYNE", "DUANE"),
                Jaro.similarity("DUANE", "DWAYNE"), 1e-12);
    }

    @Test
    public void testEdgeCases() {
        assertEquals(0.0, Jaro.similarity("A", ""), 1e-6);
        assertEquals(0.0, Jaro.similarity("", "A"), 1e-6);
        assertEquals(1.0, Jaro.similarity("A", "A"), 1e-6);
    }
}

