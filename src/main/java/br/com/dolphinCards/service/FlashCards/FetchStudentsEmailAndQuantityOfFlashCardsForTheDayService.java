package br.com.dolphinCards.service.FlashCards;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;

import br.com.dolphinCards.DTO.FlashCardsDTO;
import br.com.dolphinCards.errors.Exceptions;
import br.com.dolphinCards.form.FlashCardsForm;
import br.com.dolphinCards.model.Discipline;
import br.com.dolphinCards.model.FlashCard;
import br.com.dolphinCards.model.SendStudentsMail;
import br.com.dolphinCards.model.Student;
import br.com.dolphinCards.repository.DisciplinesRepository;
import br.com.dolphinCards.repository.FlashCardsRepository;
import br.com.dolphinCards.repository.StudentRepository;
import br.com.dolphinCards.service.Students.CheckIfLoggedStudentExistsService;

public class FetchStudentsEmailAndQuantityOfFlashCardsForTheDayService {
    private StudentRepository studentRepository;
    private DisciplinesRepository disciplineRepository;
    private FlashCardsRepository flashCardsRepository;

    public FetchStudentsEmailAndQuantityOfFlashCardsForTheDayService(StudentRepository studentRepository, 
                                   DisciplinesRepository disciplineRepository,
                                   FlashCardsRepository flashCardsRepository
                                ) {
        this.studentRepository = studentRepository;
        this.disciplineRepository = disciplineRepository;
        this.flashCardsRepository = flashCardsRepository;
    }

    public void run() {
        List<?> studentsEmail = studentRepository.findAllStudentEmails();
        studentsEmail.forEach(
            studentObject -> {
                SendStudentsMail student = new SendStudentsMail().objectFieldsToSendStudentsMail(studentObject);
                Long quantityOfFlashCardsForTheDay = flashCardsRepository.countFlashCardsForTheDay(student.getId(), new Date());
                System.out.println(student.getId() + " " + student.getEmail() + " " + quantityOfFlashCardsForTheDay);
            }
        );
    }
}
