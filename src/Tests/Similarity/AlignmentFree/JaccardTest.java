package Tests.Similarity.AlignmentFree;

import ie.atu.forge.Similarity.AlignmentFree.Jaccard;

import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
import org.junit.jupiter.api.Test;

public class JaccardTest {

    /* ---------------------------
       Tests for Classic Jaccard
       --------------------------- */

    @Test
    public void testIdenticalSets() {
        Set<String> s1 = Set.of("a", "b", "c");
        Set<String> s2 = Set.of("a", "b", "c");

        double result = Jaccard.similarity(s1, s2);
        assertEquals(1.0, result, 1e-9, "Identical sets should have similarity 1.0");
    }

    @Test
    public void testNoOverlap() {
        Set<String> s1 = Set.of("a", "b");
        Set<String> s2 = Set.of("c", "d");

        double result = Jaccard.similarity(s1, s2);
        assertEquals(0.0, result, 1e-9, "Sets with no overlap should have similarity 0.0");
    }

    @Test
    public void testPartialOverlap() {
        Set<String> s1 = Set.of("a", "b");
        Set<String> s2 = Set.of("a", "c");

        double result = Jaccard.similarity(s1, s2);
        // Intersection = 1, Union = 3
        assertEquals(1.0 / 3.0, result, 1e-9, "Should compute correct Jaccard similarity for sets");
    }

    @Test
    public void testOneEmptySet() {
        Set<String> s1 = Set.of();
        Set<String> s2 = Set.of("a", "b");

        double result = Jaccard.similarity(s1, s2);
        assertEquals(0.0, result, 1e-9, "Empty vs non-empty set should yield 0 similarity");
    }

    @Test
    public void testBothEmptySets() {
        Set<String> s1 = Set.of();
        Set<String> s2 = Set.of();

        double result = Jaccard.similarity(s1, s2);
        assertEquals(1.0, result, 1e-9, "Two empty sets should have similarity 1.0");
    }

    @Test
    public void testSubsetCase() {
        Set<String> s1 = Set.of("a", "b");
        Set<String> s2 = Set.of("a", "b", "c");

        double result = Jaccard.similarity(s1, s2);
        // Intersection = 2, Union = 3
        assertEquals(2.0 / 3.0, result, 1e-9, "Should handle subset relationships correctly");
    }

    /* -----------------------------------
       Tests for Generalized Jaccard
       (counts / multiset similarity)
       ----------------------------------- */

    @Test
    public void testGeneralisedIdenticalCounts() {
        Map<String, Integer> s1 = Map.of("a", 2, "b", 3);
        Map<String, Integer> s2 = Map.of("a", 2, "b", 3);

        double result = Jaccard.generalised_similarity(s1, s2);
        assertEquals(1.0, result, 1e-9, "Identical maps should have similarity 1.0");
    }

    @Test
    public void testGeneralisedDifferentCounts() {
        Map<String, Integer> s1 = Map.of("a", 2, "b", 1);
        Map<String, Integer> s2 = Map.of("a", 1, "b", 4);

        double result = Jaccard.generalised_similarity(s1, s2);
        // min counts = a:1, b:1 → 2
        // max counts = a:2, b:4 → 6
        // Similarity = 2 / 6 = 1/3
        assertEquals(1.0 / 3.0, result, 1e-9, "Should compute correct generalised Jaccard similarity");
    }

    @Test
    public void testGeneralisedNoOverlap() {
        Map<String, Integer> s1 = Map.of("a", 3);
        Map<String, Integer> s2 = Map.of("b", 5);

        double result = Jaccard.generalised_similarity(s1, s2);
        assertEquals(0.0, result, 1e-9, "Maps with no shared keys should have similarity 0.0");
    }

    @Test
    public void testGeneralisedZeroCounts() {
        Map<String, Integer> s1 = Map.of("a", 0, "b", 0);
        Map<String, Integer> s2 = Map.of("a", 0, "b", 0);

        double result = Jaccard.generalised_similarity(s1, s2);
        assertEquals(1.0, result, 1e-9, "Maps with only zero counts should be treated as identical");
    }

    @Test
    public void testGeneralisedEmptyVsNonEmpty() {
        Map<String, Integer> s1 = new HashMap<>();
        Map<String, Integer> s2 = Map.of("a", 1);

        double result = Jaccard.generalised_similarity(s1, s2);
        assertEquals(0.0, result, 1e-9, "Empty map vs non-empty map should yield 0 similarity");
    }

    @Test
    public void testGeneralisedBothEmpty() {
        Map<String, Integer> s1 = new HashMap<>();
        Map<String, Integer> s2 = new HashMap<>();

        double result = Jaccard.generalised_similarity(s1, s2);
        assertEquals(1.0, result, 1e-9, "Two empty maps should have similarity 1.0");
    }
}
