package com.fdmgroup.MockExam.services;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.fdmgroup.MockExam.model.PasswordResetToken;
import com.fdmgroup.MockExam.repositories.PasswordResetTokenRepository;

import lombok.AllArgsConstructor;

/**
 * The class implements the PasswordResetTokenService interface.
 * Provides methods for managing password reset tokens.
 */
@Service
@AllArgsConstructor
public class PasswordResetTokenServiceImpl implements PasswordResetTokenService{

	private final PasswordResetTokenRepository passwordResetTokenRepository;

	@Override
	public PasswordResetToken save(PasswordResetToken passwordResetToken) {
		return passwordResetTokenRepository.save(passwordResetToken);
	}

	@Override
	public Optional<PasswordResetToken> getToken(String token) {
		return passwordResetTokenRepository.findByToken(token);
	}

	@Override
	public int setResetAt(String token, LocalDateTime time) {
		return passwordResetTokenRepository.updateResetAt(token, time);
	}


}
