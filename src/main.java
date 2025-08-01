import java.io.*;
import java.util.ArrayList;
import java.util.List;

import ie.atu.forge.Tokenisers.BPE;
import ie.atu.forge.ToolBox.StringExporter;
public class main {
    public static void main(String[] args) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = new BufferedReader(new FileReader("1984.txt"));
        FileReader fr = new FileReader("1984.txt");
        reader.lines().forEach(sb::append);

        BPE tokens = new BPE();
        long startTime = System.nanoTime();
        tokens.train(sb.toString(), 2000);
        long endTime = System.nanoTime();
        System.out.printf("Time: %d \n", (endTime - startTime) / 1000000);
/*        tokens.loadVocabFromJson("bpe_vocab.json");
        String test = "A testing string for our BPE implementation.";
        int[] encoding = tokens.encode(test);*/

/*        System.out.println(tokens.decode(encoding));*/
    }
}