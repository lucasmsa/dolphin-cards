package br.com.dolphinCards.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnswerFlashCardForm {
    @NotBlank(message = "AnswerType is required")
    @Pattern(regexp = "HARD|EASY|WRONG")
    private String answerType;
}
