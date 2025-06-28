package ie.atu.forge.Stemmers;

/*
https://aclanthology.org/www.mt-archive.info/MT-1968-Lovins.pdf
 */


import java.util.HashMap;
import java.util.Map;


// Implementation of Lovin's stemmer based on the original paper.
public class Lovin {
    // In the spirit of Lovin's stemmer (i.e. sacrificing space for performance), I'm using a seperate map for each ending length.
    private static final Map<String, String> endings_11 = new HashMap<>();
    private static final Map<String, String> endings_10 = new HashMap<>();
    private static final Map<String, String> endings_9 = new HashMap<>();
    private static final Map<String, String> endings_8 = new HashMap<>();
    private static final Map<String, String> endings_7 = new HashMap<>();
    private static final Map<String, String> endings_6 = new HashMap<>();
    private static final Map<String, String> endings_5 = new HashMap<>();
    private static final Map<String, String> endings_4 = new HashMap<>();
    private static final Map<String, String> endings_3 = new HashMap<>();
    private static final Map<String, String> endings_2 = new HashMap<>();
    private static final Map<String, String> endings_1 = new HashMap<>();

    // Ending which can be removed.
    static{
        // 11
        endings_11.put("alistically", "b");
        endings_11.put("arizability", "a");
        endings_11.put("arisability", "a");
        endings_11.put("izationally", "b");
        endings_11.put("isationally", "b");
        // 10
        endings_10.put("antialness", "a");
        endings_10.put("arisations", "a");
        endings_10.put("arizations", "a");
        endings_10.put("entialness", "a");
        // 9
        endings_9.put("allically", "c");
        endings_9.put("antaneous", "a");
        endings_9.put("antiality", "a");
        endings_9.put("arisation", "a");
        endings_9.put("atization", "a");
        endings_9.put("ationally", "b");
        endings_9.put("ativeness", "a");
        endings_9.put("eableness", "e");
        endings_9.put("entations", "a");
        endings_9.put("entiality", "a");
        endings_9.put("entialize", "a");
        endings_9.put("entialise", "a");
        endings_9.put("entiation", "a");
        endings_9.put("ionalness", "a");
        endings_9.put("istically", "a");
        endings_9.put("itousness", "a");
        endings_9.put("izability", "a");
        endings_9.put("isability", "a");
        endings_9.put("izational", "a");
        endings_9.put("isational", "a");
        // 8
        endings_8.put("ableness", "a");
        endings_8.put("arizable", "a");
        endings_8.put("arisable", "a");
        endings_8.put("entation", "a");
        endings_8.put("entially", "a");
        endings_8.put("eousness", "a");
        endings_8.put("ibleness", "a");
        endings_8.put("icalness", "a");
        endings_8.put("ionalism", "a");
        endings_8.put("ionality", "a");
        endings_8.put("ionalize", "a");
        endings_8.put("ionalise", "a");
        endings_8.put("iousness", "a");
        endings_8.put("izations", "a");
        endings_8.put("isations", "a");
        endings_8.put("lessness", "a");
        // 7
        endings_7.put("ability", "a");
        endings_7.put("aically", "a");
        endings_7.put("alistic", "b");
        endings_7.put("alities", "a");
        endings_7.put("ariness", "e");
        endings_7.put("aristic", "a");
        endings_7.put("arizing", "a");
        endings_7.put("arising", "a");
        endings_7.put("ateness", "a");
        endings_7.put("atingly", "a");
        endings_7.put("ational", "b");
        endings_7.put("atively", "a");
        endings_7.put("ativism", "a");
        endings_7.put("elihood", "e");
        endings_7.put("encible", "a");
        endings_7.put("entally", "a");
        endings_7.put("entials", "a");
        endings_7.put("entiate", "a");
        endings_7.put("entness", "a");
        endings_7.put("fullness", "a");
        endings_7.put("ibility", "a");
        endings_7.put("icalism", "a");
        endings_7.put("icalist", "a");
        endings_7.put("icality", "a");
        endings_7.put("icalize", "a");
        endings_7.put("icalise", "a");
        endings_7.put("ication", "g");
        endings_7.put("icianry", "a");
        endings_7.put("ination", "a");
        endings_7.put("ingness", "a");
        endings_7.put("ionally", "a");
        endings_7.put("isation", "a");
        endings_7.put("ization", "f");
        endings_7.put("ishness", "a");
        endings_7.put("istical", "a");
        endings_7.put("iteness", "a");
        endings_7.put("iveness", "a");
        endings_7.put("ivistic", "a");
        endings_7.put("ivities", "a");
        endings_7.put("izement", "a");
        endings_7.put("isement", "a");
        endings_7.put("oidally", "a");
        endings_7.put("ousness", "a");
        // 6
        endings_6.put("aceous", "a");
        endings_6.put("acious", "b");
        endings_6.put("action", "g");
        endings_6.put("alness", "a");
        endings_6.put("ancial", "a");
        endings_6.put("ancies", "a");
        endings_6.put("ancing", "b");
        endings_6.put("ariser", "a");
        endings_6.put("arizer", "a");
        endings_6.put("arised", "a");
        endings_6.put("arized", "a");
        endings_6.put("atable", "a");
        endings_6.put("ations", "b");
        endings_6.put("atives", "a");
        endings_6.put("eature", "z");
        endings_6.put("efully", "a");
        endings_6.put("encies", "a");
        endings_6.put("encing", "a");
        endings_6.put("ential", "a");
        endings_6.put("enting", "c");
        endings_6.put("entist", "a");
        endings_6.put("eously", "a");
        endings_6.put("ialist", "a");
        endings_6.put("iality", "a");
        endings_6.put("ialize", "a");
        endings_6.put("ialise", "a");
        endings_6.put("ically", "a");
        endings_6.put("icance", "a");
        endings_6.put("icians", "a");
        endings_6.put("icists", "a");
        endings_6.put("ifully", "a");
        endings_6.put("ionals", "a");
        endings_6.put("ionate", "d");
        endings_6.put("ioning", "a");
        endings_6.put("ionist", "a");
        endings_6.put("iously", "a");
        endings_6.put("istics", "a");
        endings_6.put("izable", "e");
        endings_6.put("isable", "e");
        endings_6.put("lessly", "a");
        endings_6.put("nesses", "a");
        endings_6.put("oidism", "a");
        // 5
        endings_5.put("acies", "a");
        endings_5.put("acity", "a");
        endings_5.put("aging", "b");
        endings_5.put("aical", "a");
        endings_5.put("alist", "a");
        endings_5.put("alism", "b");
        endings_5.put("ality", "a");
        endings_5.put("alize", "a");
        endings_5.put("allic", "bb");
        endings_5.put("anced", "b");
        endings_5.put("ances", "b");
        endings_5.put("entic", "c");
        endings_5.put("arial", "a");
        endings_5.put("aries", "a");
        endings_5.put("arily", "a");
        endings_5.put("arity", "b");
        endings_5.put("arize", "a");
        endings_5.put("arise", "a");
        endings_5.put("aroid", "a");
        endings_5.put("ately", "a");
        endings_5.put("ating", "i");
        endings_5.put("ation", "b");
        endings_5.put("ative", "a");
        endings_5.put("ators", "a");
        endings_5.put("atory", "a");
        endings_5.put("ature", "e");
        endings_5.put("early", "y");
        endings_5.put("ehood", "a");
        endings_5.put("eless", "a");
        endings_5.put("elily", "a");
        endings_5.put("ement", "a");
        endings_5.put("enced", "a");
        endings_5.put("ences", "a");
        endings_5.put("eness", "e");
        endings_5.put("ening", "e");
        endings_5.put("ental", "a");
        endings_5.put("ented", "c");
        endings_5.put("ently", "a");
        endings_5.put("fully", "a");
        endings_5.put("ially", "a");
        endings_5.put("icant", "a");
        endings_5.put("ician", "a");
        endings_5.put("icide", "a");
        endings_5.put("icism", "a");
        endings_5.put("icist", "a");
        endings_5.put("icity", "a");
        endings_5.put("idine", "i");
        endings_5.put("iedly", "a");
        endings_5.put("ihood", "a");
        endings_5.put("inate", "a");
        endings_5.put("iness", "a");
        endings_5.put("ingly", "b");
        endings_5.put("inism", "j");
        endings_5.put("inity", "cc");
        endings_5.put("ional", "a");
        endings_5.put("ioned", "a");
        endings_5.put("ished", "a");
        endings_5.put("istic", "a");
        endings_5.put("ities", "a");
        endings_5.put("itous", "a");
        endings_5.put("ively", "a");
        endings_5.put("ivity", "a");
        endings_5.put("izers", "f");
        endings_5.put("isers", "f");
        endings_5.put("izing", "f");
        endings_5.put("ising", "f");
        endings_5.put("oidal", "a");
        endings_5.put("oides", "a");
        endings_5.put("otide", "a");
        endings_5.put("ously", "a");
        // 4
        endings_4.put("able", "a");
        endings_4.put("ably", "a");
        endings_4.put("ages", "b");
        endings_4.put("ally", "b");
        endings_4.put("ance", "b");
        endings_4.put("ancy", "b");
        endings_4.put("ants", "b");
        endings_4.put("aric", "a");
        endings_4.put("arly", "k");
        endings_4.put("ated", "i");
        endings_4.put("ates", "a");
        endings_4.put("atic", "b");
        endings_4.put("ator", "a");
        endings_4.put("ealy", "y");
        endings_4.put("edly", "e");
        endings_4.put("eful", "a");
        endings_4.put("eity", "a");
        endings_4.put("ence", "a");
        endings_4.put("ency", "a");
        endings_4.put("ened", "e");
        endings_4.put("enly", "e");
        endings_4.put("eous", "a");
        endings_4.put("hood", "a");
        endings_4.put("ials", "a");
        endings_4.put("ians", "a");
        endings_4.put("ible", "a");
        endings_4.put("ibly", "a");
        endings_4.put("ical", "a");
        endings_4.put("ides", "l");
        endings_4.put("iers", "a");
        endings_4.put("iful", "a");
        endings_4.put("ines", "m");
        endings_4.put("ings", "n");
        endings_4.put("ions", "b");
        endings_4.put("ious", "a");
        endings_4.put("isms", "b");
        endings_4.put("ists", "a");
        endings_4.put("itic", "h");
        endings_4.put("ized", "f");
        endings_4.put("ised", "f");
        endings_4.put("izer", "f");
        endings_4.put("iser", "f");
        endings_4.put("less", "a");
        endings_4.put("lily", "a");
        endings_4.put("ness", "a");
        endings_4.put("ogen", "a");
        endings_4.put("ward", "a");
        endings_4.put("wise", "a");
        endings_4.put("ying", "b");
        endings_4.put("yish", "a");
        // 3
        endings_3.put("acy", "a");
        endings_3.put("age", "b");
        endings_3.put("aic", "a");
        endings_3.put("als", "bb");
        endings_3.put("ant", "b");
        endings_3.put("ars", "o");
        endings_3.put("ary", "f");
        endings_3.put("ata", "a");
        endings_3.put("ate", "a");
        endings_3.put("eal", "y");
        endings_3.put("ear", "y");
        endings_3.put("ely", "e");
        endings_3.put("ene", "e");
        endings_3.put("ent", "c");
        endings_3.put("ery", "e");
        endings_3.put("ese", "a");
        endings_3.put("ful", "a");
        endings_3.put("ial", "a");
        endings_3.put("ian", "a");
        endings_3.put("ics", "a");
        endings_3.put("ide", "l");
        endings_3.put("ied", "a");
        endings_3.put("ier", "a");
        endings_3.put("ies", "p");
        endings_3.put("ily", "a");
        endings_3.put("ine", "m");
        endings_3.put("ing", "n");
        endings_3.put("ion", "q");
        endings_3.put("ish", "c");
        endings_3.put("ism", "b");
        endings_3.put("ist", "a");
        endings_3.put("ite", "aa");
        endings_3.put("ity", "a");
        endings_3.put("ium", "a");
        endings_3.put("ive", "a");
        endings_3.put("ize", "f");
        endings_3.put("ise", "f");
        endings_3.put("oid", "a");
        endings_3.put("one", "r");
        endings_3.put("ous", "a");
        // 2
        endings_2.put("ae", "a");
        endings_2.put("al", "bb");
        endings_2.put("ar", "x");
        endings_2.put("as", "b");
        endings_2.put("ed", "e");
        endings_2.put("en", "f");
        endings_2.put("es", "e");
        endings_2.put("ia", "a");
        endings_2.put("ic", "a");
        endings_2.put("is", "a");
        endings_2.put("ly", "b");
        endings_2.put("on", "s");
        endings_2.put("or", "t");
        endings_2.put("um", "u");
        endings_2.put("us", "v");
        endings_2.put("yl", "r");
        endings_2.put("s'", "a");
        endings_2.put("'s", "a");
        // 1
        endings_1.put("a", "a");
        endings_1.put("e", "a");
        endings_1.put("i", "a");
        endings_1.put("o", "a");
        endings_1.put("s", "w");
        endings_1.put("y", "b");
    }


