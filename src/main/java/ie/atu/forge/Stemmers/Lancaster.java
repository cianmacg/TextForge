package ie.atu.forge.Stemmers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * An implementation of the Lancaster (Paice-Husk) stemmer using the sample rules from the <a href="https://dl.acm.org/doi/10.1145/101306.101310">original paper</a>.
 *
 * This is an aggressive stemmer, which uses the rules from the paper exactly.
 */
public class Lancaster {
    private static final Map<String, String> intactRules = new HashMap<>();
    private static final Map<String, String> rules = new HashMap<>();

    /*
     The key indicates the ending of the word.
     The value indicates what we should do to the ending.
     (VALUE)
     The number indicates how many characters to delete.
     The letters (if any) indicate what to append.
     The last character indicates if we should continue ('>') stemming or end ('.').

     Additionally, some map entries should only be done if the word has yet to be modified (intact).
     I put these into their own map, as some endings are also altered if they are not intact (causes issues otherwise as we then have 2 values for the same key).
     */
    static{
        // Page 1
        intactRules.put("ai", "2.");
        intactRules.put("a", "1.");
        rules.put("bb", "1.");
        rules.put("city", "3s.");
        rules.put("ci", "2>");
        rules.put("cn", "1t>");
        rules.put("dd", "1.");
        rules.put("dei", "3y>");
        rules.put("deec", "2ss.");
        rules.put("dee", "1.");
        rules.put("de", "2>");
        rules.put("dooh", "4>");
        rules.put("e", "1>");
        rules.put("feil", "1v.");
        rules.put("fi", "2>");
        rules.put("gni", "3>");
        rules.put("gai", "3y.");
        rules.put("ga", "2>");
        rules.put("gg", "1.");
        intactRules.put("ht", "2.");
        rules.put("hsiug", "5ct.");
        rules.put("hsi", "3>");
        intactRules.put("i", "1.");
        rules.put("i", "1y>");
        rules.put("ji", "1d.");
        rules.put("juf", "1s.");
        rules.put("ju", "1d.");
        rules.put("jo", "1d.");
        rules.put("jeh", "1r.");
        rules.put("jrev", "1t.");
        rules.put("jsim", "2t.");
        rules.put("jn", "1d.");
        rules.put("j", "1s.");
        rules.put("lbaifi", "6.");
        rules.put("lbai", "4y.");
        rules.put("lba", "3.");
        rules.put("lbi", "3.");
        rules.put("lib", "2l.");
        rules.put("lc", "1.");
        rules.put("lufi", "4y.");
        rules.put("luf", "3>");
        rules.put("lu", "2.");
        rules.put("lai", "3>");
        rules.put("lau", "3>");
        rules.put("la", "2>");
        rules.put("ll", "1.");
        rules.put("mui", "3.");
        intactRules.put("mu", "2.");
        rules.put("msi", "3>");
        rules.put("mm", "1.");
        rules.put("nois", "4j>");
        rules.put("noix", "4ct.");
        rules.put("noi", "3>");
        rules.put("nai", "3>");
        rules.put("na", "2>");
        rules.put("nee", "0.");
        rules.put("ne", "2>");
        rules.put("nn", "1.");
        // Page 2
        rules.put("pihs", "4>");
        rules.put("pp", "1.");
        rules.put("re", "2>");
        rules.put("rae", "0.");
        rules.put("ra", "2.");
        rules.put("ro", "2>");
        rules.put("ru", "2>");
        rules.put("rr", "1.");
        rules.put("rt", "1>");
        rules.put("rei", "3y>");
        rules.put("sei", "3y>");
        rules.put("sis", "2.");
        rules.put("si", "2>");
        rules.put("ssen", "4>");
        rules.put("ss", "0.");
        rules.put("suo", "3>");
        intactRules.put("su", "2.");
        intactRules.put("s", "1>");
        rules.put("s", "0.");
        rules.put("tacilp", "4y.");
        rules.put("ta", "2>");
        rules.put("tnem", "4>");
        rules.put("tne", "3>");
        rules.put("tna", "3>");
        rules.put("tpir", "2b.");
        rules.put("tpro", "2b.");
        rules.put("tcud", "1.");
        rules.put("tpmus", "2.");
        rules.put("tpec", "2iv.");
        rules.put("tulo", "2v.");
        rules.put("tsis", "0.");
        rules.put("tsi", "3>");
        rules.put("tt", "1.");
        rules.put("uqi", "3.");
        rules.put("ugo", "1.");
        rules.put("vis", "3j>");
        rules.put("vie", "0.");
        rules.put("vi", "2>");
        rules.put("ylb", "1>");
        rules.put("yli", "3y>");
        rules.put("ylp", "0.");
        rules.put("yl", "2>");
        rules.put("ygo", "1.");
        rules.put("yhp", "1.");
        rules.put("ymo", "1.");
        rules.put("ypo", "1.");
        rules.put("yti", "3>");
        rules.put("yte", "3>");
        rules.put("ytl", "2.");
        rules.put("yrtsi", "5.");
        rules.put("yra", "3>");
        rules.put("yro", "3>");
        rules.put("yfi", "3.");
        rules.put("ycn", "2t>");
        rules.put("yca", "3>");
        rules.put("zi", "2>");
        rules.put("zy", "1s.");
    }

