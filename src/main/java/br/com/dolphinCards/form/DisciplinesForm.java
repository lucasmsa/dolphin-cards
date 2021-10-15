package br.com.dolphinCards.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DisciplinesForm {
   @NotBlank(message = "Name is required")
   @Size(min = 2, max = 50, message = "Discipline length must be between 2 and 50")
   private String name; 

   @NotNull
   private Boolean visible;
}
