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
@ApiModel(value = "SignInForm", description = "Form to create a new student, which has an `email`, `name` and a `password`")
public class SignInForm {
    @NotBlank(message = "E-mail is required")
    @Email(message = "Email is not valid")
    private String email;    

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 24, message = "Password length must be between 6 and 24")
    private String password;
}
