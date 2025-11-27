package test.java.Similarity.AlignmentFree;

import main.java.ie.atu.forge.Similarity.AlignmentFree.Tversky;

import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
import org.junit.jupiter.api.Test;

public class TverskyTest {

    @Test
    public void testIdenticalSets() {
        Map<String, Integer> s1 = Map.of("a", 1, "b", 1, "c", 1);
        Map<String, Integer> s2 = Map.of("a", 5, "b", 2, "c", 10);

        double result = Tversky.similarity(s1, s2, 0.5, 0.5);
        // Intersection = {a,b,c} (3), unique parts = 0
        // Similarity = 3 / (3 + 0 + 0) = 1
        assertEquals(1.0, result, 1e-9, "Identical sets should yield similarity 1.0");
    }

    @Test
    public void testNoOverlap() {
        Map<String, Integer> s1 = Map.of("a", 1, "b", 1);
        Map<String, Integer> s2 = Map.of("c", 1, "d", 1);

        double result = Tversky.similarity(s1, s2, 1.0, 1.0);
        // Intersection = 0, |A\B|=2, |B\A|=2
        // Similarity = 0 / (0 + 2 + 2) = 0
        assertEquals(0.0, result, 1e-9, "Disjoint sets should yield similarity 0.0");
    }

    @Test
    public void testJaccardEquivalence() {
        Map<String, Integer> s1 = Map.of("a", 1, "b", 1);
        Map<String, Integer> s2 = Map.of("a", 1, "c", 1);

        double result = Tversky.similarity(s1, s2, 1.0, 1.0);
        // Intersection = {a} (1), A\B={b} (1), B\A={c} (1)
        // Similarity = 1 / (1 + 1 + 1) = 1/3 ≈ 0.333
        assertEquals(1.0/3.0, result, 1e-9, "Should match Jaccard similarity for a=b=1");
    }

    @Test
    public void testDiceEquivalence() {
        Map<String, Integer> s1 = Map.of("a", 1, "b", 1);
        Map<String, Integer> s2 = Map.of("a", 1, "c", 1);

        double result = Tversky.similarity(s1, s2, 0.5, 0.5);
        // Intersection = {a} (1), A\B={b} (1), B\A={c} (1)
        // Similarity = 1 / (1 + 0.5*1 + 0.5*1) = 1 / 2 = 0.5
        assertEquals(0.5, result, 1e-9, "Should match Sørensen–Dice similarity for a=b=0.5");
    }

    @Test
    public void testAsymmetricBiasTowardsA() {
        Map<String, Integer> s1 = Map.of("a", 1, "b", 1, "c", 1);
        Map<String, Integer> s2 = Map.of("a", 1, "c", 1);

        double result = Tversky.similarity(s1, s2, 1.0, 0.0);
        // Intersection = {a,c} (2), A\B={b} (1), B\A={} (0)
        // Similarity = 2 / (2 + 1*1 + 0*0) = 2 / 3 ≈ 0.666
        assertEquals(2.0/3.0, result, 1e-9, "Should bias toward A when b=0");
    }

    @Test
    public void testOneEmptySet() {
        Map<String, Integer> s1 = new HashMap<>();
        Map<String, Integer> s2 = Map.of("a", 1, "b", 1);

        double result = Tversky.similarity(s1, s2, 1.0, 1.0);
        // Intersection = 0, |A\B|=0, |B\A|=2
        // Similarity = 0 / (0+0+2) = 0
        assertEquals(0.0, result, 1e-9, "Empty vs non-empty set should yield similarity 0.0");
    }

    @Test
    public void testBothEmptySets() {
        Map<String, Integer> s1 = new HashMap<>();
        Map<String, Integer> s2 = new HashMap<>();

        double result = Tversky.similarity(s1, s2, 1.0, 1.0);
        // Two empty sets are identical → by convention, similarity = 1
        assertEquals(1.0, result, 1e-9, "Two empty sets should yield similarity 1.0");
    }
}
