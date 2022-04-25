package com.inside.InsideTest.main.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

/**
 * Authorization filter which checks client's token's correctness and validity
 */

public class CustomAuthorizationFilter extends OncePerRequestFilter {
    private final String secret;

    public CustomAuthorizationFilter(String secret) {
        this.secret = secret;
    }

    /**
     * Checks jwt and creates UsernamePasswordAuthenticationToken
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (!request.getServletPath().equals("/api/login")) {
            String authorizationHeader = request.getHeader(AUTHORIZATION);
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer_")) {
                String jwtToken = authorizationHeader.substring("Bearer_".length());
                SecurityContextHolder.getContext().setAuthentication(getAuthenticationToken(jwtToken));
            }
        }
        filterChain.doFilter(request, response);
    }

    /**
     * Creates UsernamePasswordAuthenticationToken from client's jwt
     * @param jwtToken
     * @return
     */

    private UsernamePasswordAuthenticationToken getAuthenticationToken(String jwtToken) {

        Algorithm algorithm = Algorithm.HMAC256(Base64.getEncoder().encode(secret.getBytes()));
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(jwtToken);

        String username = decodedJWT.getSubject();
        String[] roles = decodedJWT.getClaim("roles").asArray(String.class);

        List<SimpleGrantedAuthority> authorities = Arrays.stream(roles)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return new UsernamePasswordAuthenticationToken(username, null, authorities);
    }
}
