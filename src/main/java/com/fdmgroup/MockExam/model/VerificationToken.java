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
 * Represents a Verification Token entity in the database.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
public class VerificationToken {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "verfication_token_id")
	@SequenceGenerator(name ="verfication_token_sequence", allocationSize = 1)
	private Long id;
	
	@Column(nullable=false)
	private String token;
	
	@Column(nullable=false)
	private LocalDateTime createdAt;
	
	@Column(nullable=false)
	private LocalDateTime expiresAt;
	
	private LocalDateTime confirmedAt;
	
	@ManyToOne
	@JoinColumn(
			nullable = false,
			name = "user_id"
			)
	private User user;
	
    /**
     * Constructs a VerificationToken object with the specified token, creation time, expiration time, and user.
     * @param token The token string used for verification.
     * @param createdAt The time stamp when the token was created.
     * @param expiresAt The time stamp when the token expires.
     * @param user The User object associated with the token.
     */
	public VerificationToken(String token,
							LocalDateTime createdAt,
							LocalDateTime expiresAt,
							User user) {
		this.token= token;
		this.createdAt = createdAt;
		this.expiresAt = expiresAt;
		this.user = user;
	}

	@Override
	public String toString() {
		return "VerificationToken [id=" + id + ", token=" + token + ", createdAt=" + createdAt + ", expiresAt="
				+ expiresAt + ", confirmedAt=" + confirmedAt + ", user=" + user + "]";
	}
	
}
