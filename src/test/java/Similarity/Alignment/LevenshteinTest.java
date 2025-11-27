package test.java.Similarity.Alignment;

import ie.atu.forge.Similarity.Alignment.Levenshtein;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class LevenshteinTest {

    @Test
    public void testExactMatch() {
        assertEquals(0, Levenshtein.distance("kitten", "kitten"));
        assertEquals(0, Levenshtein.distance("", ""));
    }

    @Test
    public void testCompletelyDifferent() {
        assertEquals(3, Levenshtein.distance("cat", "dog")); // replace all 3
        assertEquals(5, Levenshtein.distance("", "hello"));  // insert all 5
        assertEquals(5, Levenshtein.distance("world", ""));  // delete all 5
    }

    @Test
    public void testStandardExamples() {
        // Classic examples from Wagner-Fischer algorithm literature
        assertEquals(3, Levenshtein.distance("kitten", "sitting"));
        // kitten -> sitten (substitution)
        // sitten -> sittin (substitution)
        // sittin -> sitting (insertion)

        assertEquals(2, Levenshtein.distance("flaw", "lawn"));
        // flaw -> law (deletion)
        // law -> lawn (insertion)
    }

    @Test
    public void testSingleEdits() {
        assertEquals(1, Levenshtein.distance("abc", "ab"));   // deletion
        assertEquals(1, Levenshtein.distance("ab", "abc"));   // insertion
        assertEquals(1, Levenshtein.distance("abc", "adc"));  // substitution
    }

    @Test
    public void testSymmetry() {
        assertEquals(Levenshtein.distance("kitten", "sitting"),
                Levenshtein.distance("sitting", "kitten"));
        assertEquals(Levenshtein.distance("flaw", "lawn"),
                Levenshtein.distance("lawn", "flaw"));
    }

    @Test
    public void testEdgeCases() {
        assertEquals(0, Levenshtein.distance("", ""));
        assertEquals(1, Levenshtein.distance("", "A"));
        assertEquals(1, Levenshtein.distance("A", ""));
        assertEquals(0, Levenshtein.distance("A", "A"));
    }

    @Test
    public void testLongerStrings() {
        // Simple test to ensure it handles larger strings efficiently
        String s1 = "a".repeat(1000);
        String s2 = "b".repeat(1000);
        assertEquals(1000, Levenshtein.distance(s1, s2)); // 1000 substitutions

        assertEquals(1000, Levenshtein.distance("a".repeat(1000), ""));
        assertEquals(1000, Levenshtein.distance("", "a".repeat(1000)));
    }
}