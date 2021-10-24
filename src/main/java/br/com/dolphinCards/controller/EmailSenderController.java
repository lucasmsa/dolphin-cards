package br.com.dolphinCards.controller;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.dolphinCards.adapter.EmailSenderAdapter;
import br.com.dolphinCards.errors.Exceptions;
import br.com.dolphinCards.model.Student;
import br.com.dolphinCards.repository.FlashCardsRepository;
import br.com.dolphinCards.repository.StudentRepository;
import br.com.dolphinCards.service.Email.SendStudentsEmailWithFlashCardsForTheDayService;
import br.com.dolphinCards.service.Students.CheckIfLoggedStudentIsAdmin;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/send-mail")
@AllArgsConstructor
@Api(value = "EmailSender", description = "REST Controller for Email sending content", tags = { "EmailSender" })
public class EmailSenderController {
    private StudentRepository studentRepository;
    private FlashCardsRepository flashCardsRepository;
    private EmailSenderAdapter emailSenderAdapter;

    @PostMapping
    @ApiOperation(value="Send an e-mail to all students containing the quantity of Flash Cards for the day", 
                  tags = { "EmailSender" })
    public ResponseEntity<?> sendEmailToAllStudents() {
        Optional<Student> optionalStudent = new CheckIfLoggedStudentIsAdmin(studentRepository).run();
        if (optionalStudent == null) return new Exceptions("You do not have permission to send requests to this endpoint", 403).throwException();

        return new SendStudentsEmailWithFlashCardsForTheDayService(emailSenderAdapter, studentRepository, flashCardsRepository).run();
    }
}
