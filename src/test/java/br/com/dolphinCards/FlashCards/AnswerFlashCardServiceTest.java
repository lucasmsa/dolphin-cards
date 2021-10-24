package br.com.dolphinCards.FlashCards;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.util.Date;
import java.util.Optional;

import org.apache.commons.lang3.time.DateUtils;
import org.joda.time.DateTime;
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
import br.com.dolphinCards.DTO.FlashCardsDTO;
import br.com.dolphinCards.errors.Exceptions;
import br.com.dolphinCards.form.AnswerFlashCardForm;
import br.com.dolphinCards.model.FlashCard;
import br.com.dolphinCards.model.Student;
import br.com.dolphinCards.repository.FlashCardsRepository;
import br.com.dolphinCards.repository.StudentRepository;
import br.com.dolphinCards.service.FlashCards.AnswerFlashCardService;

@SpringBootTest
@RunWith(MockitoJUnitRunner.Silent.class)
public class AnswerFlashCardServiceTest {
    private StudentRepository studentRepository;

    private FlashCardsRepository flashCardsRepository;

    private AnswerFlashCardService answerFlashCardService;

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
    void shouldReturnAnErrorIfTheProvidedFlashCardIdDoesNotBelongToTheUserTest() {
        Student student = factory.studentBuilder();
        AnswerFlashCardForm answerFlashCardForm = new AnswerFlashCardForm("EASY");
        when(studentRepository.findByEmail(Mockito.any())).thenReturn(Optional.of(student));
        when(flashCardsRepository.findStudentFlashCardById("1", "1")).thenReturn(Optional.empty());
        
        answerFlashCardService = new AnswerFlashCardService(studentRepository, flashCardsRepository, "1", answerFlashCardForm);
        ResponseEntity<?> answerFlashCardServiceExecution = answerFlashCardService.run();
        
        assertEquals(answerFlashCardServiceExecution.getStatusCode(), HttpStatus.NOT_FOUND);
        assertTrue(((Exceptions) answerFlashCardServiceExecution.getBody()).getMessage().equals("Flash card with that id does not exist for user"));
    }

    @Test
    void shouldAddSevenDaysForTheNextAnswerDateOnEasyQuestionForTheFirstFlashCardAnswerTest() throws ParseException {
        Student student = factory.studentBuilder();
        FlashCard flashCard = factory.flashCardBuilder();
        AnswerFlashCardForm answerFlashCardForm = new AnswerFlashCardForm("EASY");
        Date easyNextAnswerDate = new DateTime(flashCard.getNextAnswerDate()).plusDays(7).toDate();
        when(studentRepository.findByEmail(Mockito.any())).thenReturn(Optional.of(student));
        when(flashCardsRepository.findStudentFlashCardById("1", "1")).thenReturn(Optional.of(flashCard));
        when(flashCardsRepository.save(flashCard)).thenReturn(flashCard);
        
        answerFlashCardService = new AnswerFlashCardService(studentRepository, flashCardsRepository, "1", answerFlashCardForm);
        ResponseEntity<?> answerFlashCardServiceExecution = answerFlashCardService.run();
        
        assertTrue(DateUtils.isSameDay(((FlashCardsDTO) answerFlashCardServiceExecution.getBody()).nextAnswerDateStringToDate(), easyNextAnswerDate));
    }

    
    @Test
    void shouldAddThreeDaysForTheNextAnswerDateOnHardQuestionForTheFirstFlashCardAnswerTest() throws ParseException {
        Student student = factory.studentBuilder();
        FlashCard flashCard = factory.flashCardBuilder();
        AnswerFlashCardForm answerFlashCardForm = new AnswerFlashCardForm("HARD");
        Date hardNextAnswerDate = new DateTime(flashCard.getNextAnswerDate()).plusDays(3).toDate();
        when(studentRepository.findByEmail(Mockito.any())).thenReturn(Optional.of(student));
        when(flashCardsRepository.findStudentFlashCardById("1", "1")).thenReturn(Optional.of(flashCard));
        when(flashCardsRepository.save(flashCard)).thenReturn(flashCard);
        
        answerFlashCardService = new AnswerFlashCardService(studentRepository, flashCardsRepository, "1", answerFlashCardForm);
        ResponseEntity<?> answerFlashCardServiceExecution = answerFlashCardService.run();
        
        assertTrue(DateUtils.isSameDay(((FlashCardsDTO) answerFlashCardServiceExecution.getBody()).nextAnswerDateStringToDate(), hardNextAnswerDate));
    }
    
    @Test
    void shouldAddOneDayForTheNextAnswerDateOnWrongQuestionForTheFirstFlashCardAnswerTest() throws ParseException {
        Student student = factory.studentBuilder();
        FlashCard flashCard = factory.flashCardBuilder();
        AnswerFlashCardForm answerFlashCardForm = new AnswerFlashCardForm("WRONG");
        Date wrongNextAnswerDate = new DateTime(flashCard.getNextAnswerDate()).plusDays(1).toDate();
        when(studentRepository.findByEmail(Mockito.any())).thenReturn(Optional.of(student));
        when(flashCardsRepository.findStudentFlashCardById("1", "1")).thenReturn(Optional.of(flashCard));
        when(flashCardsRepository.save(flashCard)).thenReturn(flashCard);
        
        answerFlashCardService = new AnswerFlashCardService(studentRepository, flashCardsRepository, "1", answerFlashCardForm);
        ResponseEntity<?> answerFlashCardServiceExecution = answerFlashCardService.run();
        
        assertTrue(DateUtils.isSameDay(((FlashCardsDTO) answerFlashCardServiceExecution.getBody()).nextAnswerDateStringToDate(), wrongNextAnswerDate));
    }

    @Test
    void shouldNotAddAnyDaysForTheNextAnswerDateIfTheFlashCardNextAnswerDateIsAfterTodayTest() throws ParseException {
        Student student = factory.studentBuilder();
        FlashCard flashCard = factory.futureFlashCardBuilder();
        AnswerFlashCardForm answerFlashCardForm = new AnswerFlashCardForm("EASY");
        Date sameNextAnswerDate = flashCard.getNextAnswerDate();
        when(studentRepository.findByEmail(Mockito.any())).thenReturn(Optional.of(student));
        when(flashCardsRepository.findStudentFlashCardById("1", "1")).thenReturn(Optional.of(flashCard));
        
        answerFlashCardService = new AnswerFlashCardService(studentRepository, flashCardsRepository, "1", answerFlashCardForm);
        ResponseEntity<?> answerFlashCardServiceExecution = answerFlashCardService.run();
        
        assertTrue(DateUtils.isSameDay(((FlashCardsDTO) answerFlashCardServiceExecution.getBody()).nextAnswerDateStringToDate(), sameNextAnswerDate));
    }
}
