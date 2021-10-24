package br.com.dolphinCards.form;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "SignUpForm", description = "Form to create a new student, which has an `email`, `name` and a `password`")
public class SignUpForm {
    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 50, message = "Name length must be between 2 and 50")
    private String name;

    @NotBlank(message = "E-mail is required")
    @Email(message = "Email is not valid")
    private String email;    

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 24, message = "Password length must be between 6 and 24")
    private String password;
}
