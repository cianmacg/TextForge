package ie.atu.forge.ToolBox;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class StringExporter {
    public static void toFile(String output, String path) throws IOException {
        if(output.isEmpty()) return;

        BufferedWriter writer = new BufferedWriter(new FileWriter(path + "Output.txt"));
        writer.write(output);
        writer.close();
    }

    public static void toFile(String[] output, String path) throws IOException {
        if(output.length == 0) return;

        StringBuilder sb = new StringBuilder();
        sb.append(output[0]);

        for(int i = 1; i < output.length; i++) {
            sb.append(" ");
            sb.append(output[i]);
        }

        toFile(sb.toString(), path);
    }

    public static void toFile(String output) throws IOException {
        toFile(output, "");
    }

    public static void toFile(String[] output) throws IOException {
        toFile(output, "");
    }
}
