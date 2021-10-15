package br.com.dolphinCards.security;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import br.com.dolphinCards.model.Student;

public class UserDetailsImpl extends User {
    private static final long serialVersionUID = 2144213171926616839L;
	public String email;

	public UserDetailsImpl(Student student) {
		super(student.getEmail(), student.getPassword(), authorities(student));
	}

	public String getEmail() {
		return email;
	}

	private static Collection<? extends GrantedAuthority> authorities(Student student) {
		return new ArrayList<>();
	}
}
