package br.com.dolphinCards.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.ManyToAny;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "flash_cards")
public class FlashCard {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;

    @Column
    private String question;

    @Column
    private String answer;

    @Temporal(TemporalType.DATE)
    private Date nextAnswerDate;

    @Column
    private int timesAnswered;

    @ManyToOne
    @JoinColumn(name="discipline_id")
    private Discipline discipline;

    public FlashCard(String question, String answer, Discipline discipline) {
        this.question = question;
        this.answer = answer;
        this.discipline = discipline;
        this.nextAnswerDate = new Date();
        this.timesAnswered = 0;
    }
}
