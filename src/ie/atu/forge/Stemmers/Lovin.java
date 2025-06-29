package ie.atu.forge.Stemmers;

/*
https://aclanthology.org/www.mt-archive.info/MT-1968-Lovins.pdf
 */


import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


// Implementation of Lovin's stemmer based on the original paper.
public class Lovin {
    /*
     In the spirit of Lovin's stemmer (i.e. sacrificing space for performance), I'm using a seperate map for each ending length.
     Index 0 will contain a map for endings of length 1. Ending length will increase and index increases, until ending length 11 (index 10).
     */
    private static final Map<String, String>[] endings = new HashMap[11];

    // Mapping of values to coresponding condition function to check.
    private static final Map<String, Condition> conditions = Map.ofEntries(
            Map.entry("a", Lovin::condition_a),
            Map.entry("b", Lovin::condition_b),
            Map.entry("c", Lovin::condition_c),
            Map.entry("d", Lovin::condition_d),
            Map.entry("e", Lovin::condition_e),
            Map.entry("f", Lovin::condition_f),
            Map.entry("g", Lovin::condition_g),
            Map.entry("h", Lovin::condition_h),
            Map.entry("i", Lovin::condition_i),
            Map.entry("j", Lovin::condition_j),
            Map.entry("k", Lovin::condition_k),
            Map.entry("l", Lovin::condition_l),
            Map.entry("m", Lovin::condition_m),
            Map.entry("n", Lovin::condition_n),
            Map.entry("o", Lovin::condition_o),
            Map.entry("p", Lovin::condition_p),
            Map.entry("q", Lovin::condition_q),
            Map.entry("r", Lovin::condition_r),
            Map.entry("s", Lovin::condition_s),
            Map.entry("t", Lovin::condition_t),
            Map.entry("u", Lovin::condition_u),
            Map.entry("v", Lovin::condition_v),
            Map.entry("w", Lovin::condition_w),
            Map.entry("x", Lovin::condition_x),
            Map.entry("y", Lovin::condition_y),
            Map.entry("z", Lovin::condition_z),
            Map.entry("aa", Lovin::condition_aa),
            Map.entry("bb", Lovin::condition_bb)
    );

