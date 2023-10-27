package com.fdmgroup.MockExam.repositories;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fdmgroup.MockExam.model.PasswordResetToken;

/**
 * Repository interface for managing PasswordResetToken entities in the database.
 * Extends JpaRepository to inherit basic CRUD operations.
 */
@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long>{

    /**
     * Find a PasswordResetToken entity by its token.
     * @param token The token associated with the password reset request.
     * @return An Optional containing the PasswordResetToken entity matching the provided token, or an empty Optional if not found.
     */
	Optional<PasswordResetToken> findByToken(String token);
	
    /**
     * Update the resetAt time stamp for a PasswordResetToken.
     * @param token The token associated with the password reset request.
     * @param resetAt The new resetAt time stamp.
     * @return The number of affected rows in the database after the update.
     */
	@Transactional
	@Modifying
	@Query("Update PasswordResetToken p " + 
			"SET p.resetAt = ?2 " +
			"WHERE p.token =?1")
	int updateResetAt(String token, LocalDateTime resetAt);
}

