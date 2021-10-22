package br.com.dolphinCards.service.Disciplines;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import br.com.dolphinCards.DTO.DisciplineDTO;
import br.com.dolphinCards.errors.Exceptions;
import br.com.dolphinCards.model.Discipline;
import br.com.dolphinCards.model.Student;
import br.com.dolphinCards.repository.DisciplinesRepository;
import br.com.dolphinCards.repository.StudentRepository;
import br.com.dolphinCards.service.Students.CheckIfLoggedStudentExistsService;

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
    
    public ResponseEntity<?> run() {
        Optional<Student> optionalStudent = new CheckIfLoggedStudentExistsService(studentRepository).run();
        if (optionalStudent == null) return new Exceptions().jwtUserTokenError();
        
        Student student = optionalStudent.get();
        Page<Discipline> studentsDisciplines = disciplineRepository.findAllDisciplinesFromStudent(student.getId(), pagination);
        List<DisciplineDTO> disciplinesDTO = studentsDisciplines.stream()
                                                                .map(discipline -> new DisciplineDTO(discipline, student, true))
                                                                .collect(Collectors.toList());
        return ResponseEntity.ok().body(disciplinesDTO);
    }
}
