package com.fdmgroup.MockExam.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import com.fdmgroup.MockExam.model.MessageToSend;
import com.fdmgroup.MockExam.model.PasswordResetToken;
import com.fdmgroup.MockExam.model.User;
import com.fdmgroup.MockExam.model.VerificationToken;

@ExtendWith(MockitoExtension.class)
public class test_MailService {
	@InjectMocks
	private MailServiceImpl mailService;

	String email = "mockexam.noreply@gmail.com";

	MessageToSend message;

	User user;

	@BeforeEach
	public void setUp() {
		message = new MessageToSend();
		message.setTo(email);
		message.setSubject("test subject");
		message.setBody("test body");
		user = new User();
		user.setEmail(email);
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_sendEmail_pushCorrectEmail_checkIfMailHasBeenSent_returnTrue() {
		boolean actual = mailService.sendEmail(message);

		assertEquals(true, actual);
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_sendEmail_pushIncorrectEmail_checkIfMailHasNotBeenSent_returnFalse() {
		message.setTo("mailDoesntExist");

		boolean actual = mailService.sendEmail(message);

		assertEquals(false, actual);
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_prepareVerificationMail() {

		VerificationToken token = new VerificationToken();
		token.setToken("verificationToken");

		MessageToSend result = mailService.prepareVerificationMail(user, token);

		assertNotNull(result);
		assertEquals(user.getEmail(), result.getTo());
		assertTrue(result.getSubject().contains("Verification link"));
		assertTrue(result.getBody().contains("verificationToken"));
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_preparePasswordResetMail() {

		PasswordResetToken token = new PasswordResetToken();
		token.setToken("resetToken");

		MessageToSend result = mailService.preparePasswordResetMail(user, token);

		assertNotNull(result);
		assertEquals(user.getEmail(), result.getTo());
		assertTrue(result.getSubject().contains("Password Reset Link"));
		assertTrue(result.getBody().contains("resetToken"));
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_preparePasswordChangeMail() {

		MessageToSend result = mailService.preparePasswordChangeMail(user);

		assertNotNull(result);
		assertEquals(user.getEmail(), result.getTo());
		assertTrue(result.getSubject().contains("Password For MockExamGenerator was changed"));
		assertTrue(result.getBody().contains("changed"));
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_deleteAccount() {
		MessageToSend result = mailService.deleteAccount(user);
		assertNotNull(result);
		assertEquals(user.getEmail(), result.getTo());
		assertEquals("Your account has been deleted.", result.getSubject());
		assertTrue(
				result.getBody().contains("Your account has been deleted from the database of MockExam Application."));
		assertTrue(result.getBody().contains("Thank you for using our application!"));
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_examAccess() {
		MessageToSend result = mailService.examAccess(user, "categoryString");
		assertNotNull(result);
		assertEquals(user.getEmail(), result.getTo());
		assertEquals("Access to exam has been granted.", result.getSubject());
		assertEquals("Now you have an access to exams in category categoryString.", result.getBody());

	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_questionReport() {
		MessageToSend result = mailService.questionReport(user, "categoryString");
		assertNotNull(result);
		assertEquals(user.getEmail(), result.getTo());
		assertEquals("Your question report has been handled.", result.getSubject());
		assertTrue(
				result.getBody().contains("Thank you for your opinion about question from category categoryString."));
		assertTrue(result.getBody().contains("We've updated it."));

	}

}
