package br.com.dolphinCards.errors;

import java.util.Date;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@Getter
@SuperBuilder
@NoArgsConstructor
public class Exceptions {
    protected String message;
    protected int status;
    protected Date timestamp = new Date();

    public Exceptions(String message, int status) {
        this.message = message;
        this.status = status;
    }

    public ResponseEntity<Exceptions> throwException() {
        return ResponseEntity.status(this.status).body(this);
    }

    public ResponseEntity<Exceptions> jwtUserTokenError() {
        return new Exceptions("Student does not exist", 401).throwException();
    }
}