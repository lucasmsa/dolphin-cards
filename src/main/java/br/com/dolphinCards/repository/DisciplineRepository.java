package br.com.dolphinCards.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import br.com.dolphinCards.model.Discipline;
import br.com.dolphinCards.model.Student;

@Repository
public interface DisciplineRepository extends PagingAndSortingRepository<Discipline, String> {
    public Optional<Discipline> findById(String id);

    @Query(value = "SELECT * FROM DISCIPLINES WHERE NAME = ?1 AND STUDENT_ID = ?2", nativeQuery = true)
    public List<Discipline> findAllByDisciplineNameAndStudent(String name, String studentId);

    @Query(value = "SELECT * FROM DISCIPLINES WHERE STUDENT_ID = ?1", nativeQuery = true)
    public List<Discipline> findAllByStudent(String studentId, Pageable pageable);
}