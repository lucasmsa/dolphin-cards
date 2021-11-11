package br.com.dolphinCards.Functional.MockMvc;

import com.google.gson.Gson; 
import org.junit.jupiter.api.Test;
import java.net.URISyntaxException;
import br.com.dolphinCards.Factory;
import br.com.dolphinCards.DTO.DisciplineDTO;
import static org.junit.Assert.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.http.MediaType;

import br.com.dolphinCards.form.DisciplinesForm;
import br.com.dolphinCards.form.SignInForm;
import br.com.dolphinCards.form.SignUpForm;
import br.com.dolphinCards.model.Discipline;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import br.com.dolphinCards.utils.FetchKeyFromJsonString;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.context.TestPropertySource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test-h2.properties")
public class DisciplinesControllerFuntionalTestMockMvc {
     
	@Autowired
	private MockMvc mockMvc;

	private Gson gson;

	private Factory factory;

    private String token;

	private SignInForm signInForm;

	private FetchKeyFromJsonString fetchKeyFromJsonString;

	@BeforeEach
	public void setUp() throws Exception {
		gson = new Gson();
		factory = new Factory();
		SignUpForm signUpForm = factory.signUpFormBuilder();
        signInForm = factory.signInFormBuilder();
		String signUpFormJson = gson.toJson(signUpForm);
        String signInFormJson = gson.toJson(signInForm);
		fetchKeyFromJsonString = new FetchKeyFromJsonString();
		
		// Performing Sign Up operation
		mockMvc.perform(MockMvcRequestBuilders
						.post("/auth/signup")
						.content(signUpFormJson)
						.contentType(MediaType.APPLICATION_JSON))
						.andExpect(MockMvcResultMatchers.status().is(201))
						.andExpect(jsonPath("$.name").value(signUpForm.getName()));

        // Performing Sign In operation
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                                    .post("/auth/signin")
                                    .content(signInFormJson)
                                    .contentType(MediaType.APPLICATION_JSON))
                                    .andExpect(MockMvcResultMatchers.status().is(200))
                                    .andExpect(jsonPath("$.token").exists())
                                    .andReturn();

        token = fetchKeyFromJsonString.run(result.getResponse().getContentAsString(), "token");
	}

	@Test
	public void shouldCreateANewDisciplineSuccessfully() throws URISyntaxException, Exception {
		DisciplinesForm disciplinesForm = factory.disciplinesFormBuilder();
        String disciplinesFormJson = gson.toJson(disciplinesForm);
        
        mockMvc.perform(MockMvcRequestBuilders
						.post("/discipline")
                        .header("Authorization", "Bearer " + token)
						.content(disciplinesFormJson)
						.contentType(MediaType.APPLICATION_JSON))
						.andExpect(MockMvcResultMatchers.status().is(200))
						.andExpect(jsonPath("$.name").value(disciplinesForm.getName()))
						.andExpect(jsonPath("$.creator.email").value(signInForm.getEmail()));
	}

	@Test
	public void shouldReturnAnErrorIfADisciplineWithThatNameAlreadyExists() throws URISyntaxException, Exception {
		DisciplinesForm disciplinesForm = factory.disciplinesFormBuilder();
        String disciplinesFormJson = gson.toJson(disciplinesForm);
        
        mockMvc.perform(MockMvcRequestBuilders
						.post("/discipline")
                        .header("Authorization", "Bearer " + token)
						.content(disciplinesFormJson)
						.contentType(MediaType.APPLICATION_JSON))
						.andExpect(MockMvcResultMatchers.status().is(200));

		mockMvc.perform(MockMvcRequestBuilders
						.post("/discipline")
                        .header("Authorization", "Bearer " + token)
						.content(disciplinesFormJson)
						.contentType(MediaType.APPLICATION_JSON))
						.andExpect(MockMvcResultMatchers.status().is(409))
						.andExpect(jsonPath("$.message").value("Discipline with that name already exists for user!"));
	}

	@Test
	public void shouldReturnTheUserDisciplineWhenItExists() throws URISyntaxException, Exception {
		DisciplinesForm disciplinesForm = factory.disciplinesFormBuilder();
        String disciplinesFormJson = gson.toJson(disciplinesForm);
        
        mockMvc.perform(MockMvcRequestBuilders
						.post("/discipline")
						.header("Authorization", "Bearer " + token)
						.content(disciplinesFormJson)
						.contentType(MediaType.APPLICATION_JSON))
						.andExpect(MockMvcResultMatchers.status().is(200));

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders
									.get("/discipline")
									.header("Authorization", "Bearer " + token)
									.contentType(MediaType.APPLICATION_JSON))
									.andExpect(MockMvcResultMatchers.status().is(200))
									.andReturn();

		DisciplineDTO[] response = new Gson().fromJson(result.getResponse().getContentAsString(), DisciplineDTO[].class);
		
		assertEquals(response[0].getName(), disciplinesForm.getName());
	}
}
