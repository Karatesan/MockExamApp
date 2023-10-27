package com.fdmgroup.MockExam.exceptions;

public class UserDisabledException extends RuntimeException {

	private static final long serialVersionUID = -3923805547408225965L;
	
	public UserDisabledException(String message) {
		super(message);
	}

}