    private static final int MAX_ENDING_LENGTH = 11;
    private static final int MIN_STEM_LENGTH = 2;
    // Endings that can be removed. Key mapping pair where key is the ending, and the value is the condition it must fulfil.
    static{
        // 11
        endings[10] = new HashMap<>();
        endings[10].put("alistically", "b");
        endings[10].put("arizability", "a");
        endings[10].put("arisability", "a");
        endings[10].put("izationally", "b");
        endings[10].put("isationally", "b");
        // 10
        endings[9] = new HashMap<>();
        endings[9].put("antialness", "a");
        endings[9].put("arisations", "a");
        endings[9].put("arizations", "a");
        endings[9].put("entialness", "a");
        // 9
        endings[8] = new HashMap<>();
        endings[8].put("allically", "c");
        endings[8].put("antaneous", "a");
        endings[8].put("antiality", "a");
        endings[8].put("arisation", "a");
        endings[8].put("atization", "a");
        endings[8].put("ationally", "b");
        endings[8].put("ativeness", "a");
        endings[8].put("eableness", "e");
        endings[8].put("entations", "a");
        endings[8].put("entiality", "a");
        endings[8].put("entialize", "a");
        endings[8].put("entialise", "a");
        endings[8].put("entiation", "a");
        endings[8].put("ionalness", "a");
        endings[8].put("istically", "a");
        endings[8].put("itousness", "a");
        endings[8].put("izability", "a");
        endings[8].put("isability", "a");
        endings[8].put("izational", "a");
        endings[8].put("isational", "a");
        // 8
        endings[7] = new HashMap<>();
        endings[7].put("ableness", "a");
        endings[7].put("arizable", "a");
        endings[7].put("arisable", "a");
        endings[7].put("entation", "a");
        endings[7].put("entially", "a");
        endings[7].put("eousness", "a");
        endings[7].put("ibleness", "a");
        endings[7].put("icalness", "a");
        endings[7].put("ionalism", "a");
        endings[7].put("ionality", "a");
        endings[7].put("ionalize", "a");
        endings[7].put("ionalise", "a");
        endings[7].put("iousness", "a");
        endings[7].put("izations", "a");
        endings[7].put("isations", "a");
        endings[7].put("lessness", "a");
        // 7
        endings[6] = new HashMap<>();
        endings[6].put("ability", "a");
        endings[6].put("aically", "a");
        endings[6].put("alistic", "b");
        endings[6].put("alities", "a");
        endings[6].put("ariness", "e");
        endings[6].put("aristic", "a");
        endings[6].put("arizing", "a");
        endings[6].put("arising", "a");
        endings[6].put("ateness", "a");
        endings[6].put("atingly", "a");
        endings[6].put("ational", "b");
        endings[6].put("atively", "a");
        endings[6].put("ativism", "a");
        endings[6].put("elihood", "e");
        endings[6].put("encible", "a");
        endings[6].put("entally", "a");
        endings[6].put("entials", "a");
        endings[6].put("entiate", "a");
        endings[6].put("entness", "a");
        endings[6].put("fullness", "a");
        endings[6].put("ibility", "a");
        endings[6].put("icalism", "a");
        endings[6].put("icalist", "a");
        endings[6].put("icality", "a");
        endings[6].put("icalize", "a");
        endings[6].put("icalise", "a");
        endings[6].put("ication", "g");
        endings[6].put("icianry", "a");
        endings[6].put("ination", "a");
        endings[6].put("ingness", "a");
        endings[6].put("ionally", "a");
        endings[6].put("isation", "a");
        endings[6].put("ization", "f");
        endings[6].put("ishness", "a");
        endings[6].put("istical", "a");
        endings[6].put("iteness", "a");
        endings[6].put("iveness", "a");
        endings[6].put("ivistic", "a");
        endings[6].put("ivities", "a");
        endings[6].put("izement", "a");
        endings[6].put("isement", "a");
        endings[6].put("oidally", "a");
        endings[6].put("ousness", "a");
        // 6
        endings[5] = new HashMap<>();
        endings[5].put("aceous", "a");
        endings[5].put("acious", "b");
        endings[5].put("action", "g");
        endings[5].put("alness", "a");
        endings[5].put("ancial", "a");
        endings[5].put("ancies", "a");
        endings[5].put("ancing", "b");
        endings[5].put("ariser", "a");
        endings[5].put("arizer", "a");
        endings[5].put("arised", "a");
        endings[5].put("arized", "a");
        endings[5].put("atable", "a");
        endings[5].put("ations", "b");
        endings[5].put("atives", "a");
        endings[5].put("eature", "z");
        endings[5].put("efully", "a");
        endings[5].put("encies", "a");
        endings[5].put("encing", "a");
        endings[5].put("ential", "a");
        endings[5].put("enting", "c");
        endings[5].put("entist", "a");
        endings[5].put("eously", "a");
        endings[5].put("ialist", "a");
        endings[5].put("iality", "a");
        endings[5].put("ialize", "a");
        endings[5].put("ialise", "a");
        endings[5].put("ically", "a");
        endings[5].put("icance", "a");
        endings[5].put("icians", "a");
        endings[5].put("icists", "a");
        endings[5].put("ifully", "a");
        endings[5].put("ionals", "a");
        endings[5].put("ionate", "d");
        endings[5].put("ioning", "a");
        endings[5].put("ionist", "a");
        endings[5].put("iously", "a");
        endings[5].put("istics", "a");
        endings[5].put("izable", "e");
        endings[5].put("isable", "e");
        endings[5].put("lessly", "a");
        endings[5].put("nesses", "a");
        endings[5].put("oidism", "a");
        // 5
        endings[4] = new HashMap<>();
        endings[4].put("acies", "a");
        endings[4].put("acity", "a");
        endings[4].put("aging", "b");
        endings[4].put("aical", "a");
        endings[4].put("alist", "a");
        endings[4].put("alism", "b");
        endings[4].put("ality", "a");
        endings[4].put("alize", "a");
        endings[4].put("allic", "bb");
        endings[4].put("anced", "b");
        endings[4].put("ances", "b");
        endings[4].put("entic", "c");
        endings[4].put("arial", "a");
        endings[4].put("aries", "a");
        endings[4].put("arily", "a");
        endings[4].put("arity", "b");
        endings[4].put("arize", "a");
        endings[4].put("arise", "a");
        endings[4].put("aroid", "a");
        endings[4].put("ately", "a");
        endings[4].put("ating", "i");
        endings[4].put("ation", "b");
        endings[4].put("ative", "a");
        endings[4].put("ators", "a");
        endings[4].put("atory", "a");
        endings[4].put("ature", "e");
        endings[4].put("early", "y");
        endings[4].put("ehood", "a");
        endings[4].put("eless", "a");
        endings[4].put("elily", "a");
        endings[4].put("ement", "a");
        endings[4].put("enced", "a");
        endings[4].put("ences", "a");
        endings[4].put("eness", "e");
        endings[4].put("ening", "e");
        endings[4].put("ental", "a");
        endings[4].put("ented", "c");
        endings[4].put("ently", "a");
        endings[4].put("fully", "a");
        endings[4].put("ially", "a");
        endings[4].put("icant", "a");
        endings[4].put("ician", "a");
        endings[4].put("icide", "a");
        endings[4].put("icism", "a");
        endings[4].put("icist", "a");
        endings[4].put("icity", "a");
        endings[4].put("idine", "i");
        endings[4].put("iedly", "a");
        endings[4].put("ihood", "a");
        endings[4].put("inate", "a");
        endings[4].put("iness", "a");
        endings[4].put("ingly", "b");
        endings[4].put("inism", "j");
        endings[4].put("inity", "cc");
        endings[4].put("ional", "a");
        endings[4].put("ioned", "a");
        endings[4].put("ished", "a");
        endings[4].put("istic", "a");
        endings[4].put("ities", "a");
        endings[4].put("itous", "a");
        endings[4].put("ively", "a");
        endings[4].put("ivity", "a");
        endings[4].put("izers", "f");
        endings[4].put("isers", "f");
        endings[4].put("izing", "f");
        endings[4].put("ising", "f");
        endings[4].put("oidal", "a");
        endings[4].put("oides", "a");
        endings[4].put("otide", "a");
        endings[4].put("ously", "a");
        // 4
        endings[3] = new HashMap<>();
        endings[3].put("able", "a");
        endings[3].put("ably", "a");
        endings[3].put("ages", "b");
        endings[3].put("ally", "b");
        endings[3].put("ance", "b");
        endings[3].put("ancy", "b");
        endings[3].put("ants", "b");
        endings[3].put("aric", "a");
        endings[3].put("arly", "k");
        endings[3].put("ated", "i");
        endings[3].put("ates", "a");
        endings[3].put("atic", "b");
        endings[3].put("ator", "a");
        endings[3].put("ealy", "y");
        endings[3].put("edly", "e");
        endings[3].put("eful", "a");
        endings[3].put("eity", "a");
        endings[3].put("ence", "a");
        endings[3].put("ency", "a");
        endings[3].put("ened", "e");
        endings[3].put("enly", "e");
        endings[3].put("eous", "a");
        endings[3].put("hood", "a");
        endings[3].put("ials", "a");
        endings[3].put("ians", "a");
        endings[3].put("ible", "a");
        endings[3].put("ibly", "a");
        endings[3].put("ical", "a");
        endings[3].put("ides", "l");
        endings[3].put("iers", "a");
        endings[3].put("iful", "a");
        endings[3].put("ines", "m");
        endings[3].put("ings", "n");
        endings[3].put("ions", "b");
        endings[3].put("ious", "a");
        endings[3].put("isms", "b");
        endings[3].put("ists", "a");
        endings[3].put("itic", "h");
        endings[3].put("ized", "f");
        endings[3].put("ised", "f");
        endings[3].put("izer", "f");
        endings[3].put("iser", "f");
        endings[3].put("less", "a");
        endings[3].put("lily", "a");
        endings[3].put("ness", "a");
        endings[3].put("ogen", "a");
        endings[3].put("ward", "a");
        endings[3].put("wise", "a");
        endings[3].put("ying", "b");
        endings[3].put("yish", "a");
        // 3
        endings[2] = new HashMap<>();
        endings[2].put("acy", "a");
        endings[2].put("age", "b");
        endings[2].put("aic", "a");
        endings[2].put("als", "bb");
        endings[2].put("ant", "b");
        endings[2].put("ars", "o");
        endings[2].put("ary", "f");
        endings[2].put("ata", "a");
        endings[2].put("ate", "a");
        endings[2].put("eal", "y");
        endings[2].put("ear", "y");
        endings[2].put("ely", "e");
        endings[2].put("ene", "e");
        endings[2].put("ent", "c");
        endings[2].put("ery", "e");
        endings[2].put("ese", "a");
        endings[2].put("ful", "a");
        endings[2].put("ial", "a");
        endings[2].put("ian", "a");
        endings[2].put("ics", "a");
        endings[2].put("ide", "l");
        endings[2].put("ied", "a");
        endings[2].put("ier", "a");
        endings[2].put("ies", "p");
        endings[2].put("ily", "a");
        endings[2].put("ine", "m");
        endings[2].put("ing", "n");
        endings[2].put("ion", "q");
        endings[2].put("ish", "c");
        endings[2].put("ism", "b");
        endings[2].put("ist", "a");
        endings[2].put("ite", "aa");
        endings[2].put("ity", "a");
        endings[2].put("ium", "a");
        endings[2].put("ive", "a");
        endings[2].put("ize", "f");
        endings[2].put("ise", "f");
        endings[2].put("oid", "a");
        endings[2].put("one", "r");
        endings[2].put("ous", "a");
        // 2
        endings[1] = new HashMap<>();
        endings[1].put("ae", "a");
        endings[1].put("al", "bb");
        endings[1].put("ar", "x");
        endings[1].put("as", "b");
        endings[1].put("ed", "e");
        endings[1].put("en", "f");
        endings[1].put("es", "e");
        endings[1].put("ia", "a");
        endings[1].put("ic", "a");
        endings[1].put("is", "a");
        endings[1].put("ly", "b");
        endings[1].put("on", "s");
        endings[1].put("or", "t");
        endings[1].put("um", "u");
        endings[1].put("us", "v");
        endings[1].put("yl", "r");
        endings[1].put("s'", "a");
        endings[1].put("'s", "a");
        // 1
        endings[0] = new HashMap<>();
        endings[0].put("a", "a");
        endings[0].put("e", "a");
        endings[0].put("i", "a");
        endings[0].put("o", "a");
        endings[0].put("s", "w");
        endings[0].put("y", "b");
    }

