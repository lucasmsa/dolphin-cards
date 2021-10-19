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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.dolphinCards.DTO.DisciplineDTO;
import br.com.dolphinCards.DTO.FlashCardsDTO;
import br.com.dolphinCards.form.DisciplinesForm;
import br.com.dolphinCards.model.Discipline;
import br.com.dolphinCards.repository.DisciplinesRepository;
import br.com.dolphinCards.repository.StudentRepository;
import br.com.dolphinCards.service.CreateDisciplineService;
import br.com.dolphinCards.service.GetAllDisciplineFlashCardsService;
import br.com.dolphinCards.service.GetAllStudentsDisciplinesService;

@RestController
@RequestMapping("/discipline")
public class DisciplinesController {
    private StudentRepository studentRepository;
    private DisciplinesRepository disciplineRepository;

    public DisciplinesController(StudentRepository studentRepository, DisciplinesRepository disciplineRepository) {
        this.studentRepository = studentRepository;
        this.disciplineRepository = disciplineRepository;
    }

    @PostMapping
    public ResponseEntity<DisciplineDTO> createDiscipline(@Valid @RequestBody DisciplinesForm disciplinesForm) {
        DisciplineDTO disciplineDTO = new CreateDisciplineService(studentRepository, disciplineRepository, disciplinesForm).run();
        
        return disciplineDTO == null
               ? ResponseEntity.badRequest().build()
               : ResponseEntity.ok().body(disciplineDTO);
    }   

    @GetMapping
    public ResponseEntity<List<DisciplineDTO>> fetchAllStudentDisciplines(@PageableDefault(sort = "name", direction = Direction.DESC, page=0, size=10) Pageable pagination) {
        
        List<DisciplineDTO> disciplines = new GetAllStudentsDisciplinesService(studentRepository, disciplineRepository, pagination).run();

        return disciplines == null 
                ? ResponseEntity.badRequest().build()
                : ResponseEntity.ok().body(disciplines);
    }

    @GetMapping("/{name}")
    public ResponseEntity<List<FlashCardsDTO>> fetchAlDisciplineFlashCards(@PathVariable("name") String name,
    @PageableDefault(sort = "question", direction = Direction.DESC, page=0, size=10) Pageable pagination) {
        
        List<FlashCardsDTO> flashCards = new GetAllDisciplineFlashCardsService(studentRepository, disciplineRepository, name, pagination).run();

        return flashCards == null 
                ? ResponseEntity.badRequest().build()
                : ResponseEntity.ok().body(flashCards);
    }
}
