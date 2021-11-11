package br.com.dolphinCards.Functional.MockMvc;

import org.junit.jupiter.api.Test;
import com.google.gson.Gson; 
import java.net.URISyntaxException;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import br.com.dolphinCards.Factory;
import br.com.dolphinCards.form.SignInForm;
import br.com.dolphinCards.form.SignUpForm;
import br.com.dolphinCards.utils.FetchKeyFromJsonString;

import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test-h2.properties")
public class StudentsControllerFunctionalTestMockMvc {
    
	@Autowired
	private MockMvc mockMvc;

	private Gson gson;

	private Factory factory;

	private SignUpForm signUpForm;

	private String signUpFormJson;

	@BeforeEach
	public void setUp() throws Exception {
		gson = new Gson();
		factory = new Factory();
		signUpForm = factory.signUpFormBuilder();
		signUpFormJson = gson.toJson(signUpForm);
		
		// Performing Sign Up operation
		mockMvc.perform(MockMvcRequestBuilders
						.post("/auth/signup")
						.content(signUpFormJson)
						.contentType(MediaType.APPLICATION_JSON))
						.andExpect(MockMvcResultMatchers.status().is(201))
						.andExpect(jsonPath("$.name").value(signUpForm.getName()));
	}

	@Test
	public void shouldReturnAnErrorIfUserWithEmailAlreadyExists() throws URISyntaxException, Exception {
		mockMvc.perform(MockMvcRequestBuilders
						.post("/auth/signup")
						.content(signUpFormJson)
						.contentType(MediaType.APPLICATION_JSON))
						.andExpect(MockMvcResultMatchers.status().is(409))
						.andExpect(jsonPath("$.message").value("Student with this e-mail already exists"));
	}

	@Test
	public void shouldSignInAnUserSuccessfully() throws URISyntaxException, Exception {
		SignInForm signInForm = factory.signInFormBuilder();
		String signInJson = gson.toJson(signInForm);

		mockMvc.perform(MockMvcRequestBuilders
						.post("/auth/signin")
						.content(signInJson)
						.contentType(MediaType.APPLICATION_JSON))
						.andExpect(MockMvcResultMatchers.status().is(200))
						.andExpect(jsonPath("$.token").exists());
	}

	@Test
	public void shouldReturnAnErrorWhenTryingToSignInANonExisting() throws URISyntaxException, Exception {
		SignInForm signInForm = factory.realSignInFormBuilder();
		String signInJson = gson.toJson(signInForm);

		mockMvc.perform(MockMvcRequestBuilders
						.post("/auth/signin")
						.content(signInJson)
						.contentType(MediaType.APPLICATION_JSON))
						.andExpect(MockMvcResultMatchers.status().is(401));
	}
}
