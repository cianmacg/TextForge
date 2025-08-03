package ie.atu.forge.Vectorisers;

import java.util.*;

public class TFIDF {
    private final Map<String, Integer> documentFreq = new HashMap<>(); // Map of word hashCodes to frequency count across all documents.
    private int documentCount = 0;

    // Add a document to documentFreq
    public void addDocument(String doc) {
        if(doc == null || doc.isEmpty()) return; // If the document is null or has no length, don't do anything.
        documentCount++;
        Set<String> terms = new HashSet<>(List.of(docToTerms(doc)));

        addTermsToDocumentFrequency(terms);
    }

    // Add a group of documents to documentFreq.
    public void addDocuments(String[] docs) {
        if(docs == null || docs.length == 0) return;

        for(String doc: docs) addDocument(doc); // add each documents to the list.
    }

    private void addTermsToDocumentFrequency(Set<String> terms) {
        for(String term: terms) {
            documentFreq.merge(term, 1, Integer::sum);
        }
    }

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
            double tf = (double) termCounts.get(term) / len;
            double idf = Math.log((double) documentCount / (1.0d + documentFreq.get(term)) ); // Adding 1.0d to the denominator here to prevent zeroing out of scores (log 1)

            termScores.put(term, (tf * idf));
        }

        return termScores;
    }

    // Creates a vector based on every term in every document.
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
            addTermsToDocumentFrequency(addedTerms);    // Add terms to overall document frequency (if it hasn't previously been added.
        }

        double[] vector = new double[documentFreq.size()];
        int pos = 0;
        // Begin calculations
        for(Map.Entry<String, Integer> term: documentFreq.entrySet()) {
            double tf = (double) termCounts.getOrDefault(term.getKey(), 0) / len;
            double idf = Math.log((double) documentCount / (1.0d + term.getValue()));

            vector[pos] = (tf * idf);
            pos++;
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
}