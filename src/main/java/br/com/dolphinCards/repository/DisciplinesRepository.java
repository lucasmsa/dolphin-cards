package br.com.dolphinCards.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import br.com.dolphinCards.model.Discipline;

@Repository
public interface DisciplinesRepository extends JpaRepository<Discipline, String> {
       public Optional<Discipline> findById(String id);

       public Optional<Discipline> findByName(String name);

       @Query(value = "SELECT * FROM DISCIPLINES WHERE NAME = ?1 AND STUDENT_ID = ?2", nativeQuery = true)
       public Optional<Discipline> findByDisciplineNameAndStudent(String name, String studentId);

       @Query(value = "SELECT * FROM DISCIPLINES WHERE STUDENT_ID = ?1", countQuery = "SELECT count(*) FROM DISCIPLINES WHERE STUDENT_ID = ?1", nativeQuery = true)
       public Page<Discipline> findAllDisciplinesFromStudent(String studentId, Pageable pageable);

       @Query(value = "SELECT * FROM DISCIPLINES WHERE ID = ?1 AND STUDENT_ID = ?2", nativeQuery = true)
       public Optional<Discipline> findDisciplineByIdAndStudentId(String disciplineId, String studentId);

       @Query(value = "SELECT fc.id, fc.answer, fc.question, fc.next_answer_date, fc.times_answered, fc.discipline_id FROM flash_cards as fc JOIN disciplines as d ON fc.discipline_id = d.id  JOIN STUDENTS as s ON d.student_id = s.id WHERE fc.discipline_id = ?1 AND d.student_id = ?2", countQuery = "SELECT count(*) FROM flash_cards as fc JOIN disciplines as d ON fc.discipline_id = d.id  JOIN STUDENTS as s ON d.student_id = s.id WHERE fc.discipline_id = ?1 AND d.student_id = ?2", nativeQuery = true)
       public Page<Object> findAllFlashCardsFromDiscipline(String disciplineId, String studentId, Pageable pageable);
}