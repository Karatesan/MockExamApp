package com.fdmgroup.MockExam.services;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.fdmgroup.MockExam.dto.AuthenticationRequestDTO;
import com.fdmgroup.MockExam.dto.AuthenticationResponseDTO;
import com.fdmgroup.MockExam.dto.ChangePasswordRequestDTO;
import com.fdmgroup.MockExam.dto.ChangePasswordResponseDTO;
import com.fdmgroup.MockExam.dto.ConfirmationResponseDTO;
import com.fdmgroup.MockExam.dto.PasswordResetRequestDTO;
import com.fdmgroup.MockExam.dto.PasswordResetResponseDTO;
import com.fdmgroup.MockExam.dto.RegisterRequestDTO;
import com.fdmgroup.MockExam.dto.RequestPasswordResetRequestDTO;
import com.fdmgroup.MockExam.dto.RequestPasswordResetResponseDTO;
import com.fdmgroup.MockExam.dto.ResendLinkResponseDTO;
import com.fdmgroup.MockExam.exceptions.InvalidPasswordOrEmailException;
import com.fdmgroup.MockExam.exceptions.NewPasswordIdenticalWithOldException;
import com.fdmgroup.MockExam.exceptions.PasswordsNotIdenticalException;
import com.fdmgroup.MockExam.exceptions.TokenExpiredException;
import com.fdmgroup.MockExam.exceptions.UserAccountLockedException;
import com.fdmgroup.MockExam.exceptions.UserDisabledException;
import com.fdmgroup.MockExam.exceptions.UserNotFoundException;
import com.fdmgroup.MockExam.model.MessageToSend;
import com.fdmgroup.MockExam.model.PasswordResetToken;
import com.fdmgroup.MockExam.model.Role;
import com.fdmgroup.MockExam.model.User;
import com.fdmgroup.MockExam.model.VerificationToken;
import com.fdmgroup.MockExam.repositories.UserRepository;
import com.fdmgroup.MockExam.validators.ObjectValidator;

@ExtendWith(MockitoExtension.class)
public class test_AuthenticationService {
	@Mock
	private UserRepository repository;

	@Mock
	private PasswordEncoder passwordEncoder;

	@Mock
	private JwtService jwtService;

	@Mock
	private AuthenticationManager authManager;

	@Mock
	private UserService userService;

	@Mock
	private MailService mailService;

	@Mock
	private ObjectValidator<User> userValidator;

	@Mock
	private VerificationTokenService verificationTokenService;

	@Mock
	private PasswordResetTokenService passwordResetTokenService;

	@InjectMocks
	private AuthenticationServiceImpl authenticationService;
	MessageToSend message;

	RequestPasswordResetRequestDTO request = new RequestPasswordResetRequestDTO();
	PasswordResetRequestDTO passwordReset = new PasswordResetRequestDTO();

