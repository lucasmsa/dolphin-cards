package br.com.dolphinCards.service.Students;

import br.com.dolphinCards.model.Student;
import br.com.dolphinCards.DTO.StudentDTO;
import br.com.dolphinCards.errors.Exceptions;
import br.com.dolphinCards.form.SignUpForm;
import br.com.dolphinCards.repository.StudentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    
    public ResponseEntity<?> run() {
        System.out.println("Eu to aqui? " + signUpForm.getEmail());
        boolean emailAlreadyExists = this.studentRepository.findByEmail(signUpForm.getEmail()).isPresent();
        System.out.println("DOES THE EMAIL ALREADY EXISTS " + emailAlreadyExists);
        if (emailAlreadyExists) return new Exceptions("Student with this e-mail already exists", 409).throwException();
        
        String rawPassword = signUpForm.getPassword();
        String encodedPassword = this.passwordEncoder.encode(rawPassword);
        Student savedStudent = studentRepository.save(new Student(signUpForm, encodedPassword));
        StudentDTO studentDTO = new StudentDTO(savedStudent); 
        System.out.println("Am I really here " + signUpForm.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(studentDTO);
    }
}
