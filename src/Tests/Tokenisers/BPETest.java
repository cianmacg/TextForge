package Tests.Tokenisers;

import ie.atu.forge.Tokenisers.BPE;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Set;

public class BPETest {

    @Test
    public void testVocabularyGrowth() {
        BPE bpe = new BPE();
        String[] corpus = {"low", "lower", "newest", "widest"};

        bpe.train(corpus, 20);

        Set<String> vocab = bpe.getVocabulary();

        // Should include all characters
        assertTrue(vocab.contains("l"));
        assertTrue(vocab.contains("o"));
        assertTrue(vocab.contains("w"));

        // Should include some merged tokens
        assertTrue(vocab.size() <= 20);
        assertTrue(vocab.stream().anyMatch(token -> token.length() > 1));
    }

    @Test
    public void testTokenisation_MergesCommonPairs() {
        BPE bpe = new BPE();
        String[] corpus = {"l o w", "l o w e r", "n e w e s t", "w i d e s t"};
        // Spaces here simulate initial character splits (common for BPE training)
        bpe.train(corpus, 12);

        // After training, we expect some merges:
        // - "l o w" should likely merge into "low"
        // - "e s" might merge (appears in "newest" and "widest")
        // Final tokens depend on frequency, but we can assert expected splits.

        String[] tokens1 = bpe.tokenise("low");
        assertArrayEquals(new String[]{"low"}, tokens1, "Expected 'low' to be a single token");

        String[] tokens2 = bpe.tokenise("lowest");
        // "lowest" not in corpus, but "low" + "e s t" likely
        assertTrue(tokens2.length >= 2);
        assertEquals("lowest", String.join("", tokens2));
    }

    @Test
    public void testTokenisation_HelloCorpus() {
        BPE bpe = new BPE();
        String[] corpus = {"h e l l o", "h e l l", "h e l m e t"};
        bpe.train(corpus, 15);

        // Likely merges:
        // - "h e" (common prefix)
        // - "l l" (appears in "hello", "hell")
        // - Possibly "he" + "ll" + "o" for "hello"

        String[] tokens = bpe.tokenise("hello");

        // Check deterministic expected split (typical result after these merges)
        assertArrayEquals(new String[]{"he", "ll", "o"}, tokens);
    }

    @Test
    public void testUnseenWordFallsBackToChars() {
        BPE bpe = new BPE();
        String[] corpus = {"apple", "apricot", "banana"};
        bpe.train(corpus, 20);

        String[] tokens = bpe.tokenise("grape");

        // None of "grape" appeared, so must at least fall back to characters
        assertEquals("grape", String.join("", tokens));
        assertTrue(Arrays.stream(tokens).allMatch(t -> t.length() == 1 || bpe.getVocabulary().contains(t)));
    }

    @Test
    public void testTokenisationBeforeTrainingThrows() {
        BPE bpe = new BPE();

        Exception ex = assertThrows(IllegalStateException.class, () -> {
            bpe.tokenise("hello");
        });

        assertTrue(ex.getMessage().contains("not trained"));
    }

    @Test
    public void testEmptyStringTokenisation() {
        BPE bpe = new BPE();
        String[] corpus = {"a", "b"};
        bpe.train(corpus, 10);

        String[] tokens = bpe.tokenise("");
        assertEquals(0, tokens.length);
    }

    @Test
    public void testReconstruction() {
        BPE bpe = new BPE();
        String[] corpus = {"the", "thermostat", "theme"};
        bpe.train(corpus, 30);

        String input = "thermostat";
        String[] tokens = bpe.tokenise(input);

        // Must reconstruct exactly
        assertEquals(input, String.join("", tokens));
    }
}

