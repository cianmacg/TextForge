package test.java.Vectorisers;

import main.java.ie.atu.forge.Vectorisers.BagOfWords;

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

    // NEW TESTS FOR VECTORISE FUNCTION

    @Test
    public void testVectoriseEmptyBag() {
        BagOfWords bow = new BagOfWords();
        String[] text = {"hello", "world"};

        int[] vector = bow.vectorise(text);

        assertEquals(0, vector.length);
    }

    @Test
    public void testVectoriseEmptyText() {
        BagOfWords bow = new BagOfWords();
        bow.add(new String[]{"apple", "banana", "cherry"});
        String[] text = {};

        int[] vector = bow.vectorise(text);

        assertEquals(3, vector.length);
        assertArrayEquals(new int[]{0, 0, 0}, vector);
    }

    @Test
    public void testVectoriseNullText() {
        BagOfWords bow = new BagOfWords();
        bow.add(new String[]{"apple", "banana"});

        assertThrows(IllegalArgumentException.class, () -> {
            bow.vectorise(null);
        });
    }

    @Test
    public void testVectoriseExactMatch() {
        BagOfWords bow = new BagOfWords();
        bow.add(new String[]{"apple", "banana", "cherry"});
        String[] text = {"apple", "banana", "cherry"};

        int[] vector = bow.vectorise(text);

        assertEquals(3, vector.length);
        // Words are sorted alphabetically: apple, banana, cherry
        assertArrayEquals(new int[]{1, 1, 1}, vector);
    }

    @Test
    public void testVectoriseWithRepeatedWords() {
        BagOfWords bow = new BagOfWords();
        bow.add(new String[]{"apple", "banana", "cherry"});
        String[] text = {"apple", "apple", "banana", "cherry", "cherry", "cherry"};

        int[] vector = bow.vectorise(text);

        assertEquals(3, vector.length);
        // Words are sorted alphabetically: apple, banana, cherry
        assertArrayEquals(new int[]{2, 1, 3}, vector); // apple: 2, banana: 1, cherry: 3
    }

    @Test
    public void testVectorisePartialMatch() {
        BagOfWords bow = new BagOfWords();
        bow.add(new String[]{"apple", "banana", "cherry", "date"});
        String[] text = {"apple", "banana", "grape"}; // grape not in bag

        int[] vector = bow.vectorise(text);

        assertEquals(4, vector.length);
        // Words are sorted alphabetically: apple, banana, cherry, date
        assertArrayEquals(new int[]{1, 1, 0, 0}, vector); // apple: 1, banana: 1, cherry: 0, date: 0
    }

    @Test
    public void testVectoriseNoMatch() {
        BagOfWords bow = new BagOfWords();
        bow.add(new String[]{"apple", "banana", "cherry"});
        String[] text = {"grape", "orange", "kiwi"}; // none in bag

        int[] vector = bow.vectorise(text);

        assertEquals(3, vector.length);
        assertArrayEquals(new int[]{0, 0, 0}, vector);
    }

    @Test
    public void testVectoriseCaseInsensitive() {
        BagOfWords bow = new BagOfWords();
        bow.add(new String[]{"apple", "banana"});
        String[] text = {"APPLE", "Apple", "BANANA", "bAnAnA"};

        int[] vector = bow.vectorise(text);

        assertEquals(2, vector.length);
        // Words are sorted alphabetically: apple, banana
        assertArrayEquals(new int[]{2, 2}, vector); // apple: 2, banana: 2
    }

    @Test
    public void testVectoriseWithNullAndEmptyStrings() {
        BagOfWords bow = new BagOfWords();
        bow.add(new String[]{"apple", "banana"});
        String[] text = {"apple", null, "", "banana", null};

        int[] vector = bow.vectorise(text);

        assertEquals(2, vector.length);
        // Words are sorted alphabetically: apple, banana
        assertArrayEquals(new int[]{1, 1}, vector); // null and empty should be ignored
    }

    @Test
    public void testVectoriseConsistentOrdering() {
        BagOfWords bow = new BagOfWords();
        bow.add(new String[]{"zebra", "apple", "banana"});

        String[] text1 = {"apple", "zebra"};
        String[] text2 = {"zebra", "apple"};

        int[] vector1 = bow.vectorise(text1);
        int[] vector2 = bow.vectorise(text2);

        // Vectors should be identical regardless of input order
        assertArrayEquals(vector1, vector2);
    }

    @Test
    public void testVectoriseLargeInput() {
        BagOfWords bow = new BagOfWords();
        bow.add(new String[]{"the", "quick", "brown", "fox"});

        String[] text = new String[1000];
        Arrays.fill(text, "the");

        int[] vector = bow.vectorise(text);

        assertEquals(4, vector.length);
        // Words are sorted alphabetically: brown, fox, quick, the
        assertArrayEquals(new int[]{0, 0, 0, 1000}, vector); // brown: 0, fox: 0, quick: 0, the: 1000
    }

    @Test
    public void testVectoriseAfterBagModification() {
        BagOfWords bow = new BagOfWords();
        bow.add("apple");

        String[] text = {"apple", "banana"};
        int[] vector1 = bow.vectorise(text);

        bow.add("banana"); // Add banana to bag
        int[] vector2 = bow.vectorise(text);

        // First vector should be length 1, second should be length 2
        assertEquals(1, vector1.length);
        assertEquals(2, vector2.length);

        // First vector: only apple in bag
        assertArrayEquals(new int[]{1}, vector1); // apple: 1

        // Second vector: apple and banana in bag (sorted alphabetically)
        assertArrayEquals(new int[]{1, 1}, vector2); // apple: 1, banana: 1
    }

    @Test
    public void testVectoriseSortingOrder() {
        BagOfWords bow = new BagOfWords();
        // Add words in non-alphabetical order
        bow.add(new String[]{"zebra", "apple", "banana", "cherry"});
        String[] text = {"zebra", "apple", "cherry"};

        int[] vector = bow.vectorise(text);

        assertEquals(4, vector.length);
        // Words should be sorted alphabetically: apple, banana, cherry, zebra
        assertArrayEquals(new int[]{1, 0, 1, 1}, vector); // apple: 1, banana: 0, cherry: 1, zebra: 1
    }
}