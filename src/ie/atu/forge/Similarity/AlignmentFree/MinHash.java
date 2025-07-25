package ie.atu.forge.Similarity.AlignmentFree;

import java.util.Random;
import java.util.Set;

// This class assumes the sets are not of numbers (e.g. Strings), and uses hashCode() to convert to integers. There are better hashing functions I should use that will give more even distributions.
public class MinHash {
    private int k; // The number of hashing functions.
    private int[][] funcs;
    private int prime = 2147483647; // Largest prime number for 32-bit int.

    public MinHash(int numHashes) {
        k = numHashes;
        generate_funcs();
    }

    public <T> double jaccard(Set<T> s1, Set<T> s2) {
        if(s1.isEmpty() && s2.isEmpty()) return 1.0d;
        if(s1.isEmpty() || s2.isEmpty()) return 0.0d;

        int[] v1 = minHashSet(s1), v2 = minHashSet(s2);
        int count = 0;

        for(int i = 0; i < k; i++) {
            if(v1[i] == v2[i]) count++;
        }

        return (double) count / k;
    }

    public <T> double sorensenDice(Set<T> s1, Set<T> s2) {
        if(s1.isEmpty() && s2.isEmpty()) return 1.0d;
        if(s1.isEmpty() || s2.isEmpty()) return 0.0d;

        double jac = jaccard(s1, s2);

        return (2 * jac) / (1 + jac);
    }

    public void regenerate_funcs(int numHashes) {
        k = numHashes;
        generate_funcs();
    }

    private void generate_funcs() {
        funcs = new int[k][2]; // Each function will have an a and b values (hence the 2).
        Random rand = new Random();

        for(int i = 0; i < k; i++) {
            funcs[i][0] = rand.nextInt(prime - 1) + 1; // Ensures non-0 value.
            funcs[i][1] = rand.nextInt(prime);
        }
    }

    private <T> int[] minHashSet(Set<T> s) {
        int[] hashes = new int[k];

        // Initialise every hash to the maximum possible value.
        for(int i = 0; i < k; i++) {
            hashes[i] = Integer.MAX_VALUE;
        }

        for(Object el: s) {
            for(int i = 0; i < k; i++) {
                hashes[i] = Math.min(hashes[i], apply_func(funcs[i][0], funcs[i][1], el.hashCode()));
            }
        }

        return hashes;
    }


    private int apply_func(int a, int b, int val) {
        long hash = ((long) a * val + b) % prime;

        return (int) hash;
    }
}
