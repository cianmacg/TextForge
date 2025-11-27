package test.java.Similarity.AlignmentFree;

import ie.atu.forge.Similarity.AlignmentFree.Cosine;

import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
import org.junit.jupiter.api.Test;

public class CosineTest {

    @Test
    public void testIdenticalVectors() {
        Map<String, Integer> s1 = Map.of("a", 1, "b", 2, "c", 3);
        Map<String, Integer> s2 = Map.of("a", 1, "b", 2, "c", 3);

        double result = Cosine.similarity(s1, s2);
        assertEquals(1.0, result, 1e-9, "Identical vectors should have cosine similarity 1.0");
    }

    @Test
    public void testOrthogonalVectors() {
        Map<String, Integer> s1 = Map.of("a", 1, "b", 2);
        Map<String, Integer> s2 = Map.of("c", 3, "d", 4);

        double result = Cosine.similarity(s1, s2);
        assertEquals(0.0, result, 1e-9, "Vectors with no overlap should have cosine similarity 0.0");
    }

    @Test
    public void testPartialOverlap() {
        Map<String, Integer> s1 = Map.of("a", 1, "b", 1);
        Map<String, Integer> s2 = Map.of("a", 1, "c", 1);

        double result = Cosine.similarity(s1, s2);
        // Cosine similarity = (1*1) / (sqrt(1^2+1^2) * sqrt(1^2+1^2)) = 1 / (√2 * √2) = 0.5
        assertEquals(0.5, result, 1e-9, "Should correctly compute partial overlap similarity");
    }

    @Test
    public void testZeroMagnitudeVector() {
        Map<String, Integer> s1 = new HashMap<>();
        Map<String, Integer> s2 = Map.of("a", 1);

        double result = Cosine.similarity(s1, s2);
        assertEquals(0.0, result, 1e-9, "Vector with zero magnitude should yield 0 similarity");
    }

    @Test
    public void testBothEmptyVectors() {
        Map<String, Integer> s1 = new HashMap<>();
        Map<String, Integer> s2 = new HashMap<>();

        double result = Cosine.similarity(s1, s2);
        assertEquals(0.0, result, 1e-9, "Both empty vectors should yield similarity 0.0");
    }

    @Test
    public void testDifferentMagnitudes() {
        Map<String, Integer> s1 = Map.of("a", 1, "b", 2);
        Map<String, Integer> s2 = Map.of("a", 2, "b", 4);

        double result = Cosine.similarity(s1, s2);
        // Cosine similarity = (1*2 + 2*4) / (sqrt(1^2+2^2) * sqrt(2^2+4^2)) = 10 / (√5 * √20) = 1.0
        assertEquals(1.0, result, 1e-9, "Scaled vectors should still have similarity 1.0");
    }
}

