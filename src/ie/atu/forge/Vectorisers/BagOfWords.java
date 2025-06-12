package ie.atu.forge.Vectorisers;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

//
public class BagOfWords {
    public Map<String, Integer> bag = new HashMap<String, Integer>();

    public void add(String word) {
        bag.put(word, bag.computeIfAbsent(word, k -> 0) + 1);
    }

    public void addBulk(String[] words) {
        for (String word : words) {
            add(word);
        }
    }

    public Set<String> vocabulary() {
        return bag.keySet();
    }
}
