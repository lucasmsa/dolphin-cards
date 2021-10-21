package br.com.dolphinCards.FlashCards;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.mockito.Mockito;

import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.mockito.junit.MockitoJUnitRunner;
import org.junit.runner.RunWith;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
import br.com.dolphinCards.service.FlashCards.GetAllStudentFlashCardsService;
import br.com.dolphinCards.service.Students.CheckIfLoggedStudentExistsService;

@SpringBootTest
@SuppressWarnings("unchecked")
@RunWith(MockitoJUnitRunner.Silent.class)
public class GetAllStudentFlashCardsServiceTest {
    private StudentRepository studentRepository;

    private FlashCardsRepository flashCardsRepository;

    private Authentication authentication;

    private SecurityContext securityContext;

    private Factory factory;


	@BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        studentRepository = Mockito.mock(StudentRepository.class);
        flashCardsRepository = Mockito.mock(FlashCardsRepository.class);
        factory = new Factory();

        // Authentication
        authentication = Mockito.mock(Authentication.class);
        securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }   

    @Test
    void shouldReturnAllStudentFlashCardsTest() { 
        Pageable pageable = PageRequest.of(0, 10);
        List<FlashCard> flashCards = factory.listOfFlashCardsBuilder();
        Page<FlashCard> pagedFlashCards = new PageImpl<FlashCard>(flashCards);
        Student student = factory.studentBuilder();
        when(studentRepository.findByEmail(Mockito.any())).thenReturn(Optional.of(student));
        doReturn(pagedFlashCards).when(flashCardsRepository).findAllStudentFlashCards("1", pageable);

        ResponseEntity<?> getAllStudentFlashCardServiceExecution = new GetAllStudentFlashCardsService(studentRepository, flashCardsRepository, pageable).run();

        assertEquals(getAllStudentFlashCardServiceExecution.getStatusCode(), HttpStatus.OK);
        assertEquals(((List<FlashCardsDTO>) getAllStudentFlashCardServiceExecution.getBody()).get(1).getQuestion(), "How to calculate the volume of a cylinder?");
    }
}
