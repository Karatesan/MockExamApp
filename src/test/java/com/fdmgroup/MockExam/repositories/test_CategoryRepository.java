package com.fdmgroup.MockExam.repositories;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.fdmgroup.MockExam.model.Category;

@DataJpaTest
class test_CategoryRepository {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private TestEntityManager entityManager;
    
    
    @Test
    public void test_findByCategoryName_withExistingCategory() {
        Category category = new Category();
        category.setCategoryName("ExistingCategory");
        entityManager.persist(category);
        entityManager.flush();

        Category foundCategory = categoryRepository.findByCategoryName("ExistingCategory");
        assertNotNull(foundCategory);
        assertEquals("ExistingCategory", foundCategory.getCategoryName());
    }

    @Test
    public void test_findByCategoryName_withNonexistingCategory() {
        Category foundCategory = categoryRepository.findByCategoryName("NonexistingCategory");
        assertNull(foundCategory);
    }


}