    // Conditions stem must pass to remove ending:
    // No restrictions on stem
    private boolean condition_a(char[] input) {
        return true;
    }

    // Minimum stem length == 3
    private boolean condition_b(char[] input) {
        return input.length >= 3;
    }

    // Minimum stem length == 4
    private boolean condition_c(char[] input) {
        return input.length >= 4;
    }

    // Minimum stem length == 5
    private boolean condition_d(char[] input) {
        return input.length >= 5;
    }

    // Do not remove ending after 'e'
    private boolean condition_e(char[] input) {
        return !(input[input.length - 1] == 'e');
    }

    // Minimum stem length == 3 and do not remove ending after 'e'
    private boolean condition_f(char[] input) {
        return input.length >= 3 && !(input[input.length - 1] == 'e');
    }

    // Minimum stem length == 3 and remove ending only after 'f'
    private boolean condition_g(char[] input) {
        return input.length >= 3 && (input[input.length - 1] == 'f');
    }

    // Its difficult to tell what the first letter is from the original paper. Online sources say 't' and 'll'.
    // Remove stem ending only after 't' or 'll'
    private boolean condition_h(char[] input) {
        return (input[input.length - 1] == 't') || (input[input.length - 1] == 'l' && (input[input.length - 2] == 'l'));
    }

