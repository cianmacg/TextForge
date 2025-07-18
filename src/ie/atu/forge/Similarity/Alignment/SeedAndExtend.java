package ie.atu.forge.Similarity.Alignment;

import java.util.*;
import java.util.concurrent.StructuredTaskScope;

// A record for each seed.
record Seed (int queryIndex, int subjectIndex) {}

// A record for each extension. This helps prevent duplicates.
record Extension (int start, String text) {}
/*
    A 2 stage local sequence matching algorithm.
    Begins with a seeding phase where short exact matching sequences are found.
    Extends these seeds later to enlarge the match beyond the size of a kmer.

    This implementation uses a greedy no-gap extension, and returns the full list of matches, filtering out duplicates.
 */
public class SeedAndExtend {
    public static String[] align(String subject, String query, int kmer_length) {
        List<Seed> seeds = seed(subject, query, kmer_length);
        Extension[] extensions = extend(seeds, subject, query, kmer_length);

        Map<Integer, String> filtered_extensions = new HashMap<>();

        // This should ensure no duplicate alignments are included.
        for(Extension ext: extensions) {
            filtered_extensions.merge(ext.start(), ext.text(), (existing, candidate) -> existing.length() >= candidate.length() ? existing : candidate);
        }

        List<String> results = new ArrayList<>(filtered_extensions.values());

        // Also need to filter out any alignment that is a subset of another (not if they are the same length), regardless of starting position.
        results.removeIf(res1 -> results.stream().anyMatch(res2 -> !res1.equals(res2) && res2.contains(res1)));

        return results.toArray(new String[0]);
    }

    // The seeding part should find exact matching parts of the 2 sequences.
    private static List<Seed> seed(String subject, String query, int kmer_length) {
        // The value needs to be a list as the same combination may appear multiple times.
        Map<String, List<Integer>> kmer_map = new HashMap<>();

        // Enter subject kmers into the map for matching.
        for(int i = 0; i <= subject.length() - kmer_length; i++) {
            kmer_map.computeIfAbsent(subject.substring(i, i + kmer_length), v -> new ArrayList<>()).add(i);
        }

        List<Seed> seeds = new ArrayList<>();

        // Find all matches of query kmers with subject map. Make seeds from them.
        for(int i = 0; i <= query.length() - kmer_length; i++) {
            List<Integer> match = kmer_map.get(query.substring(i, i + kmer_length));

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
    private static Extension[] extend(List<Seed> seeds, String subject, String query, int kmer_length) {
        try(var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            List<StructuredTaskScope.Subtask<Extension>> tasks = new ArrayList<>();

            for(Seed seed: seeds) {
                // Each seed can be checked independently.
                var task = scope.fork(() -> extend(seed, subject, query, kmer_length));
                tasks.add(task);
            }

            scope.join().throwIfFailed();

            // Get each string returned by each task and return them as an array.
            return tasks.stream()
                    .map(StructuredTaskScope.Subtask::get)
                    .toArray(Extension[]::new);

        } catch (Exception e) {
            System.err.println("Error: " + e);
            return null;
        }
    }

    // Extends a single seed.
    private static Extension extend(Seed seed, String subject, String query, int kmer_length) {
        int sStart = seed.subjectIndex();
        int qStart = seed.queryIndex();
        int sEnd = sStart + kmer_length - 1;
        int qEnd = qStart + kmer_length - 1;

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
        return new Extension(sStart, subject.substring(sStart, sEnd + 1));
    }
}
