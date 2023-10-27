package com.fdmgroup.MockExam.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.validation.annotation.Validated;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "EXAM_USER_ACCESS")
@AllArgsConstructor
@Validated
public class ExamAccess {
	@Id
	@GeneratedValue
	int id;
	@OneToOne
	@JoinColumn(nullable = false, name = "user_id")
	private User user;
	@OneToMany
	private List<Category> categories;

	public ExamAccess(User user) {
		super();
		this.user = user;
		categories=new ArrayList<>();
	}
	
	public List<Category> addToCategories(Category category) {
		categories.add(category);
		return categories;
	}

}
