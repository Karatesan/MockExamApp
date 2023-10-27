package com.fdmgroup.MockExam.services;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.fdmgroup.MockExam.dto.AnswerRequestDTO;
import com.fdmgroup.MockExam.dto.AnswerResponseDTO;
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
import com.fdmgroup.MockExam.model.Role;
import com.fdmgroup.MockExam.model.Tag;
import com.fdmgroup.MockExam.repositories.QuestionRepository;
import com.fdmgroup.MockExam.repositories.TagRepository;
import com.fdmgroup.MockExam.utils.ImageConverter;

@ExtendWith(MockitoExtension.class)
public class test_DTOService {

	@InjectMocks
	private DTOServiceImpl DTOService;

	@Mock
	ImageServiceImpl imageService;

	@Mock
	QuestionRepository questionRepository;
	@Mock
	TagRepository tagRepository;

	@Mock
	CategoryServiceImpl categoryService;
	@Mock
	TagServiceImpl tagService;

	@Mock
	ImageConverter imageConverter;

	Category category;
	Question question;
	Answer answer;
	QuestionRequestDTO questionRequestDTO;
	QuestionResponseDTO questionResponseDTO;
	AnswerResponseDTO answerResponseDTO;
	AnswerRequestDTO answerRequestDTO;
	ExamRequestDTO examRequestDTO;
	ImageResponseDTO imageResponseDTO;
	CreateExamResponseDTO createExamResponseDTO;
	MultipartFile imageFile;
	private Image image;

