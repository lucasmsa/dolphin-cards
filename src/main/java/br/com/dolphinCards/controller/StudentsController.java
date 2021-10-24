package br.com.dolphinCards.controller;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.dolphinCards.form.SignUpForm;
import br.com.dolphinCards.repository.StudentRepository;
import br.com.dolphinCards.service.Students.SignUpService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
@Api(value = "Students", description = "REST Controller for Students content", tags = { "Students" })
public class StudentsController {
    private StudentRepository studentRepository;
    private PasswordEncoder passwordEncoder;

    @PostMapping(value = "/signup")
    @ApiOperation(value="Student sign up", 
                  tags = { "Students" })
    public ResponseEntity<?> signUp(@Valid @RequestBody SignUpForm signUpForm) {
        return new SignUpService(studentRepository, passwordEncoder, signUpForm).run();
    }
}
