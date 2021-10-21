package br.com.dolphinCards.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.dolphinCards.model.SendStudentsMail;
import br.com.dolphinCards.model.Student;


@Repository
public interface StudentRepository extends JpaRepository<Student, String> {
    public Optional<Student> findByEmail(String email);

    @Query(value = "SELECT ID, EMAIL FROM STUDENTS S", nativeQuery = true)
    public List<?> findAllStudentEmails();
}