	@BeforeEach
	public void setup() {
		message = new MessageToSend();

	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_register_Success() {
		RegisterRequestDTO request = new RegisterRequestDTO();
		request.setEmail("test@example.com");
		request.setPassword("testPassword");
		request.setConfirmPassword("testPassword");

		User user = new User();
		user.setEmail(request.getEmail());
		user.setPassword("encodedPassword");
		user.setRole(Role.TRAINEE);
		user.setVerified(false);
		LocalDateTime time = LocalDateTime.now();

		String token = "verificationToken";

		Map<String, Object> extraClaim = new HashMap<String, Object>();
		extraClaim.put("firstname", user.getFirstName());

		when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");
		when(jwtService.generateToken(any(), any())).thenReturn(token);
		when(mailService.prepareVerificationMail(any(), any())).thenReturn(message);

		AuthenticationResponseDTO result = authenticationService.register(request, time);

		assertNotNull(result);
		verify(userService, times(1)).register(any(User.class));
		verify(verificationTokenService, times(1)).save(any(VerificationToken.class));
		verify(mailService, times(1)).sendEmail(message);
		verify(jwtService, times(1)).generateToken(any(), any());
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_authenticate_Success() {
		AuthenticationRequestDTO request = new AuthenticationRequestDTO();
		request.setEmail("test@example.com");
		request.setPassword("testPassword");

		User user = new User();
		user.setEmail(request.getEmail());
		String token = "verificationToken";
		when(repository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
		when(jwtService.generateToken(any(), eq(user))).thenReturn(token);

		AuthenticationResponseDTO result = authenticationService.authenticate(request);

		assertNotNull(result);
		verify(jwtService, times(1)).generateToken(any(), eq(user));
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_verifyToken_Success() {
		String token = "verificationToken";
		User user = new User();
		user.setEmail("test@test.test");
		user.setVerified(false);
		LocalDateTime time = LocalDateTime.now();
		VerificationToken verificationToken = new VerificationToken();
		verificationToken.setToken(token);
		verificationToken.setUser(user);
		verificationToken.setConfirmedAt(null);
		verificationToken.setExpiresAt(time.plusMinutes(30));
		List<VerificationToken> tokens = new ArrayList<>();
		tokens.add(verificationToken);

		when(verificationTokenService.findByUser(any())).thenReturn(tokens);
		when(verificationTokenService.getToken(token)).thenReturn(Optional.of(verificationToken));
		when(userService.enableUser(user.getEmail())).thenReturn(1);

		ConfirmationResponseDTO result = authenticationService.verifyToken(token, time);

		assertNotNull(result);
		assertTrue(result.getConfirmationMessage().contains("verified"));
		verify(verificationTokenService, times(1)).getToken(token);
		verify(verificationTokenService, times(1)).setConfirmedAt(token, time);
		verify(verificationTokenService, times(1)).delete(verificationToken);
		verify(userService, times(1)).enableUser(user.getEmail());
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_resendVerificationLink_Success() {
		User user = new User();
		user.setEmail("test@test.test");
		user.setVerified(false);
		LocalDateTime time = LocalDateTime.now();

		String token = "oldToken";
		VerificationToken oldVerificationToken = new VerificationToken();
		oldVerificationToken.setToken(token);
		oldVerificationToken.setUser(user);
		oldVerificationToken.setConfirmedAt(null);
		oldVerificationToken.setExpiresAt(time.minusMinutes(15));

		when(verificationTokenService.getToken(token)).thenReturn(Optional.of(oldVerificationToken));

		String newToken = "newToken";

		when(userService.generateToken()).thenReturn(newToken);

		ResendLinkResponseDTO result = authenticationService.resendVerificationLink(token, time);

		assertNotNull(result);
		assertTrue(result.getResendLinkMessage().contains("resent"));

		verify(verificationTokenService, times(1)).getToken(token);
		verify(verificationTokenService, times(1)).save(any(VerificationToken.class));
		verify(verificationTokenService, times(1)).delete(oldVerificationToken);
		verify(mailService, times(1)).sendEmail(any());

	}

	// ---------------------------------------------------------------------------------------------

	@Test
	void test_resendVerificationLink_InvalidToken() {
		String invalidToken = "invalidToken";
		LocalDateTime currentTime = LocalDateTime.now();

		when(verificationTokenService.getToken(invalidToken)).thenReturn(Optional.empty());

		assertThrows(IllegalStateException.class, () -> {
			authenticationService.resendVerificationLink(invalidToken, currentTime);
		});

	}

	// ---------------------------------------------------------------------------------------------

	@Test
	void test_resendVerificationLink_UserAlreadyVerified() {
		String token = "validToken";
		LocalDateTime currentTime = LocalDateTime.now();
		User user = new User();
		VerificationToken oldToken = new VerificationToken(token, currentTime.minusHours(1), currentTime.plusHours(1),
				user);
		user.setVerified(true);

		when(verificationTokenService.getToken(token)).thenReturn(Optional.of(oldToken));

		assertThrows(IllegalStateException.class, () -> {
			authenticationService.resendVerificationLink(token, currentTime);
		});

	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_requestPasswordReset_Success() {
		RequestPasswordResetRequestDTO request = new RequestPasswordResetRequestDTO();
		request.setEmail("test@example.com");
		User user = new User();
		user.setEmail(request.getEmail());
		PasswordResetToken passwordResetToken = new PasswordResetToken();
		String token = "resetToken";
		LocalDateTime time = LocalDateTime.now();

		when(userService.generateToken()).thenReturn(token);
		when(repository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
		when(passwordResetTokenService.save(any(PasswordResetToken.class))).thenReturn(passwordResetToken);
		when(mailService.preparePasswordResetMail(any(), any())).thenReturn(message);

		RequestPasswordResetResponseDTO result = authenticationService.requestPasswordReset(request, time);

		assertNotNull(result);
		assertTrue(result.getResponseMessage().contains(user.getEmail()));
		verify(userService, times(1)).generateToken();
		verify(passwordResetTokenService, times(1)).save(any(PasswordResetToken.class));
		verify(mailService, times(1)).sendEmail(message);
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_resetPassword_Success() {
		String token = "resetToken";
		LocalDateTime time = LocalDateTime.now();

		PasswordResetRequestDTO request = new PasswordResetRequestDTO();
		request.setPassword("newPassword");
		request.setConfirmPassword("newPassword");
		User user = new User();
		user.setEmail("test@test.test");
		PasswordResetToken passwordResetToken = new PasswordResetToken();
		passwordResetToken.setToken(token);
		passwordResetToken.setUser(user);
		passwordResetToken.setExpiresAt(time);
		passwordResetToken.setResetAt(null);

		when(passwordResetTokenService.getToken(token)).thenReturn(Optional.of(passwordResetToken));
		when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");
		when(mailService.preparePasswordChangeMail(any())).thenReturn(message);

		PasswordResetResponseDTO result = authenticationService.resetPassword(request, token, time);

		assertNotNull(result);
		assertTrue(result.getResponseMessage().contains("change successful"));
		verify(passwordResetTokenService, times(1)).getToken(token);
		verify(userService, times(1)).updatePassword(any(), any());
		verify(passwordResetTokenService, times(1)).setResetAt(token, time);
		verify(mailService, times(1)).sendEmail(message);
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	void testResetPassword_TokenExpired() {
		String token = "validToken";
		LocalDateTime currentTime = LocalDateTime.now();
		User user = new User();
		PasswordResetToken passwordResetToken = new PasswordResetToken(token, currentTime, currentTime.minusMinutes(30),
				user); // Ustawienie wygasłego tokenu

		when(passwordResetTokenService.getToken(token)).thenReturn(Optional.of(passwordResetToken));

		assertThrows(TokenExpiredException.class, () -> {
			authenticationService.resetPassword(new PasswordResetRequestDTO(), token, currentTime);
		});

	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_changePassword_Success() {
		String header = "Bearer jwtToken";
		String userEmail = "test@example.com";

		ChangePasswordRequestDTO request = new ChangePasswordRequestDTO();
		request.setOldPassword("oldPassword");
		request.setPassword("newPassword");
		request.setConfirmPassword("newPassword");
		User user = new User();

		user.setEmail(userEmail);

		when(jwtService.extractUserEmailFromHeader(header)).thenReturn(userEmail);
		when(repository.findByEmail(userEmail)).thenReturn(Optional.of(user));
		when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedNewPassword");
		when(mailService.preparePasswordChangeMail(any())).thenReturn(message);

		ChangePasswordResponseDTO result = authenticationService.changePassword(request, header);

		assertNotNull(result);
		assertTrue(result.getResponseMessage().contains("change successful"));
		verify(jwtService, times(1)).extractUserEmailFromHeader(header);
		verify(userService, times(1)).updatePassword(any(), any());
		verify(mailService, times(1)).sendEmail(message);
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_register_PasswordsNotIdenticalException() {
		RegisterRequestDTO request = new RegisterRequestDTO();
		LocalDateTime time = LocalDateTime.now();

		request.setPassword("password1");
		request.setConfirmPassword("password2");

		assertThrows(PasswordsNotIdenticalException.class, () -> authenticationService.register(request, time));
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_verifyToken_TokenNotFound() {
		String token = "someToken";
		LocalDateTime time = LocalDateTime.now();

		when(verificationTokenService.getToken(token)).thenReturn(Optional.empty());

		assertThrows(IllegalStateException.class, () -> authenticationService.verifyToken(token, time));
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_verifyToken_EmailAlreadyConfirmed() {
		String token = "someToken";
		VerificationToken verificationToken = new VerificationToken();
		User user = new User();
		LocalDateTime time = LocalDateTime.now();

		user.setVerified(true);
		verificationToken.setConfirmedAt(LocalDateTime.now());
		verificationToken.setUser(new User());
		when(verificationTokenService.getToken(token)).thenReturn(Optional.of(verificationToken));

		assertThrows(IllegalStateException.class, () -> authenticationService.verifyToken(token, time));
	}

	// ---------------------------------------------------------------------------------------------
	@Test
	public void test_verifyToken_VerificationTokenExpired() {
		String token = "someToken";
		User user = new User();

		LocalDateTime time = LocalDateTime.now();

		VerificationToken expiredToken = new VerificationToken();
		user.setVerified(false);
		expiredToken.setConfirmedAt(null);
		expiredToken.setUser(user);
		expiredToken.setExpiresAt(time.minusHours(1)); // Expired one hour ago

		when(verificationTokenService.getToken(token)).thenReturn(Optional.of(expiredToken));

		assertThrows(TokenExpiredException.class, () -> authenticationService.verifyToken(token, time));

	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_authenticate_InvalidPasswordOrEmailException() {
		AuthenticationRequestDTO request = new AuthenticationRequestDTO();
		request.setEmail("user@example.com");
		request.setPassword("password");
		when(repository.findByEmail(request.getEmail())).thenReturn(Optional.empty());

		assertThrows(NoSuchElementException.class, () -> authenticationService.authenticate(request));
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_verifyToken_EmailIsAlreadyConfirmed() {
		String token = "someToken";
		VerificationToken verificationToken = new VerificationToken();
		LocalDateTime time = LocalDateTime.now();
		User user = new User();

		user.setVerified(false);
		verificationToken.setConfirmedAt(LocalDateTime.now());
		verificationToken.setUser(new User());
		verificationToken.setExpiresAt(LocalDateTime.now().plusMinutes(10));
		when(verificationTokenService.getToken(token)).thenReturn(Optional.of(verificationToken));

		assertThrows(IllegalStateException.class, () -> authenticationService.verifyToken(token, time));
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_requestPasswordReset_cannotFindUserByEmail() {
		LocalDateTime time = LocalDateTime.now();

		when(repository.findByEmail(Mockito.any())).thenReturn(Optional.empty());

		assertThrows(UserNotFoundException.class, () -> authenticationService.requestPasswordReset(request, time));
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_resetPassword_passwordResetTokenGetResetAtNotNull() {
		PasswordResetToken passwordResetToken = new PasswordResetToken();
		LocalDateTime time = LocalDateTime.now();
		passwordResetToken.setResetAt(time);
		when(passwordResetTokenService.getToken(anyString())).thenReturn(Optional.of(passwordResetToken));

		assertThrows(IllegalStateException.class,
				() -> authenticationService.resetPassword(passwordReset, "token", time));

	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_resetPassword_passwordNotEqualsToConfirmPassword() {
		PasswordResetToken passwordResetToken = new PasswordResetToken();
		LocalDateTime time = LocalDateTime.now();

		passwordResetToken.setExpiresAt(time.plusMinutes(30));
		passwordReset.setPassword("pass");
		passwordReset.setConfirmPassword("confPass");
		when(passwordResetTokenService.getToken(anyString())).thenReturn(Optional.of(passwordResetToken));

		assertThrows(PasswordsNotIdenticalException.class,
				() -> authenticationService.resetPassword(passwordReset, "token", time));

	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_resetPassword_passInvalidTokenString_IllegalStateException() {
		LocalDateTime time = LocalDateTime.now();

		when(passwordResetTokenService.getToken(anyString())).thenReturn(Optional.empty());

		assertThrows(IllegalStateException.class,
				() -> authenticationService.resetPassword(passwordReset, "token", time));

	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_changePassword_passwordNotEqualsToConfirmPassword() {
		ChangePasswordRequestDTO changePass = new ChangePasswordRequestDTO();
		User user = new User();
		user.setEmail("test@test.test");
		changePass.setPassword("pass");
		changePass.setConfirmPassword("confPass");
		when(jwtService.extractUserEmailFromHeader(anyString())).thenReturn(user.getEmail());

		assertThrows(PasswordsNotIdenticalException.class,
				() -> authenticationService.changePassword(changePass, "header"));

	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_checkPassword_BadCredentialsException() {
		String email = "user@example.com";
		String password = "password";
		doThrow(BadCredentialsException.class).when(authManager).authenticate(any());

		assertThrows(InvalidPasswordOrEmailException.class, () -> authenticationService.checkPassword(email, password));
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_checkPassword_DisabledException() {
		String email = "user@example.com";
		String password = "password";
		doThrow(DisabledException.class).when(authManager).authenticate(any());

		assertThrows(UserDisabledException.class, () -> authenticationService.checkPassword(email, password));
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_checkPassword_LockedException() {
		String email = "user@example.com";
		String password = "password";
		doThrow(LockedException.class).when(authManager).authenticate(any());

		assertThrows(UserAccountLockedException.class, () -> authenticationService.checkPassword(email, password));
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_updatePassword_IdenticalPasswords_Exception() {
		User user = new User();
		user.setPassword("password");

		when(passwordEncoder.matches("password", "password")).thenReturn(true);

		assertThrows(NewPasswordIdenticalWithOldException.class,
				() -> authenticationService.updatePassword("password", user));
	}

}
