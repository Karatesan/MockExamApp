package com.fdmgroup.MockExam.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.fdmgroup.MockExam.model.Category;
import com.fdmgroup.MockExam.model.ExamAccess;
import com.fdmgroup.MockExam.model.Role;
import com.fdmgroup.MockExam.model.User;
import com.fdmgroup.MockExam.repositories.CategoryRepository;
import com.fdmgroup.MockExam.repositories.ExamAccessRepository;
import com.fdmgroup.MockExam.repositories.NotificationRepository;

import lombok.RequiredArgsConstructor;

/**
 * The class implements the CategoryService interface. Provides methods to save
 * and find categories.
 */
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

	private final CategoryRepository categoryRepository;
	private final ExamAccessRepository examAccessRepository;

//    /**
//     * Constructs a new CategoryServiceImpl instance.
//     * @param categoryRepository The repository for managing categories.
//     */
//	public CategoryServiceImpl(CategoryRepository categoryRepository) {
//		super();
//		this.categoryRepository = categoryRepository;
//	}

	@Override
	public Category findByName(String name) {
		return categoryRepository.findByCategoryName(name);
	}

	@Override
	public Category saveCategory(Category newCategory) {
		return categoryRepository.save(newCategory);
	}

	@Override
	public List<Category> findAllCategories(User user) {
		if (user.getRole().equals(Role.TRAINEE)) {
			Optional<ExamAccess> accessOpt = examAccessRepository.findByUser(user);
			if(accessOpt.isPresent())
			return accessOpt.get().getCategories();
			else return new ArrayList<>();
		}
		return categoryRepository.findAll();
	}

	@Override
	public List<Category> findAllCategories() {
		return categoryRepository.findAll();

	}
	
	@Override
	public Category findOrCreateCategory(String name) {
		Category category = findByName(name);
		if (category == null) {
			category = new Category(name);
			saveCategory(category);
		}
		return category;
	}

}
