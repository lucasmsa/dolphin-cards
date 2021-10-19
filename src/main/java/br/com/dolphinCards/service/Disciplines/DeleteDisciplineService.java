package br.com.dolphinCards.service.Disciplines;

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
import br.com.dolphinCards.service.Students.CheckIfLoggedStudentExistsService;

public class DeleteDisciplineService {
    private StudentRepository studentRepository;
    private DisciplinesRepository disciplineRepository;
    private String disciplineId;

    public DeleteDisciplineService(StudentRepository studentRepository, 
                                   DisciplinesRepository disciplineRepository,
                                   String disciplineId
                                    ) {
        this.studentRepository = studentRepository;
        this.disciplineRepository = disciplineRepository;
        this.disciplineId = disciplineId;
    }

    public String run() {
        Optional<Student> optionalStudent = new CheckIfLoggedStudentExistsService().run(studentRepository);
        if (optionalStudent == null) return "NOT_PRESENT";
        Student student = optionalStudent.get();

        Optional<Discipline> discipline = disciplineRepository.findDisciplineByIdAndStudentId(disciplineId, student.getId());
        if(!discipline.isPresent()) return "NOT_PRESENT";

        disciplineRepository.deleteById(disciplineId);

        return null;
    }
}
