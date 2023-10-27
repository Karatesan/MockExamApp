package com.fdmgroup.MockExam.exceptions;

import java.util.Map;
import java.util.Set;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ObjectNotValidException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	@Getter
	private final Map<String, Set<String>> errorMessages;

}