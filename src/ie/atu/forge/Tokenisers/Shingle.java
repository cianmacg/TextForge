package ie.atu.forge.Tokenisers;

public class Shingle {
    public String[] tokenise(String input, int window) {
        String[] words = input.split("\\s+");
        String[] tokens = new String[input.length() - window + 1];



        return tokens;
    }
}
