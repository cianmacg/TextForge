package Tests.Similarity.Alignment;

import static org.junit.jupiter.api.Assertions.*;

import ie.atu.forge.Similarity.Alignment.SeedAndExtend;
import org.junit.jupiter.api.Test;

public class SeedAndExtendTest {

    @Test
    public void testSinglePerfectMatch() {
        String subject = "AGTCGA";
        String query = "TCG";
        String[] results = SeedAndExtend.align(subject, query, 3);
        assertArrayEquals(new String[]{"TCG"}, results);
    }

    @Test
    public void testMultipleSeeds() {
        String subject = "ATCGATCG";
        String query = "TCG";
        String[] results = SeedAndExtend.align(subject, query, 2);

        // Matches "TCG" twice
        assertArrayEquals(new String[]{"TCG", "TCG"}, results);
    }

    @Test
    public void testExtensionBeyondKmer() {
        String subject = "AGTCGAC";
        String query = "TCGAC";
        String[] results = SeedAndExtend.align(subject, query, 3);
        // Should extend from "TCG" to "TCGAC"
        assertArrayEquals(new String[]{"TCGAC"}, results);
    }

    @Test
    public void testNoMatch() {
        String subject = "AAAAAA";
        String query = "TTT";
        String[] results = SeedAndExtend.align(subject, query, 2);
        assertEquals(0, results.length);
    }

    @Test
    public void testOverlappingMatches() {
        String subject = "AAAAA";
        String query = "AAA";
        String[] results = SeedAndExtend.align(subject, query, 2);

        // "AAA" matches at positions (0-2), (1-3), (2-4)
        assertArrayEquals(new String[]{"AAA", "AAA", "AAA"}, results);
    }

    @Test
    public void testPartialOverlapExtensions() {
        String subject = "VVVVVME";
        String query = "MEVVVVV";
        String[] results = SeedAndExtend.align(subject, query, 2);
        // Depending on implementation, could find extensions like "VVVVV"
        assertTrue(results.length > 0);
    }
}

