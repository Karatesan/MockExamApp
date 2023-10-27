package com.fdmgroup.MockExam.services;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fdmgroup.MockExam.model.MailInformation;
import com.fdmgroup.MockExam.model.MessageToSend;
import com.fdmgroup.MockExam.model.PasswordResetToken;
import com.fdmgroup.MockExam.model.User;
import com.fdmgroup.MockExam.model.VerificationToken;

/**
 * The class implements the MailService interface.
 * Provides email-related methods, including sending verification and password reset email(s).
 */
@Service
public class MailServiceImpl implements MailService {

	String frontendAddress = "http://localhost:5173/";

    /**
     * Constructs a new MailServiceImpl instance.
     */
	@Autowired
	public MailServiceImpl() {
		super();
	}

	@Override
	public MessageToSend prepareVerificationMail(User user, VerificationToken token) {

		String link =  frontendAddress + "confirm/" + token.getToken();

		MessageToSend message = new MessageToSend(user.getEmail(), "Verification link",
				"Click here to verify email: " + link);

		return message;
	}

	@Override
	public boolean sendEmail(MessageToSend messageToSend) {
		MailInformation mailInformation = new MailInformation();
		Properties properties = System.getProperties();

		properties.put("mail.smtp.host", mailInformation.getHost());
		properties.put("mail.smtp.port", mailInformation.getPort());
		properties.put("mail.smtp.ssl.enable", "true");
		properties.put("mail.smtp.auth", "true");

		Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(mailInformation.getEmail(), mailInformation.getPassword());
			}
		});
		session.setDebug(true);

		try {

			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(mailInformation.getEmail()));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(messageToSend.getTo()));
			message.setSubject(messageToSend.getSubject());
			message.setText(messageToSend.getBody());
			Transport.send(message);

		} catch (MessagingException mex) {
			mex.printStackTrace();
			return false;
		}

		return true;

	}

	@Override
	public MessageToSend preparePasswordResetMail(User user, PasswordResetToken passwordResetToken) {

		String link =  frontendAddress+"resettingPassword/" + passwordResetToken.getToken();
		

		MessageToSend message = new MessageToSend(user.getEmail(), "Password Reset Link",
				"Click here to reset your password for the MockExamGenerator: " + link);

		return message;
	}

	@Override
	public MessageToSend preparePasswordChangeMail(User user) {
		MessageToSend message = new MessageToSend(user.getEmail(), "Password For MockExamGenerator was changed",
				"The password for your MockExamGenerator account has been changed");

		return message;
	}

	@Override
	public MessageToSend deleteAccount(User user) {
		return new MessageToSend(user.getEmail(), "Your account has been deleted.",
				"Your account has been deleted from the database of MockExam Application. \n"
				+ "Thank you for using our application!");
	}

	@Override
	public MessageToSend examAccess(User user, String category) {
		return new MessageToSend(user.getEmail(), "Access to exam has been granted.",
				"Now you have access to exams in the category: " + category +".");
	}

	@Override
	public MessageToSend questionReport(User user, String category) {
		return new MessageToSend(user.getEmail(), "Your question report has been handled.",
				"Thank you for your opinion about question from category " + category +". \n"+
				"We've updated it.");
	}

}
