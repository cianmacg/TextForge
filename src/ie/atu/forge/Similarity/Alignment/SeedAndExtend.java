package ie.atu.forge.Similarity.Alignment;

import java.util.*;
import java.util.concurrent.StructuredTaskScope;

// A record for each seed.
record Seed (int queryIndex, int subjectIndex) {}

/*
    A 2 stage local sequence matching algorithm.
    Begins with a seeding phase where short exact matching sequences are found.
    Extends these seeds later to enlarge the match beyond the size of a kmer.

    This implementation uses a greedy no-gap extension, and returns the full list of matches, filtering out duplicates.
 */
public class SeedAndExtend {
    public static Extension[] align(String subject, String query, int kmerLength, SmithWaterman smithWaterman, int windowSize) throws Exception {   // smithWaterman - controls whether to use Greedy or Smith-Waterman extension. Null = Greedy.
        if(subject == null || subject.isEmpty() || query == null || query.isEmpty() || kmerLength <= 0) return new Extension[0]; // No alignments can be made on empty strings. No seeds can be generated is kmerLength is <= 0.

        List<Seed> seeds = seed(subject, query, kmerLength);
        Extension[] extensions;

        if(smithWaterman != null) {
            extensions = swExtend(seeds, subject, query, kmerLength, smithWaterman, windowSize);
        } else {
            extensions = extendSeeds(seeds, subject, query, kmerLength);
        }

        return extensions;
    }

    public static Extension[] align(String subject, String query, int kmerLength) {
        if(subject == null || subject.isEmpty() || query == null || query.isEmpty() || kmerLength <= 0) return new Extension[0];

        List<Seed> seeds = seed(subject, query, kmerLength);

        return extendSeeds(seeds, subject, query, kmerLength);
    }
    // The seeding part should find exact matching parts of the 2 sequences.
    private static List<Seed> seed(String subject, String query, int kmerLength) {
        // The value needs to be a list as the same combination may appear multiple times.
        Map<String, List<Integer>> kmerMap = new HashMap<>();

        // Enter subject kmers into the map for matching.
        for(int i = 0; i <= subject.length() - kmerLength; i++) {
            kmerMap.computeIfAbsent(subject.substring(i, i + kmerLength), v -> new ArrayList<>()).add(i);
        }

        List<Seed> seeds = new ArrayList<>();

        // Find all matches of query kmers with subject map. Make seeds from them.
        for(int i = 0; i <= query.length() - kmerLength; i++) {
            List<Integer> match = kmerMap.get(query.substring(i, i + kmerLength));

            if(match != null) {
                for(Integer index: match) {
                    seeds.add(new Seed(i, index));
                }
            }
        }

        return seeds;
    }

    // Greedy ungapped extension. Continues to extend both left and right as long as they are still exact matches.
    // Since this is a greedy no-gap implementation, both the subject match and query match will be the same.
    private static Extension[] extendSeeds(List<Seed> seeds, String subject, String query, int kmerLength) {
        try(var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            List<StructuredTaskScope.Subtask<Extension>> tasks = new ArrayList<>();

            for(Seed seed: seeds) {
                // Each seed can be checked independently.
                var task = scope.fork(() -> extendSeeds(seed, subject, query, kmerLength));
                tasks.add(task);
            }

            scope.join().throwIfFailed();

            // Get each string returned by each task and return them as an array.
            Extension[] extensions = tasks.stream().map(StructuredTaskScope.Subtask::get).toArray(Extension[]::new);

            return filterExtensions(extensions, kmerLength);

        } catch (Exception e) {
            System.err.println("Error: " + e);
            return new Extension[0];
        }
    }

    // Extends a single seed.
    private static Extension extendSeeds(Seed seed, String subject, String query, int kmerLength) {
        int sStart = seed.subjectIndex();
        int qStart = seed.queryIndex();
        int sEnd = sStart + kmerLength - 1;
        int qEnd = qStart + kmerLength - 1;

        // Extend left
        while(sStart > 0 && qStart > 0 && subject.charAt(sStart - 1) == query.charAt(qStart - 1)) {
            sStart--;
            qStart--;
        }

        // Extend right
        while(sEnd < subject.length() - 1 && qEnd < query.length() - 1 && subject.charAt(sEnd + 1) == query.charAt(qEnd + 1)) {
            sEnd++;
            qEnd++;
        }

        // It doesn't matter if we choose subject or query here, both will be the same.
        return new Extension(sStart, qStart, subject.substring(sStart, sEnd + 1));
    }

