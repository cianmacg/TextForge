package ie.atu.forge.Stemmers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Porter {
    /*
    Porter's stemmer to stem a single word.
     */
    private static final Map<String, String> step_2_endings = new HashMap<>();

    // Initialiser block to add endings to appropriate maps.
    static {
        step_2_endings.put("ational", "ate");
        step_2_endings.put("tional", "tion");
        step_2_endings.put("enci", "ence");
        step_2_endings.put("anci", "ance");
        step_2_endings.put("izer", "ize");
        step_2_endings.put("iser", "ise");
        step_2_endings.put("abli", "able");
        step_2_endings.put("alli", "al");
        step_2_endings.put("entli", "ent");
        step_2_endings.put("eli", "e");
        step_2_endings.put("ousli", "ous");
        step_2_endings.put("ization", "ize");
        step_2_endings.put("isation", "ise");
        step_2_endings.put("ation", "ate");
        step_2_endings.put("ator", "ate");
        step_2_endings.put("alism", "al");
        step_2_endings.put("iveness", "ive");
        step_2_endings.put("fulness", "ful");
        step_2_endings.put("ousness", "ous");
        step_2_endings.put("aliti", "al");
        step_2_endings.put("iviti", "ive");
        step_2_endings.put("biliti", "ble");
    }


    public static String stem(String input) {
        StringBuilder result = new StringBuilder();
        char[] chars = input.replaceAll("[^a-zA-Z ]", "").toLowerCase().toCharArray();

        result.append(step_2(step_1c(step_1b(step_1a(chars)))));

        return result.toString();
    }

    private static char[] step_1a(char[] input) {
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

    private static char[] step_1b(char[] input) {
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
            if(condition_v(result)) {
                // In this case, a further rule is applied, sub_1b
                result = sub_1b(result);
                return result;
            }

        }

        // If the ending is 'ing', and the condition_v is satisfied, remove it.
        if(input.length > 3 && (input[input.length - 3] == 'i' && input[input.length - 2] == 'n' && input[input.length - 1] == 'g')) {
            result = new char[input.length - 3];
            System.arraycopy(input, 0, result, 0, result.length);
            if(condition_v(result)) {
                // In this case, a further rule is applied, sub_1b
                result = sub_1b(result);
                return result;
            }
        }

        return input;
    }

    // If 'ed' or 'ing' are removed in step_1b, do this.
    private static char[] sub_1b(char[] input) {
        char[] result;

        // If the string ending is 'at', 'bl', or 'iz', add an 'e' to the end of it.
        if((input[input.length - 2] == 'a' && input[input.length - 1] == 't') || (input[input.length - 2] == 'b' && input[input.length - 1] == 'l') || (input[input.length - 2] == 'i' && input[input.length - 1] == 'z')) {
            result = new char[input.length + 1];
            System.arraycopy(input, 0, result, 0, input.length);
            result[result.length - 1] = 'e';
            return result;
        }

        // If the ending is a double consonant that is not l, s, or z, make is a single letter (i.e. remove the final letter)
        char consonant = condition_d(input);
        // '!' indicates no double consonant.
        if(consonant != '!' && consonant != 'l' && consonant != 's' && consonant != 'z') {
            result = new char[input.length - 1];
            System.arraycopy(input, 0, result, 0, result.length);
            return result;
        }

        // Finally, if measure == 1, and condition_o, is satisfied, append an 'e' to the end of the stem.
        if(measure(input) == 1 && condition_o(input)) {
            result = new char[input.length + 1];
            System.arraycopy(input, 0, result, 0, input.length);
            result[result.length - 1] = 'e';
            return result;
        } else {
            System.out.println("Measure: " + measure(input));
            System.out.println("Condition_o: " + condition_o(input));
        }

        return input;
    }

    private static char[] step_1c(char[] input) {
        char[] result;

        if(condition_v(input) && input[input.length - 1] == 'y') {
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
    private static char[] step_2(char[] input) {
        String input_string = new String(input);

        // Check for matching
        for(String ending: step_2_endings.keySet()) {
            if(input_string.contains(ending)) {
                char[] stem = new char[input.length - ending.length()];
                System.arraycopy(input, 0, stem, 0, stem.length);

                // If there is a match, make sure the measure of the stem is > 0
                if(measure(stem) > 0) {
                    char[] new_ending = step_2_endings.get(ending).toCharArray();
                    char[] result = new char[stem.length + new_ending.length];
                    System.arraycopy(stem, 0, result, 0, stem.length);
                    System.arraycopy(new_ending, 0, result, stem.length, new_ending.length);
                    return result;
                }
                break;
            }
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

        char[] merged_chars = new char[chars.length];
        int size = 0;
        // Second pass. Merge adjacent 'C' to a single 'C', same for 'V'.
        merged_chars[size] = chars[0];
        size++;
        for (int i = 1; i < chars.length; i++) {
            if (chars[i - 1] != chars[i]) {
                merged_chars[size] = chars[i];
                size++;
            }
        }

        char[] merged = new char[size];
        System.arraycopy(merged_chars, 0, merged, 0, size);
        merged_chars = null;

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

    // Check if the stem ends with 's'.
    private static boolean condition_s(char[] input) {
        return input[input.length - 1] == 's';
    }

    // Alternate version of above to check if the stem ends with a specific letter.
    private static boolean condition_s(char[] input, char letter) {
        return input[input.length - 1] == letter;
    }

    // Check if the stem contains a vowel.
    private static boolean condition_v(char[] input) {
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

    // Check if the stem ends with a double consonant (e.g., tt, ss, etc). Returns the consonant if true, returns '!' if false.
    private static char condition_d(char[] input) {
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
    private static boolean condition_o(char[] input) {
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
