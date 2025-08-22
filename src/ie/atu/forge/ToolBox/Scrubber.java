package ie.atu.forge.ToolBox;

// This class should clean up text. Just converts everything to lower case and removes 'special' characters such as '"','.', and '!'.

/**
 * A class to clean up strings.
 */
public class Scrubber
{
    /**
     * This function removes any non-alphabetic characters from a string and converts it to lower case.
     * @param dirty The string to be cleaned
     * @return A string with only lowercase alphabetic characters.
     */
    public static String scrub(String dirty) {
        return dirty.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }
}
