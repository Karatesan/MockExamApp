package com.fdmgroup.MockExam.services;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

/**
 * The class implements the TimeService interface.
 * Provides a method to retrieve the current date and time.
 */
@Service
public class TimeServiceImpl implements TimeService {

	@Override
	public LocalDateTime now() {
		return LocalDateTime.now();
	}

}
