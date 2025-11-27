package main.java.ie.atu.forge.ToolBox;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Used to write a string to a text file.
 */
public class StringExporter {
    /**
     * Writes a single string to a text file at the specified directory path.
     *
     * @param output The text to be written to the file.
     * @param path The directory to save the file at.
     * @throws IOException
     */
    public static void toFile(String output, String path) throws IOException {
        if(output.isEmpty()) return;

        BufferedWriter writer = new BufferedWriter(new FileWriter(path + "Output.txt"));
        writer.write(output);
        writer.close();
    }

    /**
     * Writes an array of string to a text file, joining them with a space.
     *
     * @param output The text to be written to the file.
     * @param path The directory to save the file at.
     * @throws IOException
     */
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

    /**
     * Writes a single string to a text file in the current working directory.
     *
     * @param output The text to be written to the file.
     * @throws IOException
     */
    public static void toFile(String output) throws IOException {
        toFile(output, "");
    }

    /**
     * Writes an array of string to a text file, joining them with a space.
     *
     * @param output The text to be written to the file.
     * @throws IOException
     */
    public static void toFile(String[] output) throws IOException {
        toFile(output, "");
    }
}
