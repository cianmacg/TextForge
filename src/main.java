import java.io.*;
import ie.atu.forge.Similarity.Alignment.*;
public class main {
    public static void main(String[] args) throws IOException {
        String s1 = "casting";
        String s2 = "natsigc";


        double distance = Jaro.similarity(s1, s2);
        System.out.println(distance);
    }
}