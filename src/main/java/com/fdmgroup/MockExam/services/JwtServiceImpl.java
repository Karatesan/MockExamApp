package com.fdmgroup.MockExam.services;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

/**
 * This class implements the JwtService interface and provides methods for working with JSON Web Tokens (JWTs),
 * used for authentication and authorization purposes.
 */
@Service
public class JwtServiceImpl implements JwtService {

	private static final String SECRET_KEY ="c5f7bafad365a9caf45019fd004f7adac5f824cd8dd6a4025e9327aa4c44f394";
	
	@Override
	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}
	
	@Override
	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}
	
	@Override
	public String generateToken(UserDetails userDetails) {
		return generateToken(new HashMap<>(), userDetails);
	}
	
	@Override
	public String generateToken(
			Map<String, Object> extraClaims,
			UserDetails userDetails
			) {
		return Jwts
				.builder()
				.setClaims(extraClaims)
				.setSubject(userDetails.getUsername())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 1000*60*60*24))
				.signWith(getSignInKey(), SignatureAlgorithm.HS256)
				.compact();
	}
	
	@Override
	public boolean isTokenValid(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
	}
	
	@Override
	public boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	private Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	@Override
	public Claims extractAllClaims(String token) {
		return Jwts
				.parserBuilder()
				.setSigningKey(getSignInKey())
				.build()
				.parseClaimsJws(token)
				.getBody();
	}

	@Override
	public Key getSignInKey() {
		byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	@Override
	public String extractUserEmailFromHeader(String header) {
		final String jwt;
		final String userEmail;
		
		jwt = header.substring(7);
		userEmail = extractUsername(jwt);
		System.out.println("W jwt " +userEmail);
		return userEmail;
	}
	

	
	
}
