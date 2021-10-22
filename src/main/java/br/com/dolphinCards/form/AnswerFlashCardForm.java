package br.com.dolphinCards.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "Answer FlashCard Form", description = "Form to answer a flash card, which will accept an `answerType` of EASY, HARD or WRONG")
public class AnswerFlashCardForm {
    @NotBlank(message = "AnswerType is required")
    @Pattern(regexp = "HARD|EASY|WRONG")
    private String answerType;
}
