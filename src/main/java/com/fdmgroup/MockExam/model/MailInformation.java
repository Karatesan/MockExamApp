package com.fdmgroup.MockExam.model;

import lombok.Data;

/**
 * Represents the configuration information required for sending mails.
 * Contains email-related settings such as the email address, password, SMTP host, and port.
 */
@Data
public class MailInformation {
	private final String email = "test";
	private final String password = "test";
	private final String host = "test";
	private final String port ="test";
	
}