    @FunctionalInterface
    interface Condition {
        boolean test(char[] input);
    }

    public static String stem(String input) {
        // The minimum stem length we can have is 2, so no point in checking any ending with a greater length than input.length - 2.
        // This will help us determine which map begin looking for endings in. The index for an ending length is the ending length - 1.
        int ending_map_index = Math.min(input.length() - MIN_STEM_LENGTH, MAX_ENDING_LENGTH) - 1;
        char[] stem = input.replaceAll("[^a-zA-Z ]", "").toLowerCase().toCharArray();

        while(ending_map_index >= 0) {
            String temp = input.substring(input.length() - (ending_map_index + 1));
            String rule = endings[ending_map_index].get(temp);

            if(rule != null) {
                char[] stem_candidate = input.substring(0,input.length() - ending_map_index).toCharArray();

                if(conditions.get(rule).test(stem_candidate)) {
                    stem = stem_candidate;
                    break;
                }

                /*switch(rule) {
                    case "a":
                        if(condition_a(stem_candidate)) {
                            stem = stem_candidate;
                        }
                        break;

                    case "b":
                        if(condition_b(stem_candidate)) {
                            stem = stem_candidate;
                        }
                        break;

                    case "c":
                        if(condition_c(stem_candidate)) {
                            stem = stem_candidate;
                        }
                        break;

                    case "d":
                        if(condition_d(stem_candidate)) {
                            stem = stem_candidate;
                        }
                        break;

                    case "e":
                        if(condition_e(stem_candidate)) {
                            stem = stem_candidate;
                        }
                        break;

                    case "f":
                        if(condition_f(stem_candidate)) {
                            stem = stem_candidate;
                        }
                        break;

                    case "g":
                        if(condition_g(stem_candidate)) {
                            stem = stem_candidate;
                        }
                        break;

                    case "h":
                        if(condition_h(stem_candidate)) {
                            stem = stem_candidate;
                        }
                        break;

                    case "i":
                        if(condition_i(stem_candidate)) {
                            stem = stem_candidate;
                        }
                        break;

                    case "j":
                        if(condition_j(stem_candidate)) {
                            stem = stem_candidate;
                        }
                        break;

                    case "k":
                        if(condition_k(stem_candidate)) {
                            stem = stem_candidate;
                        }
                        break;

                    case "l":
                        if(condition_l(stem_candidate)) {
                            stem = stem_candidate;
                        }
                        break;

                    case "m":
                        if(condition_m(stem_candidate)) {
                            stem = stem_candidate;
                        }
                        break;

                    case "n":
                        if(condition_n(stem_candidate)) {
                            stem = stem_candidate;
                        }
                        break;

                    case "o":
                        if(condition_o(stem_candidate)) {
                            stem = stem_candidate;
                        }
                        break;

                    case "p":
                        if(condition_p(stem_candidate)) {
                            stem = stem_candidate;
                        }
                        break;

                    case "q":
                        if(condition_q(stem_candidate)) {
                            stem = stem_candidate;
                        }
                        break;

                    case "r":
                        if(condition_r(stem_candidate)) {
                            stem = stem_candidate;
                        }
                        break;

                    case "s":
                        if(condition_s(stem_candidate)) {
                            stem = stem_candidate;
                        }
                        break;

                    case "t":
                        if(condition_t(stem_candidate)) {
                            stem = stem_candidate;
                        }
                        break;

                    case "u":
                        if(condition_u(stem_candidate)) {
                            stem = stem_candidate;
                        }
                        break;

                    case "v":
                        if(condition_v(stem_candidate)) {
                            stem = stem_candidate;
                        }
                        break;

                    case "w":
                        if(condition_w(stem_candidate)) {
                            stem = stem_candidate;
                        }
                        break;

                    case "x":
                        if(condition_x(stem_candidate)) {
                            stem = stem_candidate;
                        }
                        break;

                    case "y":
                        if(condition_y(stem_candidate)) {
                            stem = stem_candidate;
                        }
                        break;

                    case "z":
                        if(condition_z(stem_candidate)) {
                            stem = stem_candidate;
                        }
                        break;

                    case "aa":
                        if(condition_aa(stem_candidate)) {
                            stem = stem_candidate;
                        }
                        break;

                    case "bb":
                        if(condition_bb(stem_candidate)) {
                            stem = stem_candidate;
                        }
                        break;

                    default:
                        break;
                }*/
            }
            ending_map_index--;
        }

        return new String(stem);
    }

