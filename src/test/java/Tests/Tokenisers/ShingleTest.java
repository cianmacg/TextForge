package Tests.Tokenisers;

import ie.atu.forge.Tokenisers.Shingle;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

public class ShingleTest {

    @Test
    @DisplayName("Test basic word bigrams")
    public void testBasicBigrams() {
        String input = "the quick brown fox";
        String[] result = Shingle.tokenise(input, 2);
        String[] expected = {"the quick", "quick brown", "brown fox"};
        assertArrayEquals(expected, result);
    }

    @Test
    @DisplayName("Test basic word trigrams")
    public void testBasicTrigrams() {
        String input = "the quick brown fox jumps";
        String[] result = Shingle.tokenise(input, 3);
        String[] expected = {"the quick brown", "quick brown fox", "brown fox jumps"};
        assertArrayEquals(expected, result);
    }

    @Test
    @DisplayName("Test single word window (unigrams)")
    public void testUnigrams() {
        String input = "hello world test";
        String[] result = Shingle.tokenise(input, 1);
        String[] expected = {"hello", "world", "test"};
        assertArrayEquals(expected, result);
    }

    @Test
    @DisplayName("Test window size equal to number of words")
    public void testWindowEqualToWordCount() {
        String input = "one two three";
        String[] result = Shingle.tokenise(input, 3);
        String[] expected = {"one two three"};
        assertArrayEquals(expected, result);
    }

    @Test
    @DisplayName("Test window size larger than number of words")
    public void testWindowLargerThanWordCount() {
        String input = "hello world";
        String[] result = Shingle.tokenise(input, 5);
        assertEquals(0, result.length);
    }

    @Test
    @DisplayName("Test empty string input")
    public void testEmptyString() {
        String input = "";
        String[] result = Shingle.tokenise(input, 2);
        assertEquals(0, result.length);
    }

    @Test
    @DisplayName("Test null input")
    public void testNullInput() {
        String[] result = Shingle.tokenise(null, 2);
        assertEquals(0, result.length);
    }

    @Test
    @DisplayName("Test zero window size")
    public void testZeroWindow() {
        String input = "hello world";
        String[] result = Shingle.tokenise(input, 0);
        assertEquals(0, result.length);
    }

    @Test
    @DisplayName("Test negative window size")
    public void testNegativeWindow() {
        String input = "hello world";
        String[] result = Shingle.tokenise(input, -1);
        assertEquals(0, result.length);
    }

    @Test
    @DisplayName("Test single word input")
    public void testSingleWord() {
        String input = "hello";
        String[] result = Shingle.tokenise(input, 1);
        String[] expected = {"hello"};
        assertArrayEquals(expected, result);

        // Test with window larger than single word
        result = Shingle.tokenise(input, 2);
        assertEquals(0, result.length);
    }

    @Test
    @DisplayName("Test multiple spaces between words")
    public void testMultipleSpaces() {
        String input = "hello   world    test";
        String[] result = Shingle.tokenise(input, 2);
        String[] expected = {"hello world", "world test"};
        assertArrayEquals(expected, result);
    }

    @Test
    @DisplayName("Test leading and trailing spaces")
    public void testLeadingTrailingSpaces() {
        String input = "  hello world test  ";
        String[] result = Shingle.tokenise(input, 2);
        String[] expected = {"hello world", "world test"};
        assertArrayEquals(expected, result);
    }

    @Test
    @DisplayName("Test words with punctuation")
    public void testWordsWithPunctuation() {
        String input = "Hello, world! How are you?";
        String[] result = Shingle.tokenise(input, 2);
        String[] expected = {"Hello, world!", "world! How", "How are", "are you?"};
        assertArrayEquals(expected, result);
    }

    @Test
    @DisplayName("Test words with numbers")
    public void testWordsWithNumbers() {
        String input = "I have 5 cats and 3 dogs";
        String[] result = Shingle.tokenise(input, 3);
        String[] expected = {"I have 5", "have 5 cats", "5 cats and", "cats and 3", "and 3 dogs"};
        assertArrayEquals(expected, result);
    }

