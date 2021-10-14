package br.com.dolphinCards.model;

import java.io.Serializable;
import java.util.ArrayList;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.dolphinCards.form.SignUpForm;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "students")
public class Student implements Serializable {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;

    @Column 
    private String name;

    @Column(unique = true)
    private String email;

    @Column
    private String password;

    // @ManyToMany(fetch = FetchType.EAGER)
	// private List<Perfil> perfis = new ArrayList<>();

    public Student(SignUpForm studentForm, String encryptedPassword) {
        this.name = studentForm.getName();
        this.email = studentForm.getEmail();
        this.password = encryptedPassword;
    }
}
