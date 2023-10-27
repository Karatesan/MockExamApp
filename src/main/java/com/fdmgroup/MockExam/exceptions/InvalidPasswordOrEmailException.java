package com.fdmgroup.MockExam.exceptions;

public class InvalidPasswordOrEmailException extends RuntimeException {

	private static final long serialVersionUID = 5266329432378029698L;

	public InvalidPasswordOrEmailException(String message) {
		super(message);
	}
	
}
