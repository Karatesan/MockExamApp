package com.fdmgroup.MockExam.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateExamRequestDTO {
	private String category;
	private String level;
}
