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
import br.com.dolphinCards.form.DisciplinesForm;
import br.com.dolphinCards.model.Discipline;
import br.com.dolphinCards.repository.DisciplinesRepository;
import br.com.dolphinCards.repository.StudentRepository;
import br.com.dolphinCards.service.Disciplines.CreateDisciplineService;
import br.com.dolphinCards.service.Disciplines.DeleteDisciplineService;
import br.com.dolphinCards.service.Disciplines.GetAllDisciplineFlashCardsService;
import br.com.dolphinCards.service.Disciplines.GetAllStudentsDisciplinesService;

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
    @Transactional
    @CacheEvict(value = "studentDisciplines", allEntries = true)
    public ResponseEntity<?> createDiscipline(@Valid @RequestBody DisciplinesForm disciplinesForm) {
        return new CreateDisciplineService(studentRepository, disciplineRepository, disciplinesForm).run();
    }

    @GetMapping
    @Cacheable(value = "studentDisciplines")
    public ResponseEntity<?> fetchAllStudentDisciplines(@PageableDefault(sort = "name", direction = Direction.ASC, page = 0, size = 10) Pageable pagination) {
        return new GetAllStudentsDisciplinesService(studentRepository, disciplineRepository, pagination).run();
    }

    @DeleteMapping("/{disciplineId}")
    @Transactional
    @CacheEvict(value = "studentDisciplines", allEntries = true)
    public ResponseEntity<?> deleteSpecificDiscipline(@PathVariable("disciplineId") String disciplineId) {
        return new DeleteDisciplineService(studentRepository, disciplineRepository, disciplineId).run();
    }

    @GetMapping("/{name}")
    public ResponseEntity<?> fetchAllDisciplineFlashCards(@PathVariable("name") String name, @PageableDefault(sort = "question", direction = Direction.ASC, page = 0, size = 10) Pageable pagination) {
        return new GetAllDisciplineFlashCardsService(studentRepository, disciplineRepository, name, pagination).run();
    }
}
