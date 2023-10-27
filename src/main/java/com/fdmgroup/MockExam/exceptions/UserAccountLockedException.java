package com.fdmgroup.MockExam.exceptions;

public class UserAccountLockedException extends RuntimeException{

	private static final long serialVersionUID = 812611279331809038L;
	
	public UserAccountLockedException(String message){
		super(message);
	}
	
}