    // Do not remove ending after 'o' or 'e'.
    private boolean condition_i(char[] input) {
        return !(input[input.length - 1] == 'o' || input[input.length - 1] == 'e');
    }

    // Do not remove ending after 'a' or 'e'.
    private boolean condition_j(char[] input) {
        return !(input[input.length - 1] == 'a' || input[input.length - 1] == 'e');
    }


    // Minimum stem length == 3 and remove ending only after 'l', 'i', or 'uαe' (where 'α' is any letter).
    private boolean condition_k(char[] input) {
        return input.length >= 3 && ((input[input.length - 3] == 'u' && input[input.length - 1] == 'e') || input[input.length - 1] == 'l' || input[input.length - 1] == 'i');
    }

    // Do not remove ending after 'u', 'x', or 's', unless 's' follows 'o'.
    private boolean condition_l(char[] input) {
        if(input[input.length - 1] == 'u' || input[input.length - 1] == 'x') {
            return false;
        } else if(input[input.length - 1] == 's' && !(input[input.length - 2] == 'o')) {
            return false;
        }

        return true;
    }

    // Do not remove ending after 'a', 'c', 'e', or 'm'.
    private boolean condition_m(char[] input) {
        return !(input[input.length - 1] == 'a' || input[input.length - 1] == 'c' || input[input.length - 1] == 'e' || input[input.length - 1] == 'm');
    }

