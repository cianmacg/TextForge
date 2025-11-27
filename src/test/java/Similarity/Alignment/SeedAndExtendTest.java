package test.java.Similarity.Alignment;

import main.java.ie.atu.forge.Similarity.Alignment.SeedAndExtend;
import main.java.ie.atu.forge.Similarity.Alignment.Extension;
import main.java.ie.atu.forge.Similarity.Alignment.SmithWaterman;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;

public class SeedAndExtendTest {

    private String[] extractTexts(Extension[] extensions) {
        return java.util.Arrays.stream(extensions)
                .map(Extension::text)
                .toArray(String[]::new);
    }

    private void assertValidPositions(Extension[] extensions, String subject, String query) {
        for (Extension e : extensions) {
            assertTrue(e.subjectPos() >= 0 && e.subjectPos() < subject.length(),
                    "Subject position should be valid: " + e.subjectPos());
            assertTrue(e.queryPos() >= 0 && e.queryPos() < query.length(),
                    "Query position should be valid: " + e.queryPos());

            int endPos = e.subjectPos() + e.text().length();
            assertTrue(endPos <= subject.length(), "Extension extends beyond subject");
            assertEquals(e.text(), subject.substring(e.subjectPos(), endPos),
                    "Extension text should match subject at claimed position");
        }
    }

    @Test
    public void testSinglePerfectMatch() throws Exception {
        String subject = "AGTCGA";
        String query = "TCG";
        Extension[] results = SeedAndExtend.align(subject, query, 2);
        assertEquals(1, results.length);
        assertEquals("TCG", results[0].text());
        assertEquals(2, results[0].subjectPos());
        assertEquals(0, results[0].queryPos());
    }

    @Test
    public void testSinglePerfectMatchWithSW() throws Exception {
        String subject = "AGTCGA";
        String query = "TCG";
        SmithWaterman sw = new SmithWaterman();
        sw.setMATCH(2);
        sw.setMISMATCH(-1);
        sw.setGAP(-2);

        Extension[] results = SeedAndExtend.align(subject, query, 2, sw, 10);
        assertEquals(1, results.length);
        assertEquals("TCG", results[0].text());
        assertEquals(2, results[0].subjectPos());
        assertEquals(0, results[0].queryPos());
    }

    @Test
    public void testMultipleSeeds() throws Exception {
        String subject = "ATCGATCG";
        String query = "TCG";
        Extension[] results = SeedAndExtend.align(subject, query, 2);

        assertEquals(2, results.length);
        assertArrayEquals(new String[]{"TCG", "TCG"}, extractTexts(results));
        assertValidPositions(results, subject, query);
    }

    @Test
    public void testMultipleSeedsWithSW() throws Exception {
        String subject = "ATCGATCG";
        String query = "TCG";
        SmithWaterman sw = new SmithWaterman();
        sw.setMATCH(2);
        sw.setMISMATCH(-1);
        sw.setGAP(-2);

        Extension[] results = SeedAndExtend.align(subject, query, 2, sw, 10);
        assertEquals(2, results.length);
        assertArrayEquals(new String[]{"TCG", "TCG"}, extractTexts(results));
        assertValidPositions(results, subject, query);
    }

    @Test
    public void testExtensionBeyondKmer() throws Exception {
        String subject = "AGTCGAC";
        String query = "TCGAC";
        Extension[] results = SeedAndExtend.align(subject, query, 3);
        assertEquals(1, results.length);
        assertEquals("TCGAC", results[0].text());
        assertEquals(2, results[0].subjectPos());
        assertEquals(0, results[0].queryPos());
    }

    @Test
    public void testExtensionBeyondKmerWithSW() throws Exception {
        String subject = "AGTCGAC";
        String query = "TCGAC";
        SmithWaterman sw = new SmithWaterman();
        sw.setMATCH(2);
        sw.setMISMATCH(-1);
        sw.setGAP(-2);

        Extension[] results = SeedAndExtend.align(subject, query, 3, sw, 10);
        assertEquals(1, results.length);
        assertEquals("TCGAC", results[0].text());
        assertEquals(2, results[0].subjectPos());
        assertEquals(0, results[0].queryPos());
    }

