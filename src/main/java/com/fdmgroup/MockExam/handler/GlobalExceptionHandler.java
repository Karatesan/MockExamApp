package com.fdmgroup.MockExam.handler;

 

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.fdmgroup.MockExam.exceptions.ObjectNotValidException;
import com.fdmgroup.MockExam.exceptions.UserAccesException;

import jakarta.persistence.EntityNotFoundException;

import com.fdmgroup.MockExam.exceptions.AdminAccesException;
import com.fdmgroup.MockExam.exceptions.InvalidCategoryException;
import com.fdmgroup.MockExam.exceptions.InvalidDocFileException;
import com.fdmgroup.MockExam.exceptions.InvalidLevelException;
import com.fdmgroup.MockExam.exceptions.InvalidXlsFileException;
import com.fdmgroup.MockExam.exceptions.NoQuestionException;

 

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<?> handleException(IllegalStateException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());        
        }

    @ExceptionHandler(ObjectNotValidException.class)
    public ResponseEntity<?> handleException(ObjectNotValidException exception) {
        return ResponseEntity.badRequest().body(exception.getErrorMessages());        
        }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleException(EntityNotFoundException exception) {
        return ResponseEntity.notFound().build();
        }

    @ExceptionHandler(InvalidCategoryException.class)
    public ResponseEntity<?> handleException(InvalidCategoryException exception) {
        return ResponseEntity.badRequest().body(exception.getErrorMessages());
        }


    @ExceptionHandler(InvalidLevelException.class)
    public ResponseEntity<?> handleException(InvalidLevelException exception) {
        return ResponseEntity.badRequest().body(exception.getErrorMessages());
        }
    

    @ExceptionHandler(InvalidXlsFileException.class)
    public ResponseEntity<?> handleException(InvalidXlsFileException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());        
        }

    

    @ExceptionHandler(InvalidDocFileException.class)
    public ResponseEntity<?> handleException(InvalidDocFileException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());        
        }

    @ExceptionHandler(NoQuestionException.class)
    public ResponseEntity<?> handleException(NoQuestionException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());        
        }
    @ExceptionHandler(AdminAccesException.class)
    public ResponseEntity<?> handleException(AdminAccesException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());        
        }
    @ExceptionHandler(UserAccesException.class)
    public ResponseEntity<?> handleException(UserAccesException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());        
        }

}