package com.fdmgroup.MockExam.validators;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import com.fdmgroup.MockExam.exceptions.ObjectNotValidException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

@Component
public class ObjectValidator<T> {

	private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
	private final Validator validator = factory.getValidator();

	public void validate(T objectToValidate) {
		Set<ConstraintViolation<T>> violations = validator.validate(objectToValidate);
		if (!violations.isEmpty()) {
			// Set<String> errorMessages =
			// violations.stream().map(v->v.getPropertyPath().toString()).collect(Collectors.toSet());
			Map<String, Set<String>> errorMap = violations.stream()
					.collect(Collectors.groupingBy(violation -> violation.getPropertyPath().toString(),
							Collectors.mapping(ConstraintViolation::getMessage, Collectors.toSet())));
			
			throw new ObjectNotValidException(errorMap);
		}
	}
}