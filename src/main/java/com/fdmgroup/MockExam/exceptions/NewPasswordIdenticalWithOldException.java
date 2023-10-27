package com.fdmgroup.MockExam.exceptions;

public class NewPasswordIdenticalWithOldException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5472485475959147233L;
	
	public NewPasswordIdenticalWithOldException(String message) {
		super(message);
	}

}
