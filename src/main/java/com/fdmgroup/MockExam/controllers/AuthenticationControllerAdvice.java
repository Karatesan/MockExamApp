package com.fdmgroup.MockExam.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.fdmgroup.MockExam.exceptions.EmailAlreadyExistsException;
import com.fdmgroup.MockExam.exceptions.InvalidPasswordOrEmailException;
import com.fdmgroup.MockExam.exceptions.NewPasswordIdenticalWithOldException;
import com.fdmgroup.MockExam.exceptions.PasswordsNotIdenticalException;
import com.fdmgroup.MockExam.exceptions.TokenExpiredException;
import com.fdmgroup.MockExam.exceptions.UserAccountLockedException;
import com.fdmgroup.MockExam.exceptions.UserDisabledException;

@ControllerAdvice
public class AuthenticationControllerAdvice {
	
	@ExceptionHandler(value = EmailAlreadyExistsException.class)
	public ResponseEntity<String> handleEmailAlreadyExistsException(EmailAlreadyExistsException ex){
		return ResponseEntity
				.status(HttpStatus.CONFLICT)
				.body(ex.getMessage());
	}
	
	@ExceptionHandler(value = InvalidPasswordOrEmailException.class)
	public ResponseEntity<String> handleInvalidPasswordOrEmail(InvalidPasswordOrEmailException ex){
		return ResponseEntity
				.status(HttpStatus.UNAUTHORIZED)
				.body(ex.getMessage());
	}
	
	@ExceptionHandler(value = TokenExpiredException.class)
	public ResponseEntity<String> handleTokenExpiredException(TokenExpiredException ex){
		return ResponseEntity
				.status(HttpStatus.CONFLICT)
				.body(ex.getMessage());
	}
	
	
	@ExceptionHandler(value = UserDisabledException.class)
	public ResponseEntity<String> handleUserDisabledException(UserDisabledException ex){
		return ResponseEntity
				.status(HttpStatus.UNAUTHORIZED)
				.body(ex.getMessage());
	}
	@ExceptionHandler(value = UserAccountLockedException.class)
	public ResponseEntity<String> handleUserAccountLockedException(UserAccountLockedException ex){
		return ResponseEntity
				.status(HttpStatus.UNAUTHORIZED)
				.body(ex.getMessage());
	}
	
//	@ExceptionHandler(value = UserNotFoundException.class)
//	public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException ex){
//		return ResponseEntity
//				.status(HttpStatus.BAD_REQUEST)
//				.body(ex.getMessage());
//	
//	}
	
	@ExceptionHandler(value = NewPasswordIdenticalWithOldException.class)
	public ResponseEntity<String> handleNewPasswordIdenticalWithOldException(NewPasswordIdenticalWithOldException ex){
		return ResponseEntity
				.status(HttpStatus.BAD_REQUEST)
				.body(ex.getMessage());
	}
	
	@ExceptionHandler(value = PasswordsNotIdenticalException.class)
	public ResponseEntity<String> handlePasswordsNotIdenticalException(PasswordsNotIdenticalException ex){
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
	}
	
	
	/*
	@ExceptionHandler(value = MethodArgumentNotValidException.class)
	public ResponseEntity<ValidationErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
		ValidationErrorResponse error = new ValidationErrorResponse();
		for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
			error.getViolations().add(
					new Violation(fieldError.getField(), fieldError.getDefaultMessage()));
		}
		
		return ResponseEntity
				.status(HttpStatus.BAD_REQUEST)
				.body(error);
	}
	@ExceptionHandler(ConstraintViolationException.class)
	  @ResponseStatus(HttpStatus.BAD_REQUEST)
	  @ResponseBody
	  ValidationErrorResponse onConstraintValidationException(
	      ConstraintViolationException e) {
	    ValidationErrorResponse error = new ValidationErrorResponse();
	    for (ConstraintViolation violation : e.getConstraintViolations()) {
	      error.getViolations().add(
	        new Violation(violation.getPropertyPath().toString(), violation.getMessage()));
	    }
	    return error;
	  }
	*/
	

}
