package br.com.dolphinCards.service.Disciplines;

import java.util.List;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import br.com.dolphinCards.DTO.DisciplineDTO;
import br.com.dolphinCards.errors.Exceptions;
import br.com.dolphinCards.form.DisciplinesForm;
import br.com.dolphinCards.model.Discipline;
import br.com.dolphinCards.model.Student;
import br.com.dolphinCards.repository.DisciplinesRepository;
import br.com.dolphinCards.repository.StudentRepository;
import br.com.dolphinCards.service.Students.CheckIfLoggedStudentExistsService;

public class CreateDisciplineService {
    private StudentRepository studentRepository;
    private DisciplinesRepository disciplineRepository;
    private DisciplinesForm disciplinesForm;

    public CreateDisciplineService(StudentRepository studentRepository, DisciplinesRepository disciplineRepository,
            DisciplinesForm disciplinesForm) {
        this.studentRepository = studentRepository;
        this.disciplineRepository = disciplineRepository;
        this.disciplinesForm = disciplinesForm;
    }

    public ResponseEntity<?> run() {
        System.out.println("I AM CREATING A NEW DISCIPLINE " + disciplinesForm.getName());
        Optional<Student> optionalStudent = new CheckIfLoggedStudentExistsService(studentRepository).run();
        if (optionalStudent == null) return new Exceptions().jwtUserTokenError();

        System.out.println("IT PASSED THROUGH THE JWT USER TOKEN " + disciplinesForm.getName());

        Student student = optionalStudent.get();
        Discipline discipline = new Discipline(disciplinesForm.getName(), student);
        Optional<Discipline> disciplineWithTheSameName = disciplineRepository.findByDisciplineNameAndStudent(disciplinesForm.getName(), student.getId());

        if (disciplineWithTheSameName.isPresent()) {
            return new Exceptions("Discipline with that name already exists for user!", 409).throwException();
        }

        System.out.println("MEDICINA " + discipline);
        Discipline savedDiscipline = disciplineRepository.save(discipline);

        DisciplineDTO disciplineDTO = new DisciplineDTO(savedDiscipline, student, false);

        return ResponseEntity.ok().body(disciplineDTO);
    }
}
