package br.com.dolphinCards.form;

import javax.validation.constraints.NotBlank;

public class SignInForm {
    @NotBlank(message = "Email is required")
    private String email;    

    @NotBlank(message = "Password is required")
    private String password;
}