    @Test
    @DisplayName("Test tabs and newlines as separators")
    public void testTabsAndNewlines() {
        String input = "hello\tworld\ntest\r\nmore";
        String[] result = Shingle.tokenise(input, 2);
        String[] expected = {"hello world", "world test", "test more"};
        assertArrayEquals(expected, result);
    }

    @Test
    @DisplayName("Test mixed case words")
    public void testMixedCase() {
        String input = "The Quick BROWN fox";
        String[] result = Shingle.tokenise(input, 2);
        String[] expected = {"The Quick", "Quick BROWN", "BROWN fox"};
        assertArrayEquals(expected, result);
    }

    @Test
    @DisplayName("Test words with apostrophes")
    public void testApostrophes() {
        String input = "don't can't won't";
        String[] result = Shingle.tokenise(input, 2);
        String[] expected = {"don't can't", "can't won't"};
        assertArrayEquals(expected, result);
    }

    @Test
    @DisplayName("Test hyphenated words")
    public void testHyphenatedWords() {
        String input = "well-known state-of-the-art approach";
        String[] result = Shingle.tokenise(input, 2);
        String[] expected = {"well-known state-of-the-art", "state-of-the-art approach"};
        assertArrayEquals(expected, result);
    }

    @Test
    @DisplayName("Test whitespace-only input")
    public void testWhitespaceOnly() {
        String input = "   \t\n   ";
        String[] result = Shingle.tokenise(input, 2);
        assertEquals(0, result.length);
    }

    @Test
    @DisplayName("Test result array length calculation")
    public void testResultLength() {
        String input = "one two three four five";
        int window = 3;
        String[] result = Shingle.tokenise(input, window);
        // Should be: number_of_words - window_size + 1
        // 5 words - 3 + 1 = 3 shingles
        assertEquals(3, result.length);
    }

    @Test
    @DisplayName("Test longer text with various window sizes")
    public void testLongerText() {
        String input = "the quick brown fox jumps over the lazy dog";

        // Test with window size 2
        String[] result2 = Shingle.tokenise(input, 2);
        assertEquals(8, result2.length); // 9 words - 2 + 1
        assertEquals("the quick", result2[0]);
        assertEquals("lazy dog", result2[result2.length - 1]);

        // Test with window size 4
        String[] result4 = Shingle.tokenise(input, 4);
        assertEquals(6, result4.length); // 9 words - 4 + 1
        assertEquals("the quick brown fox", result4[0]);
        assertEquals("over the lazy dog", result4[result4.length - 1]);
    }

    @Test
    @DisplayName("Test unicode words")
    public void testUnicodeWords() {
        String input = "caf\u00e9 r\u00e9staurant fran\u00e7ais";
        String[] result = Shingle.tokenise(input, 2);
        String[] expected = {"caf\u00e9 r\u00e9staurant", "r\u00e9staurant fran\u00e7ais"};
        assertArrayEquals(expected, result);
    }

    @Test
    @DisplayName("Test performance with large text")
    public void testLargeText() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            sb.append("word").append(i).append(" ");
        }
        String input = sb.toString().trim();

        String[] result = Shingle.tokenise(input, 5);
        assertEquals(996, result.length); // 1000 words - 5 + 1
        assertTrue(result[0].startsWith("word0"));
        assertTrue(result[result.length - 1].endsWith("word999"));
    }

    @Test
    @DisplayName("Test words separated by multiple whitespace types")
    public void testMixedWhitespace() {
        String input = "word1 \t word2\n\nword3\r\n  word4";
        String[] result = Shingle.tokenise(input, 2);
        String[] expected = {"word1 word2", "word2 word3", "word3 word4"};
        assertArrayEquals(expected, result);
    }
}
