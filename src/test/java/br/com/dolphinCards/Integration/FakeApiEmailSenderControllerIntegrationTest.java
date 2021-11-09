package br.com.dolphinCards.Integration;

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
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import br.com.dolphinCards.form.DisciplinesForm;
import br.com.dolphinCards.form.SignInForm;
import br.com.dolphinCards.form.SignUpForm;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-test-fake-api.properties")
public class FakeApiEmailSenderControllerIntegrationTest {
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
        SignUpForm realSignUpForm = factory.realSignUpFormBuilder();
        SignUpForm adminSignUpForm = factory.adminSignUpFormBuilder();
        testRestTemplate.postForEntity("/auth/signup", realSignUpForm, StudentDTO.class);
        testRestTemplate.postForEntity("/auth/signup", adminSignUpForm, StudentDTO.class);
    } 

    @Test
    @DisplayName("Should return an exception if the API is not available")
    void shouldReturnAnExceptionIfTheApiIsNotAvailable() {
        signInForm = factory.adminSignInFormBuilder();
        ResponseEntity<SignInDTO> signInDTO = testRestTemplate.postForEntity("/auth/signin", signInForm, SignInDTO.class);
        String token = signInDTO.getBody().getToken();
        headers.setBearerAuth(token); 
        HttpEntity<?> headersEntity = new HttpEntity<>(headers);

        ResponseEntity<Exceptions> connectionRefusedException = testRestTemplate.postForEntity("/send-mail", headersEntity, Exceptions.class);

        Assertions.assertThat(connectionRefusedException.getBody().getMessage()).isEqualTo("Mail sender API is not available");
        Assertions.assertThat(connectionRefusedException.getBody().getStatus()).isEqualTo(503);
    }
}
