package br.com.dolphinCards.controller;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.apache.catalina.connector.Response;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.dolphinCards.DTO.DisciplineDTO;
import br.com.dolphinCards.DTO.FlashCardsDTO;
import br.com.dolphinCards.adapter.EmailSenderAdapter;
import br.com.dolphinCards.errors.Exceptions;
import br.com.dolphinCards.form.DisciplinesForm;
import br.com.dolphinCards.model.Discipline;
import br.com.dolphinCards.model.Student;
import br.com.dolphinCards.repository.DisciplinesRepository;
import br.com.dolphinCards.repository.FlashCardsRepository;
import br.com.dolphinCards.repository.StudentRepository;
import br.com.dolphinCards.service.Disciplines.CreateDisciplineService;
import br.com.dolphinCards.service.Disciplines.DeleteDisciplineService;
import br.com.dolphinCards.service.Disciplines.GetAllDisciplineFlashCardsService;
import br.com.dolphinCards.service.Disciplines.GetAllStudentsDisciplinesService;
import br.com.dolphinCards.service.Email.SendStudentsEmailWithFlashCardsForTheDayService;
import br.com.dolphinCards.service.Students.CheckIfLoggedStudentExistsService;
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
