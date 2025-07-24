package Tests.Similarity.AlignmentFree;

import ie.atu.forge.Similarity.AlignmentFree.Jaccard;

import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
import org.junit.jupiter.api.Test;

public class JaccardTest {

    @Test
    public void testIdenticalSets() {
        Map<String, Integer> s1 = Map.of("a", 3, "b", 2, "c", 1);
        Map<String, Integer> s2 = Map.of("a", 1, "b", 10, "c", 5);

        double result = Jaccard.similarity(s1, s2);
        // Both contain the same keys: {a, b, c} → intersection = 3, union = 3
        assertEquals(1.0, result, 1e-9, "Identical sets should have similarity 1.0");
    }

    @Test
    public void testNoOverlap() {
        Map<String, Integer> s1 = Map.of("a", 1, "b", 1);
        Map<String, Integer> s2 = Map.of("c", 1, "d", 1);

        double result = Jaccard.similarity(s1, s2);
        // No common keys: intersection = 0, union = 4
        assertEquals(0.0, result, 1e-9, "Sets with no overlap should have similarity 0.0");
    }

    @Test
    public void testPartialOverlap() {
        Map<String, Integer> s1 = Map.of("a", 2, "b", 1);
        Map<String, Integer> s2 = Map.of("a", 1, "c", 1);

        double result = Jaccard.similarity(s1, s2);
        // Keys as sets: intersection = {a} (1), union = {a, b, c} (3)
        // Similarity = 1 / 3
        assertEquals(1.0 / 3.0, result, 1e-9, "Should compute correct Jaccard similarity for sets");
    }

    @Test
    public void testOneEmptySet() {
        Map<String, Integer> s1 = new HashMap<>();
        Map<String, Integer> s2 = Map.of("a", 1, "b", 1);

        double result = Jaccard.similarity(s1, s2);
        // Intersection = 0, union = 2
        assertEquals(0.0, result, 1e-9, "Empty vs non-empty set should yield 0 similarity");
    }

    @Test
    public void testBothEmptySets() {
        Map<String, Integer> s1 = new HashMap<>();
        Map<String, Integer> s2 = new HashMap<>();

        double result = Jaccard.similarity(s1, s2);
        // Two empty sets are typically considered identical: similarity = 1.0
        assertEquals(1.0, result, 1e-9, "Two empty sets should have similarity 1.0");
    }

    @Test
    public void testSubsetCase() {
        Map<String, Integer> s1 = Map.of("a", 1, "b", 1);
        Map<String, Integer> s2 = Map.of("a", 5, "b", 10, "c", 1);

        double result = Jaccard.similarity(s1, s2);
        // Keys as sets: intersection = {a, b} (2), union = {a, b, c} (3)
        // Similarity = 2 / 3
        assertEquals(2.0 / 3.0, result, 1e-9, "Should handle subset relationships correctly");
    }
}

