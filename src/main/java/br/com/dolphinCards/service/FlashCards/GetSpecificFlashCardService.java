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

public class GetSpecificFlashCardService {
    private StudentRepository studentRepository;
    private DisciplinesRepository disciplineRepository;
    private FlashCardsRepository flashCardsRepository;
    private String flashCardId;


    public GetSpecificFlashCardService(StudentRepository studentRepository, 
                                       DisciplinesRepository disciplineRepository,
                                       FlashCardsRepository flashCardsRepository,
                                       String flashCardId
                                        ) {
        this.studentRepository = studentRepository;
        this.disciplineRepository = disciplineRepository;
        this.flashCardsRepository = flashCardsRepository;
        this.flashCardId = flashCardId;
    }

    public FlashCardsDTO run() {
        Optional<Student> optionalStudent = new CheckIfLoggedStudentExistsService().run(studentRepository);
        if (optionalStudent == null) return null;
        Student student = optionalStudent.get();
        Optional<FlashCard> optionalFlashCard = flashCardsRepository.findStudentFlashCardById(student.getId(), flashCardId);
        if (!optionalFlashCard.isPresent()) return null;
        FlashCard flashCard = optionalFlashCard.get();
                         
        return new FlashCardsDTO(flashCard, flashCard.getDiscipline(), flashCard.getDiscipline().getStudent(), false, false);
    }
}
