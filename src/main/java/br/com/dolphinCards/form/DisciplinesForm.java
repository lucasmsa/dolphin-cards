package br.com.dolphinCards.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
@ApiModel(value = "Disciplines Form", description = "Form to create a new discipline, which has a `name` attribute")
public class DisciplinesForm {
   @NotBlank(message = "Name is required")
   @Size(min = 2, max = 50, message = "Discipline length must be between 2 and 50")
   private String name; 
}
