package ie.atu.forge.Stemmers;

import java.util.*;

/*
Porter, M.F. (1980), "An algorithm for suffix stripping", Program: electronic library and information systems, Vol. 14 No. 3, pp. 130-137. https://doi.org/10.1108/eb046814
 */

// Implementation of Porter's stemmer based on the original paper.

/**
 * Implementation of Porter's stemmer based on the <a href="https://doi.org/10.1108/eb046814">original paper.</a>
 */
public class Porter {
    /*
    Porter's stemmer to stem a single word.
     */
    private static final Map<String, String> step2Endings = new HashMap<>();
    private static final Map<String, String> step3Endings = new HashMap<>();
    private static final Set<String> step4Endings = new HashSet<>();

    // Initialiser block to add endings to appropriate maps.
    static {
        // Must be measure > 0
        step2Endings.put("ational", "ate");
        step2Endings.put("tional", "tion");
        step2Endings.put("enci", "ence");
        step2Endings.put("anci", "ance");
        step2Endings.put("izer", "ize");
        step2Endings.put("iser", "ise");
        step2Endings.put("abli", "able");
        step2Endings.put("alli", "al");
        step2Endings.put("entli", "ent");
        step2Endings.put("eli", "e");
        step2Endings.put("ousli", "ous");
        step2Endings.put("ization", "ize");
        step2Endings.put("isation", "ise");
        step2Endings.put("ation", "ate");
        step2Endings.put("ator", "ate");
        step2Endings.put("alism", "al");
        step2Endings.put("iveness", "ive");
        step2Endings.put("fulness", "ful");
        step2Endings.put("ousness", "ous");
        step2Endings.put("aliti", "al");
        step2Endings.put("iviti", "ive");
        step2Endings.put("biliti", "ble");

        // Must be measure > 0
        step3Endings.put("icate", "ic");
        step3Endings.put("ative", "");
        step3Endings.put("alize", "al");
        step3Endings.put("alise", "al");
        step3Endings.put("iciti", "ic");
        step3Endings.put("ical", "ic");
        step3Endings.put("ful", "");
        step3Endings.put("ness", "");

        // Must be measure > 1
        step4Endings.add("al");
        step4Endings.add("ance");
        step4Endings.add("ence");
        step4Endings.add("er");
        step4Endings.add("ic");
        step4Endings.add("able");
        step4Endings.add("ible");
        step4Endings.add("ant");
        step4Endings.add("ement");
        step4Endings.add("ment");
        step4Endings.add("ent");
        step4Endings.add("ion"); // Also needs to satisfy ending with 's' or 't'
        step4Endings.add("ou");
        step4Endings.add("ism");
        step4Endings.add("ate");
        step4Endings.add("iti");
        step4Endings.add("ous");
        step4Endings.add("ive");
        step4Endings.add("ize");
        step4Endings.add("ise");

    }


    /**
     * Stems a single word based on the rules from the original Porter's stemmer paper.
     * @param input The word to be stemmed.
     * @return The result of applying the stemming rules to the word.
     */
    public static String stem(String input) {
        StringBuilder result = new StringBuilder();
        char[] chars = input.replaceAll("[^a-zA-Z ]", "").toLowerCase().toCharArray();

        result.append(
                step5B(
                        step5A(
                                step4(
                                        step3(
                                                step2(
                                                        step1C(
                                                                step1B(
                                                                        step1A(chars))))))))
        );

        return result.toString();
    }

    /**
     * Stems an array of words based on the rules from the original Porter's stemmer paper.
     * @param inputs The words to be stemmed.
     * @return The result of applying the stemming rules to each word, as a String array (word order is preserved).
     */
    public static String[] stem(String[] inputs) {
        String[] stems = new String[inputs.length];

        for(int i = 0; i < inputs.length; i++) {
            stems[i] = stem(inputs[i]);
        }
        return stems;
    }

    private static char[] step1A(char[] input) {
        /*
        sses -> ss
        ies -> i
        ss -> ss
        s ->
         */
        char[] result;

        if(input[input.length - 1] == 's') {
            // 'ss' endings aren't removed. This is here to prevent over-stemming. Without this, the last 's' would be removed. Don't want that.
            if(input[input.length - 2] == 's') {
                return input;
            }
            else if(input[input.length - 2] == 'e') {
                // Both 'ies' and 'sses' endings remove the last 2 characters from the word.
                if(input[input.length - 3] == 'i' || (input[input.length - 3] == 's' && input[input.length - 4] == 's')) {
                    result = new char[input.length - 2];
                    System.arraycopy(input, 0, result, 0, result.length);
                    return result;
                }
            }

            // If the ending is 's', and doesn't match the other parts of the rule, remove the 's'.
            result = new char[input.length - 1];
            System.arraycopy(input, 0, result, 0, result.length);
            return result;
        }

        // If no part of the rule is a match, return the original character array.
        return input;
    }

