package br.com.dolphinCards.security;

import org.apache.catalina.security.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.dolphinCards.model.Student;
import br.com.dolphinCards.repository.StudentRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private StudentRepository studentRepository;

    public UserDetailsServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException, DataAccessException{
        Optional<Student> domainStudent = studentRepository.findByEmail(email);
        if (!domainStudent.isPresent()) {
            throw new UsernameNotFoundException("Could not find user with name '" + email + "'"); 
        } else {
            return new UserDetailsImpl(domainStudent.get());
        }
    }
}
