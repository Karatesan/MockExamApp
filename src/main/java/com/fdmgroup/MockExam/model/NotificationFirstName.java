package com.fdmgroup.MockExam.model;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class NotificationFirstName extends Notification {
private String name;

public NotificationFirstName(User user, String name) {
	super(user);
	this.name = name;
}


}
