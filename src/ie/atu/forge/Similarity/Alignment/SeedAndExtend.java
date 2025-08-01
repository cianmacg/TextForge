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
    public static Extension[] align(String subject, String query, int kmerLength, SmithWaterman smithWaterman) {   // smithWaterman - controls whether to use Greedy or Smith-Waterman extension. Null = Greedy.
        if(subject == null || subject.isEmpty() || query == null || query.isEmpty() || kmerLength <= 0) return new Extension[0]; // No alignments can be made on empty strings. No seeds can be generated is kmerLength is <= 0.

        List<Seed> seeds = seed(subject, query, kmerLength);
        Extension[] extensions;

        if(smithWaterman != null) {
            extensions = swExtend(seeds, subject, query, kmerLength);
        } else {
            extensions = extend(seeds, subject, query, kmerLength);
        }

        return extensions;
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
    private static Extension[] extend(List<Seed> seeds, String subject, String query, int kmerLength) {
        try(var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            List<StructuredTaskScope.Subtask<Extension>> tasks = new ArrayList<>();

            for(Seed seed: seeds) {
                // Each seed can be checked independently.
                var task = scope.fork(() -> extend(seed, subject, query, kmerLength));
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
    private static Extension extend(Seed seed, String subject, String query, int kmerLength) {
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

    private static Extension[] swExtend(List<Seed> seeds, String subject, String query, int kmerLength) {
        return new Extension[0];
    }

    private static Extension swExtend(Seed seed, String subject, String query, int kmerLength) {
        return new Extension(0, 0, "");
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
