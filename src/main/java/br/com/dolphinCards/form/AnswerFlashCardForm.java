package br.com.dolphinCards.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AnswerFlashCardForm {
    @NotBlank(message = "AnswerType is required")
    @Pattern(regexp = "HARD|EASY|WRONG")
    private String answerType;
}
