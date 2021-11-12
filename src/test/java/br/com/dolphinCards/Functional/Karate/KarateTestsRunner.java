package br.com.dolphinCards.Functional.Karate;

import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import br.com.dolphinCards.Functional.Karate.utils.AdjustTerminalConfigurationsAndRunCommand;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application.properties")
public class KarateTestsRunner {
    private String karateConfigurationsPath = "src/test/java/br/com/dolphinCards/Functional/Karate/configurations/";

    private AdjustTerminalConfigurationsAndRunCommand adjustTerminalConfigurationsAndRunCommand;

    @BeforeEach
    public void setUp() {
        adjustTerminalConfigurationsAndRunCommand = new AdjustTerminalConfigurationsAndRunCommand();
    } 

    @Test
    @DisplayName("Should create a new student, should return a conflict error when trying to create an existing user")
    void signUpKarateTests() throws IOException, InterruptedException{
        adjustTerminalConfigurationsAndRunCommand.run("java -jar " + karateConfigurationsPath + "/karate.jar " + karateConfigurationsPath + "features/SignUnPOST.feature");
    }

    @Test
    @DisplayName("Should sign in an user, should return forbidden if user does not exist")
    void signInKarateTests() throws IOException, InterruptedException{
        adjustTerminalConfigurationsAndRunCommand.run("java -jar " + karateConfigurationsPath + "/karate.jar " + karateConfigurationsPath + "features/SignInPOST.feature");
    }

    @Test
    @DisplayName("Should create a new Discipline, return conflict if another one with the same name already exists, return validation errors when passing an invalid discipline name, delete a discipline")
    void disciplinesKarateTests() throws IOException, InterruptedException{
        adjustTerminalConfigurationsAndRunCommand.run("java -jar " + karateConfigurationsPath + "/karate.jar " + karateConfigurationsPath + "features/Disciplines-CRUD.feature");
    }

    @Test
    @DisplayName("Should create, delete, get and answer flash cards")
    public void flashCardsKarateTests() throws IOException, InterruptedException{
        adjustTerminalConfigurationsAndRunCommand.run("java -jar " + karateConfigurationsPath + "/karate.jar " + karateConfigurationsPath + "features/FlashCards-CRUD.feature");
    }
}
