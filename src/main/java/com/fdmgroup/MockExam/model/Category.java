package com.fdmgroup.MockExam.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Represents a Category entity in the database.
 */
@Entity
@Data
@AllArgsConstructor
public class Category {
	
	@Id
	@GeneratedValue
	private Integer id;
	private String categoryName;
	
    /**
     * Default constructor for the Category class.
     */
	public Category() {}
	
    /**
     * Constructs a Category object with the specified category name.
     * @param categoryName The name of the category.
     */
	public Category(String categoryName) {
		super();
		this.categoryName = categoryName;
	}
}
