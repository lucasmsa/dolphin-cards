package br.com.dolphinCards.Functional.Karate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application.properties")
public class KarateTestsRunner {
    private String karateConfigurationsPath = "src/test/java/br/com/dolphinCards/Functional/Karate/configurations/";

    private PrintKarateTestsResults printKarateTestsResults;

    @BeforeEach
    public void setUp() {
        printKarateTestsResults = new PrintKarateTestsResults();
    } 

    @Test
    @DisplayName("Should create a new student")
    void shouldSignUpAnUserSuccessfully() throws IOException, InterruptedException{
        String cmd = "java -jar " + karateConfigurationsPath + "/karate.jar " + karateConfigurationsPath + "features/SignUpPOST.feature";
        System.out.println("COMMAND " + cmd);
        Runtime run = Runtime.getRuntime();
        Process pr = run.exec(cmd);
        pr.waitFor();
        BufferedReader buf = new BufferedReader(new InputStreamReader(pr.getInputStream()));
        printKarateTestsResults.run(buf);
    }
}
