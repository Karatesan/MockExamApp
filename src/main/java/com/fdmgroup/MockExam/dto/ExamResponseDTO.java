package com.fdmgroup.MockExam.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExamResponseDTO {
	private Integer id;
	private String examDate;
	private List<AnswerResponseDTO> answersDTO;

	public ExamResponseDTO(String examDate, List<AnswerResponseDTO> answersDTO) {
		super();
	
		this.examDate = examDate;
		this.answersDTO = answersDTO;
	}

}
