package test.java.Vectorisers;

import main.java.ie.atu.forge.Vectorisers.TFIDF;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class TFIDFTest {
    private TFIDF tfidf;

    @BeforeEach
    public void setUp() {
        tfidf = new TFIDF();
    }

    @Test
    public void testSingleDocumentScoringOnlyDocTerms() {
        tfidf.addDocument("the cat sat on the mat");

        Map<String, Double> scores = tfidf.scoreDocument("the cat sat", false);

        // Only terms in input document are returned
        assertEquals(Set.of("the", "cat", "sat"), scores.keySet());
    }

    @Test
    public void testMultipleDocumentsTFIDFValuesNormalized() {
        String[] docs = {
                "apple banana apple",   // TF: apple=2/3, banana=1/3
                "banana fruit banana",  // TF: banana=2/3, fruit=1/3
                "fruit apple banana"    // TF: fruit=1/3, apple=1/3, banana=1/3
        };

        tfidf.addDocuments(docs);

        // Scoring a test doc NOT added to corpus
        String testDoc = "apple banana";
        Map<String, Double> scores = tfidf.scoreDocument(testDoc, false);

        // Corpus doc count = 3 (testDoc is NOT included)

        // DF(apple) = 2 → IDF = log((1 + 3) / (1 + 2)) = log(4 / 3)
        // DF(banana) = 3 → IDF = log((1 + 3) / (1 + 3)) = log(4 / 4) = 0.0

        double idfApple = Math.log(4.0 / 3.0);
        double idfBanana = Math.log(4.0 / 4.0); // 0.0

        // TFs in test doc: both terms appear once in 2-word doc
        double tfApple = 0.5;
        double tfBanana = 0.5;

        double expectedApple = tfApple * idfApple;
        double expectedBanana = tfBanana * idfBanana; // = 0.0

        assertEquals(expectedApple, scores.get("apple"), 1e-6);
        assertEquals(expectedBanana, scores.get("banana"), 1e-6);
    }



    @Test
    public void testVectorIncludesAllCorpusTerms() {
        tfidf.addDocuments(new String[]{
                "red green blue",
                "green yellow blue",
                "red yellow green"
        });

        double[] vector = tfidf.vectoriseDocument("red blue", true);
        assertEquals(4, vector.length); // vocab = {red, green, blue, yellow}
    }

    @Test
    public void testVectorValuesMatchDocTermsOnly() {
        tfidf.addDocuments(new String[]{
                "sun moon star",
                "moon star",
                "sun sun star"
        });

        String doc = "sun star";
        double[] vector = tfidf.vectoriseDocument(doc, true);

        // With smoothing, IDF = log((1 + N) / (1 + df))
        // Document count N = 4 (after vectorised doc is added)
        // Term document frequencies after adding vectorised doc:
        //  - "sun": in 3 docs → df=3 → idf = log(5/4)
        //  - "star": in all 4 docs → df=4 → idf = log(5/5) = 0
        //  - "moon": in 2 docs → df=2 → idf = log(5/3)

        // Alphabetical vocab order: moon, star, sun
        assertEquals(3, vector.length);

        assertEquals(0.0, vector[0], 1e-6); // "moon" not in doc → 0

        // "star" appears once in doc of 2 terms → TF = 0.5
        // IDF = log(5/5) = 0 → TF-IDF = 0
        assertEquals(0.0, vector[1], 1e-6);

        // "sun" appears once in doc of 2 terms → TF = 0.5
        // IDF = log(5/4) = ~0.2231 → TF-IDF = 0.5 * 0.2231 = ~0.1115
        assertEquals(0.5 * Math.log(5.0 / 4.0), vector[2], 1e-6);
    }



    @Test
    public void testUnknownTermsScoredCorrectlyWhenAdded() {
        tfidf.addDocument("alpha beta gamma"); // 1st document

        // "delta epsilon" is added as a new document (2nd total doc)
        Map<String, Double> scores = tfidf.scoreDocument("delta epsilon", true);

        assertEquals(2, scores.size());

        // Now: documentCount = 2 (alpha/beta/gamma + delta/epsilon)

        // Each term appears in only 1 document → DF = 1
        // So with smoothing:
        // IDF = log((1 + 2) / (1 + 1)) = log(3 / 2)
        double idf = Math.log(3.0 / 2.0);

        // TF = 0.5 (both terms appear once in a 2-term doc)
        double tf = 0.5;

        double expected = tf * idf;

        for (double value : scores.values()) {
            assertEquals(expected, value, 1e-6);
        }
    }

}
