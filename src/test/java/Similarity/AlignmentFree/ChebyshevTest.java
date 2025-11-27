package test.java.Similarity.AlignmentFree;

import ie.atu.forge.Similarity.AlignmentFree.Chebyshev;

import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
import org.junit.jupiter.api.Test;

public class ChebyshevTest {

    @Test
    public void testIdenticalVectors() {
        Map<String, Integer> s1 = Map.of("a", 1, "b", 2, "c", 3);
        Map<String, Integer> s2 = Map.of("a", 1, "b", 2, "c", 3);

        double result = Chebyshev.distance(s1, s2);
        assertEquals(0.0, result, 1e-9, "Distance between identical vectors should be 0");
    }

    @Test
    public void testSimpleDifference() {
        Map<String, Integer> s1 = Map.of("a", 1, "b", 4);
        Map<String, Integer> s2 = Map.of("a", 3, "b", 7);

        double result = Chebyshev.distance(s1, s2);
        // Differences: |1-3|=2, |4-7|=3 → max = 3
        assertEquals(3.0, result, 1e-9, "Should compute max absolute difference correctly");
    }

    @Test
    public void testNonOverlappingKeys() {
        Map<String, Integer> s1 = Map.of("a", 5);
        Map<String, Integer> s2 = Map.of("b", 7);

        double result = Chebyshev.distance(s1, s2);
        // Missing keys treated as 0: max(|5-0|, |0-7|) = 7
        assertEquals(7.0, result, 1e-9, "Non-overlapping keys should treat missing as zero");
    }

    @Test
    public void testOneEmptyVector() {
        Map<String, Integer> s1 = new HashMap<>();
        Map<String, Integer> s2 = Map.of("a", 4, "b", 9);

        double result = Chebyshev.distance(s1, s2);
        // Distances: |0-4|=4, |0-9|=9 → max = 9
        assertEquals(9.0, result, 1e-9, "Empty vector should be treated as all zeros");
    }

    @Test
    public void testBothEmptyVectors() {
        Map<String, Integer> s1 = new HashMap<>();
        Map<String, Integer> s2 = new HashMap<>();

        double result = Chebyshev.distance(s1, s2);
        assertEquals(0.0, result, 1e-9, "Two empty vectors should have distance 0");
    }

    @Test
    public void testNegativeValues() {
        Map<String, Integer> s1 = Map.of("a", -5, "b", 3);
        Map<String, Integer> s2 = Map.of("a", 2, "b", -1);

        double result = Chebyshev.distance(s1, s2);
        // Differences: |(-5)-2|=7, |3-(-1)|=4 → max = 7
        assertEquals(7.0, result, 1e-9, "Should correctly handle negative values");
    }
}

