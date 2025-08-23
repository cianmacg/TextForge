package Tests.Tokenisers;

import ie.atu.forge.Tokenisers.Ngram;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

public class NgramTest {

    @Test
    @DisplayName("Test basic ngram tokenization with window size 2")
    public void testBasicBigrams() {
        String input = "hello world";
        String[] result = Ngram.tokenise(input, 2);
        String[] expected = {"he", "el", "ll", "lo", "o ", " w", "wo", "or", "rl", "ld"};
        assertArrayEquals(expected, result);
    }

    @Test
    @DisplayName("Test basic ngram tokenization with window size 3")
    public void testBasicTrigrams() {
        String input = "hello";
        String[] result = Ngram.tokenise(input, 3);
        String[] expected = {"hel", "ell", "llo"};
        assertArrayEquals(expected, result);
    }

    @Test
    @DisplayName("Test single character window")
    public void testUnigrams() {
        String input = "hello";
        String[] result = Ngram.tokenise(input, 1);
        String[] expected = {"h", "e", "l", "l", "o"};
        assertArrayEquals(expected, result);
    }

    @Test
    @DisplayName("Test window size equal to input length")
    public void testWindowEqualToInputLength() {
        String input = "test";
        String[] result = Ngram.tokenise(input, 4);
        String[] expected = {"test"};
        assertArrayEquals(expected, result);
    }

    @Test
    @DisplayName("Test window size larger than input length")
    public void testWindowLargerThanInput() {
        String input = "hi";
        String[] result = Ngram.tokenise(input, 5);
        assertEquals(0, result.length);
    }

    @Test
    @DisplayName("Test empty string input")
    public void testEmptyString() {
        String input = "";
        String[] result = Ngram.tokenise(input, 2);
        assertEquals(0, result.length);
    }

    @Test
    @DisplayName("Test null input")
    public void testNullInput() {
        String[] result = Ngram.tokenise(null, 2);
        assertEquals(0, result.length);
    }

    @Test
    @DisplayName("Test zero window size")
    public void testZeroWindow() {
        String input = "test";
        String[] result = Ngram.tokenise(input, 0);
        assertEquals(0, result.length);
    }

    @Test
    @DisplayName("Test negative window size")
    public void testNegativeWindow() {
        String input = "test";
        String[] result = Ngram.tokenise(input, -1);
        assertEquals(0, result.length);
    }

    @Test
    @DisplayName("Test single character input")
    public void testSingleCharacter() {
        String input = "a";
        String[] result = Ngram.tokenise(input, 1);
        String[] expected = {"a"};
        assertArrayEquals(expected, result);

        // Test with window larger than single character
        result = Ngram.tokenise(input, 2);
        assertEquals(0, result.length);
    }

    @Test
    @DisplayName("Test input with spaces and special characters")
    public void testSpecialCharacters() {
        String input = "a b!";
        String[] result = Ngram.tokenise(input, 2);
        String[] expected = {"a ", " b", "b!"};
        assertArrayEquals(expected, result);
    }

    @Test
    @DisplayName("Test input with numbers")
    public void testWithNumbers() {
        String input = "abc123";
        String[] result = Ngram.tokenise(input, 3);
        String[] expected = {"abc", "bc1", "c12", "123"};
        assertArrayEquals(expected, result);
    }

    @Test
    @DisplayName("Test input with unicode characters")
    public void testUnicodeCharacters() {
        String input = "caf\u00e9";
        String[] result = Ngram.tokenise(input, 2);
        String[] expected = {"ca", "af", "f\u00e9"};
        assertArrayEquals(expected, result);
    }

    @Test
    @DisplayName("Test longer text")
    public void testLongerText() {
        String input = "the quick brown fox";
        String[] result = Ngram.tokenise(input, 4);
        assertEquals(input.length() - 4 + 1, result.length);
        assertEquals("the ", result[0]);
        assertEquals(" fox", result[result.length - 1]);
    }

    @Test
    @DisplayName("Test whitespace-only input")
    public void testWhitespaceOnly() {
        String input = "   ";
        String[] result = Ngram.tokenise(input, 2);
        String[] expected = {"  ", "  "};
        assertArrayEquals(expected, result);
    }

    @Test
    @DisplayName("Test result array length calculation")
    public void testResultLength() {
        String input = "testing";
        int window = 3;
        String[] result = Ngram.tokenise(input, window);
        int expectedLength = Math.max(0, input.length() - window + 1);
        assertEquals(expectedLength, result.length);
    }

    @Test
    @DisplayName("Test no overlapping tokens when window equals input length")
    public void testNoOverlap() {
        String input = "abcd";
        String[] result = Ngram.tokenise(input, 4);
        assertEquals(1, result.length);
        assertEquals("abcd", result[0]);
    }

    @Test
    @DisplayName("Test large window size performance")
    public void testLargeWindow() {
        String input = "a".repeat(1000);
        String[] result = Ngram.tokenise(input, 100);
        assertEquals(901, result.length); // 1000 - 100 + 1
        assertEquals("a".repeat(100), result[0]);
    }
}