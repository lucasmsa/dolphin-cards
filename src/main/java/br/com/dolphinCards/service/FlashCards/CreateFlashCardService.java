package br.com.dolphinCards.service.FlashCards;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;

import br.com.dolphinCards.DTO.FlashCardsDTO;
import br.com.dolphinCards.errors.Exceptions;
import br.com.dolphinCards.form.FlashCardsForm;
import br.com.dolphinCards.model.Discipline;
import br.com.dolphinCards.model.FlashCard;
import br.com.dolphinCards.model.Student;
import br.com.dolphinCards.repository.DisciplinesRepository;
import br.com.dolphinCards.repository.FlashCardsRepository;
import br.com.dolphinCards.repository.StudentRepository;
import br.com.dolphinCards.service.Students.CheckIfLoggedStudentExistsService;

public class CreateFlashCardService {
    private StudentRepository studentRepository;
    private DisciplinesRepository disciplineRepository;
    private FlashCardsRepository flashCardsRepository;
    private FlashCardsForm flashCardsForm;

    public CreateFlashCardService(StudentRepository studentRepository, 
                                   DisciplinesRepository disciplineRepository,
                                   FlashCardsRepository flashCardsRepository,
                                   FlashCardsForm flashCardsForm) {
        this.studentRepository = studentRepository;
        this.disciplineRepository = disciplineRepository;
        this.flashCardsRepository = flashCardsRepository;
        this.flashCardsForm = flashCardsForm;
    }

    public ResponseEntity<?> run() {
        Optional<Student> optionalStudent = new CheckIfLoggedStudentExistsService(studentRepository).run();
        if (optionalStudent == null) return new Exceptions().jwtUserTokenError();
        
        Student student = optionalStudent.get();
        Optional<Discipline> optionalDiscipline = disciplineRepository.findByDisciplineNameAndStudent(flashCardsForm.getDisciplineName(), student.getId());

        if (!optionalDiscipline.isPresent()) return new Exceptions("Discipline with that name does not exist for user!", 404).throwException();

        Discipline discipline = optionalDiscipline.get();

        FlashCard flashCard = new FlashCard(flashCardsForm.getQuestion(), flashCardsForm.getAnswer(), discipline);
        FlashCard savedFlashCard = flashCardsRepository.save(flashCard);
        FlashCardsDTO flashCardDTO = new FlashCardsDTO(savedFlashCard, discipline, student, false, false);

        return ResponseEntity.ok().body(flashCardDTO);
    }
}
