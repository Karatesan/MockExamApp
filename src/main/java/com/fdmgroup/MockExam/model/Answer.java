package com.fdmgroup.MockExam.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents an Answer entity in the database.
 */
@Entity
@Data
@NoArgsConstructor
public class Answer {
	
	@Id
	@GeneratedValue
	private Integer id;
	@ManyToOne
	private Question question;
	private List<String> givenAnswer;
	
    /**
     * Constructs an Answer object with the specified Question and given answer(s).
     * @param question The Question object associated with this answer.
     * @param givenAnswer The list of strings representing the given answer(s).
     */
	public Answer(Question question, List<String> givenAnswer) {
		super();
		this.question = question;
		this.givenAnswer = givenAnswer;
	}
}