	@BeforeEach
	public void setUp() {
		category = new Category("Java");

		question = new Question();
		question.setId(3);
		question.setCategory(category);
		question.setLevel(Question.Level.BEGINNER);
		question.setQuestionContent("What is Java?");
		question.setAnswers(Arrays.asList("Sand-people living on Tatooine", "Programming language",
				"Very famouns guitarist", "Sport car"));
		question.setFeedback(Arrays.asList("Incorrect", "Correct", "Incorrect", "Incorrect"));
		question.setCorrectAnswers(Arrays.asList("Programming language"));
		image = new Image("image.jpg", 20, "image data".getBytes());
		question.setImage(image);
		Tag tag = new Tag("Programming");
		List<Tag> tags = Arrays.asList(tag);
		question.setTags(tags);

		answer = new Answer(question, Arrays.asList("Programming language"));
		answer.setId(2);

		questionRequestDTO = new QuestionRequestDTO();
		questionRequestDTO.setLevel("BEGINNER");
		questionRequestDTO.setCategory("Java");
		questionRequestDTO.setQuestionContent("What is Java?");
		questionRequestDTO.setAnswers(Arrays.asList("Sand-people living on Tatooine", "Programming language",
				"Very famouns guitarist", "Sport car"));
		questionRequestDTO.setFeedback(Arrays.asList("Incorrect", "Correct", "Incorrect", "Incorrect"));
		questionRequestDTO.setCorrectAnswers(Arrays.asList("Programming language"));
		questionRequestDTO.setTags(Arrays.asList("Programming"));

		imageFile = new MockMultipartFile("image", "image.jpg", "image/jpeg", "image data".getBytes());
		questionRequestDTO.setImageFile(imageFile);

		imageResponseDTO = new ImageResponseDTO();

		questionResponseDTO = new QuestionResponseDTO();
		questionResponseDTO.setId(1);
		questionResponseDTO.setLevel("beginner");
		questionResponseDTO.setCategory("Java");
		questionResponseDTO.setQuestionContent("What is Java?");
		questionResponseDTO.setAnswers(Arrays.asList("Sand-people living on Tatooine", "Programming language",
				"Very famouns guitarist", "Sport car"));
		questionResponseDTO.setFeedback(Arrays.asList("Incorrect", "Correct", "Incorrect", "Incorrect"));
		questionResponseDTO.setCorrectAnswers(Arrays.asList("Programming language"));

		answerRequestDTO = new AnswerRequestDTO(3, Arrays.asList("Programming language"));

		examRequestDTO = new ExamRequestDTO();
		createExamResponseDTO = new CreateExamResponseDTO();

	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_questionEntityToResponse_pushQuestion_checkQuestionEntityToResponse() {
		QuestionResponseDTO result = DTOService.questionEntityToResponse(question);

		assertEquals("Java", result.getCategory());
		assertEquals("beginner", result.getLevel());
		assertEquals("What is Java?", result.getQuestionContent());
		assertEquals(Arrays.asList("Sand-people living on Tatooine", "Programming language", "Very famouns guitarist",
				"Sport car"), result.getAnswers());
		assertEquals(Arrays.asList("Incorrect", "Correct", "Incorrect", "Incorrect"), result.getFeedback());
		assertEquals(Arrays.asList("Programming language"), result.getCorrectAnswers());
		assertNotNull(result.getImage());
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_questionEntityToResponse_pushQuestionWithNullImage() {

		question.setImage(null);
		QuestionResponseDTO result = DTOService.questionEntityToResponse(question);

		assertEquals("Java", result.getCategory());
		assertEquals("beginner", result.getLevel());
		assertEquals("What is Java?", result.getQuestionContent());
		assertEquals(Arrays.asList("Sand-people living on Tatooine", "Programming language", "Very famouns guitarist",
				"Sport car"), result.getAnswers());
		assertEquals(Arrays.asList("Incorrect", "Correct", "Incorrect", "Incorrect"), result.getFeedback());
		assertEquals(Arrays.asList("Programming language"), result.getCorrectAnswers());
		assertNull(result.getImage());
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_questionRequestToEntity_success() throws IOException {
		when(categoryService.findByName("Java")).thenReturn(category);

		Question result = DTOService.questionRequestToEntity(questionRequestDTO);

		assertEquals("Java", result.getCategory().getCategoryName());

		assertEquals(Question.Level.BEGINNER, question.getLevel());
		assertEquals("What is Java?", result.getQuestionContent());
		assertEquals(Arrays.asList("Sand-people living on Tatooine", "Programming language", "Very famouns guitarist",
				"Sport car"), result.getAnswers());
		assertEquals(Arrays.asList("Incorrect", "Correct", "Incorrect", "Incorrect"), result.getFeedback());
		assertEquals(Arrays.asList("Programming language"), result.getCorrectAnswers());
		assertEquals("image.jpg", result.getImage().getName());
		assertNotNull(result.getImage());

	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_questionRequestToEntity_nullImage() throws IOException {
		when(categoryService.findByName("Java")).thenReturn(category);
		questionRequestDTO.setImageFile(null);
		when(imageService.findByName(anyString())).thenReturn(null);
		Question result = DTOService.questionRequestToEntity(questionRequestDTO);

		assertEquals("Java", result.getCategory().getCategoryName());

		assertEquals(Question.Level.BEGINNER, question.getLevel());
		assertEquals("What is Java?", result.getQuestionContent());
		assertEquals(Arrays.asList("Sand-people living on Tatooine", "Programming language", "Very famouns guitarist",
				"Sport car"), result.getAnswers());
		assertEquals(Arrays.asList("Incorrect", "Correct", "Incorrect", "Incorrect"), result.getFeedback());
		assertEquals(Arrays.asList("Programming language"), result.getCorrectAnswers());

	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_questionRequestToEntity_WithIOException() throws IOException {

		MultipartFile mockFile = mock(MultipartFile.class);
		when(mockFile.getBytes()).thenThrow(IOException.class);

		questionRequestDTO.setImageFile(mockFile);

		when(categoryService.findByName("Java")).thenReturn(category);

		Question result = DTOService.questionRequestToEntity(questionRequestDTO);

		assertEquals("Java", result.getCategory().getCategoryName());

		assertEquals(Question.Level.BEGINNER, question.getLevel());
		assertEquals("What is Java?", result.getQuestionContent());
		assertEquals(Arrays.asList("Sand-people living on Tatooine", "Programming language", "Very famouns guitarist",
				"Sport car"), result.getAnswers());
		assertEquals(Arrays.asList("Incorrect", "Correct", "Incorrect", "Incorrect"), result.getFeedback());
		assertEquals(Arrays.asList("Programming language"), result.getCorrectAnswers());
		assertNull(result.getImage().getName());
		assertNull(result.getImage().getData());

	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_answerEntityToResponse() {

		AnswerResponseDTO result = DTOService.answerEntityToResponse(answer);
		System.out.println(result);

		assertEquals(Arrays.asList("Sand-people living on Tatooine", "Programming language", "Very famouns guitarist",
				"Sport car"), result.getQuestionResponseDTO().getAnswers());
		assertEquals(Arrays.asList("Programming language"), result.getQuestionResponseDTO().getCorrectAnswers());
		assertEquals("Java", result.getQuestionResponseDTO().getCategory());
		assertEquals(Arrays.asList("Incorrect", "Correct", "Incorrect", "Incorrect"),
				result.getQuestionResponseDTO().getFeedback());
		assertEquals("What is Java?", result.getQuestionResponseDTO().getQuestionContent());
		assertEquals("beginner", result.getQuestionResponseDTO().getLevel());
		assertEquals(Arrays.asList("Programming language"), result.getGivenAnswer());
		assertNotNull(result.getQuestionResponseDTO().getImage());
		assertEquals(2, result.getId());
		assertEquals(3, result.getQuestionResponseDTO().getId());

	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_answerRequestToEntity() {
		when(questionRepository.findById(answerRequestDTO.getQuestionId())).thenReturn(Optional.of(question));

		Answer result = DTOService.answerRequestToEntity(answerRequestDTO);

		assertEquals(Question.Level.BEGINNER, result.getQuestion().getLevel());
		assertEquals(Arrays.asList("Sand-people living on Tatooine", "Programming language", "Very famouns guitarist",
				"Sport car"), result.getQuestion().getAnswers());
		assertEquals("Java", result.getQuestion().getCategory().getCategoryName());
		assertEquals(Arrays.asList("Programming language"), result.getQuestion().getCorrectAnswers());
		assertEquals(Arrays.asList("Incorrect", "Correct", "Incorrect", "Incorrect"),
				result.getQuestion().getFeedback());
		assertEquals("What is Java?", result.getQuestion().getQuestionContent());
		assertNotNull(result.getQuestion().getImage());

		assertEquals(Arrays.asList("Programming language"), answerRequestDTO.getGivenAnswer());
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_examRequestToEntity() {
		when(questionRepository.findById(answerRequestDTO.getQuestionId())).thenReturn(Optional.of(question));

		examRequestDTO.setAnswers(Arrays.asList(answerRequestDTO));

		Exam result = DTOService.examRequestToEntity_complete(examRequestDTO);

		assertEquals(1, result.getAnswers().size());
		assertEquals(Arrays.asList("Programming language"), result.getAnswers().get(0).getGivenAnswer());
		assertEquals(Arrays.asList("Sand-people living on Tatooine", "Programming language", "Very famouns guitarist",
				"Sport car"), result.getAnswers().get(0).getQuestion().getAnswers());
		assertEquals(Question.Level.BEGINNER, result.getAnswers().get(0).getQuestion().getLevel());
		assertEquals(Arrays.asList("Programming language"),
				result.getAnswers().get(0).getQuestion().getCorrectAnswers());
		assertEquals(Arrays.asList("Incorrect", "Correct", "Incorrect", "Incorrect"),
				result.getAnswers().get(0).getQuestion().getFeedback());
		assertEquals("What is Java?", result.getAnswers().get(0).getQuestion().getQuestionContent());
		assertEquals("Java", result.getAnswers().get(0).getQuestion().getCategory().getCategoryName());

		assertNotNull(result.getAnswers().get(0).getQuestion().getImage());

	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_examEntityToResponsey() {

		LocalDateTime time = LocalDateTime.now();
		Exam exam = new Exam(time, Arrays.asList(answer));

		ExamResponseDTO result = DTOService.examEntityToResponse_complete(exam);

		assertEquals(1, result.getAnswersDTO().size());
		assertEquals(Arrays.asList("Programming language"), result.getAnswersDTO().get(0).getGivenAnswer());
		assertEquals(Arrays.asList("Sand-people living on Tatooine", "Programming language", "Very famouns guitarist",
				"Sport car"), result.getAnswersDTO().get(0).getQuestionResponseDTO().getAnswers());
		assertEquals(Arrays.asList("Programming language"),
				result.getAnswersDTO().get(0).getQuestionResponseDTO().getCorrectAnswers());
		assertEquals(Arrays.asList("Incorrect", "Correct", "Incorrect", "Incorrect"),
				result.getAnswersDTO().get(0).getQuestionResponseDTO().getFeedback());
		assertEquals("What is Java?", result.getAnswersDTO().get(0).getQuestionResponseDTO().getQuestionContent());
		assertNotNull(result.getAnswersDTO().get(0).getQuestionResponseDTO().getImage());
		assertEquals("Java", result.getAnswersDTO().get(0).getQuestionResponseDTO().getCategory());
		assertEquals(2, result.getAnswersDTO().get(0).getId());
		assertEquals(3, result.getAnswersDTO().get(0).getQuestionResponseDTO().getId());

	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_examEntityToResponse_create() {
		List<Question> questions = Arrays.asList(question);
		CreateExamResponseDTO result = DTOService.examEntityToResponse_create(questions);
		assertEquals(Arrays.asList("Sand-people living on Tatooine", "Programming language", "Very famouns guitarist",
				"Sport car"), result.getQuestionsDTO().get(0).getAnswers());
		assertEquals(Arrays.asList("Programming language"), result.getQuestionsDTO().get(0).getCorrectAnswers());
		assertEquals(Arrays.asList("Incorrect", "Correct", "Incorrect", "Incorrect"),
				result.getQuestionsDTO().get(0).getFeedback());
		assertEquals("What is Java?", result.getQuestionsDTO().get(0).getQuestionContent());
		assertNotNull(result.getQuestionsDTO().get(0).getImage());
		assertEquals("Java", result.getQuestionsDTO().get(0).getCategory());
		assertEquals(3, result.getQuestionsDTO().get(0).getId());

	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_FindRoleByName_ValidRoleName_ReturnsCorrectRole() {
		String roleName = "ADMIN";

		Role role = DTOService.findRoleByName(roleName);

		assertEquals(Role.ADMIN, role);
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_FindRoleByName_InvalidRoleName_ReturnsDefaultRole() {
		String roleName = "NonExistentRole";

		Role role = DTOService.findRoleByName(roleName);

		assertEquals(Role.TRAINEE, role);
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_FindRoleByName_CaseInsensitiveRoleName_ReturnsCorrectRole() {
		String roleName = "admin";

		Role role = DTOService.findRoleByName(roleName);

		assertEquals(Role.ADMIN, role);
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_findOrCreateTag_tagExists() {
		String tagName = "TestTag";
		Tag existingTag = new Tag(tagName);
		when(tagService.findByName(tagName)).thenReturn(Optional.of(existingTag));

		Tag result = DTOService.findOrCreateTag(tagName);

		assertNotNull(result);
		assertEquals(existingTag, result);
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_findOrCreateTag_tagDoesNotExist() {
		String tagName = "NonExistingTag";
		Tag newTag = new Tag(tagName);
		when(tagService.findByName(tagName)).thenReturn(Optional.empty());
		when(tagService.newTag(tagName)).thenReturn(newTag);

		Tag result = DTOService.findOrCreateTag(tagName);
		verify(tagService).newTag(tagName);
		assertNotNull(result);
		assertEquals(tagName, result.getName());
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_convertListTagsToString() {
		List<Tag> tags = new ArrayList<>();
		tags.add(new Tag("Tag1"));
		tags.add(new Tag("Tag2"));
		tags.add(new Tag("Tag3"));

		List<String> strings = DTOService.convertListTagsToString(tags);

		assertNotNull(strings);
		assertEquals(3, strings.size());
		assertTrue(strings.contains("Tag1"));
		assertTrue(strings.contains("Tag2"));
		assertTrue(strings.contains("Tag3"));
	}
}
