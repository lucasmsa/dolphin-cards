package br.com.dolphinCards.security;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import br.com.dolphinCards.model.Student;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDetailsImpl extends User {
    private static final long serialVersionUID = 2144213171926616839L;
	private String id; 
	private String email;

	public UserDetailsImpl(Student student) {
		super(student.getEmail(), student.getPassword(), authorities(student));
		this.id = student.getId();
		this.email = student.getEmail();
	}

	private static Collection<? extends GrantedAuthority> authorities(Student student) {
		return new ArrayList<>();
	}
}
