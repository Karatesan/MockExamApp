package com.fdmgroup.MockExam.services;

import java.time.LocalDateTime;

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
import com.fdmgroup.MockExam.model.User;

/**
 * Interface to define the contract for the AuthenticationService.
 * Provides authentication and user-related operations.
 */
public interface AuthenticationService {

	/**
	 * Registers a new user with the provided request and time stamp.
	 * @param request The registration request containing user details.
	 * @param time The time stamp when the registration request is made.
	 * @return An authentication response indicating the success of the registration.
	 */
	AuthenticationResponseDTO register(RegisterRequestDTO request, LocalDateTime time);

	/**
	 * Authenticates a user with the provided authentication request.
	 * @param request The authentication request containing user credentials.
	 * @return An authentication response containing a JWT token upon successful authentication.
	 */
	AuthenticationResponseDTO authenticate(AuthenticationRequestDTO request);

	/**
	 * Verifies a user's account using a verification token.
	 * @param token The verification token to be verified.
	 * @param time The current time stamp.
	 * @return A confirmation response indicating the success of the verification.
	 */
	ConfirmationResponseDTO verifyToken(String token, LocalDateTime time);
	
	/**
	 * Resends a verification link to a user's email address (after previous link expires).
	 * @param token The token associated with the user's email address.
	 * @param time The current time stamp.
	 * @return A response indicating the success of sending new verification link.
	 */
	ResendLinkResponseDTO resendVerificationLink(String token, LocalDateTime time);

	/**
	 * Requests a password reset for a user with the provided email address.
	 * @param request The request containing the user's email address.
	 * @param time The current time stamp.
	 * @return A response indicating the success of the password reset request.
	 */
	RequestPasswordResetResponseDTO requestPasswordReset(RequestPasswordResetRequestDTO request, LocalDateTime time);

	/**
	 * Changes the password for the user identified by the provided JWT header.
	 * @param request The request containing the old and new passwords.
	 * @param header The JWT token in the header identifying the user.
	 * @return A response indicating the success of the password change.
	 */
	ChangePasswordResponseDTO changePassword(ChangePasswordRequestDTO request, String header);
	
	/**
	 * Checks the validity of a user's email and password combination.
	 * @param email The user's email address.
	 * @param password The user's password.
	 */
	void checkPassword(String email, String password);

	/**
	 * Resets a user's password using a valid password reset token.
	 * @param request The request containing the new password and confirmation.
	 * @param token The token used for validation.
	 * @param time The current time stamp.
	 * @return A response indicating the success of the password reset.
	 */
	PasswordResetResponseDTO resetPassword(PasswordResetRequestDTO request, String token, LocalDateTime time);

	/**
	 * Checks if a new password is identical to the user's old password.
	 * @param password The new password to be checked.
	 * @param user The user entity containing the old password.
	 * @return True if the new password is identical to the old password, false otherwise.
	 */
	boolean oldPasswordIdenticalToNewPasswordCheck(String password, User user);

	/**
	 * Updates a user's password with a new one, after ensuring it is not identical to the old password.
	 * @param newPassword The new password to be set for the user.
	 * @param user The user entity whose password needs to be updated.
	 * @return A message indicating the success of the password update.
	 */
	String updatePassword(String newPassword, User user);

}