    private static Extension[] swExtend(List<Seed> seeds, String subject, String query, int kmerLength, SmithWaterman smithWaterman, int windowSize) throws Exception {
        try(var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            List<StructuredTaskScope.Subtask<Extension>> tasks = new ArrayList<>();

            for(Seed seed: seeds) {
                var task = scope.fork(() -> swExtendSeed(seed, subject, query, kmerLength, smithWaterman, windowSize));
                tasks.add(task);
            }

            scope.join().throwIfFailed();
            Extension[] extensions = tasks.stream().map(StructuredTaskScope.Subtask::get).toArray(Extension[]::new);

            return filterSmithWaterman(extensions);

        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    // Smith-Waterman returns 2 strings (1 for subject, 1 for query). However, an extension will only have a single string.
    private static Extension swExtendSeed(Seed seed, String subject, String query, int kmerLength, SmithWaterman smithWaterman, int window) {
        int halfWindow = (int) ((window + 1) * 0.5); // Adding 1 to get a rounded up, even number.

        int proposedSStart = seed.subjectIndex() - (halfWindow - kmerLength), proposedQStart = seed.queryIndex() - (halfWindow - kmerLength); // The window should start half the window - kmer length to the left of the seed start point.
        int proposedSEnd = seed.subjectIndex() + (halfWindow + kmerLength), proposedQEnd = seed.queryIndex() + (halfWindow + kmerLength);  // The window should end half the window + kmer length to the right of the seed start point. (1 kmer length the right will be the seed, the next will be the extended window).

        // If the proposedSStart is less than 0, we should shift the window to the right until proposedSStart is 0.
        int actualSStart = 0, actualQStart = 0;
        int actualSEnd = 0, actualQEnd = 0;

        // Subject Start
        if(proposedSStart >= 0) {
            actualSStart = proposedSStart;
        } else {
            // Shift the end to the right by the difference between the start and 0.
            proposedSEnd += (proposedSStart * -1);
        }

        // Query Start
        if (proposedQStart >= 0) {
            actualQStart = proposedQStart;
        } else {
            proposedQEnd += (proposedQStart * -1);
        }

        // Subject End
        if(proposedSEnd < subject.length()) {
            actualSEnd = proposedSEnd;
        } else {
            actualSEnd = subject.length();
            actualSStart -= (proposedSEnd - subject.length());
        }

        // Query End
        if(proposedQEnd < query.length()) {
            actualQEnd = proposedQEnd;
        } else {
            actualQEnd = query.length();
            actualQStart -= (proposedQEnd - query.length());
        }

        // Make sure we haven't pushed actualSStart to be less than 0.
        actualSStart = Math.max(0, actualSStart);
        actualQStart = Math.max(0, actualQStart);

        // If the 'smithWaterman' object has a scoring matrix load, the matrix will be used. Otherwise, the MATCH, MISTMATCH, GAP scores will be used.
        char[][] alignments = smithWaterman.align(subject.substring(actualSStart, actualSEnd).toCharArray(), query.substring(actualQStart, actualQEnd).toCharArray());

        char[] consensusAlignment = findConsensusAlignment(alignments);
        int[] startingPoints = findConsensusStartingPoints(alignments, consensusAlignment, subject, query);

        return new Extension(startingPoints[0], startingPoints[1], new String(consensusAlignment));
    }


    // Create a 'consensus' alignment from the 2 alignments returned from the 'Smith-Waterman' algorithm.
    private static char[] findConsensusAlignment(char[][] alignments) {
        char[] subject = alignments[0];
        char[] query = alignments[1];

        char[] alignment = new char[subject.length];
        int pos = 0;
        for(int i = 0; i < alignment.length; i++) {
            // Consensus
            if(subject[i] == query[i] && subject[i] != '-') alignment[pos++] = subject[i];

            // Favour non-gaps.
            else if(subject[i] == '-' && query[i] != '-') alignment[pos++] = query[i];
            else if(subject[i] != '-' && query[i] == '-') alignment[pos++] = subject[i];

            // Worst-case scenario (both gaps), skip.
        }
        char[] finalAlignment = new char[pos];
        System.arraycopy(alignment, 0, finalAlignment, 0, pos);
        return finalAlignment;
    }

    // At the moment, just returns 0s.
    private static int[] findConsensusStartingPoints(char[][] alignments, char[] consensusAlignment, String subject, String query) {
        return new int[]{0, 0};
    }

    private static Extension[] filterExtensions(Extension[] extensions, int kmerLength) {
        if(extensions == null || extensions.length == 0) return new Extension[0];

        Map<String, Extension> filteredExtensions = new HashMap<>();
        // This should ensure no duplicate alignments are included. Also, only the longest extension for the positions should be kept.
        for(Extension ext: extensions) {
            if(ext.text().length() <= kmerLength) continue; // In this case, no extension was done and just the original seed was returned. Dump it.
            String key = ext.subjectPos() + "," + ext.queryPos();
            filteredExtensions.merge(key, ext, (existing, candidate) -> existing.text().length() >= candidate.text().length() ? existing : candidate);
        }

        List<Extension> results = new ArrayList<>(filteredExtensions.values());
        return results.toArray(new Extension[0]);
    }

    private static Extension[] filterSmithWaterman(Extension[] extensions) {
        return new Extension[0];
    }
}