    // Minimum stem length == 4 after 'sαα', elsewhere minimum stem length == 3. (where 'α' is any letter)
    private boolean condition_n(char[] input) {
        if(input[input.length - 3] == 's') {
            return input.length >= 4;
        } else {
            return input.length >= 3;
        }
    }

    // Remove ending only after 'l' or 'i'.
    private boolean condition_o(char[] input) {
        return input[input.length - 1] == 'l' || input[input.length - 1] == 'i';
    }

    // Do not remove ending after 'c'.
    private boolean condition_p(char[] input) {
        return !(input[input.length - 1] == 'c');
    }

    // Minimum stem length == 3 and do not remove ending after 'l' or 'n'.
    private boolean condition_q(char[] input) {
        return input.length >= 3 && !(input[input.length - 1] == 'l' || input[input.length - 1] == 'n');
    }

    // Remove ending only after 'n' or 'r'.
    private boolean condition_r(char[] input) {
        return input[input.length - 1] == 'n' || input[input.length - 1] == 'r';
    }

    // Remove ending only after 'dr' or 't', unless 't' follows 't'.
    private boolean condition_s(char[] input) {
        return (input[input.length - 2] == 'd' && input[input.length - 1] == 'r') || (input[input.length - 1] == 't' && !(input[input.length - 2] == 't'));
    }

