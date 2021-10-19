package br.com.dolphinCards.service.FlashCards;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.joda.time.DateTime;
import org.springframework.security.core.context.SecurityContextHolder;

import br.com.dolphinCards.DTO.DisciplineDTO;
import br.com.dolphinCards.DTO.FlashCardsDTO;
import br.com.dolphinCards.DTO.StudentDTO;
import br.com.dolphinCards.constants.PlusDaysConstants;
import br.com.dolphinCards.form.AnswerFlashCardForm;
import br.com.dolphinCards.form.DisciplinesForm;
import br.com.dolphinCards.form.FlashCardsForm;
import br.com.dolphinCards.model.Discipline;
import br.com.dolphinCards.model.FlashCard;
import br.com.dolphinCards.model.Student;
import br.com.dolphinCards.repository.DisciplinesRepository;
import br.com.dolphinCards.repository.FlashCardsRepository;
import br.com.dolphinCards.repository.StudentRepository;
import br.com.dolphinCards.service.Students.CheckIfLoggedStudentExistsService;

public class AnswerFlashCardService {
    private StudentRepository studentRepository;
    private DisciplinesRepository disciplineRepository;
    private FlashCardsRepository flashCardsRepository;
    private String flashCardId;
    private AnswerFlashCardForm answerFlashCardForm;

    public AnswerFlashCardService(StudentRepository studentRepository, 
                                   DisciplinesRepository disciplineRepository,
                                   FlashCardsRepository flashCardsRepository,
                                   String flashCardId,
                                   AnswerFlashCardForm answerFlashCardForm) {
        this.studentRepository = studentRepository;
        this.disciplineRepository = disciplineRepository;
        this.flashCardsRepository = flashCardsRepository;
        this.flashCardId = flashCardId;
        this.answerFlashCardForm = answerFlashCardForm;
    }

    public FlashCardsDTO run() {
        Optional<Student> optionalStudent = new CheckIfLoggedStudentExistsService().run(studentRepository);
        if (optionalStudent == null) return null;
        Student student = optionalStudent.get();
        Optional<FlashCard> optionalFlashCard = flashCardsRepository.findStudentFlashCardById(student.getId(), flashCardId);
        if (!optionalFlashCard.isPresent()) return null;
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

        FlashCard savedFlashCard = nextAnswerDateTodayOrBefore 
                                    ? flashCardsRepository.save(flashCard)
                                    : flashCard;
                                    
        return new FlashCardsDTO(savedFlashCard, savedFlashCard.getDiscipline(), savedFlashCard.getDiscipline().getStudent(), false, false);
    }
}
