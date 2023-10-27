package com.fdmgroup.MockExam.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fdmgroup.MockExam.dto.AnswerRequestDTO;
import com.fdmgroup.MockExam.dto.AnswerResponseDTO;
import com.fdmgroup.MockExam.dto.CreateExamRequestDTO;
import com.fdmgroup.MockExam.dto.CreateExamResponseDTO;
import com.fdmgroup.MockExam.dto.ExamRequestDTO;
import com.fdmgroup.MockExam.dto.ExamResponseDTO;
import com.fdmgroup.MockExam.dto.ImageResponseDTO;
import com.fdmgroup.MockExam.dto.QuestionRequestDTO;
import com.fdmgroup.MockExam.dto.QuestionResponseDTO;
import com.fdmgroup.MockExam.model.Answer;
import com.fdmgroup.MockExam.model.Category;
import com.fdmgroup.MockExam.model.Exam;
import com.fdmgroup.MockExam.model.Image;
import com.fdmgroup.MockExam.model.Question;
import com.fdmgroup.MockExam.model.Tag;
import com.fdmgroup.MockExam.model.User;
import com.fdmgroup.MockExam.services.DTOService;
import com.fdmgroup.MockExam.services.ExamService;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(Lifecycle.PER_CLASS)
public class test_ExamController {
	
	@Autowired
	private ExamController controller;
	
	@Autowired
	MockMvc mvc;
	
	@Autowired
    private ObjectMapper objectMapper;
	
	@MockBean
	DTOService dtoService;
	
	@MockBean(name ="examService")
	ExamService examService;
	
