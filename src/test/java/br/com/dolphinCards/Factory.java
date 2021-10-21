package br.com.dolphinCards;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.joda.time.DateTime;

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

    public FlashCard futureFlashCardBuilder() {
        return new FlashCard("2", "How to calculate the volume of a cylinder?", "Pi*R^2*h", new DateTime(new Date()).plusDays(10).toDate(), 0, disciplineBuilder());
    }

    public Student studentBuilder() {
        return new Student("1", "test", "test@mail.com", "123456", new HashSet<Discipline>());
    }

    public List<FlashCard> listOfFlashCardsBuilder() {
        FlashCard firstFlashCard = flashCardBuilder();
        FlashCard secondFlashCard = futureFlashCardBuilder();
        List<FlashCard> flashCards = new ArrayList<>();
        flashCards.add(firstFlashCard);
        flashCards.add(secondFlashCard);

        return flashCards;
    }

    public Discipline disciplineBuilder() {
        return new Discipline("1", "Mathematics", studentBuilder(), new HashSet<FlashCard>());
    }
}
