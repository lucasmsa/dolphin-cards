package br.com.dolphinCards.service;

import java.util.Optional;

import org.springframework.security.core.context.SecurityContextHolder;

import br.com.dolphinCards.DTO.DisciplineDTO;
import br.com.dolphinCards.DTO.StudentDTO;
import br.com.dolphinCards.form.DisciplinesForm;
import br.com.dolphinCards.model.Discipline;
import br.com.dolphinCards.model.Student;
import br.com.dolphinCards.repository.DisciplineRepository;
import br.com.dolphinCards.repository.StudentRepository;

public class CreateDisciplineService {
    private StudentRepository studentRepository;
    private DisciplineRepository disciplineRepository;
    private DisciplinesForm disciplinesForm;

    public CreateDisciplineService(StudentRepository studentRepository, 
                                   DisciplineRepository disciplineRepository,
                                   DisciplinesForm disciplinesForm) {
        this.studentRepository = studentRepository;
        this.disciplineRepository = disciplineRepository;
        this.disciplinesForm = disciplinesForm;
    }
    
    public DisciplineDTO run() {
        String studentEmail = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<Student> optionalStudent = studentRepository.findByEmail(studentEmail);
        if (!optionalStudent.isPresent()) {
            return null;
        }
        Student student = optionalStudent.get();
        StudentDTO studentDTO = new StudentDTO(student);
        Discipline discipline = new Discipline(disciplinesForm.getName(), disciplinesForm.getVisible(), student);
        Discipline savedDiscipline = disciplineRepository.save(discipline);

        return new DisciplineDTO(savedDiscipline, studentDTO);
    }
}
