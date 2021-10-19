package br.com.dolphinCards.service.FlashCards;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import br.com.dolphinCards.DTO.FlashCardsDTO;
import br.com.dolphinCards.errors.Exceptions;
import br.com.dolphinCards.model.FlashCard;
import br.com.dolphinCards.model.Student;
import br.com.dolphinCards.repository.FlashCardsRepository;
import br.com.dolphinCards.repository.StudentRepository;
import br.com.dolphinCards.service.Students.CheckIfLoggedStudentExistsService;

public class GetAllStudentFlashCardsService {
    private StudentRepository studentRepository;
    private FlashCardsRepository flashCardsRepository;
    private Pageable pagination;

    public GetAllStudentFlashCardsService(StudentRepository studentRepository, 
                                   FlashCardsRepository flashCardsRepository,
                                   Pageable pagination) {
        this.studentRepository = studentRepository;
        this.flashCardsRepository = flashCardsRepository;
        this.pagination = pagination;
    }

    public ResponseEntity<?> run() {
        Optional<Student> optionalStudent = new CheckIfLoggedStudentExistsService().run(studentRepository);
        if (optionalStudent == null) return new Exceptions().jwtUserTokenError();

        Student student = optionalStudent.get();

        Page<FlashCard> flashCardsForTheDay = flashCardsRepository.findAllStudentFlashCards(student.getId(), pagination);
        List<FlashCardsDTO> flashCardsDTO = flashCardsForTheDay.stream()
                                                                .map(flashCard -> new FlashCardsDTO(flashCard, flashCard.getDiscipline(), flashCard.getDiscipline().getStudent(), false, true))
                                                                .collect(Collectors.toList()); 
        return ResponseEntity.ok().body(flashCardsDTO);
    }
}