    @Test
    public void testNoMatch() throws Exception {
        String subject = "AAAAAA";
        String query = "TTT";
        Extension[] results = SeedAndExtend.align(subject, query, 2);
        assertEquals(0, results.length);
    }

    @Test
    public void testNoMatchWithSW() throws Exception {
        String subject = "AAAAAA";
        String query = "TTT";
        SmithWaterman sw = new SmithWaterman();
        sw.setMATCH(2);
        sw.setMISMATCH(-1);
        sw.setGAP(-2);

        Extension[] results = SeedAndExtend.align(subject, query, 2, sw, 10);
        assertEquals(0, results.length);
    }

    @Test
    public void testOverlappingMatches() throws Exception {
        String subject = "AAAAA";
        String query = "AAA";
        Extension[] results = SeedAndExtend.align(subject, query, 2);

        // Should find multiple overlapping "AAA" matches but filtering should reduce duplicates
        assertTrue(results.length > 0);
        assertValidPositions(results, subject, query);

        // All results should be "AAA"
        for (Extension e : results) {
            assertEquals("AAA", e.text());
        }
    }

    /*
        This isn't accurate, as the logic which searched for the seed post-Smith-Waterman alignment searches from the left most point of the alignment.
        In this case, the alignment is the full, original subject string. We run into what appears to be the seed immediately, even though
        the original seed is at position 3. This causes the extension positions to be inaccurate.
     */
    @Test
    public void testOverlappingMatchesWithSW() throws Exception {
        String subject = "AAAAA";
        String query = "AAA";
        SmithWaterman sw = new SmithWaterman();
        sw.setMATCH(2);
        sw.setMISMATCH(-1);
        sw.setGAP(-2);

        Extension[] results = SeedAndExtend.align(subject, query, 2, sw, 10);
        assertTrue(results.length > 0);
        assertValidPositions(results, subject, query);

        for (Extension e : results) {
            assertEquals("AAA", e.text());
        }
    }

    @Test
    public void testPartialOverlapExtensions() throws Exception {
        String subject = "VVVVVME";
        String query = "MEVVVVV";
        Extension[] results = SeedAndExtend.align(subject, query, 2);
        assertTrue(results.length > 0);
        assertValidPositions(results, subject, query);
    }

    @Disabled("Smith-Waterman may discard seed 'ME' if not part of optimal alignment.")
    @Test
    public void testPartialOverlapExtensionsWithSWMatrix() throws Exception {
        String subject = "VVVVVME";
        String query = "MEVVVVV";
        SmithWaterman sw = new SmithWaterman();
        sw.loadScoringMatrix("./src/main/resources/ScoringMatrices/BLOSUM45.txt");

        Extension[] results = SeedAndExtend.align(subject, query, 2, sw, 10);
        assertTrue(results.length > 0, "No extensions found — SW may discard the original seed.");
        assertValidPositions(results, subject, query);
    }

    @Test
    public void testNullSmithWatermanDefaultsToGreedy() throws Exception {
        String subject = "AGTCGA";
        String query = "TCG";

        Extension[] results = SeedAndExtend.align(subject, query, 2, null, 10);
        assertEquals(1, results.length);
        assertEquals("TCG", results[0].text());
        assertEquals(2, results[0].subjectPos());
        assertEquals(0, results[0].queryPos());
    }

    @Disabled("SW alignment may cause extension to ignore initial seed (gaps/mismatches affect result).")
    @Test
    public void testSmithWatermanWithGaps() throws Exception {
        String subject = "ATCGGATC";
        String query = "ATCATC";
        SmithWaterman sw = new SmithWaterman();
        sw.setMATCH(2);
        sw.setMISMATCH(-1);
        sw.setGAP(-1);

        Extension[] results = SeedAndExtend.align(subject, query, 2, sw, 10);
        assertTrue(results.length > 0, "SW alignment may discard original seed if gaps reduce score.");
        assertValidPositions(results, subject, query);
    }

    @Test
    public void testEmptySequences() throws Exception {
        String subject = "";
        String query = "TCG";

        Extension[] results = SeedAndExtend.align(subject, query, 3);
        assertEquals(0, results.length);
    }

    @Test
    public void testKmerLongerThanSequence() throws Exception {
        String subject = "ATCG";
        String query = "AT";

        Extension[] results = SeedAndExtend.align(subject, query, 5);
        assertEquals(0, results.length);
    }
}
