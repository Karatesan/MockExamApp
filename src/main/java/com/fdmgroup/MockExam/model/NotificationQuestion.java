package com.fdmgroup.MockExam.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import jakarta.persistence.InheritanceType;
@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class NotificationQuestion extends Notification {
	@ManyToOne
	private Question question;
	private String comment;
	
	public NotificationQuestion(User user, Question question, String comment) {
		super(user);
		this.question = question;
		this.comment = comment;
	}
	
}
