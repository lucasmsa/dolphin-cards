package br.com.dolphinCards.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.dolphinCards.model.Discipline;

@Repository
public interface DisciplineRepository extends JpaRepository<Discipline, String> {
    public Optional<Discipline> findById(String id);
}