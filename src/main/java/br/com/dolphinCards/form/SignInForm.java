package br.com.dolphinCards.form;

import javax.validation.constraints.NotBlank;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignInForm {
    @NotBlank(message = "Email is required")
    private String email;    

    @NotBlank(message = "Password is required")
    private String password;

    public UsernamePasswordAuthenticationToken convert() {
        return new UsernamePasswordAuthenticationToken(this.email, this.password);
    }
}
