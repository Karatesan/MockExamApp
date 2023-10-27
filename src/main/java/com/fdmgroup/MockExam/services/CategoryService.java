package com.fdmgroup.MockExam.services;

import java.util.List;

import com.fdmgroup.MockExam.model.Category;
import com.fdmgroup.MockExam.model.User;

/**
 * Interface to define the contract for the CategoryService.
 * Provides methods for managing categories.
 */
public interface CategoryService {

    /**
     * Finds a category by its name.
     * @param name The name of the category to search for.
     * @return The category with the specified name, if found.
     */
	Category findByName(String name);

    /**
     * Saves a new category.
     * @param newCategory The category to be saved.
     * @return The saved category.
     */
	Category saveCategory(Category newCategory);
	
    /**
     * Retrieves a list of all categories.
     * @return A list containing all categories in the repository.
     */
	List<Category> findAllCategories(User user);

	List<Category> findAllCategories();

	Category findOrCreateCategory(String name);

}