package com.fdmgroup.MockExam.model;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class NotificationAccount extends Notification {
public NotificationAccount(User user)
{
	super(user);
}
}
