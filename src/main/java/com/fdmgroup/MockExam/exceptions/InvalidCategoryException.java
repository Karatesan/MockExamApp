package com.fdmgroup.MockExam.exceptions;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class InvalidCategoryException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	@Getter
	private final String errorMessages;

}