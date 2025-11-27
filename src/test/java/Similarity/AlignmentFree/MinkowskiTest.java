package test.java.Similarity.AlignmentFree;

import ie.atu.forge.Similarity.AlignmentFree.Minkowski;

import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
import org.junit.jupiter.api.Test;

public class MinkowskiTest {

    @Test
    public void testIdenticalVectors() {
        Map<String, Integer> s1 = Map.of("a", 1, "b", 2, "c", 3);
        Map<String, Integer> s2 = Map.of("a", 1, "b", 2, "c", 3);

        double result = Minkowski.distance(s1, s2, 2);
        assertEquals(0.0, result, 1e-9, "Distance between identical vectors should be 0");
    }

    @Test
    public void testManhattanEquivalent() {
        Map<String, Integer> s1 = Map.of("a", 1, "b", 4);
        Map<String, Integer> s2 = Map.of("a", 3, "b", 7);

        double result = Minkowski.distance(s1, s2, 1);
        // Manhattan: |1-3| + |4-7| = 2 + 3 = 5
        assertEquals(5.0, result, 1e-9, "Should match Manhattan distance for p=1");
    }

    @Test
    public void testEuclideanEquivalent() {
        Map<String, Integer> s1 = Map.of("a", 1, "b", 4);
        Map<String, Integer> s2 = Map.of("a", 3, "b", 7);

        double result = Minkowski.distance(s1, s2, 2);
        // Euclidean: sqrt((1-3)^2 + (4-7)^2) = sqrt(4+9)=sqrt(13)
        assertEquals(Math.sqrt(13), result, 1e-9, "Should match Euclidean distance for p=2");
    }

    @Test
    public void testChebyshevEquivalent() {
        Map<String, Integer> s1 = Map.of("a", 1, "b", 4);
        Map<String, Integer> s2 = Map.of("a", 3, "b", 7);

        double result = Minkowski.distance(s1, s2, -1);
        // Chebyshev: max(|1-3|, |4-7|) = max(2, 3) = 3
        assertEquals(3.0, result, 1e-9, "Should match Chebyshev distance for p=-1");
    }

    @Test
    public void testNonOverlappingKeys() {
        Map<String, Integer> s1 = Map.of("a", 5);
        Map<String, Integer> s2 = Map.of("b", 12);

        double result = Minkowski.distance(s1, s2, 3);
        // p=3: (|5|^3 + |12|^3)^(1/3) = (125 + 1728)^(1/3) = (1853)^(1/3)
        double expected = Math.cbrt(1853);
        assertEquals(expected, result, 1e-9, "Should compute general Minkowski distance correctly");
    }

    @Test
    public void testNegativeValues() {
        Map<String, Integer> s1 = Map.of("a", -5, "b", 3);
        Map<String, Integer> s2 = Map.of("a", 2, "b", -1);

        double result = Minkowski.distance(s1, s2, 2);
        // Euclidean: sqrt((-5-2)^2 + (3-(-1))^2) = sqrt(49 + 16) = sqrt(65)
        assertEquals(Math.sqrt(65), result, 1e-9, "Should handle negatives with p=2");
    }

    @Test
    public void testBothEmptyVectors() {
        Map<String, Integer> s1 = new HashMap<>();
        Map<String, Integer> s2 = new HashMap<>();

        double result = Minkowski.distance(s1, s2, 2);
        assertEquals(0.0, result, 1e-9, "Two empty vectors should have distance 0");
    }
}

