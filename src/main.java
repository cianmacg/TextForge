import java.io.IOException;

import ie.atu.forge.Similarity.Alignment.SeedAndExtend;
import ie.atu.forge.Similarity.Alignment.Extension;

public class main {
    public static void main(String[] args) throws IOException {
        String s1 = "ABBACDDABBABCDCDA";
        String s2 = "ACCDDBABBADGHDCDA";

        Extension[] exts = SeedAndExtend.align(s1, s2, 2);
        for(Extension ext: exts) {
            System.out.printf("Start: %d, Alignment: %s \n", ext.start(), ext.text());
        }
    }
}