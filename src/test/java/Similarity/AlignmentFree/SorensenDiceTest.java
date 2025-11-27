package test.java.Similarity.AlignmentFree;

import ie.atu.forge.Similarity.AlignmentFree.SorensenDice;

import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
import org.junit.jupiter.api.Test;

public class SorensenDiceTest {

    @Test
    public void testIdenticalSets() {
        Map<String, Integer> s1 = Map.of("a", 1, "b", 2, "c", 3);
        Map<String, Integer> s2 = Map.of("a", 5, "b", 10, "c", 100);

        double result = SorensenDice.similarity(s1, s2);
        // Intersection = {a,b,c} (3)
        // Dice = 2*3 / (3+3) = 6/6 = 1
        assertEquals(1.0, result, 1e-9, "Identical sets should yield similarity 1.0");
    }

    @Test
    public void testNoOverlap() {
        Map<String, Integer> s1 = Map.of("a", 1, "b", 1);
        Map<String, Integer> s2 = Map.of("c", 1, "d", 1);

        double result = SorensenDice.similarity(s1, s2);
        // Intersection = 0, |s1|=2, |s2|=2
        // Dice = 0 / 4 = 0
        assertEquals(0.0, result, 1e-9, "Disjoint sets should yield similarity 0.0");
    }

    @Test
    public void testPartialOverlap() {
        Map<String, Integer> s1 = Map.of("a", 2, "b", 1);
        Map<String, Integer> s2 = Map.of("a", 1, "c", 1);

        double result = SorensenDice.similarity(s1, s2);
        // Intersection = {a} (1)
        // |s1|=2, |s2|=2
        // Dice = 2*1 / (2+2) = 2/4 = 0.5
        assertEquals(0.5, result, 1e-9, "Should compute correct Sørensen–Dice similarity");
    }

    @Test
    public void testSubset() {
        Map<String, Integer> s1 = Map.of("a", 1, "b", 1);
        Map<String, Integer> s2 = Map.of("a", 1, "b", 1, "c", 1);

        double result = SorensenDice.similarity(s1, s2);
        // Intersection = {a,b} (2)
        // |s1|=2, |s2|=3
        // Dice = 2*2 / (2+3) = 4/5 = 0.8
        assertEquals(0.8, result, 1e-9, "Should handle subset relationships correctly");
    }

    @Test
    public void testOneEmptySet() {
        Map<String, Integer> s1 = new HashMap<>();
        Map<String, Integer> s2 = Map.of("a", 1);

        double result = SorensenDice.similarity(s1, s2);
        // Intersection = 0, |s1|=0, |s2|=1
        // Dice = 0 / 1 = 0
        assertEquals(0.0, result, 1e-9, "Empty vs non-empty set should yield similarity 0.0");
    }

    @Test
    public void testBothEmptySets() {
        Map<String, Integer> s1 = new HashMap<>();
        Map<String, Integer> s2 = new HashMap<>();

        double result = SorensenDice.similarity(s1, s2);
        // Convention: two empty sets are identical, similarity = 1
        assertEquals(1.0, result, 1e-9, "Two empty sets should yield similarity 1.0");
    }
}

