package com.fdmgroup.MockExam.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionFoundInSearchDTO extends IdFoundInSearchDTO {
	//private int id;
	private String category;
	private String level;
	private String questionContent;
	private List<String> answers;
	private List<String> feedback;
	private List<String> correctAnswers;	
	private boolean locked;
	private List<String> tags;
	public QuestionFoundInSearchDTO(int id, String category, String level, String questionContent, List<String> answers,
			List<String> feedback, List<String> correctAnswers, boolean locked, List<String> tags) {
		super(id);
		this.category = category;
		this.level = level;
		this.questionContent = questionContent;
		this.answers = answers;
		this.feedback = feedback;
		this.correctAnswers = correctAnswers;
		this.locked = locked;
		this.tags = tags;
	}
	
	
}
