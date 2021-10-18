package br.com.dolphinCards.controller;


import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.apache.catalina.connector.Response;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.dolphinCards.DTO.DisciplineDTO;
import br.com.dolphinCards.DTO.FlashCardsDTO;
import br.com.dolphinCards.form.DisciplinesForm;
import br.com.dolphinCards.form.FlashCardsForm;
import br.com.dolphinCards.model.Discipline;
import br.com.dolphinCards.repository.DisciplinesRepository;
import br.com.dolphinCards.repository.FlashCardsRepository;
import br.com.dolphinCards.repository.StudentRepository;
import br.com.dolphinCards.service.CreateDisciplineService;
import br.com.dolphinCards.service.CreateFlashCardService;
import br.com.dolphinCards.service.GetAllStudentsDisciplines;

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
}
