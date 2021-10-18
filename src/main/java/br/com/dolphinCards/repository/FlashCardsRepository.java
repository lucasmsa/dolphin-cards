package br.com.dolphinCards.repository;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.dolphinCards.model.FlashCard;

public interface FlashCardsRepository extends JpaRepository<FlashCard, String> {
    @Query(value = "SELECT * FROM FLASH_CARDS as fc JOIN DISCIPLINES as d ON fc.discipline_id = d.id JOIN STUDENTS as s ON d.student_id = s.id  where s.id = ?1 and fc.next_answer_date = ?2", 
           countQuery = "SELECT count(*) FROM FLASH_CARDS as fc JOIN DISCIPLINES as d ON fc.discipline_id = d.id JOIN STUDENTS as s ON d.student_id = s.id  where s.id = ?1 and fc.next_answer_date = ?2",
           nativeQuery = true)
    public Page<FlashCard> findAllFlashCardsForTheDay(String studentId, Date date, Pageable pageable);
}

// flashCards.discipline.student_id 
