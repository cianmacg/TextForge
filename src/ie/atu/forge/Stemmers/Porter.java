package ie.atu.forge.Stemmers;

public class Porter {
    /*
    Porter's stemmer to stem a single word.
     */
    public static String stem(String input) {
        StringBuilder result = new StringBuilder();
        int m = measure(input);

        return result.toString();
    }

    private static int measure(String input) {
        char[] chars = input.replaceAll("[^a-zA-Z ]", "").toLowerCase().toCharArray();

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

        for (int i = 1; i < chars.length; i++) {
            if (chars[i - 1] != chars[i]) {
                size++;
                merged_chars[size] = chars[i];
            }
        }

        char[] merged = new char[size];
        System.arraycopy(merged_chars, 0, merged, 0, size);
        merged_chars = null;

        // Measure is what we will return, it is the count of consecutive VC in the word.
        int m = 0;
        // Finally count the 'VC' occurences.
        char[] counted_chars = new char[4];
        int i = 0;
        if (merged[0] == 'C') {
            counted_chars[0] = 'C';
            i++;
        }

        counted_chars[i] = 'V';
        counted_chars[i + 1] = 'C';

        if (merged[merged.length - 1] == 'V') {
            counted_chars[i + 2] = 'V';
        }

        while (i < merged.length) {
            if (merged[i] == 'V' && merged[i + 1] == 'C') {
                m++;
                i += 2;
            }
        }

        return m;
    }
}
