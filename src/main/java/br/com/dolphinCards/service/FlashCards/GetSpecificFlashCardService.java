package br.com.dolphinCards.service.FlashCards;

import java.util.Optional;

import org.springframework.http.ResponseEntity;

import br.com.dolphinCards.DTO.FlashCardsDTO;
import br.com.dolphinCards.errors.Exceptions;
import br.com.dolphinCards.model.FlashCard;
import br.com.dolphinCards.model.Student;
import br.com.dolphinCards.repository.FlashCardsRepository;
import br.com.dolphinCards.repository.StudentRepository;
import br.com.dolphinCards.service.Students.CheckIfLoggedStudentExistsService;

public class GetSpecificFlashCardService {
    private StudentRepository studentRepository;
    private FlashCardsRepository flashCardsRepository;
    private String flashCardId;


    public GetSpecificFlashCardService(StudentRepository studentRepository, 
                                       FlashCardsRepository flashCardsRepository,
                                       String flashCardId
                                        ) {
        this.studentRepository = studentRepository;
        this.flashCardsRepository = flashCardsRepository;
        this.flashCardId = flashCardId;
    }

    public ResponseEntity<?> run() {
        Optional<Student> optionalStudent = new CheckIfLoggedStudentExistsService(studentRepository).run();
        if (optionalStudent == null) return new Exceptions().jwtUserTokenError();

        Student student = optionalStudent.get();
        
        Optional<FlashCard> optionalFlashCard = flashCardsRepository.findStudentFlashCardById(student.getId(), flashCardId);
        if (!optionalFlashCard.isPresent()) return new Exceptions("Flash card with this ID does not exist for user", 404).throwException();
        
        FlashCard flashCard = optionalFlashCard.get();

        FlashCardsDTO flashCardDTO = new FlashCardsDTO(flashCard, flashCard.getDiscipline(), flashCard.getDiscipline().getStudent(), false, false);
                         
        return ResponseEntity.ok().body(flashCardDTO);
    }
}