    private static char[] step1B(char[] input) {
        /*
        m>0 eed -> ee
        ed ->
        ing ->
         */
        char[] result;

        if(input.length > 2 && (input[input.length - 2] == 'e' && input[input.length - 1] == 'd')) {
            // If the measure (m) is > 0 and the end of the word is 'eed', we remove the last 'd', making the new ending 'ee'.
            if(input.length > 3 && input[input.length - 3] == 'e') {
                result = new char[input.length - 1];
                System.arraycopy(input, 0, result, 0, result.length);

                if(measure(result) > 0) {
                    return result;
                }

                // If the measure is 0, no stemming is needed and the original input is returned.
                return input;
            }

            // If the ending is 'ed', and condition_v is satisfied, remove it.
            result = new char[input.length - 2];
            System.arraycopy(input, 0, result, 0, result.length);
            if(conditionV(result)) {
                // In this case, a further rule is applied, sub_1b
                result = sub1B(result);
                return result;
            }

        }

        // If the ending is 'ing', and the condition_v is satisfied, remove it.
        if(input.length > 3 && (input[input.length - 3] == 'i' && input[input.length - 2] == 'n' && input[input.length - 1] == 'g')) {
            result = new char[input.length - 3];
            System.arraycopy(input, 0, result, 0, result.length);
            if(conditionV(result)) {
                // In this case, a further rule is applied, sub_1b
                result = sub1B(result);
                return result;
            }
        }

        return input;
    }

    // If 'ed' or 'ing' are removed in step_1b, do this.
    private static char[] sub1B(char[] input) {
        char[] result;

        // If the string ending is 'at', 'bl', or 'iz', add an 'e' to the end of it.
        if((input[input.length - 2] == 'a' && input[input.length - 1] == 't') || (input[input.length - 2] == 'b' && input[input.length - 1] == 'l') || (input[input.length - 2] == 'i' && input[input.length - 1] == 'z')) {
            result = new char[input.length + 1];
            System.arraycopy(input, 0, result, 0, input.length);
            result[result.length - 1] = 'e';
            return result;
        }

        // If the ending is a double consonant that is not l, s, or z, make is a single letter (i.e. remove the final letter)
        char consonant = conditionD(input);
        // '!' indicates no double consonant.
        if(consonant != '!' && consonant != 'l' && consonant != 's' && consonant != 'z') {
            result = new char[input.length - 1];
            System.arraycopy(input, 0, result, 0, result.length);
            return result;
        }

        // Finally, if measure == 1, and condition_o, is satisfied, append an 'e' to the end of the stem.
        if(measure(input) == 1 && conditionO(input)) {
            result = new char[input.length + 1];
            System.arraycopy(input, 0, result, 0, input.length);
            result[result.length - 1] = 'e';
            return result;
        }

        return input;
    }

    private static char[] step1C(char[] input) {
        char[] result;

        if(conditionV(input) && input[input.length - 1] == 'y') {
            result = new char[input.length];
            System.arraycopy(input, 0, result, 0, result.length);
            result[result.length - 1] = 'i';
            return result;
        }

        return input;
    }

    /*
    Not sure the best way to go about doing this part. I'm sure efficiency will be suboptimal.
    Thinking putting these into a map may be the best way to go.
     */
    private static char[] step2(char[] input) {
        String inputString = new String(input);

        // Check for matching
        for(String ending: step2Endings.keySet()) {
            int input_len = inputString.length();
            int ending_len = ending.length();
            if(input_len > ending_len && inputString.substring(input_len - ending_len).equals(ending)) {
                char[] stem = new char[input.length - ending.length()];
                System.arraycopy(input, 0, stem, 0, stem.length);

                // If there is a match, make sure the measure of the stem is > 0
                if(measure(stem) > 0) {
                    char[] newEnding = step2Endings.get(ending).toCharArray();
                    char[] result = new char[stem.length + newEnding.length];
                    System.arraycopy(stem, 0, result, 0, stem.length);
                    System.arraycopy(newEnding, 0, result, stem.length, newEnding.length);
                    return result;
                }
                break;
            }
        }

        return input;
    }

    /*
    Same idea as step 2, but using different endings (i.e. a different map).
     */
    private static char[] step3(char[] input) {
        String input_string = new String(input);

        for(String ending: step3Endings.keySet()) {
            int inputLen = input_string.length();
            int endingLen = ending.length();
            if(inputLen > endingLen && input_string.substring(inputLen - endingLen).equals(ending)) {
                char[] stem = new char[input.length - ending.length()];
                System.arraycopy(input, 0, stem, 0, stem.length);
                if(measure(stem) > 0) {
                    char[] newEnding = step3Endings.get(ending).toCharArray();
                    char[] result = new char[stem.length + newEnding.length];
                    System.arraycopy(stem, 0, result, 0, stem.length);
                    System.arraycopy(newEnding, 0, result, stem.length, newEnding.length);
                    return result;
                }
            }
        }

        return input;
    }

