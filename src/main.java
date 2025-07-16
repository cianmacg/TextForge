import java.io.IOException;

import  ie.atu.forge.Similarity.Alignment.NeedlemanWunsch;

public class main {
    public static void main(String[] args) throws IOException {
        NeedlemanWunsch.loadMatrix("./src/ScoringMatrices/BLOSUM45.txt");
    }
}