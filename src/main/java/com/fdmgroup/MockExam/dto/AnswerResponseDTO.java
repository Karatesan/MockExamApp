package com.fdmgroup.MockExam.dto;


import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnswerResponseDTO {
	private Integer id;
	private QuestionResponseDTO questionResponseDTO;
	private List<String> givenAnswer;	
}
