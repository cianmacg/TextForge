package ie.atu.forge.Normalisation;

// This class should clean up text. Just converts everything to lower case and removes 'special' characters such as '"','.', and '!'.
public class Scrubber
{
    public static String scrub(String dirty) {
        return dirty.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }
}
