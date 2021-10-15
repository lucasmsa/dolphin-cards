package br.com.dolphinCards.DTO;

import java.util.HashMap;
import java.util.Map;

import br.com.dolphinCards.model.Student;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignInDTO {
    private Map<String, String> student = new HashMap<String, String>();
    private String token;

    public SignInDTO(Student student, String token) {
        this.student.put("id", student.getId());
        this.student.put("name", student.getName());
        this.student.put("email", student.getEmail());
        
        this.token = token;
    }
}

// {
//     user: {
//         id: 1,
//         name: "oi",
//         email: "oi@email.com"
//     },
//     token: "8392u2dha9iud"
// }