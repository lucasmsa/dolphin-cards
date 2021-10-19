package br.com.dolphinCards.service.Students;

import java.util.Optional;

import org.springframework.security.core.context.SecurityContextHolder;

import br.com.dolphinCards.model.Student;
import br.com.dolphinCards.repository.StudentRepository;

public class CheckIfLoggedStudentExistsService {
    public Optional<Student> run(StudentRepository studentRepository) {
        String studentEmail = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<Student> optionalStudent = studentRepository.findByEmail(studentEmail);

        if (!optionalStudent.isPresent()) {
            return null;
        }

        return optionalStudent;
    }
}
