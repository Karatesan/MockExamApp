package com.fdmgroup.MockExam.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fdmgroup.MockExam.model.User;
import com.fdmgroup.MockExam.model.VerificationToken;

/**
 * Repository interface for managing VerificationToken entities in the database.
 * Extends JpaRepository to inherit basic CRUD operations.
 */
@Repository
@Transactional(readOnly = true)
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long>{
	
    /**
     * Find a VerificationToken entity by its token.
     * @param token The token associated with the verification token to search for.
     * @return An Optional containing the VerificationToken entity matching the provided token, or an empty Optional if not found.
     */
	Optional<VerificationToken> findByToken(String token);
	
    /**
     * Update the confirmedAt time stamp for a VerificationToken.
     * @param token The token associated with the verification token.
     * @param confirmedAt The new confirmedAt time stamp.
     * @return The number of affected rows in the database after the update.
     */
	@Transactional
	@Modifying
	@Query("Update VerificationToken v " + 
			"SET v.confirmedAt = ?2 " + 
			"WHERE v.token =?1")
	int updateConfirmedAt(String token, LocalDateTime confirmedAt);
	
    /**
     * Find all VerificationToken entities associated with a User.
     * @param user The User object for which verification tokens are to be retrieved.
     * @return A list of VerificationToken objects associated with the provided User.
     */
	List<VerificationToken> findByUser(User user);
	
}
