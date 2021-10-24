package br.com.dolphinCards.repository;

import java.util.List;
import java.util.Optional;
import br.com.dolphinCards.model.Student;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;


@Repository
public interface StudentRepository extends JpaRepository<Student, String> {
    public Optional<Student> findByEmail(String email);

    @Query(value = "SELECT ID, EMAIL, NAME FROM STUDENTS WHERE EMAIL != 'admin@email.com'", nativeQuery = true)
    public List<?> findAllStudentEmails();
}
