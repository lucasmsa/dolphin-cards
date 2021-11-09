package br.com.dolphinCards.Unitary.Disciplines;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import br.com.dolphinCards.Factory;
import br.com.dolphinCards.DTO.DisciplineDTO;
import br.com.dolphinCards.errors.Exceptions;
import br.com.dolphinCards.form.DisciplinesForm;
import br.com.dolphinCards.model.Discipline;
import br.com.dolphinCards.model.Student;
import br.com.dolphinCards.repository.DisciplinesRepository;
import br.com.dolphinCards.repository.StudentRepository;
import br.com.dolphinCards.service.Disciplines.CreateDisciplineService;

@SpringBootTest
@RunWith(MockitoJUnitRunner.Silent.class)
public class CreateDisciplineServiceTest {
    private StudentRepository studentRepository;

	private DisciplinesRepository disciplineRepository;

    private CreateDisciplineService createDisciplineService;

    private Authentication authentication;

    private SecurityContext securityContext;

    private Factory factory;


	@BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        studentRepository = Mockito.mock(StudentRepository.class);
        disciplineRepository = Mockito.mock(DisciplinesRepository.class);
        factory = new Factory();

        // Authentication
        authentication = Mockito.mock(Authentication.class);
        securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }   

    @Test
    void shouldReturnAnErrorIfDisciplineWithThatNameAlreadyExistsForUserTest() {
        Student student = factory.studentBuilder(); 
        Discipline discipline = factory.disciplineBuilder();
        DisciplinesForm disciplinesForm = new DisciplinesForm("Mathematics");
        when(studentRepository.findByEmail(Mockito.any())).thenReturn(Optional.of(student));
        when(disciplineRepository.findByDisciplineNameAndStudent("Mathematics", student.getId())).thenReturn(Optional.of(discipline));

        createDisciplineService = new CreateDisciplineService(studentRepository, disciplineRepository, disciplinesForm);
        ResponseEntity<?> createDisciplineServiceExecution = createDisciplineService.run();
        
        assertEquals(createDisciplineServiceExecution.getStatusCode(), HttpStatus.CONFLICT);
        assertTrue(((Exceptions) createDisciplineServiceExecution.getBody()).getMessage().equals("Discipline with that name already exists for user!"));
    }

    @Test
    void shouldSuccessfullyCreateANewDisciplineTest() {
        Student student = factory.studentBuilder(); 
        Discipline discipline = factory.disciplineBuilder();
        DisciplinesForm disciplinesForm = new DisciplinesForm("Mathematics");
        when(studentRepository.findByEmail(Mockito.any())).thenReturn(Optional.of(student));
        when(disciplineRepository.findByDisciplineNameAndStudent("Mathematics", student.getId())).thenReturn(Optional.empty());
        when(disciplineRepository.save(Mockito.any(Discipline.class))).thenReturn(discipline);

        createDisciplineService = new CreateDisciplineService(studentRepository, disciplineRepository, disciplinesForm);
        ResponseEntity<?> createDisciplineServiceExecution = createDisciplineService.run();
        
        assertEquals(createDisciplineServiceExecution.getStatusCode(), HttpStatus.OK);
        assertTrue(((DisciplineDTO) createDisciplineServiceExecution.getBody()).getName().equals("Mathematics"));
    }
}
