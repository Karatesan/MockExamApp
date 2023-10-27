package com.fdmgroup.MockExam.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionRequestDTO {

	@NotEmpty(message = "Category can't be empty")
	private String category;
	@NotEmpty(message = "Level can't be empty")
	private String level;
	@NotEmpty(message = "Question can't be empty")
	@Size(max = 500, message = "The question is too long, it cannot exceed 500 characters")
	private String questionContent;
	@NotEmpty(message = "Answers can't be empty")
	@Size(min=2,message = "Question must have at least 2 answers")
	private List<String> answers;
	private List<String> feedback;
	@NotEmpty(message = "Question must have at least 1 correctAnswer")
	private List<String> correctAnswers;
	private MultipartFile imageFile;
	private List<String> tags;

	
	public QuestionRequestDTO(String category, String level, String questionContent, List<String> answers,
			List<String> feedback, List<String> correctAnswers, List<String> tags) {
		super();
		this.category = category;
		this.level = level;
		this.questionContent = questionContent;
		this.answers = answers;
		this.feedback = feedback;
		this.correctAnswers = correctAnswers;
		this.tags = tags;

	}


	public QuestionRequestDTO(@NotEmpty(message = "Category can't be empty") String category,
			@NotEmpty(message = "Level can't be empty") String level,
			@NotEmpty(message = "Question can't be empty") @Size(max = 500, message = "The question is too long, it cannot exceed 500 characters") String questionContent,
			@NotEmpty(message = "Answers can't be empty") @Size(min = 2, message = "Question must have at least 2 answers") List<String> answers,
			List<String> feedback,
			@NotEmpty(message = "Question must have at least 1 correctAnswer") List<String> correctAnswers) {
		super();
		this.category = category;
		this.level = level;
		this.questionContent = questionContent;
		this.answers = answers;
		this.feedback = feedback;
		this.correctAnswers = correctAnswers;
	}
	
	

}