    public static String[] stem(String[] inputs) {
        String[] stems = new String[inputs.length];

        for(int i = 0; i < inputs.length; i++) {
            stems[i] = stem(inputs[i]);
        }

        return stems;
    }

    // Conditions stem must pass to remove ending:
    // No restrictions on stem
    private static boolean condition_a(char[] input) {
        return true;
    }

    // Minimum stem length == 3
    private static boolean condition_b(char[] input) {
        return input.length >= 3;
    }

    // Minimum stem length == 4
    private static boolean condition_c(char[] input) {
        return input.length >= 4;
    }

    // Minimum stem length == 5
    private static boolean condition_d(char[] input) {
        return input.length >= 5;
    }

    // Do not remove ending after 'e'
    private static boolean condition_e(char[] input) {
        return !(input[input.length - 1] == 'e');
    }

    // Minimum stem length == 3 and do not remove ending after 'e'
    private static boolean condition_f(char[] input) {
        return input.length >= 3 && !(input[input.length - 1] == 'e');
    }

    // Minimum stem length == 3 and remove ending only after 'f'
    private static boolean condition_g(char[] input) {
        return input.length >= 3 && (input[input.length - 1] == 'f');
    }

    // Its difficult to tell what the first letter is from the original paper. Online sources say 't' and 'll'.
    // Remove stem ending only after 't' or 'll'
    private static boolean condition_h(char[] input) {
        return (input[input.length - 1] == 't') || (input[input.length - 1] == 'l' && (input[input.length - 2] == 'l'));
    }

