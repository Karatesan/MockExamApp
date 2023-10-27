package com.fdmgroup.MockExam.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fdmgroup.MockExam.model.User;
import com.fdmgroup.MockExam.model.VerificationToken;
import com.fdmgroup.MockExam.repositories.VerificationTokenRepository;

@ExtendWith(MockitoExtension.class)
public class test_VerificationTokenService {

	@Mock
	private VerificationTokenRepository verificationTokenRepository;

	@InjectMocks
	private VerificationTokenServiceImpl verificationTokenService;

	// ---------------------------------------------------------------------------------------------

	@Test
	public void testSaveToken() {
		VerificationToken verificationToken = new VerificationToken();

		verificationTokenService.save(verificationToken);

		verify(verificationTokenRepository, times(1)).save(verificationToken);
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_getToken_TokenFound() {
		String token = "testToken";
		VerificationToken verificationToken = new VerificationToken();

		when(verificationTokenRepository.findByToken(token)).thenReturn(Optional.of(verificationToken));

		Optional<VerificationToken> result = verificationTokenService.getToken(token);

		assertTrue(result.isPresent());
		assertEquals(verificationToken, result.get());
		verify(verificationTokenRepository, times(1)).findByToken(token);
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_getToken_TokenNotFound() {
		String token = "nonexistentToken";

		when(verificationTokenRepository.findByToken(token)).thenReturn(Optional.empty());

		Optional<VerificationToken> result = verificationTokenService.getToken(token);

		assertFalse(result.isPresent());
		verify(verificationTokenRepository, times(1)).findByToken(token);
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_setConfirmedAt() {
		String token = "testToken";
		LocalDateTime time = LocalDateTime.now();
		when(verificationTokenRepository.updateConfirmedAt(token, time)).thenReturn(1);

		int result = verificationTokenService.setConfirmedAt(token, time);

		verify(verificationTokenRepository, times(1)).updateConfirmedAt(token, time);
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_findByUser() {
		User user = new User();
		List<VerificationToken> tokens = new ArrayList<>();

		when(verificationTokenRepository.findByUser(user)).thenReturn(tokens);

		List<VerificationToken> result = verificationTokenService.findByUser(user);

		assertEquals(tokens, result);
		verify(verificationTokenRepository, times(1)).findByUser(user);
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_deleteToken() {
		VerificationToken verificationToken = new VerificationToken();

		verificationTokenService.delete(verificationToken);

		verify(verificationTokenRepository, times(1)).delete(verificationToken);
	}
}
