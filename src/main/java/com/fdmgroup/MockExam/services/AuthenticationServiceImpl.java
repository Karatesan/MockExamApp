package com.fdmgroup.MockExam.services;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
import com.fdmgroup.MockExam.model.PasswordResetToken;
import com.fdmgroup.MockExam.model.Role;
import com.fdmgroup.MockExam.model.User;

import com.fdmgroup.MockExam.model.VerificationToken;

import com.fdmgroup.MockExam.repositories.UserRepository;
import com.fdmgroup.MockExam.validators.ObjectValidator;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

/**
 * This class implements the AuthenticationService interface and provides
 * authentication and user-related operations.
 */
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

	private final UserRepository repository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	private final AuthenticationManager authManager;
	private final UserService userService;
	private final MailService mailService;
	private final ObjectValidator<User> userValidator;
	private final VerificationTokenService verificationTokenService;
	private final PasswordResetTokenService passwordResetTokenService;

	@Override
	public AuthenticationResponseDTO register(RegisterRequestDTO request, LocalDateTime time) {

		if (!(request.getConfirmPassword().equals(request.getPassword()))) {
			throw new PasswordsNotIdenticalException("Password and confirm password are not identical!");
		}

		var user = User.builder().firstName(request.getFirstname()).lastName(request.getLastname())
				.email(request.getEmail()).password(passwordEncoder.encode(request.getPassword())).role(Role.TRAINEE)
				.build();

		user.setVerified(false);
//		user.setB(false);
		userValidator.validate(user);
		userService.register(user);
		// Create Verification Token

		String token = userService.generateToken();

		VerificationToken verificationToken = new VerificationToken(

				token, time, time.plusMinutes(15), user);

		verificationTokenService.save(verificationToken);

		mailService.sendEmail(mailService.prepareVerificationMail(user, verificationToken));

		// Testing this
		Map<String, Object> extraClaim = new HashMap<String, Object>();
		extraClaim.put("firstname", user.getFirstName());

		var jwtToken = jwtService.generateToken(extraClaim, user);
		return AuthenticationResponseDTO.builder().token(jwtToken).build();
	}

	@Override
	public AuthenticationResponseDTO authenticate(AuthenticationRequestDTO request) {
		// This does the entire authentication process -> looks for email, compares
		// password etc.
		checkPassword(request.getEmail(), request.getPassword());

		var user = repository.findByEmail(request.getEmail()).orElseThrow();

		Map<String, Object> extraClaim = new HashMap<String, Object>();
		extraClaim.put("firstname", user.getFirstName());
		extraClaim.put("lastname", user.getLastName());
		extraClaim.put("role", user.getRole());

		var jwtToken = jwtService.generateToken(extraClaim, user);
		return AuthenticationResponseDTO.builder().token(jwtToken).build();
	}

	@Override
	@Transactional
	public ConfirmationResponseDTO verifyToken(String token, LocalDateTime time) {
		VerificationToken verificationToken = verificationTokenService.getToken(token)
				.orElseThrow(() -> new IllegalStateException("Invalid verification link!"));

		if (verificationToken.getConfirmedAt() != null || verificationToken.getUser().getVerified() == true) {
			throw new IllegalStateException("Email is already confirmed!");
		}

		LocalDateTime expiredAt = verificationToken.getExpiresAt();

		if (expiredAt.isBefore(time)) {
			throw new TokenExpiredException("Verification token is expired!");
		}


		verificationTokenService.setConfirmedAt(token, time);

		userService.enableUser(verificationToken.getUser().getEmail());

		List<VerificationToken> tokens = verificationTokenService.findByUser(verificationToken.getUser());
		for (int i = 0; i < tokens.size(); i++) {
			verificationTokenService.delete(tokens.get(i));
		}

		return ConfirmationResponseDTO.builder().confirmationMessage("Email account is verified now!").build();
	}

	@Override
	@Transactional
	public ResendLinkResponseDTO resendVerificationLink(String token, LocalDateTime time) {
		Optional<VerificationToken> verificationTokenOptional = verificationTokenService.getToken(token);

		if (verificationTokenOptional.isEmpty()) {
			throw new IllegalStateException("Invalid verification token!");
		}

		VerificationToken oldVerificationToken = verificationTokenOptional.get();
		User user = oldVerificationToken.getUser();

		// Not necessarily needed
		if (user.isEnabled()) {
			throw new IllegalStateException("User's account is already verified.");
		}

		// Generate a new verification token
		String newToken = userService.generateToken();

		VerificationToken newVerificationToken = new VerificationToken(newToken, time, time.plusMinutes(15), user);

		verificationTokenService.save(newVerificationToken);

		// Delete old token from database
		verificationTokenService.delete(oldVerificationToken);

		mailService.sendEmail(mailService.prepareVerificationMail(user, newVerificationToken));

		return ResendLinkResponseDTO.builder().resendLinkMessage("Verification link has been resent.").build();
	}

	@Override
	public RequestPasswordResetResponseDTO requestPasswordReset(RequestPasswordResetRequestDTO request,
			LocalDateTime time) {

		Optional<User> user = repository.findByEmail(request.getEmail());

		if (user.isEmpty()) {
			throw new UserNotFoundException("Failed to send reset password request");
		}

		String token = userService.generateToken();

		PasswordResetToken passwordResetToken = new PasswordResetToken(

				token, time, time.plusMinutes(30), user.get());

		passwordResetTokenService.save(passwordResetToken);

		mailService.sendEmail(mailService.preparePasswordResetMail(user.get(), passwordResetToken));

		return RequestPasswordResetResponseDTO.builder()
				.responseMessage("Password Reset Mail was sent to " + user.get().getEmail() + "!").build();
	}

	@Override
	public PasswordResetResponseDTO resetPassword(PasswordResetRequestDTO request, String token, LocalDateTime time) {

		PasswordResetToken passwordResetToken = passwordResetTokenService.getToken(token)
				.orElseThrow(() -> new IllegalStateException("Token not found"));

		if (passwordResetToken.getResetAt() != null) {
			throw new IllegalStateException("Password already reset!");
		}

		LocalDateTime expiredAt = passwordResetToken.getExpiresAt();

		if (expiredAt.isBefore(time)) {
			throw new TokenExpiredException("Password reset token is expired!");
		}

		User user = passwordResetToken.getUser();

		if (!(request.getPassword().equals(request.getConfirmPassword()))) {
			throw new PasswordsNotIdenticalException("Password and confirm password are not identical!");
		}

		String responseMessage = updatePassword(request.getPassword(), user);

		passwordResetTokenService.setResetAt(passwordResetToken.getToken(), time);

		return PasswordResetResponseDTO.builder().responseMessage(responseMessage).build();
	}

	@Override
	public ChangePasswordResponseDTO changePassword(ChangePasswordRequestDTO request, String header) {

		final String userEmail;
		userEmail = jwtService.extractUserEmailFromHeader(header);

		if (!(request.getPassword().equals(request.getConfirmPassword()))) {
			throw new PasswordsNotIdenticalException("New password and confirm password are not identical!");
		}

		checkPassword(userEmail, request.getOldPassword());

		var user = repository.findByEmail(userEmail).orElseThrow();

		String responseMessage = updatePassword(request.getPassword(), user);

		return ChangePasswordResponseDTO.builder().responseMessage(responseMessage).build();
	}

	@Override()
	public String updatePassword(String newPassword, User user) {
		if (oldPasswordIdenticalToNewPasswordCheck(newPassword, user)) {
			throw new NewPasswordIdenticalWithOldException("Please create a new password!");
		}

		userService.updatePassword(passwordEncoder.encode(newPassword), user);
		mailService.sendEmail(mailService.preparePasswordChangeMail(user));
		return "Password change successful!";
	}

	@Override()
	public boolean oldPasswordIdenticalToNewPasswordCheck(String password, User user) {
		String encodedPassword = user.getPassword();
		return passwordEncoder.matches(password, encodedPassword);
	}

	@Override
	public void checkPassword(String email, String password) {
		try {
			authManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
		} catch (BadCredentialsException e) {
			throw new InvalidPasswordOrEmailException("E-Mail or Password invalid!");
		} catch (DisabledException e) {
			throw new UserDisabledException("Your account needs to be verified!");
		} catch (LockedException e) {
			throw new UserAccountLockedException(
					"Your account has been locked! \n Please contact an Admin if you want to unlock it!");
		}
	}
}