    private static char[] step4(char[] input) {
        String inputString = new String(input);

        for(String ending: step4Endings.toArray(new String[0])) {
            int inputLen = inputString.length();
            int endingLen = ending.length();
            if(inputLen > endingLen && inputString.substring(inputLen - endingLen).equals(ending)) {
                char[] stem = new char[input.length - ending.length()];
                System.arraycopy(input, 0, stem, 0, stem.length);
                if(measure(stem) > 1) {
                    // For the "ion" entry, the stem must end with 's' or 't', otherwise it is ignored.
                    if(ending.equals("ion")) {
                        if(!conditionS(stem,'s') && !conditionS(stem,'t')) break;
                    }

                    return stem;
                }
            }
        }

        return input;
    }

    private static char[] step5A(char[] input) {
        if(input[input.length - 1] == 'e') {
            char[] stem = new char[input.length - 1];
            System.arraycopy(input, 0, stem, 0, stem.length);
            int m = measure(stem);
            if(m > 1) {
                return stem;
            }
            else if(m == 1 && !conditionO(stem)) {
                return stem;
            }
        }
        return input;
    }

    private static char[] step5B(char[] input) {
        char[] stem = new char[input.length - 1];
        System.arraycopy(input, 0, stem, 0, stem.length);
        int m = measure(stem);

        // If measure > 1 and the word ends with a double 'l'
        if(m > 1 && conditionD(input) == 'l') {
            return stem;
        }

        return input;
    }

    private static int measure(char[] input) {
        char[] chars = Arrays.copyOf(input, input.length);

        // First Pass. Change characters to 'C' for consonant or 'V' for vowel.
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == 'a' || chars[i] == 'e' || chars[i] == 'i' || chars[i] == 'o' || chars[i] == 'u') {
                chars[i] = 'V';
            }
            // y is special. If it is preceded by a consonant, it is considered a vowel, otherwise it is a consonant.
            else if (chars[i] == 'y') {
                if (i == 0 || chars[i - 1] == 'V') {
                    chars[i] = 'C';
                } else {
                    chars[i] = 'V';
                }
            } else {
                chars[i] = 'C';
            }
        }

        char[] mergedChars = new char[chars.length];
        int size = 0;
        // Second pass. Merge adjacent 'C' to a single 'C', same for 'V'.
        mergedChars[size] = chars[0];
        size++;
        for (int i = 1; i < chars.length; i++) {
            if (chars[i - 1] != chars[i]) {
                mergedChars[size] = chars[i];
                size++;
            }
        }

        char[] merged = new char[size];
        System.arraycopy(mergedChars, 0, merged, 0, size);
        mergedChars = null;

        // Measure is what we will return, it is the count of consecutive VC in the word.
        int m = 0;
        // Finally count the 'VC' occurences.
        int i = 0;

        // If the first character is a c, skip to the next one to begin counting.
        if (merged[0] == 'C') {
            i++;
        }

        while (i < merged.length - 1) {
            if (merged[i] == 'V' && merged[i + 1] == 'C') {
                m++;
                i += 2;
            }
        }

        return m;
    }

    // Check if the stem ends with a specific letter.
    private static boolean conditionS(char[] input, char letter) {
        return input[input.length - 1] == letter;
    }

    // Check if the stem contains a vowel.
    private static boolean conditionV(char[] input) {
        char[] vowels = {'a', 'e', 'i', 'o', 'u'};

        for(char i: input) {
            for (char v: vowels) {
                if (i == v) {
                    return true;
                }
            }
        }

        return false;
    }

    // Check if the stem ends with a double consonant (e.g., tt, ss, etc.). Returns the consonant if true, returns '!' if false.
    private static char conditionD(char[] input) {
        // Check if the stem ending is a repeated letter
        if(input[input.length - 1] == input[input.length - 2]) {
            char[] vowels = {'a', 'e', 'i', 'o', 'u'};

            // If the repeated letter is a vowel, we can ignore it.
            for(char v: vowels) {
                if(input[input.length - 1] == v) {
                    return '!';
                }
            }
            return input[input.length - 1];
        }

        return '!';
    }

    // Check if stem ends with cvc, and the second c is not w, x, or y.
    private static boolean conditionO(char[] input) {
        char[] vowels = {'a', 'e', 'i', 'o', 'u'};
        char[] consonants = {'w', 'x', 'y'}; // Consonants the last letter cannot be.

        // For the last 3 letters of the stem, check each to see if they are a vowel. False, True, False is what we are looking for.
        for(char v: vowels) {
            // Check if for the first c of cvc. If it is a vowel, we can end here.
            if(input[input.length - 3] == v) return false;
        }

        boolean vowel = false;
        for(char v: vowels) {
            // Now, lets check for the v in cvc.
            if(input[input.length - 2] == v) {
                vowel = true;
                break;
            }
        }

        // If a vowel wasn't found, we can end here.
        if(!vowel) return false;

        for(char v: vowels) {
            // Finally, check for the second c.
            if(input[input.length - 1] == v) return false;
        }

        // If we have made it here, we have cvc. The last thing to do is make sure the second c is not w, x, or y.
        for(char c: consonants) {
            if(input[input.length - 1] == c) return false;
        }

        // If we have made it this far, the condition is satisfied.
        return true;
    }
}