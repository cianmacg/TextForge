package ie.atu.forge.Vectorisers;

import java.util.*;

/**
 * A weighting method that scores words based on how often they appear in a document (TF) and how rare they are across the whole corpus (IDF).<br><br>
 * Relevant Papers:<br>
 * <a href="https://www.emerald.com/jd/article-abstract/28/1/11/196227/A-STATISTICAL-INTERPRETATION-OF-TERM-SPECIFICITY?redirectedFrom=fulltext"> A Statistical Interpretation of Term Specificity and Its Application in Retrieval.
 * Journal of Documentation.</a> <br>
 * <a href="https://dl.acm.org/doi/10.1145/361219.361220">A vector space model for automatic indexing</a><br>
 * <a href="https://www.sciencedirect.com/science/article/abs/pii/0306457388900210?via%3Dihub">Term-weighting approaches in automatic text retrieval</a>
 */
public class TFIDF {
    private final Map<String, Integer> corpusTermFreq = new HashMap<>(); // Map of words to frequency count across all documents.
    private int documentCount = 0;

    /**
     * Adds a document to the corpus.
     *
     * @param doc The document to be added.
     */
    public void addDocument(String doc) {
        if(doc == null || doc.isEmpty()) return; // If the document is null or has no length, don't do anything.
        documentCount++;
        Set<String> terms = new HashSet<>(List.of(docToTerms(doc)));

        addTermsToDocumentFrequency(terms);
    }

    // Add a group of documents to documentFreq.

    /**
     * Adds a group of documents to the corpus.
     *
     * @param docs The documents to be added.
     */
    public void addDocuments(String[] docs) {
        if(docs == null || docs.length == 0) return;

        for(String doc: docs) addDocument(doc); // add each documents to the list.
    }

    private void addTermsToDocumentFrequency(Set<String> terms) {
        for(String term: terms) {
            corpusTermFreq.merge(term, 1, Integer::sum);
        }
    }

    /**
     * Scores every term in the provided document. If the document does not already exist in the corpus, newDocument should be set to true. This will add the document to the corpus before scoring the document.
     *
     * @param doc The document to be scored.
     * @param newDocument If the document doesn't exist in the corpus already.
     * @return A mapping of each term to its score.
     */
    public Map<String, Double> scoreDocument(String doc, boolean newDocument) { // A new document is on which has not been added to the 'documentFreq', and needs to be added first.
        if(doc == null || doc.isEmpty() || (documentCount == 0 && !newDocument)) return new HashMap<>(); // Attemping to add an empty or null document, or trying to score while there are no documents added, return an empty HashMap.
        String[] terms = docToTerms(doc);
        int len = terms.length; // Used for the overall number of terms in a doc.

        Set<String> addedTerms = new HashSet<>();
        Map<String, Integer> termCounts = new HashMap<>(); // The counts for this document alone.
        Map<String, Double> termScores = new HashMap<>();

        for(String term: terms){
            addedTerms.add(term);
            termCounts.merge(term, 1, Integer::sum);
        }

        if(newDocument) {
            documentCount++;
            addTermsToDocumentFrequency(addedTerms);    // Add terms to overall document frequency (if it hasn't previously been added.
        }

        // Begin calculations
        for(String term: addedTerms) {
            int docFreq = corpusTermFreq.getOrDefault(term, 0);
            // If the user stated the document is not new, but actually it is, it is possible a term doesn't appear in the documentFreq.
            if(docFreq == 0) {
                termScores.put(term, 0.0d);
            } else {
                double tf = (double) termCounts.get(term) / len;
                double idf = Math.log((1.0d + documentCount) / (1.0d + corpusTermFreq.get(term)) ); // Adding 1.0d to the denominator here to prevent zeroing out of scores (log 1)
                termScores.put(term, (tf * idf));
            }
        }

        return termScores;
    }

    /**
     * Creates a vector from a document. If the document does not already exist in the corpus, newDocument should be set to true. This will add the document to the corpus before scoring the document.
     * <br><br>
     * A vector will contain values for every term in the corpus. If the term appears in the corpus but not the document, it will have a score of 0. If the term appears in the document, its TF-IDF score will be calculated.
     * @param doc The document to be vectorised.
     * @param newDocument If the document doesn't exist in the corpus already.
     * @return A vector representing the provided document.
     */
    public double[] vectoriseDocument(String doc, boolean newDocument) {
        if(doc == null || doc.isEmpty() || (documentCount == 0 && !newDocument)) return new double[0]; // Attemping to add an empty or null document, or trying to score while there are no documents added, return an empty array.
        String[] terms = docToTerms(doc);
        int len = terms.length; // Used for the overall number of terms in a doc.

        Set<String> addedTerms = new HashSet<>();
        Map<String, Integer> termCounts = new HashMap<>(); // The counts for this document alone.
        Map<String, Double> termScores = new HashMap<>();

        for(String term: terms){
            addedTerms.add(term);
            termCounts.merge(term, 1, Integer::sum);
        }

        if(newDocument) {
            documentCount++;
            addTermsToDocumentFrequency(addedTerms);    // Add terms to overall document frequency (if it hasn't previously been added). Terms should only be incremented once per document (i.e. even if a word appears many times in the document, it should only increase the corpus count by 1).
        }

        // To ensure the same order each time a vector is created (assuming no new terms added)
        List<String> vocab = new ArrayList<>(corpusTermFreq.keySet());
        Collections.sort(vocab);

        double[] vector = new double[corpusTermFreq.size()];

        // Begin calculations
        for(int i = 0; i < vocab.size(); i++) {
            String term = vocab.get(i);
            double tf = (double) termCounts.getOrDefault(term, 0) / len;
            double idf = Math.log((1.0d + documentCount) / (1.0d + corpusTermFreq.get(term))); // This idf calculation includes smoothing (+ 1.0d) to ensure no division by 0 errors.

            vector[i] = (tf * idf);
        }

        return vector;
    }

    // This function can later be updated to clean the string before splitting (e.g. stop word removal, punctuation removal, stemming, etc.) (hence keeping the variable splits, allowing for easy changes later)
    private String[] docToTerms(String doc) {
        String[] splits = doc.toLowerCase()
                                .replaceAll("\\p{Punct}", "")   // "\\p{Punct}" removes all punctuation.
                                .split("\\s+"); // "\\s+" splits on one or more whitespace characters,


        // Filter out empty strings that might result from cleaning
        String[] filter = new String[splits.length];
        int counter = 0;
        for(String split: splits) {
            if(!split.isEmpty()) {
                filter[counter] = split;
                counter++;
            }
        }

        splits = new String[counter];
        System.arraycopy(filter, 0, splits, 0, counter);

        return splits;
    }

    /**
     * Gets the frequency of a term in the corpus.
     *
     * @return The term frequency in the corpus.
     */
    public Map<String, Integer> getCorpusTermFreq() {
        return new HashMap<>(corpusTermFreq);
    }

    /**
     * Gets the total number of documents that have been added to the corpus.
     *
     * @return The document count in the corpus.
     */
    public int getDocumentCount() {
        return documentCount;
    }
}