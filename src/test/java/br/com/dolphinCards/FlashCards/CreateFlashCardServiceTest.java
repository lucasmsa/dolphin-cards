package br.com.dolphinCards.FlashCards;

import java.util.Optional;

import org.mockito.Mockito;

import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.when;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.mockito.junit.MockitoJUnitRunner;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import br.com.dolphinCards.Factory;
import br.com.dolphinCards.DTO.FlashCardsDTO;
import br.com.dolphinCards.errors.Exceptions;
import br.com.dolphinCards.form.FlashCardsForm;
import br.com.dolphinCards.model.Discipline;
import br.com.dolphinCards.model.FlashCard;
import br.com.dolphinCards.model.Student;
import br.com.dolphinCards.repository.DisciplinesRepository;
import br.com.dolphinCards.repository.FlashCardsRepository;
import br.com.dolphinCards.repository.StudentRepository;
import br.com.dolphinCards.service.FlashCards.CreateFlashCardService;
import br.com.dolphinCards.service.Students.CheckIfLoggedStudentExistsService;

@SpringBootTest
@RunWith(MockitoJUnitRunner.Silent.class)
public class CreateFlashCardServiceTest {
	private StudentRepository studentRepository;

	private DisciplinesRepository disciplineRepository;

    private FlashCardsRepository flashCardsRepository;

    private CreateFlashCardService createFlashCardService;

    private CheckIfLoggedStudentExistsService loggedStudentExists;

    private Authentication authentication;

    private SecurityContext securityContext;

    private Factory factory;


	@BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        studentRepository = Mockito.mock(StudentRepository.class);
        disciplineRepository = Mockito.mock(DisciplinesRepository.class);
        flashCardsRepository = Mockito.mock(FlashCardsRepository.class);
        loggedStudentExists = Mockito.mock(CheckIfLoggedStudentExistsService.class);
        factory = new Factory();

        // Authentication
        authentication = Mockito.mock(Authentication.class);
        securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }   

    @Test
    void shouldReturnAnErrorIfUserFetchedFromJWTTokenIsNotFoundTest() { 
        FlashCardsForm flashCardsForm = factory.flashCardFormBuilder();
        when(studentRepository.findByEmail(Mockito.any())).thenReturn(Optional.empty());
        when(loggedStudentExists.run()).thenReturn(null);
        
        createFlashCardService = new CreateFlashCardService(studentRepository, disciplineRepository, flashCardsRepository, flashCardsForm);
        ResponseEntity<?> createFlashCardServiceExecution = createFlashCardService.run();
        
        assertEquals(createFlashCardServiceExecution.getStatusCode(), HttpStatus.UNAUTHORIZED);
        assertTrue(((Exceptions) createFlashCardServiceExecution.getBody()).getMessage().equals("Student does not exist"));
    }

    @Test
    void shouldReturnAnErrorIfDisciplineDoesNotExistForTheGivenUserTest() {
        Student student = factory.studentBuilder();
        FlashCardsForm flashCardsForm = factory.flashCardFormBuilder();
        when(studentRepository.findByEmail(Mockito.any())).thenReturn(Optional.of(student));
        when(disciplineRepository.findByDisciplineNameAndStudent("Mathematics", student.getId())).thenReturn(Optional.empty());
        
        createFlashCardService = new CreateFlashCardService(studentRepository, disciplineRepository, flashCardsRepository, flashCardsForm);
        ResponseEntity<?> createFlashCardServiceExecution = createFlashCardService.run();
        
        assertEquals(createFlashCardServiceExecution.getStatusCode(), HttpStatus.NOT_FOUND);
        assertTrue(((Exceptions) createFlashCardServiceExecution.getBody()).getMessage().equals("Discipline with that name does not exist for user!"));
    }

    @Test
    void shouldSuccessfullyCreateAFlashCardTest() {
        Student student = factory.studentBuilder();
        FlashCard flashCard = factory.flashCardBuilder();
        Discipline discipline = factory.disciplineBuilder();
        FlashCardsForm flashCardsForm = factory.flashCardFormBuilder();
        when(studentRepository.findByEmail(Mockito.any())).thenReturn(Optional.of(student));
        when(disciplineRepository.findByDisciplineNameAndStudent("Mathematics", student.getId())).thenReturn(Optional.of(discipline));
        when(flashCardsRepository.save(Mockito.any(FlashCard.class))).thenReturn(flashCard);

        createFlashCardService = new CreateFlashCardService(studentRepository, disciplineRepository, flashCardsRepository, flashCardsForm);
        ResponseEntity<?> createFlashCardServiceExecution = createFlashCardService.run();
        
        assertEquals(createFlashCardServiceExecution.getStatusCode(), HttpStatus.OK);
        assertEquals(((FlashCardsDTO) createFlashCardServiceExecution.getBody()).getQuestion(), flashCardsForm.getQuestion());
        assertEquals(((FlashCardsDTO) createFlashCardServiceExecution.getBody()).getAnswer(), flashCardsForm.getAnswer());
    }
}
