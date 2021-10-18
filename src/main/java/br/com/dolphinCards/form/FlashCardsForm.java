package br.com.dolphinCards.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FlashCardsForm {
    @NotBlank(message = "Question is required")
    @Size(min = 1, max = 1000, message = "Question length must be between 2 and 5000")
    private String question;

    @NotBlank(message = "Answer is required")
    @Size(min = 1, max = 1000, message = "Answer length must be between 2 and 5000")
    private String answer;    

    @NotBlank(message = "Discipline is required")
    @Size(min = 2, max = 50, message = "Discipline name length must be between 2 and 50")
    private String disciplineName;
}
