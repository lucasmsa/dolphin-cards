package br.com.dolphinCards.service;

import br.com.dolphinCards.model.Student;
import br.com.dolphinCards.DTO.StudentDTO;
import br.com.dolphinCards.form.SignUpForm;
import br.com.dolphinCards.repository.StudentRepository;
import org.springframework.security.crypto.password.PasswordEncoder;


public class SignUpService {
    private StudentRepository studentRepository;
    private PasswordEncoder passwordEncoder;
    private SignUpForm signUpForm;

    public SignUpService(StudentRepository studentRepository, 
                         PasswordEncoder passwordEncoder,
                         SignUpForm signUpForm) {
        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
        this.signUpForm = signUpForm;
    }
    
    public StudentDTO run() {
        boolean emailAlreadyExists = this.studentRepository.findByEmail(signUpForm.getEmail()).isPresent();
        if (emailAlreadyExists) {
            return null;
        }
        String rawPassword = signUpForm.getPassword();
        String encodedPassword = this.passwordEncoder.encode(rawPassword);
        Student savedStudent = studentRepository.save(new Student(signUpForm, encodedPassword));
        StudentDTO studentDTO = new StudentDTO(savedStudent); 

        return studentDTO;
    }
}
