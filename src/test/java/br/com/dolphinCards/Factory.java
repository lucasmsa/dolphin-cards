package br.com.dolphinCards;

import java.util.Date;
import java.util.HashSet;

import br.com.dolphinCards.form.FlashCardsForm;
import br.com.dolphinCards.model.Discipline;
import br.com.dolphinCards.model.FlashCard;
import br.com.dolphinCards.model.Student;

public class Factory {
    public FlashCardsForm flashCardFormBuilder() {
        return new FlashCardsForm("Pi with 2 decimal points", "3.14", "Mathematics");
    }

    public FlashCard flashCardBuilder() {
        return new FlashCard("1", "Pi with 2 decimal points", "3.14", new Date(), 0, disciplineBuilder());
    }

    public Student studentBuilder() {
        return new Student("1", "test", "test@mail.com", "123456", new HashSet<Discipline>());
    }

    public Discipline disciplineBuilder() {
        return new Discipline("Mathematics", studentBuilder());
    }
}
