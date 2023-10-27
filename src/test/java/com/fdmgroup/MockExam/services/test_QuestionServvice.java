package com.fdmgroup.MockExam.services;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fdmgroup.MockExam.dto.QuestionRequestDTO;
import com.fdmgroup.MockExam.dto.QuestionResponseDTO;
import com.fdmgroup.MockExam.exceptions.InvalidCategoryException;
import com.fdmgroup.MockExam.model.Category;
import com.fdmgroup.MockExam.model.Question;
import com.fdmgroup.MockExam.model.Question.Level;
import com.fdmgroup.MockExam.model.Tag;
import com.fdmgroup.MockExam.repositories.NotificationRepository;
import com.fdmgroup.MockExam.repositories.ImageRepository;
import com.fdmgroup.MockExam.repositories.QuestionRepository;
import com.fdmgroup.MockExam.utils.ImageConverter;
import com.fdmgroup.MockExam.validators.ObjectValidator;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
public class test_QuestionServvice {
	@InjectMocks
	private QuestionServiceImpl questionService;

	@Mock
	QuestionRepository questionRepository;
	@Mock
	CategoryServiceImpl categoryService;

	@Mock
	ObjectValidator<QuestionRequestDTO> questionRequestDTOValidator;
	   @Mock
	    private NotificationRepository flagQuestionRepository;
	@Mock
	ImageRepository imageRepository;

	ImageConverter imageConverter;
	@Mock
	DTOServiceImpl dTOService;

	private Question q1;
	private Question q2;
	private Question q3;
	private List<Question> questions;
	private Category category;
	QuestionResponseDTO questionResponseDTO;
	QuestionRequestDTO questionRequestDTO;

	@BeforeEach
	public void setUp() {
		questionRequestDTO = new QuestionRequestDTO();
		q1 = new Question();
		q2 = new Question();
		q3 = new Question();
		q1.setLevel(Level.BEGINNER);
		q2.setLevel(Level.INTERMEDIATE);
		q3.setLevel(Level.EXPERT);

		questions = new ArrayList<>();
		questions.add(q1);
		questions.add(q2);
		questions.add(q3);
		category = new Category("Java");
		new ObjectMapper();
		questionRequestDTO.setCategory("Java");
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_findAllQuestions_checkRepositoryFinding() {
		doReturn(questions).when(questionRepository).findAll();

		List<Question> actual = questionService.findAllQuestions();

		assertEquals(questions, actual);
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_findById_ValidId_ReturnsQuestion() {
		Integer id = 1;
		Question expectedQuestion = new Question();
		when(questionRepository.findById(id)).thenReturn(Optional.of(expectedQuestion));

		Question actualQuestion = questionService.findById(id);

		assertEquals(expectedQuestion, actualQuestion);
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_findById_InvalidId_ThrowsEntityNotFoundException() {
		Integer id = 1;
		when(questionRepository.findById(id)).thenReturn(Optional.empty());

		assertThrows(EntityNotFoundException.class, () -> {
			questionService.findById(id);
		});
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_findById_NullId_ThrowsIllegalArgumentException() {

		assertThrows(EntityNotFoundException.class, () -> {
			questionService.findById(null);
		});
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_findAllQuestionsInCategory_pushCategpry_checkRepositoryFinding() {
		doReturn(category).when(categoryService).findByName(Mockito.anyString());
		doReturn(questions).when(questionRepository).findByCategory(category);

		List<Question> actual = questionService.findAllQuestionsInCategory("java");

		assertEquals(questions, actual);
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void testFindAllQuestionsInCategory_InvalidCategory() {
		String invalidCategory = "NonExistentCategory";

		when(categoryService.findByName(invalidCategory)).thenReturn(null);

		assertThrows(InvalidCategoryException.class, () -> {
			questionService.findAllQuestionsInCategory(invalidCategory);
		});
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_filterQuestionsByLevel_removesNonMatchingQuestions() {
		List<Question> questions = new ArrayList<>();
		Level level = Level.INTERMEDIATE;

		Question question1 = new Question();
		question1.setLevel(Level.BEGINNER);
		questions.add(question1);

		Question question2 = new Question();
		question2.setLevel(Level.INTERMEDIATE);
		questions.add(question2);

		Question question3 = new Question();
		question3.setLevel(Level.EXPERT);
		questions.add(question3);

		List<Question> filteredQuestions = questionService.filterQuestionsByLevel(questions, level);

		assertEquals(1, filteredQuestions.size());
		assertEquals(question2, filteredQuestions.get(0));
		assertEquals(q2, filteredQuestions.get(0));
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_findRandomQuestions_returnsRandomSubset() {
		questions = new ArrayList<>();
		for (int i = 1; i <= 10; i++) {
			Question question = new Question();
			question.setId(i);
			questions.add(question);
		}

		List<Question> randomQuestions = questionService.findRandomQuestions(questions, 5);

		assertEquals(5, randomQuestions.size());
		assertTrue(questions.containsAll(randomQuestions));
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_findByQuestionContentContainsIgnoreCase_checkRepositoryFinding() {
		doReturn(questions).when(questionRepository).findByQuestionContentContainsIgnoreCaseAndLockedIsFalse("abc");

		List<Question> actual = questionService.findByQuestionContentContainsIgnoreCaseAndLockedIsFalse("abc");

		assertEquals(questions, actual);
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_findByAnswers_checkRepositoryFinding() {
		doReturn(questions).when(questionRepository).findByAnswers("abc");

		List<Question> actual = questionService.findByAnswers("abc");

		assertEquals(questions, actual);
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_findByFeedback_checkRepositoryFinding() {
		doReturn(questions).when(questionRepository).findByFeedback("abc");

		List<Question> actual = questionService.findByFeedback("abc");

		assertEquals(questions, actual);
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_findByTags_checkRepositoryFinding() {
		doReturn(questions).when(questionRepository).findByTags("abc");

		List<Question> actual = questionService.findByTags("abc");

		assertEquals(questions, actual);
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_findByTagsContains_checkRepositoryFinding() {
		Tag tag = new Tag();
		doReturn(questions).when(questionRepository).findByTagsContains(tag);

		List<Question> actual = questionService.findByTagsContains(tag);

		assertEquals(questions, actual);
	}
	
	  @Test
	    public void test_findByQuestionContent() {
	        String searchString = "example";

	       

	        Mockito.when(questionRepository.findByQuestionContent(anyString())).thenReturn(questions);

	        List<Question> result = questionService.findByQuestionContent(searchString);

	        assertEquals(questions, result);
	    }
}
