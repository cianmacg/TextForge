package Tests.Vectorisers;

import ie.atu.forge.Vectorisers.BagOfWords;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

public class BagOfWordsTest {

    @Test
    public void testAddSingleWord() {
        BagOfWords bow = new BagOfWords();
        bow.add("Apple");

        assertEquals(1, bow.getCount("apple")); // case normalization
        assertEquals(1, bow.size());
        assertEquals(1, bow.totalWordCount());
    }

    @Test
    public void testAddSameWordMultipleTimes() {
        BagOfWords bow = new BagOfWords();
        bow.add("banana");
        bow.add("Banana");
        bow.add("BANANA");

        assertEquals(3, bow.getCount("banana"));
        assertEquals(1, bow.size());
        assertEquals(3, bow.totalWordCount());
    }

    @Test
    public void testAddArrayOfWords() {
        BagOfWords bow = new BagOfWords();
        bow.add(new String[]{"cat", "dog", "cat"});

        assertEquals(2, bow.getCount("cat"));
        assertEquals(1, bow.getCount("dog"));
        assertEquals(2, bow.size());
        assertEquals(3, bow.totalWordCount());
    }

    @Test
    public void testAddEmptyOrNullWord() {
        BagOfWords bow = new BagOfWords();
        bow.add("");
        bow.add((String) null);
        bow.add(new String[] { "", null });

        assertEquals(0, bow.size());
        assertEquals(0, bow.totalWordCount());
    }

    @Test
    public void testAddNullArray() {
        BagOfWords bow = new BagOfWords();
        bow.add((String[]) null);

        assertEquals(0, bow.size());
        assertEquals(0, bow.totalWordCount());
    }

    @Test
    public void testGetCountForMissingWord() {
        BagOfWords bow = new BagOfWords();
        bow.add("fish");

        assertEquals(0, bow.getCount("dog"));
        assertEquals(0, bow.getCount(""));
        assertEquals(0, bow.getCount(null));
    }

    @Test
    public void testVocabularyReturnsCopy() {
        BagOfWords bow = new BagOfWords();
        bow.add("apple");
        Set<String> vocab = bow.vocabulary();

        vocab.add("banana"); // should not affect internal state

        assertFalse(bow.vocabulary().contains("banana"));
        assertEquals(Set.of("apple"), bow.vocabulary());
    }

    @Test
    public void testGetBagReturnsCopy() {
        BagOfWords bow = new BagOfWords();
        bow.add("apple");
        Map<String, Integer> bag = bow.getBag();

        bag.put("banana", 42); // should not affect internal state

        assertFalse(bow.getBag().containsKey("banana"));
        assertEquals(1, bow.getBag().size());
    }

    @Test
    public void testClearResetsBag() {
        BagOfWords bow = new BagOfWords();
        bow.add("a");
        bow.add("b");

        bow.clear();

        assertEquals(0, bow.size());
        assertEquals(0, bow.totalWordCount());
        assertEquals(0, bow.getCount("a"));
    }
}
