package br.com.dolphinCards.service.Disciplines;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import br.com.dolphinCards.errors.Exceptions;
import br.com.dolphinCards.model.Discipline;
import br.com.dolphinCards.model.Student;
import br.com.dolphinCards.repository.DisciplinesRepository;
import br.com.dolphinCards.repository.StudentRepository;
import br.com.dolphinCards.service.Students.CheckIfLoggedStudentExistsService;

public class DeleteDisciplineService {
    private StudentRepository studentRepository;
    private DisciplinesRepository disciplineRepository;
    private String disciplineId;

    public DeleteDisciplineService(StudentRepository studentRepository, DisciplinesRepository disciplineRepository, String disciplineId) {
        this.studentRepository = studentRepository;
        this.disciplineRepository = disciplineRepository;
        this.disciplineId = disciplineId;
    }

    public ResponseEntity<?> run() {
        Optional<Student> optionalStudent = new CheckIfLoggedStudentExistsService(studentRepository).run();
        if (optionalStudent == null) return new Exceptions().jwtUserTokenError();

        Student student = optionalStudent.get();

        Optional<Discipline> discipline = disciplineRepository.findDisciplineByIdAndStudentId(disciplineId, student.getId());
        if (!discipline.isPresent()) return new Exceptions("Discipline with this ID does not exist for user", 404).throwException();

        disciplineRepository.deleteById(disciplineId);

        return ResponseEntity.noContent().build();
    }
}
