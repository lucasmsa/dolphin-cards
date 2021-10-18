package br.com.dolphinCards.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.context.SecurityContextHolder;

import br.com.dolphinCards.DTO.DisciplineDTO;
import br.com.dolphinCards.DTO.FlashCardsDTO;
import br.com.dolphinCards.DTO.StudentDTO;
import br.com.dolphinCards.form.DisciplinesForm;
import br.com.dolphinCards.form.FlashCardsForm;
import br.com.dolphinCards.model.Discipline;
import br.com.dolphinCards.model.FlashCard;
import br.com.dolphinCards.model.Student;
import br.com.dolphinCards.repository.DisciplinesRepository;
import br.com.dolphinCards.repository.FlashCardsRepository;
import br.com.dolphinCards.repository.StudentRepository;

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

    public FlashCardsDTO run() {
        Optional<Student> optionalStudent = new CheckIfLoggedStudentExists().run(studentRepository);
        if (optionalStudent == null) return null;
        
        Student student = optionalStudent.get();
        Optional<Discipline> optionalDiscipline = disciplineRepository.findByName(flashCardsForm.getDisciplineName());
        if (!optionalDiscipline.isPresent()) {
            System.out.println("Discipline w/ that name does not exist");
            return null;
        }
        Discipline discipline = optionalDiscipline.get();
        if (discipline.getStudent() != student) {
            System.out.println("You are not the creator of that discipline!");
            return null;
        }
        FlashCard flashCard = new FlashCard(flashCardsForm.getQuestion(), flashCardsForm.getAnswer(), discipline);
        FlashCard savedFlashCard = flashCardsRepository.save(flashCard);

        return new FlashCardsDTO(savedFlashCard, discipline, student, false);
    }
}