    // Remove ending only after 's' or 't', unless 't' follows 'o'.
    private boolean condition_t(char[] input) {
        return input[input.length - 1] == 's' || (input[input.length - 1] == 't' && !(input[input.length - 2] == 'o'));
    }

    // Remove ending only after 'l', 'm', 'n', or 'r'.
    private boolean condition_u(char[] input) {
        return input[input.length - 1] == 'l' || input[input.length - 1] == 'm' || input[input.length - 1] == 'n' || input[input.length - 1] == 'r';
    }

    // Remove ending only after 'c'.
    private boolean condition_v(char[] input) {
        return input[input.length - 1] == 'c';
    }

    // Do not remove ending after 's' or 'u'.
    private boolean condition_w(char[] input) {
        return !(input[input.length - 1] == 's' || input[input.length - 1] == 'u');
    }

    // Remove ending only after 'l', 'i', or 'uαe' (where 'α' is any letter).
    private boolean condition_x(char[] input) {
        return input[input.length - 1] == 'l' || input[input.length - 1] == 'i' || (input[input.length - 3] == 'u' && input[input.length - 1] == 'e');
    }

    // Remove ending only after 'in'.
    private boolean condition_y(char[] input) {
        return input[input.length - 2] == 'i' && input[input.length - 1] == 'n';
    }

    // Do not remove ending after 'f'.
    private boolean condition_z(char[] input) {
        return !(input[input.length - 1] == 'f');
    }

    // For readability, I wrote this one a little differently.
    // Remove ending only after 'd', 'f', 'ph', 'th', 'l', 'er', 'or', 'es', or 't'.
    private boolean condition_aa(char[] input) {
        if(input[input.length - 1] == 'd') return true;
        else if (input[input.length - 1] == 'f') return true;
        else if (input[input.length - 2] == 'p' && input[input.length - 1] == 'h') return true;
        else if (input[input.length - 2] == 't' && input[input.length - 1] == 'h') return true;
        else if (input[input.length - 1] == 'l') return true;
        else if (input[input.length - 2] == 'e' && input[input.length - 1] == 'r') return true;
        else if (input[input.length - 2] == 'o' && input[input.length - 1] == 'r') return true;
        else if (input[input.length - 2] == 'e' && input[input.length - 1] == 's') return true;
        else if (input[input.length - 1] == 't') return true;
        return false;
    }

    // Again, written differently for readability.
    // Minimum stem length == 3 and do not remove ending after 'met', or 'ryst'.
    private boolean condition_bb(char[] input) {
        if(input.length >= 3) {
            if(!(input[input.length - 1] == 't')) {
                return true;
            }

            if(input[input.length - 3] == 'm' && input[input.length - 2] == 'e') return false;
            else if (input[input.length - 4] == 'r' && input[input.length - 3] == 'y' && input[input.length - 2] == 's') return false;
        }

        return false;
    }

    // Remove ending only after 'l'.
    private boolean condition_cc(char[] input) {
        return input[input.length - 1] == 'l';
    }
}
