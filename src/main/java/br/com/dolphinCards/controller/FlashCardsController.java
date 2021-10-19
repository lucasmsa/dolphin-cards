package br.com.dolphinCards.controller;

import java.util.List;

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

import br.com.dolphinCards.DTO.DisciplineDTO;
import br.com.dolphinCards.DTO.FlashCardsDTO;
import br.com.dolphinCards.form.AnswerFlashCardForm;
import br.com.dolphinCards.form.DisciplinesForm;
import br.com.dolphinCards.form.FlashCardsForm;

import br.com.dolphinCards.repository.DisciplinesRepository;
import br.com.dolphinCards.repository.FlashCardsRepository;
import br.com.dolphinCards.repository.StudentRepository;
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

    public FlashCardsController(StudentRepository studentRepository, DisciplinesRepository disciplineRepository, FlashCardsRepository flashCardsRepository) {
        this.studentRepository = studentRepository;
        this.disciplineRepository = disciplineRepository;
        this.flashCardsRepository = flashCardsRepository;
    }

    @PostMapping
    public ResponseEntity<FlashCardsDTO> createFlashCard(@Valid @RequestBody FlashCardsForm flashCardsForm) {
        FlashCardsDTO flashCardDTO = new CreateFlashCardService(studentRepository, disciplineRepository, flashCardsRepository, flashCardsForm).run();
        
        return flashCardDTO == null
               ? ResponseEntity.badRequest().build()
               : ResponseEntity.ok().body(flashCardDTO);
    }  

    @GetMapping("/today")
    public ResponseEntity<List<FlashCardsDTO>> fetchAllTodayFlashCards(@RequestParam(required = false) String name,
    @PageableDefault(sort = "question", direction = Direction.DESC, page=0, size=10) Pageable pagination) {
        List<FlashCardsDTO> flashCards = new GetAllFlashCardsForTheDayService(studentRepository, flashCardsRepository, pagination).run();

        return flashCards == null 
                ? ResponseEntity.badRequest().build()
                : ResponseEntity.ok().body(flashCards);
    }

    @GetMapping
    public ResponseEntity<List<FlashCardsDTO>> fetchAllStudentFlashCards(@RequestParam(required = false) String name,
    @PageableDefault(sort = "question", direction = Direction.DESC, page=0, size=10) Pageable pagination) {
        List<FlashCardsDTO> flashCards = new GetAllStudentFlashCardsService(studentRepository, flashCardsRepository, pagination).run();

        return flashCards == null 
                ? ResponseEntity.badRequest().build()
                : ResponseEntity.ok().body(flashCards);
    }

    @GetMapping("/{flashCardId}") 
    public ResponseEntity<FlashCardsDTO> fetchSpecificFlashCard(@PathVariable("flashCardId") String flashCardId) {
        FlashCardsDTO flashCard = new GetSpecificFlashCardService(studentRepository, disciplineRepository, flashCardsRepository, flashCardId).run();

        return flashCard == null 
                ? ResponseEntity.badRequest().build()
                : ResponseEntity.ok().body(flashCard);
    }

    @DeleteMapping("/{flashCardId}") 
    public ResponseEntity<FlashCardsDTO> deleteSpecificFlashCard(@PathVariable("flashCardId") String flashCardId) {
        String flashCard = new DeleteFlashCardService(studentRepository, disciplineRepository, flashCardsRepository, flashCardId).run();

        return flashCard == "NOT_PRESENT" 
                ? ResponseEntity.badRequest().build()
                : ResponseEntity.noContent().build();
    }

    @PatchMapping("/answer/{flashCardId}") 
    public ResponseEntity<FlashCardsDTO> answerFlashCard(@PathVariable("flashCardId") String flashCardId, @Valid @RequestBody AnswerFlashCardForm answerFlashCardForm) {
        FlashCardsDTO flashCard = new AnswerFlashCardService(studentRepository, disciplineRepository, flashCardsRepository, flashCardId, answerFlashCardForm).run();

        return flashCard == null 
                ? ResponseEntity.badRequest().build()
                : ResponseEntity.ok().body(flashCard);
    }
}
