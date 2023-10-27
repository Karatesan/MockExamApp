package com.fdmgroup.MockExam.services;

import com.fdmgroup.MockExam.model.MessageToSend;
import com.fdmgroup.MockExam.model.PasswordResetToken;
import com.fdmgroup.MockExam.model.User;
import com.fdmgroup.MockExam.model.VerificationToken;

/**
 * Interface to define the contract for the MailService.
 * Provides methods to prepare and send email(s).
 */
public interface MailService {

    /**
     * Prepares an email message for email verification.
     * @param user The user for whom the verification email is intended.
     * @param token The verification token to be included in the email.
     * @return A message to be sent for email verification.
     */
	MessageToSend prepareVerificationMail(User user, VerificationToken verificationToken);

    /**
     * Sends an email using the provided message.
     * @param messageToSend The message to be sent.
     * @return {@code true} if the email was sent successfully, {@code false} otherwise.
     */
	boolean sendEmail(MessageToSend message);

    /**
     * Prepares an email message for password reset.
     * @param user The user for whom the password reset email is intended.
     * @param passwordResetToken The password reset token to be included in the email.
     * @return A message to be sent for password reset.
     */
	MessageToSend preparePasswordResetMail(User user, PasswordResetToken passwordResetToken);

    /**
     * Prepares an email message for notifying a user about a password change.
     * @param user The user for whom the password change notification is intended.
     * @return A message to be sent for password change notification.
     */
	MessageToSend preparePasswordChangeMail(User user);
	
	MessageToSend deleteAccount(User user);
	
	MessageToSend examAccess(User user, String category);
	
	MessageToSend questionReport(User user, String category);

}
