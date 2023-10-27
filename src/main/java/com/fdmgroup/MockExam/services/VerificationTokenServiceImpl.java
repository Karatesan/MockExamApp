package com.fdmgroup.MockExam.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.fdmgroup.MockExam.model.User;
import com.fdmgroup.MockExam.model.VerificationToken;
import com.fdmgroup.MockExam.repositories.VerificationTokenRepository;

import lombok.AllArgsConstructor;

/**
 * The class implements the VerificationTokenService interface.
 * Provides services related to user verification tokens.
 */
@Service
@AllArgsConstructor
public class VerificationTokenServiceImpl implements VerificationTokenService {

	private final VerificationTokenRepository verificationTokenRepository;

	@Override
	public void save(VerificationToken verificationToken) {
		verificationTokenRepository.save(verificationToken);
	}

	@Override
	public Optional<VerificationToken> getToken(String token) {
		return verificationTokenRepository.findByToken(token);
	}

	@Override
	public int setConfirmedAt(String token, LocalDateTime time) {
		return verificationTokenRepository.updateConfirmedAt(token, time);
	}

	@Override
	public List<VerificationToken> findByUser(User user) {
		return verificationTokenRepository.findByUser(user);
	}

	@Override
	public void delete(VerificationToken verificationToken) {
		verificationTokenRepository.delete(verificationToken);
	}

}
