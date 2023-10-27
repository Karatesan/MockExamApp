package com.fdmgroup.MockExam.controllers;


import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

//import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fdmgroup.MockExam.dto.ImageResponseDTO;
import com.fdmgroup.MockExam.dto.QuestionRequestDTO;
import com.fdmgroup.MockExam.dto.QuestionResponseDTO;
import com.fdmgroup.MockExam.model.Category;
import com.fdmgroup.MockExam.model.Image;
import com.fdmgroup.MockExam.model.Question;
import com.fdmgroup.MockExam.model.Question.Level;
import com.fdmgroup.MockExam.model.Role;
import com.fdmgroup.MockExam.model.Tag;
import com.fdmgroup.MockExam.model.User;
import com.fdmgroup.MockExam.services.CategoryService;
import com.fdmgroup.MockExam.services.QuestionService;
import com.fdmgroup.MockExam.services.UserService;

import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(Lifecycle.PER_CLASS)
public class test_QuestionController {
	
	@Autowired
	private QuestionController controller;
	
	@MockBean
	QuestionService questionService;
	
	@Autowired
    private ObjectMapper objectMapper;
	
	@Autowired
	MockMvc mvc;
	
	User user;
	Category category;
	Image image;
	Question question;
	ImageResponseDTO imageRes;
	QuestionResponseDTO questionResponseDTO;
	QuestionRequestDTO questionRequestDTO;
	static String validJwtToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QHRlc3QuY29tIiwiaWF0IjoxNjkyMDIwOTIxLCJleHAiOjE3OTIwMjIzNjF9.R3OqXtVwU5DdANVB8Qk8w1MFvYkdJFSOqnqsG3jVNtU";
	String email = "test@test.com";
	
	List<Question> questions;
	
