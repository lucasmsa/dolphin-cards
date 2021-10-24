package br.com.dolphinCards.service.Students;

import java.util.Optional;

import org.springframework.security.core.context.SecurityContextHolder;

import br.com.dolphinCards.model.Student;
import br.com.dolphinCards.repository.StudentRepository;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CheckIfLoggedStudentIsAdmin {
    private StudentRepository studentRepository;

    public Optional<Student> run() {
        String studentEmail = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        if (!studentEmail.equals("admin@email.com")) {
            return null;
        }

        Optional<Student> optionalStudent = studentRepository.findByEmail(studentEmail);
        if (!optionalStudent.isPresent()) {
            return null;
        }

        return optionalStudent;
    }
}
