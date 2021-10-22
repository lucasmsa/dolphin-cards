package br.com.dolphinCards.controller;

import java.util.List;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.dolphinCards.DTO.FlashCardsDTO;
import br.com.dolphinCards.form.AnswerFlashCardForm;
import br.com.dolphinCards.form.FlashCardsForm;

import br.com.dolphinCards.repository.DisciplinesRepository;
import br.com.dolphinCards.repository.FlashCardsRepository;
import br.com.dolphinCards.repository.StudentRepository;
import br.com.dolphinCards.service.Email.SendStudentsEmailWithFlashCardsForTheDayService;
import br.com.dolphinCards.service.FlashCards.AnswerFlashCardService;
import br.com.dolphinCards.service.FlashCards.CreateFlashCardService;
import br.com.dolphinCards.service.FlashCards.DeleteFlashCardService;
import br.com.dolphinCards.service.FlashCards.GetAllFlashCardsForTheDayService;
import br.com.dolphinCards.service.FlashCards.GetAllStudentFlashCardsService;
import br.com.dolphinCards.service.FlashCards.GetSpecificFlashCardService;

@RestController
@RequestMapping("/flash-cards")
public class FlashCardsController {
    private StudentRepository studentRepository;
    private DisciplinesRepository disciplineRepository;
    private FlashCardsRepository flashCardsRepository;

    public FlashCardsController(StudentRepository studentRepository, DisciplinesRepository disciplineRepository,
            FlashCardsRepository flashCardsRepository) {
        this.studentRepository = studentRepository;
        this.disciplineRepository = disciplineRepository;
        this.flashCardsRepository = flashCardsRepository;
    }

    @PostMapping
    public ResponseEntity<?> createFlashCard(@Valid @RequestBody FlashCardsForm flashCardsForm) {
        return new CreateFlashCardService(studentRepository, disciplineRepository, flashCardsRepository, flashCardsForm).run();
    }

    @GetMapping("/today")
    public ResponseEntity<?> fetchAllTodayFlashCards(@RequestParam(required = false) String name, @PageableDefault(sort = "question", direction = Direction.ASC, page = 0, size = 10) Pageable pagination) {
        return new GetAllFlashCardsForTheDayService(studentRepository, flashCardsRepository, pagination).run();
    }

    @GetMapping
    public ResponseEntity<?> fetchAllStudentFlashCards(@RequestParam(required = false) String name, @PageableDefault(sort = "question", direction = Direction.ASC, page = 0, size = 10) Pageable pagination) {
        return new GetAllStudentFlashCardsService(studentRepository, flashCardsRepository, pagination).run();
    }

    @GetMapping("/{flashCardId}")
    public ResponseEntity<?> fetchSpecificFlashCard(@PathVariable("flashCardId") String flashCardId) {
        return new GetSpecificFlashCardService(studentRepository, flashCardsRepository, flashCardId).run();
    }

    @DeleteMapping("/{flashCardId}")
    @Transactional
    public ResponseEntity<?> deleteSpecificFlashCard(@PathVariable("flashCardId") String flashCardId) {
        return new DeleteFlashCardService(studentRepository, flashCardsRepository, flashCardId).run();
    }

    @PatchMapping("/answer/{flashCardId}")
    @Transactional
    public ResponseEntity<?> answerFlashCard(@PathVariable("flashCardId") String flashCardId, @Valid @RequestBody AnswerFlashCardForm answerFlashCardForm) {
        return new AnswerFlashCardService(studentRepository, flashCardsRepository, flashCardId, answerFlashCardForm).run();
    }
}
