package com.fdmgroup.MockExam.services;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class test_TimeService {
	@Test
	public void test_now() {
		TimeServiceImpl timeService = new TimeServiceImpl();

		LocalDateTime result = timeService.now();
		LocalDateTime current = LocalDateTime.now();

		assertTrue(result.isBefore(current.plusSeconds(1)));
		assertTrue(result.isAfter(current.minusSeconds(1)));
	}
}
