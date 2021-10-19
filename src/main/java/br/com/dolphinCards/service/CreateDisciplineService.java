package br.com.dolphinCards.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.context.SecurityContextHolder;

import br.com.dolphinCards.DTO.DisciplineDTO;
import br.com.dolphinCards.DTO.StudentDTO;
import br.com.dolphinCards.form.DisciplinesForm;
import br.com.dolphinCards.model.Discipline;
import br.com.dolphinCards.model.Student;
import br.com.dolphinCards.repository.DisciplinesRepository;
import br.com.dolphinCards.repository.StudentRepository;

public class CreateDisciplineService {
    private StudentRepository studentRepository;
    private DisciplinesRepository disciplineRepository;
    private DisciplinesForm disciplinesForm;

    public CreateDisciplineService(StudentRepository studentRepository, 
                                   DisciplinesRepository disciplineRepository,
                                   DisciplinesForm disciplinesForm) {
        this.studentRepository = studentRepository;
        this.disciplineRepository = disciplineRepository;
        this.disciplinesForm = disciplinesForm;
    }
    
    public DisciplineDTO run() {
        Optional<Student> optionalStudent = new CheckIfLoggedStudentExistsService().run(studentRepository);
        if (optionalStudent == null) return null;
        
        Student student = optionalStudent.get();
        Discipline discipline = new Discipline(disciplinesForm.getName(), disciplinesForm.getVisible(), student);
        List<Discipline> disciplineWithTheSameName = disciplineRepository.findAllByDisciplineNameAndStudent(disciplinesForm.getName(), student.getId());

        // Discipline with that name already exists for this student
        if (disciplineWithTheSameName.size() > 0) {
            System.out.println("Discipline with that name already exists!");
            return null;
        }

        Discipline savedDiscipline = disciplineRepository.save(discipline);

        return new DisciplineDTO(savedDiscipline, student, false);
    }
}
