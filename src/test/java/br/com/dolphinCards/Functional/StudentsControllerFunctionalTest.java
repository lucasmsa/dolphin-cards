package br.com.dolphinCards.Functional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.com.dolphinCards.Factory;
import br.com.dolphinCards.DTO.SignInDTO;
import br.com.dolphinCards.DTO.StudentDTO;
import br.com.dolphinCards.errors.Exceptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import br.com.dolphinCards.form.SignInForm;
import br.com.dolphinCards.form.SignUpForm;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-test.properties")
class StudentsControllerFunctionalTest {
    @Autowired
    private TestRestTemplate testRestTemplate;

    @LocalServerPort
    private int port;

    private Factory factory;

    @BeforeEach
    public void setUp() {
        factory = new Factory();
    } 

    @Test
    @DisplayName("Signs up a student")
    void shouldSignUpAStudentSuccessfully() {
        SignUpForm signUpForm = factory.signUpFormBuilder();
        
        ResponseEntity<StudentDTO> response = testRestTemplate.postForEntity("/auth/signup", signUpForm, StudentDTO.class);
        
        Assertions.assertThat(response.getBody().getEmail()).isEqualTo("test@gmail.com");
    }

    @Test
    @DisplayName("Should throw an error when trying to add a student that already exists")
    void shouldReturnAnExceptionIfAStudentWithTheFormEmailAlreadyExists(){
        SignUpForm signUpForm = factory.signUpFormBuilder();
        testRestTemplate.postForEntity("/auth/signup", signUpForm, StudentDTO.class);
        
        ResponseEntity<Exceptions> response = testRestTemplate.postForEntity("/auth/signup", signUpForm, Exceptions.class);

        Assertions.assertThat(response.getBody().getMessage()).isEqualTo("Student with this e-mail already exists");
    }

    @Test
    @DisplayName("Should sign in an user successfully")
    void shouldSignInAnUserSuccessfully() {
        SignUpForm signUpForm = factory.signUpFormBuilder();
        testRestTemplate.postForEntity("/auth/signup", signUpForm, StudentDTO.class);
        SignInForm signInForm = factory.signInFormBuilder();
        
        ResponseEntity<SignInDTO> response = testRestTemplate.postForEntity("/auth/signin", signInForm, SignInDTO.class);

        Assertions.assertThat(response.getBody().getStudent().get("email")).isEqualTo("test@gmail.com");
    }
}
