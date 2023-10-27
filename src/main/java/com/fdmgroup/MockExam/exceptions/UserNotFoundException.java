package com.fdmgroup.MockExam.exceptions;

public class UserNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 269902139370387682L;

	public UserNotFoundException(String message) {
		super(message);
	}
	
}
