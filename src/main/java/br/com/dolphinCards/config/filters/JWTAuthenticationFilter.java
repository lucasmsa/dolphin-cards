package br.com.dolphinCards.config.filters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import br.com.dolphinCards.model.Student;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter{
    
    private AuthenticationManager authenticationManager;
    private Environment env;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, Environment env) {
        this.authenticationManager = authenticationManager;
        this.env = env;
        setFilterProcessesUrl("/auth/signin"); 
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            Student credentials = new ObjectMapper().readValue(request.getInputStream(), Student.class);

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            credentials.getEmail(),
                            credentials.getPassword(),
                            new ArrayList<>())
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain filterchain,
                                            Authentication auth) throws IOException {
        String token = JWT.create()
                .withSubject(((Student) auth.getPrincipal()).getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis() + this.env.getProperty("EXPIRATION_TIME")))
                .sign(Algorithm.HMAC512(this.env.getProperty("SECRET").getBytes()));

        String body = ((Student) auth.getPrincipal()).getEmail() + " " + token;

        response.getWriter().write(body);
        response.getWriter().flush();
    }
}
