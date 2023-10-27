package com.fdmgroup.MockExam.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fdmgroup.MockExam.model.Category;
import com.fdmgroup.MockExam.model.ExamAccess;
import com.fdmgroup.MockExam.model.Role;
import com.fdmgroup.MockExam.model.User;
import com.fdmgroup.MockExam.repositories.CategoryRepository;
import com.fdmgroup.MockExam.repositories.ExamAccessRepository;

@ExtendWith(MockitoExtension.class)
public class test_CategoryService {

	@InjectMocks
	private CategoryServiceImpl categoryService;

	@Mock
	private CategoryRepository categoryRepository;
	@Mock
	private ExamAccessRepository examAccessRepository;

	private Category category;
	private Category actual;

	@BeforeEach
	public void setUp() {
		category = new Category("UNIX");
		actual = new Category();
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_findByName_pushName_checkRepositoryFindingWorks() {
		doReturn(category).when(categoryRepository).findByCategoryName("UNIX");

		actual = categoryService.findByName("UNIX");

		assertEquals(category, actual);
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_saveCategory_pushCategory_checkRepositorySavingWorks() {
		doReturn(category).when(categoryRepository).save(category);

		actual = categoryService.saveCategory(category);

		assertEquals(category, actual);
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_findAllCategories_admin() {
		Category category2 = new Category("SQK");
		List<Category> categories = new ArrayList<>();
		categories.add(category2);
		categories.add(category);
		doReturn(categories).when(categoryRepository).findAll();
		User user = new User();
		user.setRole(Role.ADMIN);
		List<Category> actual = categoryService.findAllCategories(user);

		assertEquals(categories, actual);
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_findAllCategories_trainee() {
		Category category2 = new Category("SQK");
		List<Category> categories = new ArrayList<>();
		categories.add(category2);
		categories.add(category);
		User user = new User();
		user.setRole(Role.TRAINEE);
		ExamAccess access = new ExamAccess(user);
		access.addToCategories(category2);
		access.addToCategories(category);
		when(examAccessRepository.findByUser(user)).thenReturn(Optional.of(access));

		List<Category> actual = categoryService.findAllCategories(user);

		assertEquals(categories, actual);
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_findAllCategories_trainee_noAccessToAnyExam() {
		User user = new User();
		user.setRole(Role.TRAINEE);
		List<Category> emptyList = new ArrayList<>();

		when(examAccessRepository.findByUser(user)).thenReturn(Optional.empty());

		List<Category> actual = categoryService.findAllCategories(user);

		assertEquals(emptyList, actual);
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_findAllCategories() {
		Category category2 = new Category("SQK");
		List<Category> categories = new ArrayList<>();
		categories.add(category2);
		categories.add(category);

		doReturn(categories).when(categoryRepository).findAll();

		List<Category> actual = categoryService.findAllCategories();

		assertEquals(categories, actual);
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_findOrCreateCategory_CategoryExists() {
		String categoryName = "JAVA";

		when(categoryRepository.findByCategoryName(categoryName)).thenReturn(category);

		Category result = categoryService.findOrCreateCategory(categoryName);

		assertEquals(category, result);
		verify(categoryRepository, never()).save(any(Category.class));
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_findOrCreateCategory_CategoryDoesNotExist() {
		String categoryName = "UNIX";

		when(categoryRepository.findByCategoryName(categoryName)).thenReturn(null);
		when(categoryRepository.save(category)).thenReturn(category);

		Category result = categoryService.findOrCreateCategory(categoryName);

		assertEquals(category, result);
		verify(categoryRepository).save(category);
	}
}
