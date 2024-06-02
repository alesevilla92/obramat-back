package es.obramat.technicalTest.infrastructure.filter.security;

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.obramat.technicalTest.application.service.UserService;
import es.obramat.technicalTest.domain.model.security.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static es.obramat.technicalTest.infrastructure.utils.Constants.*;

@RequiredArgsConstructor
public class JWTAuthenticationFilterImpl extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    private final UserService userService;


    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
            throws AuthenticationException {
        User creds;
        try {
            creds = new ObjectMapper().readValue(req.getInputStream(), User.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            Optional<User> u = userService.findOne(creds.getUsername());

            if (u.isEmpty()) {
                throw new UsernameNotFoundException("User" + creds.getUsername() + "not found");
            }

            List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("user"));

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(creds.getUsername(), creds.getPassword(),
                            authorities));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res,
                                            FilterChain chain, Authentication auth) throws IOException, ServletException {

        String[] activeProfiles = getEnvironment().getActiveProfiles();
        boolean isLocal = activeProfiles.length == 0 || Arrays.stream(activeProfiles)
                .anyMatch(item -> item.equalsIgnoreCase("default"));

        Date expiresAt = new Date(System.currentTimeMillis() + (EXPIRATION_TIME * 1000));
        String token = JWT.create()
                .withSubject(((org.springframework.security.core.userdetails.User) auth.getPrincipal())
                        .getUsername())
                .withExpiresAt(expiresAt).sign(HMAC512(SECRET.getBytes()));

        Cookie cookie = new Cookie(HEADER_STRING, token);
        cookie.setPath("/");
        cookie.setSecure(!isLocal);
        cookie.setHttpOnly(true);

        // https://stackoverflow.com/questions/23629868/cookie-max-age-in-java
        cookie.setMaxAge(EXPIRATION_TIME);

        res.setHeader("Access-Control-Allow-Credentials", "true");
        res.addCookie(cookie);
    }

}
