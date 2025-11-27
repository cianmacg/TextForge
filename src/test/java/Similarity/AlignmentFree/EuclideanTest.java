package test.java.Similarity.AlignmentFree;

import ie.atu.forge.Similarity.AlignmentFree.Euclidean;

import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
import org.junit.jupiter.api.Test;

public class EuclideanTest {

    @Test
    public void testIdenticalVectors() {
        Map<String, Integer> s1 = Map.of("a", 1, "b", 2, "c", 3);
        Map<String, Integer> s2 = Map.of("a", 1, "b", 2, "c", 3);

        double result = Euclidean.distance(s1, s2);
        assertEquals(0.0, result, 1e-9, "Distance between identical vectors should be 0");
    }

    @Test
    public void testSimpleDifference() {
        Map<String, Integer> s1 = Map.of("a", 1, "b", 4);
        Map<String, Integer> s2 = Map.of("a", 3, "b", 7);

        double result = Euclidean.distance(s1, s2);
        // Differences: (1-3)^2=4, (4-7)^2=9 → sqrt(4+9)=sqrt(13)
        assertEquals(Math.sqrt(13), result, 1e-9, "Should compute correct Euclidean distance");
    }

    @Test
    public void testNonOverlappingKeys() {
        Map<String, Integer> s1 = Map.of("a", 5);
        Map<String, Integer> s2 = Map.of("b", 12);

        double result = Euclidean.distance(s1, s2);
        // Missing keys as 0: (5-0)^2=25, (0-12)^2=144 → sqrt(169)=13
        assertEquals(13.0, result, 1e-9, "Non-overlapping keys should be treated as zeros");
    }

    @Test
    public void testOneEmptyVector() {
        Map<String, Integer> s1 = new HashMap<>();
        Map<String, Integer> s2 = Map.of("a", 4, "b", 3);

        double result = Euclidean.distance(s1, s2);
        // (0-4)^2=16, (0-3)^2=9 → sqrt(25)=5
        assertEquals(5.0, result, 1e-9, "Empty vector should yield magnitude of the other vector");
    }

    @Test
    public void testBothEmptyVectors() {
        Map<String, Integer> s1 = new HashMap<>();
        Map<String, Integer> s2 = new HashMap<>();

        double result = Euclidean.distance(s1, s2);
        assertEquals(0.0, result, 1e-9, "Two empty vectors should have distance 0");
    }

    @Test
    public void testNegativeValues() {
        Map<String, Integer> s1 = Map.of("a", -5, "b", 3);
        Map<String, Integer> s2 = Map.of("a", 2, "b", -1);

        double result = Euclidean.distance(s1, s2);
        // Differences: (-5-2)^2=49, (3-(-1))^2=16 → sqrt(65)
        assertEquals(Math.sqrt(65), result, 1e-9, "Should handle negatives correctly");
    }
}