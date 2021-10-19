package br.com.dolphinCards.security;

import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.dolphinCards.errors.Exceptions;
import br.com.dolphinCards.model.Student;
import br.com.dolphinCards.repository.StudentRepository;
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
