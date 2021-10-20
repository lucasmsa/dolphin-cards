package br.com.dolphinCards.service.Disciplines;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import br.com.dolphinCards.DTO.FlashCardsDTO;
import br.com.dolphinCards.errors.Exceptions;
import br.com.dolphinCards.model.Discipline;
import br.com.dolphinCards.model.Student;
import br.com.dolphinCards.repository.DisciplinesRepository;
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
    
    public ResponseEntity<?> run() {
        Optional<Student> optionalStudent = new CheckIfLoggedStudentExistsService(studentRepository).run();
        if (optionalStudent == null) return new Exceptions().jwtUserTokenError();
    
        Student student = optionalStudent.get();

        Optional<Discipline> discipline = disciplineRepository.findByDisciplineNameAndStudent(disciplineName, student.getId());

        if (!discipline.isPresent()) {
            return new Exceptions("Discipline with that name does not exists!", 404).throwException();
        }

        List<Object> disciplineFlashCards = disciplineRepository.findAllFlashCardsFromDiscipline(discipline.get().getId(), student.getId(), pagination).toList();
        List<FlashCardsDTO> flashCardsDTO = new FlashCardsDTO().objectFieldsToFlashCardsDTO(disciplineFlashCards);
        
        return ResponseEntity.ok().body(flashCardsDTO);
    }
}