    /**
     * Stems a single word using the Lancaster (Paice-Husk) rules from the original paper.
     *
     * @param input The word to be stemmed.
     * @return The result of applying the stemming rules.
     */
    public static String stem(String input) {
        if(input.length() <= 0) {
            return input.toLowerCase(); // For consistency, converted to lower case.
        }
        char[] stem = input.replaceAll("[^a-zA-Z ]", "").toLowerCase().toCharArray();

        return new String(findIntactRule(stem));
    }

    /**
     * Stems an array of words using the Lancaster (Paice-Husk) rules from the original paper.
     * @param inputs The words to be stemmed.
     * @return The results of stemming each word, as a String array (word order is preserved).
     */
    public static String[] stem(String[] inputs) {
        String[] stems = new String[inputs.length];

        for(int i = 0; i < inputs.length; i++) {
            stems[i] = stem(inputs[i]);
        }

        return stems;
    }

    /*
     The largest intact ending is only 2 characters. Because of this, I'm not going to bother with a loop.
     */
    private static char[] findIntactRule(char[] input) {
        /*
        This is the same as 'find_rule', but is used on the first pass.
        On the first pass, we also need to look at 'intact' rules.
         */

        if(input.length<=2) {
            return input;
        }

        /*
         Need to find what ending length to begin with. The maximum ending length in the map is 6.
         The first ending length we should check is the smaller of input.length - 2 (as the smallest possible stem length is 2) and 6.
         */
        int endLen = Math.min(input.length - 2, 6);
        char[] ending;
        // Keep looking for smaller ending matches until the stem is different from the original input.
        while(endLen >= 3) {
            ending = new char[endLen];
            /*
             Originally I was using 'System.arraycopy' here, but since the keys are the reverse of the ending (matching how they are in the original Lancaster paper),
             I thought it would be better to do this manually.
             */
            for(int i = 0; i < endLen; i++) {
                ending[i] = input[input.length - 1 - i];
            }
            String rule = rules.get(new String(ending));

            if(rule!=null) {
                char[] stem = applyRule(input, rule.toCharArray());

                if(!Arrays.equals(input, stem)) {
                    return stem;
                }
            }

            // No rule was found, reduce the ending size and try again.
            endLen--;
        }

        /*
         Intact rules only exist for endings of length 2 or less.
         I could have put this as an 'if' condition in the other loop, but thought removing that check for some of the endings lengths might improve performance.
         */
        while(endLen >= 1) {
            ending = new char[endLen];
            /*
             Originally I was using 'System.arraycopy' here, but since the keys are the reverse of the ending (matching how they are in the original Lancaster paper),
             I thought it would be better to do this manually.
             */
            for(int i = 0; i < endLen; i++) {
                ending[i] = input[input.length - 1 - i];
            }

            // Search intact rules first.
            String rule = intactRules.get(new String(ending));
            if(rule!=null) {
                char[] stem = applyRule(input, rule.toCharArray());

                if(!Arrays.equals(input, stem)) {
                    return stem;
                }
            }

            // If no intact rules exist, check the other rules.
            rule = rules.get(new String(ending));
            if(rule!=null) {
                char[] stem = applyRule(input, rule.toCharArray());

                if(!Arrays.equals(input, stem)) {
                    return stem;
                }
            }

            // No rule was found, reduce the ending size and try again.
            endLen--;
        }


        // If no matching rule was found, no stemming can be done. Return the original input.
        return input;
    }

