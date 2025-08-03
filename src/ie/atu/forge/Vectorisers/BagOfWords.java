package ie.atu.forge.Vectorisers;

import java.util.*;

//
public class BagOfWords {
    private final Map<String, Integer> bag = new HashMap<String, Integer>();

    public void add(String word) {
        if(word == null || word.isEmpty()) return;
        String cleanedWord = cleanWord(word);
        bag.put(cleanedWord, bag.computeIfAbsent(cleanedWord, k -> 0) + 1);
    }

    public void add(String[] tokens) {
        if(tokens == null || tokens.length == 0) return;

        for (String token : tokens) {
            add(token);
        }
    }

    // Currently only makes everything lower case. Can easily be expanded upon in the furute.
    private String cleanWord(String word) {
        return word.toLowerCase();
    }

    public Map<String, Integer> getBag() {
        return new HashMap<>(bag);  // returning bag directly would allow modification of bag from outside this object.
    }

    public Set<String> vocabulary() {
        return new HashSet<>(bag.keySet());
    }

    // Returns the count of a specific word.
    public int getCount(String word) {
        if(word == null || word.isEmpty()) return 0;
        return bag.getOrDefault(cleanWord(word), 0);
    }

    // Returns the number of words in the bag.
    public int size() {
        return bag.size();
    }

    // Returns the sum of all word counts (long returned just in case a large number of words have been added to the bag.
    public long totalWordCount() {
        long result = 0;
        for(Integer count: bag.values()) result += count;

        return result;
    }

    // Empties the bag.
    public void clear() {
        bag.clear();
    }
}
