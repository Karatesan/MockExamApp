package com.fdmgroup.MockExam.exceptions;

public class TokenExpiredException extends RuntimeException{

	private static final long serialVersionUID = 226858245808701060L;
	
	public TokenExpiredException(String message) {
		super(message);
	}

}