    // Do not remove ending after 'o' or 'e'.
    private static boolean condition_i(char[] input) {
        return !(input[input.length - 1] == 'o' || input[input.length - 1] == 'e');
    }

    // Do not remove ending after 'a' or 'e'.
    private static boolean condition_j(char[] input) {
        return !(input[input.length - 1] == 'a' || input[input.length - 1] == 'e');
    }


    // Minimum stem length == 3 and remove ending only after 'l', 'i', or 'uαe' (where 'α' is any letter).
    private static boolean condition_k(char[] input) {
        return input.length >= 3 && ((input[input.length - 3] == 'u' && input[input.length - 1] == 'e') || input[input.length - 1] == 'l' || input[input.length - 1] == 'i');
    }

    // Do not remove ending after 'u', 'x', or 's', unless 's' follows 'o'.
    private static boolean condition_l(char[] input) {
        if(input[input.length - 1] == 'u' || input[input.length - 1] == 'x') {
            return false;
        } else if(input[input.length - 1] == 's' && !(input[input.length - 2] == 'o')) {
            return false;
        }

        return true;
    }

    // Do not remove ending after 'a', 'c', 'e', or 'm'.
    private static boolean condition_m(char[] input) {
        return !(input[input.length - 1] == 'a' || input[input.length - 1] == 'c' || input[input.length - 1] == 'e' || input[input.length - 1] == 'm');
    }

