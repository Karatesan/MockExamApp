package com.fdmgroup.MockExam.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionResponseDTO {

	private Integer id;
	private String category;
	private String level;
	private String questionContent;
	private List<String> answers;
	private List<String> feedback;
	private List<String> correctAnswers;
	private ImageResponseDTO image;
	private List<String> tags;
	private int noteId;

	public QuestionResponseDTO(String category, String level, String questionContent, List<String> answers,
			List<String> feedback, List<String> correctAnswers, ImageResponseDTO image, List<String> tags) {
		super();
		this.category = category;
		this.level = level;
		this.questionContent = questionContent;
		this.answers = answers;
		this.feedback = feedback;
		this.correctAnswers = correctAnswers;
		this.image = image;
		this.tags = tags;
	}

	public QuestionResponseDTO(Integer id, String category, String level, String questionContent, List<String> answers,
			List<String> feedback, List<String> correctAnswers, ImageResponseDTO image, List<String> tags) {
		super();
		this.id = id;
		this.category = category;
		this.level = level;
		this.questionContent = questionContent;
		this.answers = answers;
		this.feedback = feedback;
		this.correctAnswers = correctAnswers;
		this.image = image;
		this.tags = tags;
	}

}
