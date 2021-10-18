package br.com.dolphinCards.DTO;

import java.time.temporal.Temporal;
import java.util.Date;
import java.util.Map;

import br.com.dolphinCards.model.Discipline;
import br.com.dolphinCards.model.FlashCard;
import br.com.dolphinCards.model.Student;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FlashCardsDTO {
    private String id;

    private String question;

    private String answer;

    private Date nextAnswerDate;

    private DisciplineDTO discipline;

    public FlashCardsDTO(FlashCard flashCards, Discipline discipline, Student student) {
        this.id = flashCards.getId();
        this.question = flashCards.getQuestion();
        this.answer = flashCards.getAnswer();
        this.nextAnswerDate = flashCards.getNextAnswerDate();
        this.discipline = new DisciplineDTO(discipline, student, false);
    }
}
