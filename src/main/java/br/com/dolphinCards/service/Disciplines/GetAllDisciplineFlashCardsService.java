package br.com.dolphinCards.service.Disciplines;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;

import br.com.dolphinCards.DTO.DisciplineDTO;
import br.com.dolphinCards.DTO.FlashCardsDTO;
import br.com.dolphinCards.DTO.StudentDTO;
import br.com.dolphinCards.form.DisciplinesForm;
import br.com.dolphinCards.model.Discipline;
import br.com.dolphinCards.model.FlashCard;
import br.com.dolphinCards.model.Student;
import br.com.dolphinCards.repository.DisciplinesRepository;
import br.com.dolphinCards.repository.FlashCardsRepository;
import br.com.dolphinCards.repository.StudentRepository;
import br.com.dolphinCards.service.Students.CheckIfLoggedStudentExistsService;

public class GetAllDisciplineFlashCardsService {
    private StudentRepository studentRepository;
    private DisciplinesRepository disciplineRepository;
    private String disciplineName;
    private Pageable pagination;

    public GetAllDisciplineFlashCardsService(StudentRepository studentRepository, 
                                     DisciplinesRepository disciplineRepository,
                                     String disciplineName, 
                                     Pageable pagination
                                    ) {
        this.studentRepository = studentRepository;
        this.disciplineRepository = disciplineRepository;
        this.disciplineName = disciplineName;
        this.pagination = pagination;
    }
    
    public List<FlashCardsDTO> run() {
        Optional<Student> optionalStudent = new CheckIfLoggedStudentExistsService().run(studentRepository);
        if (optionalStudent == null) return null;
    
        Student student = optionalStudent.get();

        List<Discipline> discipline = disciplineRepository.findAllByDisciplineNameAndStudent(disciplineName, student.getId());

        // Discipline with that name does not exists for this student
        if (discipline.size() == 0) {
            System.out.println("Discipline with that name does not exists!");
            return null;
        }
        List<Object> disciplineFlashCards = disciplineRepository.findAllFlashCardsFromDiscipline(discipline.get(0).getId(), student.getId(), pagination).toList();
        
        return new FlashCardsDTO().objectFieldsToFlashCardsDTO(disciplineFlashCards);
    }
}
// disciplineFlashCards.stream()
// .map(flashCard -> System.out.println("I am here damnit " + flashCard))
// .collect(Collectors.toList())