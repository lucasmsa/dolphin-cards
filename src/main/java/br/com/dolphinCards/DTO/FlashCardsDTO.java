package br.com.dolphinCards.DTO;

import java.text.SimpleDateFormat;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

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

    private String nextAnswerDate;

    private int timesAnswered;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private DisciplineDTO discipline;

    public String dateToString(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");  
        String strDate = formatter.format(date);  
        return strDate;
    }
    
    public FlashCardsDTO(String id, String question, String answer, int timesAnswered, Date nextAnswerDate) { 
        this.id = id;
        this.question = question;
        this.answer = answer;
        this.timesAnswered = timesAnswered;
        this.nextAnswerDate = dateToString(nextAnswerDate);
    }

    public FlashCardsDTO(FlashCard flashCards, Discipline discipline, Student student, boolean fetchingFromDiscipline, boolean flashCardsListing) {
        this.id = flashCards.getId();
        this.question = flashCards.getQuestion();
        this.answer = flashCards.getAnswer();
        this.timesAnswered = flashCards.getTimesAnswered();
        this.nextAnswerDate = dateToString(flashCards.getNextAnswerDate());
        if (!fetchingFromDiscipline) {
            this.discipline = new DisciplineDTO(discipline, student, flashCardsListing ? true : false);
        }
    }

    public List<FlashCardsDTO> objectFieldsToFlashCardsDTO(List<Object> flashCardsObjects) {
        List<FlashCardsDTO> flashCardsDTOs = new ArrayList<FlashCardsDTO>();
        for (Object flashCardObject : flashCardsObjects) {
            Object[] fields = (Object[]) flashCardObject;
            flashCardsDTOs.add(new FlashCardsDTO((String) fields[0], (String) fields[2], (String) fields[1], (int) fields[4], (Date) fields[3]));
        }

        return flashCardsDTOs;
    }
}
