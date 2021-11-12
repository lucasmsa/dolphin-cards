package br.com.dolphinCards.Functional.Karate.utils;

import java.io.BufferedReader;
import java.io.IOException;

public class PrintKarateTestsResults {

    private String divider = "======================";

    public void run(BufferedReader bufferedReader) throws IOException {
        String line = "";
        System.out.println("\n\nBEGINNING KARATE TESTS");
        System.out.println(divider);
        while ((line = bufferedReader.readLine()) != null) {
            System.out.println(line);
        }
        System.out.println(divider);

        return;
    }
}
