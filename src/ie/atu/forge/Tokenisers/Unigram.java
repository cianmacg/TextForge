package ie.atu.forge.Tokenisers;

import java.util.HashSet;
import java.util.Set;

public class Unigram {
    public String[] Unigram(String[] tokens, int target) {
        Set<String> vocabulary = new HashSet<>();
        Set<String> base = new HashSet<>();
        
        for(String token: tokens) {
            if(token.length() > 1) {
                vocabulary.add(token);
            }
            else{
                base.add(token);
            }
        }

        while(vocabulary.size() > target) {
            
        }

        String[] reduced = new String[vocabulary.size() + base.size()];
        System.arraycopy(base.toArray(), 0, reduced, 0, base.size());
        System.arraycopy(vocabulary.toArray(), 0, reduced, base.size(), vocabulary.size());

        return reduced;
    }

    // Log-likelihood loss
    private double calc_loss() {
        double loss = 0.0;
        return loss;
    }
}
