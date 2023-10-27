package com.fdmgroup.MockExam.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.fdmgroup.MockExam.model.User;
import com.fdmgroup.MockExam.model.VerificationToken;

/**
 * Interface to define the contract for the VerificationTokenService.
 * It provides methods related to user verification process using tokens.
 */
public interface VerificationTokenService {

    /**
     * Saves a verification token to the repository.
     * @param verificationToken The verification token to be saved.
     */
	public void save(VerificationToken verificationToken);
	
    /**
     * Retrieves a verification token by its token string.
     * @param token The token string to search for.
     * @return An optional containing the matching verification token, if found.
     */
	public Optional<VerificationToken> getToken(String token);
	
    /**
     * Sets the confirmation time for a verification token.
     * @param token The token string associated with the verification token.
     * @param time  The confirmation time to be set.
     * @return The number of affected rows in the database after the update.
     */
	public int setConfirmedAt(String token, LocalDateTime time);
	
    /**
     * Finds verification tokens associated with a specific user.
     * @param user The user for whom verification tokens are to be retrieved.
     * @return A list of verification tokens associated with the user.
     */
	List<VerificationToken> findByUser(User user);
	
    /**
     * Deletes a verification token from the repository.
     * @param verificationToken The verification token to be deleted.
     */
	public void delete(VerificationToken verificationToken);

}