    // Minimum stem length == 4 after 'sαα', elsewhere minimum stem length == 3. (where 'α' is any letter)
    private static boolean condition_n(char[] input) {
        if(input[input.length - 3] == 's') {
            return input.length >= 4;
        } else {
            return input.length >= 3;
        }
    }

    // Remove ending only after 'l' or 'i'.
    private static boolean condition_o(char[] input) {
        return input[input.length - 1] == 'l' || input[input.length - 1] == 'i';
    }

    // Do not remove ending after 'c'.
    private static boolean condition_p(char[] input) {
        return !(input[input.length - 1] == 'c');
    }

    // Minimum stem length == 3 and do not remove ending after 'l' or 'n'.
    private static boolean condition_q(char[] input) {
        return input.length >= 3 && !(input[input.length - 1] == 'l' || input[input.length - 1] == 'n');
    }

    // Remove ending only after 'n' or 'r'.
    private static boolean condition_r(char[] input) {
        return input[input.length - 1] == 'n' || input[input.length - 1] == 'r';
    }

    // Remove ending only after 'dr' or 't', unless 't' follows 't'.
    private static boolean condition_s(char[] input) {
        return (input[input.length - 2] == 'd' && input[input.length - 1] == 'r') || (input[input.length - 1] == 't' && !(input[input.length - 2] == 't'));
    }

    // Remove ending only after 's' or 't', unless 't' follows 'o'.
    private static boolean condition_t(char[] input) {
        return input[input.length - 1] == 's' || (input[input.length - 1] == 't' && !(input[input.length - 2] == 'o'));
    }

    // Remove ending only after 'l', 'm', 'n', or 'r'.
    private static boolean condition_u(char[] input) {
        return input[input.length - 1] == 'l' || input[input.length - 1] == 'm' || input[input.length - 1] == 'n' || input[input.length - 1] == 'r';
    }

    // Remove ending only after 'c'.
    private static boolean condition_v(char[] input) {
        return input[input.length - 1] == 'c';
    }

    // Do not remove ending after 's' or 'u'.
    private static boolean condition_w(char[] input) {
        return !(input[input.length - 1] == 's' || input[input.length - 1] == 'u');
    }

    // Remove ending only after 'l', 'i', or 'uαe' (where 'α' is any letter).
    private static boolean condition_x(char[] input) {
        return input[input.length - 1] == 'l' || input[input.length - 1] == 'i' || (input[input.length - 3] == 'u' && input[input.length - 1] == 'e');
    }

    // Remove ending only after 'in'.
    private static boolean condition_y(char[] input) {
        return input[input.length - 2] == 'i' && input[input.length - 1] == 'n';
    }

    // Do not remove ending after 'f'.
    private static boolean condition_z(char[] input) {
        return !(input[input.length - 1] == 'f');
    }

    // For readability, I wrote this one a little differently.
    // Remove ending only after 'd', 'f', 'ph', 'th', 'l', 'er', 'or', 'es', or 't'.
    private static boolean condition_aa(char[] input) {
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
    private static boolean condition_bb(char[] input) {
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
    private static boolean condition_cc(char[] input) {
        return input[input.length - 1] == 'l';
    }

    private char[] tranform(char[] input) {


        return input;
    }
}