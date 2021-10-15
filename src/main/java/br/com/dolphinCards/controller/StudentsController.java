package br.com.dolphinCards.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.com.dolphinCards.DTO.StudentDTO;
import br.com.dolphinCards.errors.ResourceAlreadyExistsException;
import br.com.dolphinCards.form.SignInForm;
import br.com.dolphinCards.form.SignUpForm;
import br.com.dolphinCards.model.Student;
import br.com.dolphinCards.repository.StudentRepository;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/auth")
public class StudentsController {
    private StudentRepository studentRepository;
    private PasswordEncoder passwordEncoder;
    private UserDetailsService userDetailsService;

    public StudentsController(StudentRepository studentRepository, PasswordEncoder passwordEncoder) {
        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping(value="/signup")
    public ResponseEntity<StudentDTO> signUp(@Valid @RequestBody SignUpForm signUpForm) {
        boolean emailAlreadyExists = this.studentRepository.findByEmail(signUpForm.getEmail()).isPresent();
        if (emailAlreadyExists) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
        String rawPassword = signUpForm.getPassword();
        String encodedPassword = this.passwordEncoder.encode(rawPassword);
        Student savedStudent = studentRepository.save(new Student(signUpForm, encodedPassword));
        StudentDTO studentDTO = new StudentDTO(savedStudent);
        
        return ResponseEntity.ok().body(studentDTO);
    }   
    
    
    @PostMapping(value="/signin")
    public ResponseEntity signIn(@Valid @RequestBody SignInForm signInForm) {
        try {
            System.out.println("Logging in the student ⏳");

            return ResponseEntity.ok().body(null);
        } catch (Exception exception) {
            return ResponseEntity.badRequest().build();
        }
    }
}
