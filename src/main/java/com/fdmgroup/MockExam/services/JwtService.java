package com.fdmgroup.MockExam.services;

import java.security.Key;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;

import io.jsonwebtoken.Claims;

/**
 * Interface to define the contract for the JwtService.
 * Provides methods for working with JSON Web Tokens.
 */
public interface JwtService {

	/**
	 * Extracts the user name from a JWT token.
	 * @param token The JWT token from which to extract the user name.
	 * @return The user name extracted from the token.
	 */
	String extractUsername(String token);

	/**
	 * Extracts a specific claim from a JWT token using a custom claims resolver function.
	 * @param <T> The type of the claim to extract.
	 * @param token The JWT token from which to extract the claim.
	 * @param claimsResolver A function to resolve the claim from the token's claims.
	 * @return The extracted claim.
	 */
	<T> T extractClaim(String token, Function<Claims, T> claimsResolver);

	/**
	 * Generates a JWT token based on the provided UserDetails.
	 * @param userDetails The UserDetails containing user information.
	 * @return The generated JWT token.
	 */
	String generateToken(UserDetails userDetails);

	/**
	 * Generates a JWT token with additional claims based on the provided UserDetails.
	 * @param extraClaims Additional claims to include in the JWT.
	 * @param userDetails The UserDetails containing user information.
	 * @return The generated JWT token with extra claims.
	 */
	String generateToken(Map<String, Object> extraClaims, UserDetails userDetails);

	/**
	 * Checks if a JWT token is valid for the provided UserDetails.
	 * @param token The JWT token to validate.
	 * @param userDetails The UserDetails for which to validate the token.
	 * @return True if the token is valid, false otherwise.
	 */
	boolean isTokenValid(String token, UserDetails userDetails);

	/**
	 * Checks if a JWT token is expired.
	 * @param token The JWT token to check for expiration.
	 * @return True if the token is expired, false otherwise.
	 */
	boolean isTokenExpired(String token);

	/**
	 * Extracts all claims from a JWT token.
	 * @param token The JWT token from which to extract claims.
	 * @return A Claims object containing all claims from the token.
	 */
	Claims extractAllClaims(String token);

	/**
	 * Retrieves the signing key used for JWT token validation.
	 * @return The signing key.
	 */
	Key getSignInKey();
	
	/**
	 * Extracts the user's email address from a JWT token stored in the header.
	 * @param header The JWT token stored in the header.
	 * @return The user's email address extracted from the token.
	 */
	String extractUserEmailFromHeader(String header);

}