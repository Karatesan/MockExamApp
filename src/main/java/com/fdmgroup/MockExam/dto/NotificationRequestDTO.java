package com.fdmgroup.MockExam.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
//@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationRequestDTO {
	private String action;
	private int id;
	private int page;

	public NotificationRequestDTO(String action, int page) {
		super();
		this.action = action;
		this.page = page;
	}

	public NotificationRequestDTO(int id) {
		super();
		this.id = id;
	}

}