	@BeforeAll
	public void setUp() {
		category = new Category("Java");
		image = new Image("Empty Image", 0, new byte[0]);
		image.setId(1);
		imageRes = new ImageResponseDTO();
		imageRes.setId(1);
		imageRes.setName("Empty Image");
		imageRes.setSize(0);
		List<Tag> tags = new ArrayList<>();

		question = new Question(category, Question.Level.BEGINNER, "What is your favorite dependency?",
				Arrays.asList("JPA", "JUnit", "Spring", "Hibernate"),
				Arrays.asList("Incorrect", "Correct", "Correct", "Incorrect"), Arrays.asList("JUnit", "Spring"), image, tags);

		questions = Arrays.asList(question);
		questionResponseDTO = new QuestionResponseDTO("Java", "beginner", "What is your favorite dependency?",
		Arrays.asList("JPA", "JUnit", "Spring", "Hibernate"),
		Arrays.asList("Incorrect", "Correct", "Correct", "Incorrect"), Arrays.asList("JUnit", "Spring")
		,imageRes, Arrays.asList("JUnit", "Spring"));
		MultipartFile imageFile = new MockMultipartFile("image", "image.jpg", "image/jpeg", "image data".getBytes());
		questionRequestDTO = new QuestionRequestDTO("Java", "beginner", "What is your favorite dependency?",
				Arrays.asList("JPA", "JUnit", "Spring", "Hibernate"),
				Arrays.asList("Incorrect", "Correct", "Correct", "Incorrect"), Arrays.asList("JUnit", "Spring"),
				imageFile, Arrays.asList("JUnit", "Spring"));
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	void test_ControllerNotNull() {
		assertThat(controller).isNotNull();
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	@WithMockUser(username= "test@test.com" , password = "testPassword")

	public void test_getAllQuestions_checkingIfWorksAndReturningProperlyValues() throws Exception {
		
		when(questionService.findAllQuestions()).thenReturn(questions);	

		mvc.perform(get("/api/questions/all").header("Authorization", "Bearer " + validJwtToken))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$[0].category").value("Java")).andExpect(jsonPath("$[0].answers[0]").value("JPA"))
				.andExpect(jsonPath("$[0].answers[1]").value("JUnit"))
				.andExpect(jsonPath("$[0].answers[2]").value("Spring"))
				.andExpect(jsonPath("$[0].answers[3]").value("Hibernate"))
				.andExpect(jsonPath("$[0].correctAnswers[0]").value("JUnit"))
				.andExpect(jsonPath("$[0].correctAnswers[1]").value("Spring"))
				.andExpect(jsonPath("$[0].image").exists()).andExpect(jsonPath("$[0].feedback[0]").value("Incorrect"))
				.andExpect(jsonPath("$[0].feedback[1]").value("Correct"))
				.andExpect(jsonPath("$[0].feedback[2]").value("Correct"))
				.andExpect(jsonPath("$[0].feedback[3]").value("Incorrect"))
				.andExpect(jsonPath("$[0].level").value("beginner"))
				.andExpect(jsonPath("$[0].questionContent").value("What is your favorite dependency?"));
	
	}

	// ---------------------------------------------------------------------------------------------

//	@Test
//	@WithMockUser(username= "test@test.com" , password = "testPassword")
//	public void test_getQuestionById_checkingIfWorksAndReturningProperlyValues() throws Exception {
//		when(questionService.findById(1)).thenReturn(question);
//		
//		mvc.perform(get("/api/questions").param("id", "1").header("Authorization", "Bearer " + validJwtToken))
//				.andExpect(status().isOk())
//				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
//				.andExpect(jsonPath("$.category").value("Java")).andExpect(jsonPath("$.answers[0]").value("JPA"))
//				.andExpect(jsonPath("$.answers[1]").value("JUnit")).andExpect(jsonPath("$.answers[2]").value("Spring"))
//				.andExpect(jsonPath("$.answers[3]").value("Hibernate"))
//				.andExpect(jsonPath("$.correctAnswers[0]").value("JUnit"))
//				.andExpect(jsonPath("$.correctAnswers[1]").value("Spring")).andExpect(jsonPath("$.image").exists())
//				.andExpect(jsonPath("$.feedback[0]").value("Incorrect"))
//				.andExpect(jsonPath("$.feedback[1]").value("Correct"))
//				.andExpect(jsonPath("$.feedback[2]").value("Correct"))
//				.andExpect(jsonPath("$.feedback[3]").value("Incorrect"))
//				.andExpect(jsonPath("$.level").value("beginner"))
//				.andExpect(jsonPath("$.questionContent").value("What is your favorite dependency?"));
//
//		verify(questionService, times(1)).findById(1);
//	}
	
	@Test
	@WithMockUser(username= "test@test.com" , password = "testPassword")
	public void test_getAllQuestionsInCategory_checkingIfWorksAndReturningProperlyValues() throws Exception {

		when(questionService.findAllQuestionsInCategory("Java")).thenReturn(questions);

		mvc.perform(get("/api/questions/itemsInCategory").param("category", "Java")
				.header("Authorization", "Bearer " + validJwtToken))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$[0].category").value("Java")).andExpect(jsonPath("$[0].answers[0]").value("JPA"))
				.andExpect(jsonPath("$[0].answers[1]").value("JUnit"))
				.andExpect(jsonPath("$[0].answers[2]").value("Spring"))
				.andExpect(jsonPath("$[0].answers[3]").value("Hibernate"))
				.andExpect(jsonPath("$[0].correctAnswers[0]").value("JUnit"))
				.andExpect(jsonPath("$[0].correctAnswers[1]").value("Spring"))
				.andExpect(jsonPath("$[0].image").exists()).andExpect(jsonPath("$[0].feedback[0]").value("Incorrect"))
				.andExpect(jsonPath("$[0].feedback[1]").value("Correct"))
				.andExpect(jsonPath("$[0].feedback[2]").value("Correct"))
				.andExpect(jsonPath("$[0].feedback[3]").value("Incorrect"))
				.andExpect(jsonPath("$[0].level").value("beginner"))
				.andExpect(jsonPath("$[0].questionContent").value("What is your favorite dependency?"));
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	@WithMockUser(username= "test@test.com" , password = "testPassword")
	public void test_getRandomQuestions_checkingIfWorksAndReturningProperlyValues() throws Exception {

		when(questionService.findAllQuestionsInCategory(category.getCategoryName()))
			.thenReturn(questions);
		when(questionService.findRandomQuestions(questions, 10)).thenReturn(questions);
	
		mvc.perform(
				get("/api/questions/randomItemsInCategory").param("category", "Java").param("numberOfQuestions", "10").header("Authorization", "Bearer " + validJwtToken))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$[0].category").value("Java")).andExpect(jsonPath("$[0].answers[0]").value("JPA"))
				.andExpect(jsonPath("$[0].answers[1]").value("JUnit"))
				.andExpect(jsonPath("$[0].answers[2]").value("Spring"))
				.andExpect(jsonPath("$[0].answers[3]").value("Hibernate"))
				.andExpect(jsonPath("$[0].correctAnswers[0]").value("JUnit"))
				.andExpect(jsonPath("$[0].correctAnswers[1]").value("Spring"))
				.andExpect(jsonPath("$[0].image").exists()).andExpect(jsonPath("$[0].feedback[0]").value("Incorrect"))
				.andExpect(jsonPath("$[0].feedback[1]").value("Correct"))
				.andExpect(jsonPath("$[0].feedback[2]").value("Correct"))
				.andExpect(jsonPath("$[0].feedback[3]").value("Incorrect"))
				.andExpect(jsonPath("$[0].level").value("beginner"))
				.andExpect(jsonPath("$[0].questionContent").value("What is your favorite dependency?"));
	
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	@WithMockUser(username= "test@test.com" , password = "testPassword")
	public void test_getRandomQuestionsWithLevel_checkingIfWorksAndReturningProperlyValues() throws Exception {

		when(questionService.findAllQuestionsInCategory(category.getCategoryName()))
			.thenReturn(questions);
		when(questionService.filterQuestionsByLevel(questions, Level.BEGINNER))
		.thenReturn(questions);
		when(questionService.findRandomQuestions(questions, 10)).thenReturn(questions);
		
		mvc.perform(
				get("/api/questions/randomItemsInCategoryAndLevel")
				.param("category", "Java")
				.param("numberOfQuestions", "10")
				.param("level", "beginner")
				.header("Authorization", "Bearer " + validJwtToken))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$[0].category").value("Java")).andExpect(jsonPath("$[0].answers[0]").value("JPA"))
				.andExpect(jsonPath("$[0].answers[1]").value("JUnit"))
				.andExpect(jsonPath("$[0].answers[2]").value("Spring"))
				.andExpect(jsonPath("$[0].answers[3]").value("Hibernate"))
				.andExpect(jsonPath("$[0].correctAnswers[0]").value("JUnit"))
				.andExpect(jsonPath("$[0].correctAnswers[1]").value("Spring"))
				.andExpect(jsonPath("$[0].image").exists()).andExpect(jsonPath("$[0].feedback[0]").value("Incorrect"))
				.andExpect(jsonPath("$[0].feedback[1]").value("Correct"))
				.andExpect(jsonPath("$[0].feedback[2]").value("Correct"))
				.andExpect(jsonPath("$[0].feedback[3]").value("Incorrect"))
				.andExpect(jsonPath("$[0].level").value("beginner"))
				.andExpect(jsonPath("$[0].questionContent").value("What is your favorite dependency?"));

	}
}

