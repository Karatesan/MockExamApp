package com.fdmgroup.MockExam.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fdmgroup.MockExam.model.PasswordResetToken;
import com.fdmgroup.MockExam.repositories.PasswordResetTokenRepository;

@ExtendWith(MockitoExtension.class)
public class test_PasswordResetTokenService {
	@Mock
	private PasswordResetTokenRepository passwordResetTokenRepository;

	@InjectMocks
	private PasswordResetTokenServiceImpl passwordResetTokenService;

	@BeforeEach
	public void setup() {
//		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void test_saveToken() {
		PasswordResetToken passwordResetToken = new PasswordResetToken();

		passwordResetTokenService.save(passwordResetToken);

		verify(passwordResetTokenRepository, times(1)).save(passwordResetToken);
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_getToken_TokenFound() {
		String token = "testToken";
		PasswordResetToken passwordResetToken = new PasswordResetToken();

		when(passwordResetTokenRepository.findByToken(token)).thenReturn(Optional.of(passwordResetToken));

		Optional<PasswordResetToken> result = passwordResetTokenService.getToken(token);

		assertTrue(result.isPresent());
		assertEquals(passwordResetToken, result.get());
		verify(passwordResetTokenRepository, times(1)).findByToken(token);
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_getToken_TokenNotFound() {
		String token = "nonexistentToken";

		when(passwordResetTokenRepository.findByToken(token)).thenReturn(Optional.empty());

		Optional<PasswordResetToken> result = passwordResetTokenService.getToken(token);

		assertFalse(result.isPresent());
		verify(passwordResetTokenRepository, times(1)).findByToken(token);
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_setResetAt() {
		String token = "testToken";
		LocalDateTime time = LocalDateTime.now();

		when(passwordResetTokenRepository.updateResetAt(token, time)).thenReturn(0);

		int result = passwordResetTokenService.setResetAt(token,time);

		assertEquals(0, result);

		verify(passwordResetTokenRepository, times(1)).updateResetAt(token, time);
	}
}
