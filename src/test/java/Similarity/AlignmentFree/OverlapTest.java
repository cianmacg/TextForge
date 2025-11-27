package test.java.Similarity.AlignmentFree;

import ie.atu.forge.Similarity.AlignmentFree.Overlap;

import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
import org.junit.jupiter.api.Test;

public class OverlapTest {

    @Test
    public void testIdenticalSets() {
        Map<String, Integer> s1 = Map.of("a", 1, "b", 2, "c", 3);
        Map<String, Integer> s2 = Map.of("a", 9, "b", 5, "c", 100);

        double result = Overlap.distance(s1, s2);
        // Similarity = |{a,b,c}| / min(3,3) = 3/3 = 1
        // Distance = 1 - 1 = 0
        assertEquals(0.0, result, 1e-9, "Identical sets should yield distance 0");
    }

    @Test
    public void testNoOverlap() {
        Map<String, Integer> s1 = Map.of("a", 1, "b", 1);
        Map<String, Integer> s2 = Map.of("c", 1, "d", 1);

        double result = Overlap.distance(s1, s2);
        // Similarity = 0 / min(2,2) = 0 → Distance = 1
        assertEquals(1.0, result, 1e-9, "Disjoint sets should yield distance 1");
    }

    @Test
    public void testPartialOverlap() {
        Map<String, Integer> s1 = Map.of("a", 2, "b", 1);
        Map<String, Integer> s2 = Map.of("a", 1, "c", 1);

        double result = Overlap.distance(s1, s2);
        // Intersection = {a} (1)
        // min(|s1|,|s2|) = min(2,2)=2
        // Similarity = 1 / 2 = 0.5 → Distance = 0.5
        assertEquals(0.5, result, 1e-9, "Should compute correct overlap-based distance");
    }

    @Test
    public void testSubset() {
        Map<String, Integer> s1 = Map.of("a", 1, "b", 1);
        Map<String, Integer> s2 = Map.of("a", 1, "b", 1, "c", 1);

        double result = Overlap.distance(s1, s2);
        // Intersection = {a,b} (2)
        // min(|s1|,|s2|) = min(2,3) = 2
        // Similarity = 2 / 2 = 1 → Distance = 0
        assertEquals(0.0, result, 1e-9, "A subset should yield distance 0 (perfect overlap)");
    }

    @Test
    public void testOneEmptySet() {
        Map<String, Integer> s1 = new HashMap<>();
        Map<String, Integer> s2 = Map.of("a", 1);

        double result = Overlap.distance(s1, s2);
        // Usually defined as distance = 1 when one set is empty (similarity = 0)
        assertEquals(1.0, result, 1e-9, "Empty vs non-empty should yield distance 1");
    }

    @Test
    public void testBothEmptySets() {
        Map<String, Integer> s1 = new HashMap<>();
        Map<String, Integer> s2 = new HashMap<>();

        double result = Overlap.distance(s1, s2);
        // By convention, two empty sets are identical (similarity=1 → distance=0)
        assertEquals(0.0, result, 1e-9, "Two empty sets should yield distance 0");
    }
}

