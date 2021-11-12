package br.com.dolphinCards.Integration.ExternalApi;

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
import br.com.dolphinCards.model.MailResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-test.properties")
public class EmailSenderControllerIntegrationTest {
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
    @DisplayName("Should not be able to send e-mail if the user is not admin")
    void shouldReturnAnExceptionIfTheUserIsNotAdmin() {
        signInForm = factory.realSignInFormBuilder();
        ResponseEntity<SignInDTO> signInDTO = testRestTemplate.postForEntity("/auth/signin", signInForm, SignInDTO.class);
        String token = signInDTO.getBody().getToken();
        headers.setBearerAuth(token); 
        HttpEntity<?> headersEntity = new HttpEntity<>(headers);

        ResponseEntity<Exceptions> userNotAllowedException = testRestTemplate.postForEntity("/send-mail", headersEntity, Exceptions.class);

        Assertions.assertThat(userNotAllowedException.getBody().getStatus()).isEqualTo(403);
        Assertions.assertThat(userNotAllowedException.getBody().getMessage()).isEqualTo("You do not have permission to send requests to this endpoint");
    }

    @Test
    @DisplayName("Should successfully send e-mails to all users that are not admin")
    void shouldSuccessfullySendEmailsToUsers() {
        SignInForm adminSignInForm = factory.adminSignInFormBuilder();
        ResponseEntity<SignInDTO> signInDTO = testRestTemplate.postForEntity("/auth/signin", adminSignInForm, SignInDTO.class);
        String token = signInDTO.getBody().getToken();
        headers.setBearerAuth(token); 
        HttpEntity<?> headersEntity = new HttpEntity<>(headers);

        ResponseEntity<MailResponse> mailResponse = testRestTemplate.postForEntity("/send-mail", headersEntity, MailResponse.class);

        Assertions.assertThat(mailResponse.getBody().getStatus()).isEqualTo(200);
        Assertions.assertThat(mailResponse.getBody().getMessage()).isEqualTo("All e-mails were sent successfully ㊣");
    }
}
