package main.java.ie.atu.forge.Vectorisers;

import java.util.*;

/**
 * Converts text into a collection of unordered words (or tokens) and counts.
 */
public class BagOfWords {
    private final Map<String, Integer> bag = new HashMap<String, Integer>();

    /**
     * Adds a word to the bag (corpus). Assumes any necessary text normalisation has already been done.
     *
     * @param word The word to be added.
     */
    public void add(String word) {
        if(word == null || word.isEmpty()) return;
        String cleanedWord = cleanWord(word);
        bag.put(cleanedWord, bag.computeIfAbsent(cleanedWord, k -> 0) + 1);
    }

    /**
     * Adds an array of words to the bag (corpus). Assumes any necessary text normalisation has already been done.
     *
     * @param words The words to be added.
     */
    public void add(String[] words) {
        if(words == null || words.length == 0) return;

        for (String word : words) {
            add(word);
        }
    }

    /**
     * Adds a full sentence (or document) to the bag. Will split the sentence by white space before adding. Will remove punctuation.
     * @param sentence The sentence containing the words to be added to the bag.
     */
    public void addSentence(String sentence) {
        add(sentence.replaceAll("\\p{Punct}", "").split("\\s+")); // \\p{Punct} removes punctuation. \\s+ splits on white space.
    }

    /**
     * Converts the given text array into a vector of integers (counts) based on the words in the bag (corpus).
     * If a word appears in the text array but not in the bag, it won't be represented in the vector.
     * If a word in the bag is not present in the text array, it will receive a value of 0. Otherwise, the value will be the number of times the word appears in the text array.
     * @param text The text to be vectorised.
     * @return The vector representation of the provided text array.
     */
    public int[] vectorise(String[] text) throws IllegalArgumentException {
        if(text == null) throw new IllegalArgumentException("Attempted to vectorise a null text.");

        int[] vector = new int[size()];
        Map<String, Integer> counts = new HashMap<>();

        // First step is to count how often each word appears in the text array.
        for(String word: text) {
            String cleanedWord = cleanWord(word);
            counts.put(cleanedWord, counts.computeIfAbsent(cleanedWord, k -> 0) + 1);
        }

        // Guarantee the corpus words are sorted each time, as using a hashmap will not guarantee order by itself. An alternative is using a TreeMap, however TreeMap look up times are O(log n), while HashMaps are O(1).
        List<String> vocab = new ArrayList<>(bag.keySet());
        Collections.sort(vocab);

        // For every word in the bag (corpus), find out how often it appears in the text array, and build the vector.
        for(int i = 0; i < vocab.size(); i++) {
            vector[i] = counts.getOrDefault(vocab.get(i), 0);
        }

        return vector;
    }


    // Currently only makes everything lower case. Can easily be expanded upon in the future.
    private String cleanWord(String word) {
        if(word == null) return word;
        return word.toLowerCase();
    }

    /**
     * Gets the map containing all added words and their counts.
     * @return
     */
    public Map<String, Integer> getBag() {
        return new HashMap<>(bag);  // returning bag directly would allow modification of bag from outside this object.
    }

    /**
     * Gets the set of unique words that are in the bag.
     * @return A set of Strings containing all unique words from the bag.
     */
    public Set<String> vocabulary() {
        return new HashSet<>(bag.keySet());
    }


    /**
     * Gets the count for a specified word.
     *
     * @param word The word to find the count of.
     * @return The count of the word.
     */
    public int getCount(String word) {
        if(word == null || word.isEmpty()) return 0;
        return bag.getOrDefault(cleanWord(word), 0);
    }

    /**
     * Gets the number of unique words in the bag.
     *
     * @return The number of unique words in the bag.
     */
    public int size() {
        return bag.size();
    }

    /**
     * Gets the total number of words in the bag (i.e. sums all word counts).
     *
     * @return The sum of all word counts in the bag.
     */
    public long totalWordCount() {
        long result = 0;
        for(Integer count: bag.values()) result += count;

        return result;
    }

    /**
     * Clears all entries from the bag.
     */
    public void clear() {
        bag.clear();
    }
}
