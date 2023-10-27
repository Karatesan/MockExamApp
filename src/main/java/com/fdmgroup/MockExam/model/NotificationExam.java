package com.fdmgroup.MockExam.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class NotificationExam extends Notification {
	@ManyToOne
	private Category category;

	public NotificationExam(User user, Category category) {
		super(user);
		this.category = category;
	}
	
}
