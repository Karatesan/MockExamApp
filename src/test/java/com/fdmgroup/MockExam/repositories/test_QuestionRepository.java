package com.fdmgroup.MockExam.repositories;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.fdmgroup.MockExam.model.Category;
import com.fdmgroup.MockExam.model.Question;

@DataJpaTest
class test_QuestionRepository {
	
    @Autowired
    private QuestionRepository questionRepository;
    
    @Autowired
    private TestEntityManager entityManager;
    
    private Category category;
    
    @BeforeEach
    void setUp() {
        category = new Category();
        category.setCategoryName("TestCategory");
        entityManager.persist(category);
    }

    @Test
    public void test_findByCategory_whenTwoQuestionsInCategory() {
        Question question1 = new Question();
        question1.setCategory(category);
        entityManager.persist(question1);

        Question question2 = new Question();
        question2.setCategory(category);
        entityManager.persist(question2);

        List<Question> foundQuestions = questionRepository.findByCategory(category);

        assertNotNull(foundQuestions);
        assertEquals(2, foundQuestions.size());
    }
    
    @Test
    public void test_findByCategory_whenNoQuestionsInCategory() {
    	List<Question> foundQuestions = questionRepository.findByCategory(category);
    	assertEquals(0, foundQuestions.size());
    } 
    

}