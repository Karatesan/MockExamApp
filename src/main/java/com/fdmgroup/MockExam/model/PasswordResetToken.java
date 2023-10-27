package com.fdmgroup.MockExam.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a Password Reset Token entity in the database.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
public class PasswordResetToken {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "password_reset_token_id")
	@SequenceGenerator(name= "password_reset_token_sequence", allocationSize = 1)
	private Long id;
	
	@Column(nullable=false)
	private String token;
	
	@Column(nullable=false)
	private LocalDateTime createdAt;
	
	@Column(nullable=false)
	private LocalDateTime expiresAt;
	
	@Column
	private LocalDateTime resetAt;
	
	@ManyToOne
	@JoinColumn(
			nullable = false,
			name = "user_id"
			
			)
	private User user;
	
    /**
     * Constructs a PasswordResetToken object with the specified token, creation time, expiration time, and user.
     * @param token The token string used for password reset.
     * @param createdAt The time stamp when the token was created.
     * @param expiresAt The time stamp when the token expires.
     * @param user The User object associated with the token.
     */
	public PasswordResetToken(String token,
							LocalDateTime createdAt,
							LocalDateTime expiresAt,
							User user) {
		this.token= token;
		this.createdAt = createdAt;
		this.expiresAt = expiresAt;
		this.user = user;
	
	}
	
}
