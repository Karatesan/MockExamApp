package com.fdmgroup.MockExam.services;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.fdmgroup.MockExam.model.PasswordResetToken;

/**
 * Interface to define the contract for the PasswordResetTokenService.
 * Provides methods to managing password reset tokens.
 */
@Service
public interface PasswordResetTokenService{
	
    /**
     * Saves a password reset token to the repository.
     * @param passwordResetToken The password reset token to be saved.
     * @return The saved password reset token.
     */
	public PasswordResetToken save(PasswordResetToken passwordResetToken);
	
    /**
     * Retrieves a password reset token by its token string.
     * @param token The token string to search for.
     * @return An optional containing the matching password reset token, if found.
     */
	public Optional<PasswordResetToken> getToken(String token);

    /**
     * Sets the reset time for a password reset token.
     * @param token The token string associated with the password reset token.
     * @param time  The reset time to be set.
     * @return The number of affected rows in the database after the update.
     */
	int setResetAt(String token, LocalDateTime time);
	
}
