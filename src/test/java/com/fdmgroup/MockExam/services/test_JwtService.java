package com.fdmgroup.MockExam.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;

@ExtendWith(MockitoExtension.class)
public class test_JwtService {
//	private static final String SECRET_KEY = "c5f7bafad365a9caf45019fd004f7adac5f824cd8dd6a4025e9327aa4c44f394";
	private static final String USERNAME = "testuser";

	@InjectMocks
	private JwtServiceImpl jwtService;

	@Mock
	private UserDetails userDetails;

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_generateToken() {

		Map<String, Object> extraClaims = new HashMap<>();
		extraClaims.put("key1", "value1");
		when(userDetails.getUsername()).thenReturn(USERNAME);

		String token = jwtService.generateToken(extraClaims, userDetails);
		Claims claims = Jwts.parserBuilder().setSigningKey(jwtService.getSignInKey()).build().parseClaimsJws(token)
				.getBody();

		assertNotNull(token);
		assertEquals(USERNAME, claims.getSubject());
		assertEquals("value1", claims.get("key1"));
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_isTokenValid_ValidToken() {
		when(userDetails.getUsername()).thenReturn(USERNAME);

		String token = jwtService.generateToken(userDetails);
		boolean isValid = jwtService.isTokenValid(token, userDetails);

		assertTrue(isValid);
	}

	// ---------------------------------------------------------------------------------------------

	private String generateExpiredToken(String subject) {
		return Jwts.builder().setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis() - 20000)) // Expired
																											// 20
																											// seconds
																											// ago
				.setExpiration(new Date(System.currentTimeMillis() - 10000)) // Expired 10 seconds ago
				.signWith(jwtService.getSignInKey()).compact();
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_isTokenValid_ExpiredToken() {

		String token = generateExpiredToken(USERNAME);

		assertThrows(ExpiredJwtException.class, () -> jwtService.isTokenValid(token, userDetails));

	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_isTokenValid_InvalidUsername() {
		String token = generateTokenWithExpiration("otheruser", new Date(System.currentTimeMillis() + 10000));

		boolean isValid = jwtService.isTokenValid(token, userDetails);

		assertFalse(isValid);
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_extractUserEmailFromHeader() {
		String header = "Bearer " + generateTokenWithExpiration(USERNAME, new Date(System.currentTimeMillis() + 10000));

		String userEmail = jwtService.extractUserEmailFromHeader(header);

		assertEquals(USERNAME, userEmail);
	}

	// ---------------------------------------------------------------------------------------------

	private String generateTokenWithExpiration(String subject, Date expiration) {
		return Jwts.builder().setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(expiration).signWith(jwtService.getSignInKey()).compact();
	}

}
