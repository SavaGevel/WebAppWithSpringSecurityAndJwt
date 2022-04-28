package com.inside.InsideTest;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.inside.InsideTest.service.UserServiceImpl;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Base64;
import java.util.Date;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
@TestPropertySource("/application-test.properties")
class InsideTestApplicationTests {

	@Value("${jwt.token.secret}")
	public String secret;

	@Value("${jwt.token.expired}")
	public long validityInMilliseconds;

	@Autowired
	public MockMvc mockMvc;

	@Autowired
	public UserServiceImpl userService;


	@Test
	@Sql(value = {"/create-user-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(value = {"/create-user-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	void rightCredentialsTest() throws Exception {
		JSONObject credentials = new JSONObject();
		credentials.put("username", "Sava");
		credentials.put("password", "1234");

		this.mockMvc.perform(post("/api/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content(credentials.toString()))
				.andDo(print())
				.andExpect(status().isOk());

	}

	@Test
	void wrongCredentialsTest() throws Exception {
		JSONObject credentials = new JSONObject();
		credentials.put("username", "random");
		credentials.put("password", "random");

		this.mockMvc.perform(post("/api/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content(credentials.toString()))
				.andExpect(status().isUnauthorized());
	}


	@Test
	@Sql(value = {"/create-user-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(value = {"/create-user-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	public void messageSaveTest() throws Exception {

		JSONObject message = new JSONObject();
		message.put("username", "Sava");
		message.put("message", "My test message");

		String token = createCorrectJwt((User) userService.loadUserByUsername("Sava"));

		this.mockMvc.perform(post("/api/user/message")
						.contentType(MediaType.APPLICATION_JSON)
						.content(message.toString())
						.header("Authorization", "Bearer_" + token))
				.andExpect(status().isOk());
	}

	@Test
	@Sql(value = {"/create-user-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(value = {"/create-10-messages-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(value = {"/create-user-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	public void getLastTenMessagesTest() throws Exception {

		String username = "Sava";
		String text = "history 10";

		JSONObject message = new JSONObject();
		message.put("username", username);
		message.put("message", text);

		String token = createCorrectJwt((User) userService.loadUserByUsername("Sava"));

		this.mockMvc.perform(post("/api/user/message")
						.contentType(MediaType.APPLICATION_JSON)
						.content(message.toString())
						.header("Authorization", "Bearer_" + token))
				.andExpect(status().isOk());
	}

	private String createCorrectJwt(User user) {
		Algorithm algorithm = Algorithm.HMAC256(Base64.getEncoder().encode(secret.getBytes()));
		return JWT.create()
				.withSubject(user.getUsername())
				.withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
				.withExpiresAt(new Date(System.currentTimeMillis() + validityInMilliseconds))
				.sign(algorithm);
	}

}
