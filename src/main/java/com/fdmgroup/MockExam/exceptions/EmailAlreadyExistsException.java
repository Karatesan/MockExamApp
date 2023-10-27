package com.fdmgroup.MockExam.exceptions;

public class EmailAlreadyExistsException extends RuntimeException {

	private static final long serialVersionUID = 4893283305819224975L;

	public EmailAlreadyExistsException(String message) {
		super(message);
	}
	
}
