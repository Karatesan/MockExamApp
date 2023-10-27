package com.fdmgroup.MockExam.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a message to be sent entity.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageToSend {
	
	private String to;
	private String subject;
	private String body;
	
}
