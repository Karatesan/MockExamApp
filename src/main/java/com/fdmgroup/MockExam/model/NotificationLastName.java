package com.fdmgroup.MockExam.model;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class NotificationLastName extends Notification {
	
	private String name;
	
	public NotificationLastName(User user, String name) {
		super(user);
		this.name=name;
	}
}

