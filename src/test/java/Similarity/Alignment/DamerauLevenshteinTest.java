package test.java.Similarity.Alignment;

import ie.atu.forge.Similarity.Alignment.DamerauLevenshtein;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class DamerauLevenshteinTest {

    @Test
    public void testExactMatch() {
        assertEquals(0, DamerauLevenshtein.distance("test", "test"));
        assertEquals(0, DamerauLevenshtein.distance("", ""));
    }

    @Test
    public void testSingleOperations() {
        assertEquals(1, DamerauLevenshtein.distance("abc", "ab"));   // deletion
        assertEquals(1, DamerauLevenshtein.distance("ab", "abc"));   // insertion
        assertEquals(1, DamerauLevenshtein.distance("abc", "adc"));  // substitution
        assertEquals(1, DamerauLevenshtein.distance("ab", "ba"));    // transposition
    }

    @Test
    public void testMultipleEdits() {
        // "kitten" -> "sitting"
        // Same as Levenshtein, no transpositions reduce cost
        assertEquals(3, DamerauLevenshtein.distance("kitten", "sitting"));

        // "ca" -> "abc"
        // OSA can't chain transpositions, so result is 3 edits instead of 2.
        assertEquals(3, DamerauLevenshtein.distance("ca", "abc"));

        // "abcd" -> "acbd"
        // Single adjacent transposition (swap b and c), still 1
        assertEquals(1, DamerauLevenshtein.distance("abcd", "acbd"));
    }

    @Test
    public void testSymmetry() {
        assertEquals(DamerauLevenshtein.distance("abcd", "acbd"),
                DamerauLevenshtein.distance("acbd", "abcd"));
        assertEquals(DamerauLevenshtein.distance("kitten", "sitting"),
                DamerauLevenshtein.distance("sitting", "kitten"));
    }

    @Test
    public void testEdgeCases() {
        assertEquals(0, DamerauLevenshtein.distance("", ""));
        assertEquals(1, DamerauLevenshtein.distance("", "A"));
        assertEquals(1, DamerauLevenshtein.distance("A", ""));
        assertEquals(0, DamerauLevenshtein.distance("A", "A"));
    }

    @Test
    public void testLongStringsPerformance() {
        // Just to check it doesn't explode on larger input
        String s1 = "a".repeat(500);
        String s2 = "b".repeat(500);
        assertEquals(500, DamerauLevenshtein.distance(s1, s2));  // 500 substitutions

        assertEquals(500, DamerauLevenshtein.distance("a".repeat(500), ""));
        assertEquals(500, DamerauLevenshtein.distance("", "a".repeat(500)));
    }

    @Test
    public void testTranspositionAdvantage() {
        // OSA allows single adjacent swaps, so this stays 1:
        assertEquals(1, DamerauLevenshtein.distance("abcd", "acbd"));

        // For OSA, "abcdef" vs "abcfed" is 2 edits (can't chain transpositions)
        assertEquals(2, DamerauLevenshtein.distance("abcdef", "abcfed"));

        // Still benefits from adjacent swaps:
        assertEquals(1, DamerauLevenshtein.distance("ca", "ac"));

        // More complex case: "abc" vs "cab"
        // OSA counts this as 2 edits (swap + move), not 1.
        assertEquals(2, DamerauLevenshtein.distance("abc", "cab"));
    }
}

