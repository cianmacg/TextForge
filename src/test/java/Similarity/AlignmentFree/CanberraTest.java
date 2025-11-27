package test.java.Similarity.AlignmentFree;

import ie.atu.forge.Similarity.AlignmentFree.Canberra;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class CanberraTest {

    @Test
    public void testIdenticalVectors() {
        Map<String, Integer> v1 = new HashMap<>();
        v1.put("a", 3);
        v1.put("b", 5);

        Map<String, Integer> v2 = new HashMap<>();
        v2.put("a", 3);
        v2.put("b", 5);

        double result = Canberra.distance(v1, v2);
        assertEquals(0.0, result, 1e-9, "Distance between identical vectors should be 0");
    }

    @Test
    public void testSingleDimensionDifference() {
        Map<String, Integer> v1 = new HashMap<>();
        v1.put("a", 4);

        Map<String, Integer> v2 = new HashMap<>();
        v2.put("a", 0);

        // |4 - 0| / (4 + 0) = 1
        double result = Canberra.distance(v1, v2);
        assertEquals(1.0, result, 1e-9, "Distance should equal normalized absolute difference");
    }

    @Test
    public void testTwoDimensions() {
        Map<String, Integer> v1 = new HashMap<>();
        v1.put("a", 1);
        v1.put("b", 2);

        Map<String, Integer> v2 = new HashMap<>();
        v2.put("a", 2);
        v2.put("b", 4);

        // For "a": |1-2| / (1+2) = 1/3
        // For "b": |2-4| / (2+4) = 2/6 = 1/3
        // Total = 2/3 ≈ 0.666...
        double result = Canberra.distance(v1, v2);
        assertEquals(2.0/3.0, result, 1e-9, "Sum of normalized differences should match expected");
    }

    @Test
    public void testWithMissingDimension() {
        Map<String, Integer> v1 = new HashMap<>();
        v1.put("a", 3);

        Map<String, Integer> v2 = new HashMap<>();
        v2.put("b", 4);

        // "a": |3-0| / (3+0) = 1
        // "b": |0-4| / (0+4) = 1
        // Total = 2
        double result = Canberra.distance(v1, v2);
        assertEquals(2.0, result, 1e-9, "Distance should handle missing keys as 0");
    }

    @Test
    public void testZeroVectors() {
        Map<String, Integer> v1 = new HashMap<>();
        Map<String, Integer> v2 = new HashMap<>();

        double result = Canberra.distance(v1, v2);
        assertEquals(0.0, result, 1e-9, "Distance between two empty vectors should be 0");
    }

    @Test
    public void testNegativeValues() {
        Map<String, Integer> v1 = new HashMap<>();
        v1.put("a", -3);
        v1.put("b", 2);

        Map<String, Integer> v2 = new HashMap<>();
        v2.put("a", 3);
        v2.put("b", -2);

        // "a": |-3-3| / (|-3|+|3|) = 6 / 6 = 1
        // "b": |2-(-2)| / (|2|+|2|) = 4 / 4 = 1
        // Total = 2
        double result = Canberra.distance(v1, v2);
        assertEquals(2.0, result, 1e-9, "Distance should handle negatives correctly");
    }
}

