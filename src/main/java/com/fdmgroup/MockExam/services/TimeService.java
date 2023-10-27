package com.fdmgroup.MockExam.services;

import java.time.LocalDateTime;

/**
 * Interface to define the contract for the TimeService.
 * Provides methods for obtaining the current date and time.
 */
public interface TimeService {
   
	/**
     * Retrieves the current date and time.
     * @return The current date and time as a LocalDateTime object.
     */
	LocalDateTime now();
	
}
