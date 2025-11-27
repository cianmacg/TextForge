package test.java.Similarity.AlignmentFree;

import main.java.ie.atu.forge.Similarity.AlignmentFree.MinHash;

import org.junit.jupiter.api.Test;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class MinHashTest {

    @Test
    public void testJaccardIdenticalSets() {
        MinHash mh = new MinHash(128);
        Set<String> s1 = new HashSet<>(Arrays.asList("a", "b", "c", "d"));
        Set<String> s2 = new HashSet<>(Arrays.asList("a", "b", "c", "d"));

        double sim = mh.jaccard(s1, s2);
        assertEquals(1.0, sim, 1e-9, "Identical sets should have Jaccard=1");
    }

    @Test
    public void testJaccardDisjointSets() {
        MinHash mh = new MinHash(128);
        Set<String> s1 = new HashSet<>(Arrays.asList("a", "b", "c"));
        Set<String> s2 = new HashSet<>(Arrays.asList("x", "y", "z"));

        double sim = mh.jaccard(s1, s2);
        assertEquals(0.0, sim, 0.1, "Disjoint sets should have Jaccard≈0");
    }

    @Test
    public void testJaccardPartialOverlap() {
        MinHash mh = new MinHash(256); // More hashes for accuracy
        Set<String> s1 = new HashSet<>(Arrays.asList("a", "b", "c", "d", "e"));
        Set<String> s2 = new HashSet<>(Arrays.asList("c", "d", "e", "f", "g"));

        // Actual Jaccard = 3 / 7 ≈ 0.4286
        double sim = mh.jaccard(s1, s2);
        assertEquals(3.0 / 7.0, sim, 0.15, "Jaccard estimate should be close to true value");
    }

    @Test
    public void testSorensenDiceIdenticalSets() {
        MinHash mh = new MinHash(128);
        Set<String> s1 = new HashSet<>(Arrays.asList("a", "b", "c"));
        Set<String> s2 = new HashSet<>(Arrays.asList("a", "b", "c"));

        double sim = mh.sorensenDice(s1, s2);
        assertEquals(1.0, sim, 1e-9, "Dice for identical sets should be 1");
    }

    @Test
    public void testSorensenDicePartialOverlap() {
        MinHash mh = new MinHash(256);
        Set<String> s1 = new HashSet<>(Arrays.asList("a", "b", "c", "d", "e"));
        Set<String> s2 = new HashSet<>(Arrays.asList("c", "d", "e", "f", "g"));

        // True Dice = 2*3 / (5+5) = 0.6
        double sim = mh.sorensenDice(s1, s2);
        assertTrue(Math.abs(sim - 0.6) < 0.2, "Dice estimate should be near 0.6");
    }

    @Test
    public void testEmptySets() {
        MinHash mh = new MinHash(128);
        Set<String> s1 = new HashSet<>();
        Set<String> s2 = new HashSet<>();

        double j = mh.jaccard(s1, s2);
        double d = mh.sorensenDice(s1, s2);

        assertEquals(1.0, j, 1e-9, "Two empty sets should have Jaccard=1 (by convention)");
        assertEquals(1.0, d, 1e-9, "Two empty sets should have Dice=1 (by convention)");
    }

    @Test
    public void testDeterministicResults() {
        MinHash mh = new MinHash(128);
        Set<String> s = new HashSet<>(Arrays.asList("a", "b", "c"));

        double sim1 = mh.jaccard(s, s);
        double sim2 = mh.jaccard(s, s);

        assertEquals(sim1, sim2, 1e-9, "Same sets should produce identical results each time");
    }
}
