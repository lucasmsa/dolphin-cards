package br.com.dolphinCards.service;

import java.util.Optional;

import org.springframework.security.core.context.SecurityContextHolder;

import br.com.dolphinCards.model.Student;
import br.com.dolphinCards.repository.StudentRepository;

public class CheckIfLoggedStudentExists {
    public Optional<Student> run(StudentRepository studentRepository) {
        String studentEmail = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<Student> optionalStudent = studentRepository.findByEmail(studentEmail);
        // Student does not exist
        if (!optionalStudent.isPresent()) {
            System.out.println("DOES NOT EXIST");
            return null;
        }

        return optionalStudent;
    }
}
