package com.inside.InsideTest.main.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Authentication filter which checks username and password and send jwt to client if credentials were correct.
 */

public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final String secret;
    private final long validityInMilliseconds;

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager, String secret, long validityInMilliseconds) {
        this.authenticationManager = authenticationManager;
        this.secret = secret;
        this.validityInMilliseconds = validityInMilliseconds;
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        return authenticationManager.authenticate(createAuthenticationToken(request));
    }

    /**
     * If authentication was successful create jwt and send it to client
     * @param request Http request from client
     * @param response Response which contains jwt in body
     * @param chain filter chain
     * @param authentication contains client information like username and password
     * @throws IOException
     * @throws ServletException
     */

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException {
        User user = (User) authentication.getPrincipal();
        String token = createJwt(user);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("token", token);

        response.setContentType(APPLICATION_JSON_VALUE);
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getWriter(), tokens);
    }

    /**
     * Creates authentication token depends on username and password
     * @param request Request contains username and password
     * @return UsernamePasswordAuthenticationToken
     */

    private UsernamePasswordAuthenticationToken createAuthenticationToken(HttpServletRequest request) {
        String jsonString = "";

        try {
            jsonString = request.getReader().lines().collect(Collectors.toList()).stream().reduce(String::concat).get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        JSONObject loginParameters = new JSONObject(jsonString);

        String username = loginParameters.get("username").toString();
        String password = loginParameters.get("password").toString();

        return new UsernamePasswordAuthenticationToken(username, password);
    }

    /**
     * Creates a jwt depends on user's name and roles;
     * @param user authenticated user
     * @return jwt
     */

    private String createJwt(User user) {
        Algorithm algorithm = Algorithm.HMAC256(Base64.getEncoder().encode(secret.getBytes()));
        return JWT.create()
                .withSubject(user.getUsername())
                .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .withExpiresAt(new Date(System.currentTimeMillis() + validityInMilliseconds))
                .sign(algorithm);
    }
}
