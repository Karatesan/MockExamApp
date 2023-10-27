package com.fdmgroup.MockExam.model;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a Question entity in the database.
 */
@Entity
@NoArgsConstructor
@Data
public class Question {
	
	public enum Level{
		BEGINNER,
		INTERMEDIATE,
		EXPERT
	}
	
	@Id
	@GeneratedValue
	private Integer id;
	@ManyToOne
	private Category category;
	private Level level;
	@Size(max = 500,message = "Question cannot exceed 500 characters")
	private String questionContent;
	private List<String> answers;
	@Size(max=1000)
	@Column(length = 1000)
	private List<String> feedback;
	private List<String> correctAnswers;	
	@ManyToOne(cascade = {CascadeType.PERSIST},fetch = FetchType.LAZY)	
	@JoinColumn(nullable = true)
	private Image image;
	private boolean locked = false;
	@ManyToMany
	private List<Tag> tags;

    /**
     * Constructs a Question object with the specified category, level, question content, answers, feedback, and correct answers.
     * @param category The Category object representing the category to which the question belongs.
     * @param level The level of difficulty for the question.
     * @param questionContent The content of the question.
     * @param answers The list of possible answers to the question.
     * @param feedback The list of feedback associated with each answer.
     * @param correctAnswers The list of correct answers to the question.
     */
	public Question(Category category, Level level, String questionContent, List<String> answers, List<String> feedback, List<String> correctAnswers, List<Tag> tags) {
		super();
		this.category = category;
		this.level = level;
		this.questionContent = questionContent;
		this.answers = answers;
		this.correctAnswers = correctAnswers;
		this.feedback = feedback;
		this.tags = tags;
	}
	
    /**
     * Constructs a Question object with the specified category, level, question content, answers, feedback, correct answers, and image.
     * @param category The Category object representing the category to which the question belongs.
     * @param level The level of difficulty for the question.
     * @param questionContent The content of the question.
     * @param answers The list of possible answers to the question.
     * @param feedback The list of feedback associated with each answer.
     * @param correctAnswers The list of correct answers to the question.
     * @param image The Image object associated with the question.
     */
	public Question(Category category, Level level, String questionContent, List<String> answers, List<String> feedback,
			List<String> correctAnswers, Image image, List<Tag> tags) {
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
	
    /**
     * Shuffles randomly the order of answers and their corresponding feedback.
     */
	public void shuffleAnswers() {	
        long seed = System.nanoTime();
        Collections.shuffle(answers, new Random(seed));
        Collections.shuffle(feedback, new Random(seed));  
	}

    /**
     * Constructs a Question object with the specified category and level.
     * @param category The Category object representing the category to which the question belongs.
     * @param level The level of difficulty for the question.
     */
	public Question(Category category, Level level) {
		super();
		this.category = category;
		this.level = level;
	}

	public boolean getLocked() {
		return locked;
	}
	
	
	

}
