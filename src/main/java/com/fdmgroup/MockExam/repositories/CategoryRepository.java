package com.fdmgroup.MockExam.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fdmgroup.MockExam.model.Category;

/**
 * Repository interface for managing Category entities in the database.
 * Extends JpaRepository to inherit basic CRUD operations.
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
	
    /**
     * Find a Category entity by its name.
     * @param name The name of the category to search for.
     * @return The Category entity matching the provided name, or null if not found.
     */
	public Category findByCategoryName(String name);

}