    // Find an appropriate rule from the rule map.
    private static char[] findRule(char[] input) {
        /*
         Need to find what ending length to begin with. The maximum ending length in the map is 6.
         The first ending length we should check is the smaller of input.length - 2 (as the smallest possible stem length is 2) and 6.
         */
        int endLen = Math.min(input.length - 2, 6);
        // Keep looking for smaller ending matches until the stem is different from the original input.
        while(endLen >= 1) {
            char[] ending = new char[endLen];
            /*
             Originally I was using 'System.arraycopy' here, but since the keys are the reverse of the ending (matching how they are in the original Lancaster paper),
             I thought it would be better to do this manually.
             */
            for(int i = 0; i < endLen; i++) {
                ending[i] = input[input.length - 1 - i];
            }
            String rule = rules.get(new String(ending));

            if(rule!=null) {
                char[] stem = applyRule(input, rule.toCharArray());

                if(!Arrays.equals(input, stem)) { // If the rule was applied, and the stem was accepted, the 'stem' will be different from the input.
                    return stem;
                }
            }

            // No rule was found, reduce the ending size and try again.
            endLen--;
        }

        // If no matching rule was found, no stemming can be done. Return the original input.
        return input;
    }

    // Apply a rule to a word.
    private static char[] applyRule(char[] input, char[] rule) {
        char[] stem;

        // The first character in the rule tells us how many characters to remove from the input word.
        int num_del = rule[0] - '0';

        // The characters in between the first and last are to be appended to the end of the stem. The resulting stem will be the input - num_del + the characters to be appended.
        stem = new char[input.length - num_del + rule.length - 2];
        System.arraycopy(input, 0, stem, 0, input.length - num_del);
        if(rule.length > 2){
            System.arraycopy(rule, 1, stem, input.length - num_del, rule.length - 2);
        }

        // Before we go any further, make sure the stem satisfies the minimum stem rules. If it doesn't, return the original input.
        if(!verifyStem(stem)){
            return input;
        }

        // If the final character of the rule is '>', it means we continue to try to stem the word further. If it is '.'.
        // Since '>' and '.' are the only possible values, we check for '>', and otherwise it must be '.'.
        if(rule[rule.length - 1] == '>') {
            return findRule(stem);
        }

        return stem;
    }

    // A stem must satisfy these conditions to be accepted.
    private static boolean verifyStem(char[] stem) {
        char[] vowels = new char[] { 'a', 'e', 'i', 'o', 'u' };
        int len = stem.length;
        // If the stem begins with a vowel, the stem must contain at least 2 letters.
        for(char vowel : vowels) {
            if(stem[0] == vowel) {
                if(len >= 2) {
                    return true;
                }
                return false;
            }
        }

        // If the stem begins with a consonant, the stem must be at least 3 letters long, and one of the letters must be a vowel (or 'y').
        if(len < 3) {
            return false;
        }

        // We know the first character must be a consonant, so skip it.
        // Searching the remainder of the stem for a vowel (or 'y').
        for(char letter : stem) {
            for(char vowel : vowels) {
                if(letter == vowel) {
                    return true;
                }
            }
            if(letter == 'y') {
                return true;
            }
        }

        return false;
    }
}
