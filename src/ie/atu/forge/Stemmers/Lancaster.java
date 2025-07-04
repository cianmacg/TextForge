package ie.atu.forge.Stemmers;

import java.util.HashMap;
import java.util.Map;

/*
An implementation of the Lancaster (Paice-Husk) stemmer using the sample rules from the original paper.
https://dl.acm.org/doi/10.1145/101306.101310
 */
public class Lancaster {
    private static final Map<String, String> intact_rules = new HashMap<>();
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
        intact_rules.put("ai", "2.");
        intact_rules.put("a", "1.");
        rules.put("bb", "1.");
        rules.put("city", "3s");
        rules.put("ci", "2>");
        rules.put("cn", "1t>");
        rules.put("dd", "1.");
        rules.put("dei", "3y");
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
        intact_rules.put("ht", "2.");
        rules.put("hsiug", "5ct.");
        rules.put("hsi", "3>");
        intact_rules.put("i", "1.");
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
        intact_rules.put("mu", "2.");
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
        intact_rules.put("su", "2.");
        intact_rules.put("s", "1>");
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
        rules.put("ypo", "1.");
        rules.put("yrtsi", "5.");
        rules.put("yra", "3>");
        rules.put("yro", "3>");
        rules.put("yfi", "3.");
        rules.put("ycn", "2t>");
        rules.put("yca", "3>");
        rules.put("zi", "2>");
        rules.put("zy", "1s.");
    }

    public static String stem(String input) {
        if(input.length()<=0) {
            return input;
        }
        char[] stem = input.replaceAll("[^a-zA-Z ]", "").toLowerCase().toCharArray();
        char[] vowels = new char[] { 'a', 'e', 'i', 'o', 'u' };

        stem = apply_intact_rule(stem);

        return new String(stem);
    }

    // The largest intact ending is only 2 characters.
    private static char[] apply_intact_rule(char[] input) {
        if(input.length<=2) {
            return input;
        }

        //String end = new String(input[input.length - 1]);
        String val = intact_rules.get(input[0]);

        return input;
    }

    private static char[] apply_rule(char[] input) {
        return input;
    }

}
