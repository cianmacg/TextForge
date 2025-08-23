package Tests.Tokenisers;

import ie.atu.forge.Tokenisers.BPE;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class BPETest {

    private BPE bpe;

    @BeforeEach
    public void setUp() {
        bpe = new BPE();
        // Train on a small corpus to build the vocabulary.
        // Example corpus contains common words and punctuation.
        String corpus = "low lower lowest slow slower fastest fast";
        bpe.train(corpus, 50);  // vocab size 50 for plenty of merges
    }

    @Test
    public void testBasicEncodeDecode() {
        String text = "lower";
        int[] tokens = bpe.encode(text);
        String decoded = bpe.decode(tokens);

        assertEquals(text, decoded, "Decoded text should match original");
    }

    @Test
    public void testEncodingConsistency() {
        String text = "fastest";
        int[] firstEncoding = bpe.encode(text);
        int[] secondEncoding = bpe.encode(text);

        assertArrayEquals(firstEncoding, secondEncoding,
                "Encoding the same text twice should yield identical tokens");
    }

    @Test
    public void testRoundTripMultipleWords() {
        String text = "slow lower fast";
        int[] tokens = bpe.encode(text);
        String decoded = bpe.decode(tokens);

        assertEquals(text, decoded, "Decoded text should match for multi-word inputs");
    }

    @Test
    public void testHandlesUnknownWord() {
        // If "rocket" wasn't in training, BPE should still encode it
        String text = "rocket";
        int[] tokens = bpe.encode(text);
        String decoded = bpe.decode(tokens);

        assertEquals(text, decoded, "BPE should handle words outside training corpus");
        assertTrue(tokens.length > 0, "Tokens should not be empty for unknown words");
    }

    @Test
    public void testMergesHappen() {
        // Ensure BPE is actually merging subwords (not just characters)
        String word = "lowest";
        int[] tokens = bpe.encode(word);

        assertTrue(tokens.length < word.length(),
                "BPE should merge characters into fewer tokens");
    }

    @Test
    public void testPunctuation() {
        String text = "fast!";
        int[] tokens = bpe.encode(text);
        String decoded = bpe.decode(tokens);

        assertEquals(text, decoded, "BPE should handle punctuation correctly");
    }

    @Test
    public void testEmptyInput() {
        String text = "";
        int[] tokens = bpe.encode(text);
        String decoded = bpe.decode(tokens);

        assertEquals("", decoded, "Empty input should encode/decode to empty");
        assertEquals(0, tokens.length, "Encoding an empty string should yield no tokens");
    }

    @Test
    public void testWhitespacePreservation() {
        String text = "fast slow";
        int[] tokens = bpe.encode(text);
        String decoded = bpe.decode(tokens);

        assertEquals(text, decoded, "Whitespace must be preserved after round-trip");
    }

    @Test
    public void testSaveAndLoadVocabHex() throws IOException {
        String text = "lower slow";
        int[] originalTokens = bpe.encode(text);

        // Save vocab in Hex format to local workspace
        bpe.saveVocabToJsonHex();

        // Load vocab into a fresh BPE instance
        BPE loadedBpe = new BPE();
        loadedBpe.loadVocabFromJsonHex("bpe_vocab_hex.json");

        int[] loadedTokens = loadedBpe.encode(text);
        assertArrayEquals(originalTokens, loadedTokens,
                "Encoding should be identical after saving/loading Hex vocab");

        String decoded = loadedBpe.decode(loadedTokens);
        assertEquals(text, decoded, "Decoded text should match after Hex vocab load");

        // Cleanup
        Files.deleteIfExists(Paths.get("bpe_vocab_hex.json"));
    }
}