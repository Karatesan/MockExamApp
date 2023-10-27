package com.fdmgroup.MockExam.exceptions;

public class PasswordsNotIdenticalException extends RuntimeException {

	private static final long serialVersionUID = 7799456955353801145L;

	public PasswordsNotIdenticalException(String message) {
		super(message);
	}
	
}
