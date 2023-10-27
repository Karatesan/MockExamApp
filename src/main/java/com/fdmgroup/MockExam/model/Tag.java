package com.fdmgroup.MockExam.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
@Entity
@Data
@AllArgsConstructor
public class Tag {
	@Id
	@GeneratedValue
	private Integer id;
	private String name;
	
	
	
	public Tag(String name) {
		super();
		this.name = name;
	}



	public Tag() {
		super();
	}
	
	
}
