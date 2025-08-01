package Tests.Similarity.Alignment;

import ie.atu.forge.Similarity.Alignment.SeedAndExtend;
import ie.atu.forge.Similarity.Alignment.Extension;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class SeedAndExtendTest {

    private String[] extractTexts(Extension[] extensions) {
        return java.util.Arrays.stream(extensions)
                .map(Extension::text)
                .toArray(String[]::new);
    }

    @Test
    public void testSinglePerfectMatch() {
        String subject = "AGTCGA";
        String query = "TCG";
        Extension[] results = SeedAndExtend.align(subject, query, 3, null);

        // Expecting no results because we only report extended seeds
        assertEquals(0, results.length);
    }

    @Test
    public void testMultipleSeeds() {
        String subject = "ATCGATCG";
        String query = "TCG";
        Extension[] results = SeedAndExtend.align(subject, query, 2, null);

        // Matches "TCG" twice
        assertArrayEquals(new String[]{"TCG", "TCG"}, extractTexts(results));
    }

    @Test
    public void testExtensionBeyondKmer() {
        String subject = "AGTCGAC";
        String query = "TCGAC";
        Extension[] results = SeedAndExtend.align(subject, query, 3, null);
        // Should extend from "TCG" to "TCGAC"
        assertArrayEquals(new String[]{"TCGAC"}, extractTexts(results));
    }

    @Test
    public void testNoMatch() {
        String subject = "AAAAAA";
        String query = "TTT";
        Extension[] results = SeedAndExtend.align(subject, query, 2, null);
        assertEquals(0, results.length);
    }

    @Test
    public void testOverlappingMatches() {
        String subject = "AAAAA";
        String query = "AAA";
        Extension[] results = SeedAndExtend.align(subject, query, 2, null);

        // "AAA" matches at positions (0-2), (1-3), (2-4)
        assertArrayEquals(new String[]{"AAA", "AAA", "AAA"}, extractTexts(results));
    }

    @Test
    public void testPartialOverlapExtensions() {
        String subject = "VVVVVME";
        String query = "MEVVVVV";
        Extension[] results = SeedAndExtend.align(subject, query, 2, null);
        // Depending on implementation, could find extensions like "VVVVV"
        assertTrue(results.length > 0);
    }
}

