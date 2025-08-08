import java.io.*;
import java.util.ArrayList;
import java.util.List;

import ie.atu.forge.Tokenisers.BPE;
import ie.atu.forge.ToolBox.StringExporter;
public class main {
    public static void main(String[] args) throws IOException {
/*        StringBuilder sb = new StringBuilder();
        BufferedReader reader = new BufferedReader(new FileReader("1984.txt"));
        FileReader fr = new FileReader("1984.txt");
        reader.lines().forEach(sb::append);*/


        String test = "Here is some sample text to try training on. It should be good I hope. Give it a go and see. With a little more text we should be good to go. Right about now.";
        BPE tokens = new BPE();
        long startTime = System.nanoTime();
        tokens.train(test, 50);
        long endTime = System.nanoTime();
        System.out.printf("Time: %d ms \n", (endTime - startTime) / 1000000);
/*        tokens.loadVocabFromJson("bpe_vocab_hex.json");
        String test = "A testing string for our BPE implementation.";
        int[] encoding = tokens.encode(test);*/

/*        System.out.println(tokens.decode(encoding));*/
        tokens.saveVocabToJsonHex();
    }
}