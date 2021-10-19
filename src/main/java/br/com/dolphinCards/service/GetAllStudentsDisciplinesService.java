package br.com.dolphinCards.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;

import br.com.dolphinCards.DTO.DisciplineDTO;
import br.com.dolphinCards.DTO.StudentDTO;
import br.com.dolphinCards.form.DisciplinesForm;
import br.com.dolphinCards.model.Discipline;
import br.com.dolphinCards.model.Student;
import br.com.dolphinCards.repository.DisciplinesRepository;
import br.com.dolphinCards.repository.StudentRepository;


public class GetAllStudentsDisciplinesService {
    private StudentRepository studentRepository;
    private DisciplinesRepository disciplineRepository;
    private Pageable pagination;

    public GetAllStudentsDisciplinesService(StudentRepository studentRepository, 
                                     DisciplinesRepository disciplineRepository,
                                     Pageable pagination
                                    ) {
        this.studentRepository = studentRepository;
        this.disciplineRepository = disciplineRepository;
        this.pagination = pagination;
    }
    
    public List<DisciplineDTO> run() {
        Optional<Student> optionalStudent = new CheckIfLoggedStudentExistsService().run(studentRepository);
        if (optionalStudent == null) return null;
        
        Student student = optionalStudent.get();
        Page<Discipline> studentsDisciplines = disciplineRepository.findAllDisciplinesFromStudent(student.getId(), pagination);

        return studentsDisciplines.stream()
                                  .map(discipline -> new DisciplineDTO(discipline, student, true))
                                  .collect(Collectors.toList());
    }
}
