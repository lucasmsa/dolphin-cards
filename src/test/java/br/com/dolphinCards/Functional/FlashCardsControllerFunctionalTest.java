package br.com.dolphinCards.Functional;

import java.util.ArrayList;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import br.com.dolphinCards.Factory;
import br.com.dolphinCards.DTO.DisciplineDTO;
import br.com.dolphinCards.DTO.FlashCardsDTO;
import br.com.dolphinCards.DTO.SignInDTO;
import br.com.dolphinCards.DTO.StudentDTO;
import br.com.dolphinCards.errors.Exceptions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import br.com.dolphinCards.form.DisciplinesForm;
import br.com.dolphinCards.form.FlashCardsForm;
import br.com.dolphinCards.form.SignInForm;
import br.com.dolphinCards.form.SignUpForm;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-test.properties")
public class FlashCardsControllerFunctionalTest {
    @Autowired
    private TestRestTemplate testRestTemplate;

    @LocalServerPort
    private int port;

    private Factory factory;

    private HttpHeaders headers = new HttpHeaders();
    
    private SignInForm signInForm;

    @BeforeEach
    public void setUp() {
        factory = new Factory();
        SignUpForm signUpForm = factory.signUpFormBuilder();
        testRestTemplate.postForEntity("/auth/signup", signUpForm, StudentDTO.class);
        signInForm = factory.signInFormBuilder();
        ResponseEntity<SignInDTO> signInDTO = testRestTemplate.postForEntity("/auth/signin", signInForm, SignInDTO.class);
        String token = signInDTO.getBody().getToken();
        headers.setBearerAuth(token);
        testRestTemplate.postForEntity("/discipline", new HttpEntity<>(new DisciplinesForm("Mathematics"), headers), DisciplineDTO.class); 
    } 

    @Test
    @DisplayName("Should create a valid flash card given a valid discipline")
    void shouldCreateAFlashCardSuccessfully(){
        HttpEntity<?> flashCardEntity = new HttpEntity<>(factory.flashCardFormBuilder(), headers);
        
        ResponseEntity<FlashCardsDTO> response = testRestTemplate.postForEntity("/flash-cards", flashCardEntity, FlashCardsDTO.class);
        
        Assertions.assertThat(response.getBody().getQuestion()).isEqualTo("Pi with 2 decimal points");
        Assertions.assertThat(response.getBody().getAnswer()).isEqualTo("3.14");
    }

    @Test
    @DisplayName("Should not create a flash card given an invalid discipline")
    void shouldReturnAnExceptionIfTheDisciplineAssociatedWithTheFlashCardDoesNotExist(){
        HttpEntity<?> flashCardEntity = new HttpEntity<>(new FlashCardsForm("a?", "b", "test"), headers);
        
        ResponseEntity<Exceptions> response = testRestTemplate.postForEntity("/flash-cards", flashCardEntity, Exceptions.class);
        
        Assertions.assertThat(response.getBody().getMessage()).isEqualTo("Discipline with that name does not exist for user!");
    }

    @Test
    @DisplayName("Should delete a flash card related to the user")
    void shouldSuccessfullyDeleteAFlashCard(){
        HttpEntity<?> flashCardEntity = new HttpEntity<>(factory.flashCardFormBuilder(), headers);
        HttpEntity<?> headersEntity = new HttpEntity<Object>(headers);
        ResponseEntity<FlashCardsDTO> flashCardsDTO = testRestTemplate.postForEntity("/flash-cards", flashCardEntity, FlashCardsDTO.class);
        String flashCardIdPath = "/flash-cards/" + flashCardsDTO.getBody().getId();
        ResponseEntity<FlashCardsDTO> fetchFlashCardsDTO = testRestTemplate.exchange(flashCardIdPath, HttpMethod.GET, headersEntity, FlashCardsDTO.class);
        Assertions.assertThat(fetchFlashCardsDTO.getBody().getQuestion()).isEqualTo("Pi with 2 decimal points");
        
        testRestTemplate.exchange(flashCardIdPath, HttpMethod.DELETE, headersEntity, Void.class);
        ResponseEntity<Exceptions> fetchNonExistingFlashCard = testRestTemplate.exchange(flashCardIdPath, HttpMethod.GET, headersEntity, Exceptions.class);

        Assertions.assertThat(fetchNonExistingFlashCard.getBody().getMessage()).isEqualTo("Flash card with this ID does not exist for user");
    }

    @Test
    @DisplayName("Should fetch all available flash cards")
    void shouldSuccessfullyFetchAllFlashCards() {
        HttpEntity<?> flashCardEntity = new HttpEntity<>(factory.flashCardFormBuilder(), headers);
        HttpEntity<?> secondFlashCardEntity = new HttpEntity<>(new FlashCardsForm("test?", "test", "Mathematics"), headers);
        HttpEntity<?> headersEntity = new HttpEntity<Object>(headers);
        testRestTemplate.postForEntity("/flash-cards", flashCardEntity, FlashCardsDTO.class);
        testRestTemplate.postForEntity("/flash-cards", secondFlashCardEntity, FlashCardsDTO.class);
        
        ResponseEntity<FlashCardsDTO[]> fetchFlashCards = testRestTemplate.exchange("/flash-cards", HttpMethod.GET, headersEntity, FlashCardsDTO[].class);

        Assertions.assertThat(fetchFlashCards.getBody().length).isEqualTo(2);
        Assertions.assertThat(fetchFlashCards.getBody()[1].getQuestion()).isEqualTo("test?");
    }
}
