package com.fdmgroup.MockExam.model;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents an Exam entity in the database.
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Exam {
	
	@Id
	@GeneratedValue
	private Integer id;
	private LocalDateTime examDate;
	@ManyToOne
	private User user;
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	private List<Answer> answers;
	
    /**
     * Constructs an Exam object with the specified exam date, user, and answers.
     * @param examDate The date and time when the exam was taken.
     * @param user The User object representing the user who took the exam.
     * @param answers The list of Answer objects representing the answers given in the exam.
     */
	public Exam(LocalDateTime examDate, User user, List<Answer> answers) {
		super();
		this.examDate = examDate;
		this.user = user;
		this.answers = answers;
	}

    /**
     * Constructs an Exam object with the specified exam date and answers.
     * @param examDate The date and time when the exam was taken.
     * @param answers The list of Answer objects representing the answers given in the exam.
     */
	public Exam(LocalDateTime examDate, List<Answer> answers) {
		super();
		this.examDate = examDate;
		this.answers = answers;
	}

    /**
     * Constructs an Exam object with the specified answers.
     * @param answers The list of Answer objects representing the answers given in the exam.
     */
	public Exam(List<Answer> answers) {
		super();
		this.answers = answers;
	}

}
