package com.fdmgroup.MockExam.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Notification {

	@Id
	@GeneratedValue
	private int id;
	@ManyToMany
	private List<User> admins = new ArrayList<>();
//	private boolean handling = false;
	@ManyToOne
	private User handleBy;
	@ManyToOne
	private User user;

	public Notification(User user) {
		super();
		this.user = user;
	}

	public List<User> addToAdmins(User admin) {
		admins.add(admin);
		return admins;
	}

}
