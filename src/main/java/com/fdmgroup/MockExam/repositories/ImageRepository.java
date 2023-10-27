package com.fdmgroup.MockExam.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fdmgroup.MockExam.model.Image;

/**
 * Repository interface for managing Image entities in the database.
 * Extends JpaRepository to inherit basic CRUD operations.
 */
@Repository
public interface ImageRepository extends JpaRepository<Image, Integer> {
	
	/**
	 * Find a Image entity by its name.
	 * @param name The name of the image to search for.
	 * @return The Image entity matching the provided name, or null if not found.
	 */
	public Image findByName(String name);

}
