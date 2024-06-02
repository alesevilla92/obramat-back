package es.obramat.technicalTest.infrastructure.filter.security;

import static es.obramat.technicalTest.infrastructure.utils.Constants.HEADER_STRING;
import static es.obramat.technicalTest.infrastructure.utils.Constants.SECRET;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


import es.obramat.technicalTest.application.service.UserService;
import es.obramat.technicalTest.domain.model.security.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;



public class JWTAuthorizationFilterImpl extends BasicAuthenticationFilter {

    private final UserService userService;

    public JWTAuthorizationFilterImpl(AuthenticationManager authenticationManager, UserService userService) {
        super(authenticationManager);
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {
        Cookie cookie = getCookieAuth(req);

        UsernamePasswordAuthenticationToken authentication = getAuthentication(cookie);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(req, res);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(Cookie cookie) {
        if (cookie != null && !StringUtils.isEmpty(cookie.getValue())) {
            // parse the token.
            String user = JWT.require(Algorithm.HMAC512(SECRET.getBytes()))
                    .build().verify(cookie.getValue()).getSubject();

            if (user != null) {
                Optional<User> u = userService.findOne(user);

                if(u.isEmpty()) {
                    throw new UsernameNotFoundException("User" + user + "not found");
                }

                List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("user"));

                return new UsernamePasswordAuthenticationToken(user, null, authorities);
            }
            return null;
        }
        return null;
    }

    private Cookie getCookieAuth(HttpServletRequest request) {
        if (request.getCookies() == null) {
            return null;
        }

        Optional<Cookie> cookie = Arrays.stream(request.getCookies())
                .filter(c -> c.getName().equals(HEADER_STRING))
                .findFirst();

        return cookie.orElse(null);
    }
}
