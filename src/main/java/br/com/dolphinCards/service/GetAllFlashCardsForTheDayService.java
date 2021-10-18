package br.com.dolphinCards.service;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import br.com.dolphinCards.DTO.DisciplineDTO;
import br.com.dolphinCards.DTO.FlashCardsDTO;
import br.com.dolphinCards.DTO.StudentDTO;
import br.com.dolphinCards.form.DisciplinesForm;
import br.com.dolphinCards.form.FlashCardsForm;
import br.com.dolphinCards.model.Discipline;
import br.com.dolphinCards.model.FlashCard;
import br.com.dolphinCards.model.Student;
import br.com.dolphinCards.repository.DisciplinesRepository;
import br.com.dolphinCards.repository.FlashCardsRepository;
import br.com.dolphinCards.repository.StudentRepository;

public class GetAllFlashCardsForTheDayService {
    private StudentRepository studentRepository;
    private FlashCardsRepository flashCardsRepository;
    private Pageable pagination;

    public GetAllFlashCardsForTheDayService(StudentRepository studentRepository, 
                                   FlashCardsRepository flashCardsRepository,
                                   Pageable pagination) {
        this.studentRepository = studentRepository;
        this.flashCardsRepository = flashCardsRepository;
        this.pagination = pagination;
    }

    public List<FlashCardsDTO> run() {
        Optional<Student> optionalStudent = new CheckIfLoggedStudentExists().run(studentRepository);
        if (optionalStudent == null) return null;
        Student student = optionalStudent.get();
        Page<FlashCard> flashCardsForTheDay = flashCardsRepository.findAllFlashCardsForTheDay(student.getId(), new Date(), pagination);
        return flashCardsForTheDay.stream()
                   .map(flashCard -> new FlashCardsDTO(flashCard, flashCard.getDiscipline(), flashCard.getDiscipline().getStudent(), false, true))
                   .collect(Collectors.toList());
    }
}
