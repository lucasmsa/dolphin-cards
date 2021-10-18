package br.com.dolphinCards.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.dolphinCards.model.FlashCard;

public interface FlashCardsRepository extends JpaRepository<FlashCard, String> {
    
}
