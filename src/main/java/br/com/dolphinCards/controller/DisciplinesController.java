package br.com.dolphinCards.controller;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.dolphinCards.DTO.DisciplineDTO;
import br.com.dolphinCards.form.DisciplinesForm;
import br.com.dolphinCards.repository.DisciplineRepository;
import br.com.dolphinCards.repository.StudentRepository;
import br.com.dolphinCards.service.CreateDisciplineService;

@RestController
@RequestMapping("/discipline")
public class DisciplinesController {
    private StudentRepository studentRepository;
    private DisciplineRepository disciplineRepository;

    public DisciplinesController(StudentRepository studentRepository, DisciplineRepository disciplineRepository) {
        this.studentRepository = studentRepository;
        this.disciplineRepository = disciplineRepository;
    }

    @PostMapping
    public ResponseEntity<DisciplineDTO> createDiscipline(@Valid @RequestBody DisciplinesForm disciplinesForm) {
        DisciplineDTO disciplineDTO = new CreateDisciplineService(studentRepository, disciplineRepository, disciplinesForm).run();
        
        return ResponseEntity.ok().body(disciplineDTO);
    }   
}
