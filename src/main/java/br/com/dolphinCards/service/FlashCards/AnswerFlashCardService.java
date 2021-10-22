package br.com.dolphinCards.service.FlashCards;

import java.util.Date;
import java.util.Optional;

import org.joda.time.DateTime;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import br.com.dolphinCards.DTO.FlashCardsDTO;
import br.com.dolphinCards.constants.PlusDaysConstants;
import br.com.dolphinCards.errors.Exceptions;
import br.com.dolphinCards.form.AnswerFlashCardForm;
import br.com.dolphinCards.model.FlashCard;
import br.com.dolphinCards.model.Student;
import br.com.dolphinCards.repository.FlashCardsRepository;
import br.com.dolphinCards.repository.StudentRepository;
import br.com.dolphinCards.service.Students.CheckIfLoggedStudentExistsService;

public class AnswerFlashCardService {
    private StudentRepository studentRepository;
    private FlashCardsRepository flashCardsRepository;
    private String flashCardId;
    private AnswerFlashCardForm answerFlashCardForm;

    public AnswerFlashCardService(StudentRepository studentRepository, 
                                   FlashCardsRepository flashCardsRepository,
                                   String flashCardId,
                                   AnswerFlashCardForm answerFlashCardForm) {
        this.studentRepository = studentRepository;
        this.flashCardsRepository = flashCardsRepository;
        this.flashCardId = flashCardId;
        this.answerFlashCardForm = answerFlashCardForm;
    }

    public ResponseEntity<?> run() {
        Optional<Student> optionalStudent = new CheckIfLoggedStudentExistsService(studentRepository).run();
        if (optionalStudent == null) return new Exceptions().jwtUserTokenError();

        Student student = optionalStudent.get();

        Optional<FlashCard> optionalFlashCard = flashCardsRepository.findStudentFlashCardById(student.getId(), flashCardId);
        if (!optionalFlashCard.isPresent()) return new Exceptions("Flash card with that id does not exist for user", 404).throwException();
        
        FlashCard flashCard = optionalFlashCard.get();

        Date originalFlashCardNextAnswerDate = flashCard.getNextAnswerDate();
        boolean nextAnswerDateTodayOrBefore = originalFlashCardNextAnswerDate.equals(new Date()) || originalFlashCardNextAnswerDate.before(new Date());
        
        if (nextAnswerDateTodayOrBefore) {
            Integer timesAnswered = flashCard.getTimesAnswered() == 0 ? 1 : flashCard.getTimesAnswered();
            Integer daysUntilNextAnswer = Math.round(PlusDaysConstants.TYPE.get(answerFlashCardForm.getAnswerType())*timesAnswered);
            Date newNextAnswerDate = new DateTime(originalFlashCardNextAnswerDate).plusDays(daysUntilNextAnswer).toDate();
            flashCard.setNextAnswerDate(newNextAnswerDate);
            flashCard.setTimesAnswered(flashCard.getTimesAnswered() + 1);
        }

        FlashCard savedFlashCard = nextAnswerDateTodayOrBefore ? flashCardsRepository.save(flashCard) : flashCard;

        FlashCardsDTO flashCardsDTO = new FlashCardsDTO(savedFlashCard, savedFlashCard.getDiscipline(), savedFlashCard.getDiscipline().getStudent(), false, false);
        return ResponseEntity.ok().body(flashCardsDTO);
    }
}