	Category category;
	Image image;
	Question question;
	ImageResponseDTO imageRes;
	List<Question> questions;
	QuestionResponseDTO questionResponseDTO;
	List<QuestionResponseDTO> examQuestions;
	List<AnswerRequestDTO> answerRequests;
	List<String> answers;
	QuestionRequestDTO completeExamQuestion;
	AnswerRequestDTO completeExamAnswer;
	AnswerResponseDTO answerResponseDTO;
	Exam exam;
	ExamResponseDTO examResult;
	
	
	static String validJwtToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QHRlc3QuY29tIiwiaWF0IjoxNjkyMDIwOTIxLCJleHAiOjE3OTIwMjIzNjF9.R3OqXtVwU5DdANVB8Qk8w1MFvYkdJFSOqnqsG3jVNtU";
	
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
				Arrays.asList("Incorrect", "Correct", "Correct", "Incorrect")
				, Arrays.asList("JUnit", "Spring"), image, tags);

		questions = Arrays.asList(question);
		questionResponseDTO = new QuestionResponseDTO("Java", "beginner", "What is your favorite dependency?",
				Arrays.asList("JPA", "JUnit", "Spring", "Hibernate"),
				Arrays.asList("Incorrect", "Correct", "Correct", "Incorrect"), Arrays.asList("JUnit", "Spring")
				,imageRes, Arrays.asList("JUnit", "Spring"));
		
		examQuestions = Arrays.asList(questionResponseDTO);
		completeExamQuestion = new QuestionRequestDTO(
				"Java",
				"beginner",
				"What is your favorite dependency?",
				Arrays.asList("JPA", "JUnit", "Spring", "Hibernate"),
				Arrays.asList("Incorrect", "Correct", "Correct", "Incorrect"),
				Arrays.asList("JUnit", "Spring"), Arrays.asList("JUnit", "Spring"));
		answers = Arrays.asList("JUnit","Spring");
	
		completeExamAnswer = new AnswerRequestDTO(question.getId(), answers);
		answerRequests = Arrays.asList(completeExamAnswer);
		
		answerResponseDTO = new AnswerResponseDTO(0, questionResponseDTO, answers);
		
		examResult = new ExamResponseDTO("today", Arrays.asList(answerResponseDTO));
		
		
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_controller_NotNull() {
		
		assertThat(controller).isNotNull();
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	@WithMockUser(username= "test@test.com" , password = "testPassword")
	public void test_takeExam_passesCorrectCreateExamRequestDTOtoDTOService() throws JsonProcessingException, Exception {
		CreateExamRequestDTO request = new CreateExamRequestDTO("testCategory","testLevel");
		CreateExamResponseDTO response = new CreateExamResponseDTO(examQuestions);
		when(examService.takeExam(request)).thenReturn(questions);
		when(dtoService.examEntityToResponse_create(questions)).thenReturn(response);

		mvc.perform(get("/api/exam/takeExam").header("Authorization", "Bearer " + validJwtToken)
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.flashAttr("CreateExamRequestDTO", request))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.questionsDTO[0].category").value("Java"))
				.andExpect(jsonPath("$.questionsDTO[0].answers[0]").value("JPA"))
				.andExpect(jsonPath("$.questionsDTO[0].answers[1]").value("JUnit"))
				.andExpect(jsonPath("$.questionsDTO[0].answers[2]").value("Spring"))
				.andExpect(jsonPath("$.questionsDTO[0].answers[3]").value("Hibernate"))
				.andExpect(jsonPath("$.questionsDTO[0].correctAnswers[0]").value("JUnit"))
				.andExpect(jsonPath("$.questionsDTO[0].correctAnswers[1]").value("Spring"))
				.andExpect(jsonPath("$.questionsDTO[0].image").exists())
				.andExpect(jsonPath("$.questionsDTO[0].feedback[0]").value("Incorrect"))
				.andExpect(jsonPath("$.questionsDTO[0].feedback[1]").value("Correct"))
				.andExpect(jsonPath("$.questionsDTO[0].feedback[2]").value("Correct"))
				.andExpect(jsonPath("$.questionsDTO[0].feedback[3]").value("Incorrect"))
				.andExpect(jsonPath("$.questionsDTO[0].level").value("beginner"))
				.andExpect(jsonPath("$.questionsDTO[0].questionContent").value("What is your favorite dependency?"));
			
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	@WithMockUser(username= "test@test.com" , password = "testPassword")
	public void test_completeExam_passesCorrectExamRequestDTOtoDTOService() throws JsonProcessingException, Exception {
		LocalDateTime time = LocalDateTime.now();
		ExamRequestDTO examRequest = new ExamRequestDTO(answerRequests);
		User user = new User();
		Answer answer = new Answer(question, answers);
		exam = new Exam(LocalDateTime.now(), user , Arrays.asList(answer));
		
		when(dtoService.examRequestToEntity_complete(examRequest)).thenReturn(exam);
		when(examService.completeExam("Bearer " + validJwtToken, exam, time)).thenReturn(exam);
		when(dtoService.examEntityToResponse_complete(exam)).thenReturn(examResult);
		
		
		mvc.perform(post("/api/exam/completeExam").header("Authorization", "Bearer " + validJwtToken)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(examRequest)))
				.andExpect(status().isOk())
//				.andExpect(jsonPath("$.examDate").value("today"))
//				.andExpect(jsonPath("$.answersDTO[0].questionResponseDTO.category").value("Java"))
//				.andExpect(jsonPath("$.answersDTO[0].questionResponseDTO.answers[0]").value("JPA"))
//				.andExpect(jsonPath("$.answersDTO[0].questionResponseDTO.answers[1]").value("JUnit"))
//				.andExpect(jsonPath("$.answersDTO[0].questionResponseDTO.answers[2]").value("Spring"))
//				.andExpect(jsonPath("$.answersDTO[0].questionResponseDTO.answers[3]").value("Hibernate"))
//				.andExpect(jsonPath("$.answersDTO[0].questionResponseDTO.correctAnswers[0]").value("JUnit"))
//				.andExpect(jsonPath("$.answersDTO[0].questionResponseDTO.correctAnswers[1]").value("Spring"))
//				.andExpect(jsonPath("$.answersDTO[0].questionResponseDTO.image").exists())
//				.andExpect(jsonPath("$.answersDTO[0].questionResponseDTO.feedback[0]").value("Incorrect"))
//				.andExpect(jsonPath("$.answersDTO[0].questionResponseDTO.feedback[1]").value("Correct"))
//				.andExpect(jsonPath("$.answersDTO[0].questionResponseDTO.feedback[2]").value("Correct"))
//				.andExpect(jsonPath("$.answersDTO[0].questionResponseDTO.feedback[3]").value("Incorrect"))
//				.andExpect(jsonPath("$.answersDTO[0].questionResponseDTO.level").value("beginner"))
//				.andExpect(jsonPath("$.answersDTO[0].questionResponseDTO.questionContent").value("What is your favorite dependency?"))
//				.andExpect(jsonPath("$.answersDTO[0].givenAnswer[0]").value("JUnit"))
//				.andExpect(jsonPath("$.answersDTO[0].givenAnswer[1]").value("Spring"))
				;
				
	}
	
	
}

