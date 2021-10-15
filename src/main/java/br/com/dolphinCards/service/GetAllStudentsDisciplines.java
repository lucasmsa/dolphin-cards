package br.com.dolphinCards.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;

import br.com.dolphinCards.DTO.DisciplineDTO;
import br.com.dolphinCards.DTO.StudentDTO;
import br.com.dolphinCards.form.DisciplinesForm;
import br.com.dolphinCards.model.Discipline;
import br.com.dolphinCards.model.Student;
import br.com.dolphinCards.repository.DisciplineRepository;
import br.com.dolphinCards.repository.StudentRepository;


public class GetAllStudentsDisciplines {
    private StudentRepository studentRepository;
    private DisciplineRepository disciplineRepository;
    private Pageable pagination;

    public GetAllStudentsDisciplines(StudentRepository studentRepository, 
                                     DisciplineRepository disciplineRepository,
                                     Pageable pagination
                                    ) {
        this.studentRepository = studentRepository;
        this.disciplineRepository = disciplineRepository;
        this.pagination = pagination;
    }
    
    public List<DisciplineDTO> run() {
        Optional<Student> optionalStudent = new CheckIfLoggedStudentExists().run(studentRepository);
        if (optionalStudent == null) return null;
        
        Student student = optionalStudent.get();
        List<Discipline> studentsDisciplines = disciplineRepository.findAllByStudent(student.getId(), pagination);

        return studentsDisciplines.stream()
                                  .map(discipline -> new DisciplineDTO(discipline, new StudentDTO(student), true))
                                  .collect(Collectors.toList());
    }
}
