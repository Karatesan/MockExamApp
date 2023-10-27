package com.fdmgroup.MockExam.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fdmgroup.MockExam.exceptions.AdminAccesException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fdmgroup.MockExam.dto.AccountIdRequestDTO;
import com.fdmgroup.MockExam.dto.AccountIdRoleRequestDTO;
import com.fdmgroup.MockExam.dto.ChangeNameResponseDTO;
import com.fdmgroup.MockExam.dto.ConfirmationResponseDTO;
import com.fdmgroup.MockExam.dto.IdFoundInSearchDTO;
import com.fdmgroup.MockExam.dto.QuestionRequestDTO;
import com.fdmgroup.MockExam.dto.QuestionResponseDTO;
import com.fdmgroup.MockExam.dto.SearchQuestionRequestDTO;
import com.fdmgroup.MockExam.dto.SearchQuestionResponseDTO;
import com.fdmgroup.MockExam.dto.SearchUserRequestDTO;
import com.fdmgroup.MockExam.dto.SearchUserResponseDTO;
import com.fdmgroup.MockExam.dto.UserFoundInSearchDTO;
import com.fdmgroup.MockExam.dto.QuestionFoundInSearchDTO;
import com.fdmgroup.MockExam.model.Category;
import com.fdmgroup.MockExam.model.Image;
import com.fdmgroup.MockExam.model.NotificationFirstName;
import com.fdmgroup.MockExam.model.NotificationLastName;
import com.fdmgroup.MockExam.model.Question;
import com.fdmgroup.MockExam.model.Role;
import com.fdmgroup.MockExam.model.Tag;
import com.fdmgroup.MockExam.model.User;
import com.fdmgroup.MockExam.model.Question.Level;
import com.fdmgroup.MockExam.repositories.ImageRepository;
import com.fdmgroup.MockExam.repositories.QuestionRepository;
import com.fdmgroup.MockExam.repositories.UserRepository;
import com.fdmgroup.MockExam.validators.ObjectValidator;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)

public class test_AdminService {
	@InjectMocks
	private AdminServiceImpl adminService;

	@Mock
	private UserRepository userRepository;

	@Mock
	private UserService userService;

	@Mock
	private QuestionRepository questionRepository;

	@Mock
	private ImageRepository imageRepository;

	@Mock
	private CategoryService categoryService;
	@Mock
	private QuestionService questionService;

	@Mock
	TagService tagService;

	@Mock
	private ObjectValidator<QuestionRequestDTO> questionRequestDTOValidator;

	@Mock
	private DTOService dTOService;

	@Mock
	private SearchingSortService searchingSortService;

	@Mock
	private NotificationService notificationService;

	private Question question1;
	private Question question2;
	private Question question3;
	Integer questionId = 1;
	private List<Question> questions;
	private Category category;
	QuestionResponseDTO questionResponseDTO;
	QuestionRequestDTO questionRequestDTO;

	List<String> answersDTO;

	List<String> feedbackDTO;
	List<String> correctAnswersDTO;
	List<String> tagsDTO;
	List<IdFoundInSearchDTO> list;
	List<IdFoundInSearchDTO> listFull;

	List<Question> mockQuestions;
	Category mockCategory;
	Level mockLevel;
	Tag tag;
	Optional<Tag> optTag;
	SearchQuestionRequestDTO request;

	List<User> mockUsers;
	SearchUserRequestDTO requestUser;

	public void setUpQuestions() {

		mockQuestions = new ArrayList<>();
		mockCategory = new Category("Java");
		mockLevel = Level.INTERMEDIATE;
		questionRequestDTO = new QuestionRequestDTO();
		questionResponseDTO = new QuestionResponseDTO();
		list = new ArrayList<>();
		listFull = new ArrayList<>();

		questionRequestDTO.setCategory("Java");
		questionRequestDTO.setLevel("BEGINNER");
		questionRequestDTO.setQuestionContent("Why is the code gone?");
		questionResponseDTO.setQuestionContent("Why is the code gone?");

		answersDTO = new ArrayList<>();
		answersDTO.add("Because git get crashed");
		answersDTO.add("Magic");
		questionRequestDTO.setAnswers(answersDTO);
		questionResponseDTO.setAnswers(answersDTO);

		feedbackDTO = new ArrayList<>();
		feedbackDTO.add("Sometimes it's true");
		feedbackDTO.add("Sometimes it's also true, but not this case");
		questionRequestDTO.setFeedback(feedbackDTO);
		questionResponseDTO.setFeedback(feedbackDTO);

		correctAnswersDTO = new ArrayList<>();
		correctAnswersDTO.add("Because git get crashed");
		questionRequestDTO.setCorrectAnswers(correctAnswersDTO);
		questionResponseDTO.setCorrectAnswers(correctAnswersDTO);

		tagsDTO = new ArrayList<>();
		tagsDTO.add("Guten tag");
		questionRequestDTO.setTags(tagsDTO);
		questionResponseDTO.setTags(tagsDTO);

		question1 = new Question();
		question2 = new Question();
		question3 = new Question();
		question1.setId(questionId);
		question1.setLevel(Level.BEGINNER);
		question2.setLevel(Level.INTERMEDIATE);
		question3.setLevel(Level.EXPERT);

		questions = new ArrayList<>();
		questions.add(question1);
		questions.add(question2);
		questions.add(question3);
		category = new Category("Java");
		new ObjectMapper();

		request = new SearchQuestionRequestDTO();
		request.setSearchText("sample");
		request.setCategory("Java");
		request.setLevel("INTERMEDIATE");
		request.setPage(1);
		request.setTag("Guten Tag!");

		tag = new Tag("Guten Tag!");
		optTag = Optional.of(tag);

		for (int i = 1; i <= 10; i++) {
			Question question0 = new Question();
			question0.setCategory(mockCategory);
			question0.setLevel(mockLevel);
			question0.setId(i);
			question0.setQuestionContent("This is content of question: " + i);
			question0.setTags(Arrays.asList(tag));

			QuestionFoundInSearchDTO questionDTO = new QuestionFoundInSearchDTO();
			questionDTO.setId(11);
			questionDTO.setCategory(mockCategory.getCategoryName());
			questionDTO.setLevel(mockLevel.toString());
			questionDTO.setQuestionContent("Sample question 1");
			questionDTO.setAnswers(Arrays.asList("Sample answer 1", "Answer 2"));
			questionDTO.setTags(Arrays.asList(tag.getName()));
			listFull.add(questionDTO);

			mockQuestions.add(question0);
		}
		Question question1 = new Question();
		QuestionFoundInSearchDTO questionDTO = new QuestionFoundInSearchDTO();

		question1.setId(11);
		questionDTO.setId(11);

		question1.setCategory(mockCategory);
		questionDTO.setCategory(mockCategory.getCategoryName());

		question1.setLevel(mockLevel);
		questionDTO.setLevel(mockLevel.toString());

		question1.setQuestionContent("Sample question 1");
		questionDTO.setQuestionContent("Sample question 1");

		question1.setAnswers(Arrays.asList("Sample answer 1", "Answer 2"));
		questionDTO.setAnswers(Arrays.asList("Sample answer 1", "Answer 2"));
		question1.setTags(Arrays.asList(tag));
		questionDTO.setTags(Arrays.asList(tag.getName()));

		mockQuestions.add(question1);
		list.add(questionDTO);
		listFull.add(questionDTO);

		Question question2 = new Question();
		QuestionFoundInSearchDTO questionDTO2 = new QuestionFoundInSearchDTO();

		question2.setId(12);
		questionDTO2.setId(12);

		question2.setCategory(mockCategory);
		questionDTO2.setCategory(mockCategory.getCategoryName());

		question2.setLevel(mockLevel);
		questionDTO2.setLevel(mockLevel.toString());
		question2.setQuestionContent("Sample question 2");
		questionDTO2.setQuestionContent("Sample question 2");
		question2.setAnswers(Arrays.asList("Sample answer 2", "Huston"));
		questionDTO2.setAnswers(Arrays.asList("Sample answer 2", "Huston"));
		question2.setTags(Arrays.asList(tag));
		questionDTO2.setTags(Arrays.asList(tag.getName()));
		mockQuestions.add(question2);
		list.add(questionDTO2);
		listFull.add(questionDTO2);
	}

	public void setUpUsers() {
		mockUsers = new ArrayList<>();
		list = new ArrayList<>();
		listFull = new ArrayList<>();

		tag = new Tag("Guten Tag!");
		optTag = Optional.of(tag);

		requestUser = new SearchUserRequestDTO();
		requestUser.setSearchText("sample");

		requestUser.setSearchFields(Arrays.asList("first_name", "last_name", "email"));
		requestUser.setPage(3);
		requestUser.setTag("Guten Tag!");

		for (int i = 1; i <= 10; i++) {
			User user0 = new User();

			user0.setId(i);
			user0.setFirstName("First name of: " + i);
			user0.setLastName("Last name of: " + i);
			user0.setEmail("Email name of: " + i);
			user0.setTags(Arrays.asList(tag));
			user0.setLocked(false);
			mockUsers.add(user0);

			UserFoundInSearchDTO userDTO = new UserFoundInSearchDTO();
			userDTO.setId(i);
			userDTO.setFirstName("First name of: " + i);
			userDTO.setLastName("Last name of: " + i);
			userDTO.setEmail("Email of: " + i);
			userDTO.setTags(Arrays.asList(tag.getName()));
			listFull.add(userDTO);
		}
		User user1 = new User();
		user1.setId(11);
		user1.setFirstName("First name of: 11");
		user1.setLastName("Last name of: 11");
		user1.setEmail("Email of: 11");
		user1.setTags(Arrays.asList(tag));
		user1.setLocked(false);

		mockUsers.add(user1);

		UserFoundInSearchDTO userDTO = new UserFoundInSearchDTO();
		userDTO.setId(11);
		userDTO.setFirstName("First name of: 11");
		userDTO.setLastName("Last name of: 11");
		userDTO.setEmail("Email of: 11");
		userDTO.setTags(Arrays.asList(tag.getName()));
		list.add(userDTO);
		listFull.add(userDTO);

		User user2 = new User();
		user2.setId(12);
		user2.setFirstName("First name of: 12");
		user2.setLastName("Last name of: 12");
		user2.setEmail("Email of: 12");
		user2.setTags(Arrays.asList(tag));
		user2.setLocked(false);

		mockUsers.add(user2);

		UserFoundInSearchDTO userDTO2 = new UserFoundInSearchDTO();
		userDTO2.setId(12);
		userDTO2.setFirstName("First name of: 12");
		userDTO2.setLastName("Last name of: 12");
		userDTO2.setEmail("Email of: 12");
		userDTO2.setTags(Arrays.asList(tag.getName()));
		list.add(userDTO2);
		listFull.add(userDTO2);

	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_lockAccount_ValidId_lockAccount() {
		Integer userId = 1;
		AccountIdRequestDTO requestDTO = new AccountIdRequestDTO();
		User user = new User();
		user.setId(userId);
		requestDTO.setId(userId);
		when(userRepository.findById(userId)).thenReturn(Optional.of(user));

		ConfirmationResponseDTO responseDTO = adminService.blockAccount(requestDTO, true);

		assertTrue(user.getLocked());
		assertEquals("Account has been blocked.", responseDTO.getConfirmationMessage());
		verify(userRepository, times(1)).save(user);
	}

	@Test
	public void test_lockAccount_ValidId_unllockAccount() {
		Integer userId = 1;
		AccountIdRequestDTO requestDTO = new AccountIdRequestDTO();
		User user = new User();
		user.setId(userId);
		requestDTO.setId(userId);
		when(userRepository.findById(userId)).thenReturn(Optional.of(user));

		ConfirmationResponseDTO responseDTO = adminService.blockAccount(requestDTO, false);

		assertFalse(user.getLocked());
		assertEquals("Account has been unblocked.", responseDTO.getConfirmationMessage());
		verify(userRepository, times(1)).save(user);
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_blockAccount_InvalidId_ReturnInvalidIDResponse() {
		AccountIdRequestDTO requestDTO = new AccountIdRequestDTO(null);

		assertThrows(EntityNotFoundException.class, () -> {

			adminService.blockAccount(requestDTO, true);
		});

	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_deleteAccount_ValidId_DeleteAccount() {
		Integer userId = 1;
		AccountIdRequestDTO requestDTO = new AccountIdRequestDTO(userId);
		User user = new User();
		user.setId(userId);
		requestDTO.setId(userId);
		when(userRepository.findById(userId)).thenReturn(Optional.of(user));

		ConfirmationResponseDTO responseDTO = adminService.deleteAccount(userId);

		assertNull(user.getFirstName());
		assertNull(user.getLastName());
		assertEquals("deletedAccount1@mail.com", user.getEmail());
		assertNull(user.getUserImage());
		assertEquals("deletedPassword", user.getPassword());
		assertEquals("Account has been deleted.", responseDTO.getConfirmationMessage());
		verify(userRepository, times(1)).save(user);
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_deleteAccount_UserNotFound_ReturnUserNotFoundResponse() {
		Integer userId = 1;

		when(userRepository.findById(userId)).thenReturn(Optional.empty());

		assertThrows(EntityNotFoundException.class, () -> {
			adminService.deleteAccount(userId);
		});
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_changeAccountRole_ValidIdAndRoleName_ChangesRole() {
		Integer userId = 1;
		String roleName = "ADMIN";
		AccountIdRoleRequestDTO requestDTO = new AccountIdRoleRequestDTO();
		User user = new User();
		user.setId(userId);
		requestDTO.setId(userId);
		requestDTO.setRoleName(roleName);

		when(userRepository.findById(userId)).thenReturn(Optional.of(user));
		when(dTOService.findRoleByName(roleName)).thenReturn(Role.ADMIN);

		ConfirmationResponseDTO responseDTO = adminService.changeAccountRole(requestDTO);

		assertEquals(Role.ADMIN, user.getRole());
		assertEquals("Role has been changed.", responseDTO.getConfirmationMessage());
		verify(userRepository, times(1)).save(user);
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_changeAccountRole_UserNotFound_ReturnsUserNotFoundResponse() {
		Integer userId = 1;
		String roleName = "ADMIN";
		AccountIdRoleRequestDTO requestDTO = new AccountIdRoleRequestDTO();
		requestDTO.setId(userId);
		requestDTO.setRoleName(roleName);

		when(userRepository.findById(userId)).thenReturn(Optional.empty());
		assertThrows(EntityNotFoundException.class, () -> {
			adminService.changeAccountRole(requestDTO);
		});
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_addQuestion_NullCategory() {
		setUpQuestions();

		questionRequestDTO.setCategory("Java");
		Category savedCategory = new Category(questionRequestDTO.getCategory());
		QuestionResponseDTO responseDTO = new QuestionResponseDTO();

		when(categoryService.findByName(questionRequestDTO.getCategory())).thenReturn(null);
		when(categoryService.saveCategory(any())).thenReturn(savedCategory);
		when(dTOService.questionRequestToEntity(questionRequestDTO)).thenReturn(question1);
		when(questionRepository.save(question1)).thenReturn(question1);
		when(dTOService.questionEntityToResponse(question1)).thenReturn(responseDTO);

		QuestionResponseDTO addedQuestion = adminService.addQuestion(questionRequestDTO);

		assertNotNull(addedQuestion);
		verify(categoryService).findByName(questionRequestDTO.getCategory());
		verify(categoryService).saveCategory(any());
		verify(questionRepository).save(question1);
		verify(dTOService).questionEntityToResponse(question1);
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_addQuestion_ValidRequest_returnsResponseDTO() {
		setUpQuestions();
		QuestionRequestDTO requestDTO = new QuestionRequestDTO();
		Question newQuestion = new Question();
		QuestionResponseDTO expectedResponseDTO = new QuestionResponseDTO();

		requestDTO.setCategory("Java");

		when(categoryService.findByName("Java")).thenReturn(category);
		when(dTOService.questionRequestToEntity(requestDTO)).thenReturn(newQuestion);
		when(questionRepository.save(newQuestion)).thenReturn(newQuestion);
		when(dTOService.questionEntityToResponse(newQuestion)).thenReturn(expectedResponseDTO);

		QuestionResponseDTO responseDTO = adminService.addQuestion(requestDTO);

		assertNotNull(responseDTO);
		assertEquals(expectedResponseDTO, responseDTO);
		verify(questionRequestDTOValidator).validate(requestDTO);
		verify(categoryService).findByName("Java");
		verify(dTOService).questionRequestToEntity(requestDTO);
		verify(questionRepository).save(newQuestion);
		verify(dTOService).questionEntityToResponse(newQuestion);
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_deleteQuestion_questionExists() {
		setUpQuestions();
		Image image = new Image();
		image.setId(1);
		image.setName("TestImage");
		question1.setImage(image);
		Optional<Question> questionOptional = Optional.of(question1);

		when(questionRepository.findById(questionId)).thenReturn(questionOptional);

		ConfirmationResponseDTO result = adminService.deleteQuestion(questionId);

		assertNotNull(result);
		assertEquals("Question has been deleted.", result.getConfirmationMessage());
		verify(imageRepository).deleteById(image.getId());
		verify(questionRepository).deleteById(questionId);
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_deleteQuestion_questionExists_lockedException() {
		setUpQuestions();
		question1.setLocked(false);
		Image image = new Image();
		image.setId(1);
		image.setName("TestImage");
		question1.setImage(image);
		Optional<Question> questionOptional = Optional.of(question1);

		when(questionRepository.findById(questionId)).thenReturn(questionOptional);
		doThrow(new RuntimeException("Simulated Exception")).when(imageRepository).deleteById(image.getId());

		ConfirmationResponseDTO result = adminService.deleteQuestion(questionId);

		assertNotNull(result);
		assertEquals("Question has been locked, because it has been used in exam.", result.getConfirmationMessage());
		assertTrue(question1.getLocked());
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_deleteQuestion_questionDoesNotExist() {
		setUpQuestions();

		Optional<Question> questionOptional = Optional.empty();

		when(questionRepository.findById(questionId)).thenReturn(questionOptional);

		assertThrows(EntityNotFoundException.class, () -> adminService.deleteQuestion(questionId));
	}

	// ---------------------------------------------------------------------------------------------

//	@Test
//	public void test_findAllLockedQuestions() {
//		List<Question> lockedQuestions = new ArrayList<>();
//		Question question1 = new Question();
//		question1.setId(1);
//		question1.setLocked(true);
//		Question question2 = new Question();
//		question2.setId(2);
//		question2.setLocked(true);
//		lockedQuestions.add(question1);
//		lockedQuestions.add(question2);
//
//		when(questionRepository.findByLocked(true)).thenReturn(lockedQuestions);
//
//		List<Question> result = adminService.findAllLockedQuestions();
//
//		assertNotNull(result);
//		assertEquals(lockedQuestions.size(), result.size());
//		assertTrue(result.stream().allMatch(Question::getLocked));
//	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_findAllUsers() {
		List<User> users = new ArrayList<>();
		User user1 = new User();
		user1.setId(1);
		User user2 = new User();
		user2.setId(2);
		users.add(user1);
		users.add(user2);

		when(userRepository.findAll()).thenReturn(users);

		List<User> result = adminService.findAllUsers();

		assertNotNull(result);
		assertEquals(users.size(), result.size());
		assertTrue(result.containsAll(users));
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_searchQuestion() {
		setUpQuestions();

		request.setSearchFields(Arrays.asList("question_content", "answers", "feedback"));

		when(questionService.findByQuestionContentContainsIgnoreCaseAndLockedIsFalse(anyString()))
				.thenReturn(mockQuestions);
		when(questionService.findByAnswers(anyString())).thenReturn(mockQuestions);
		when(questionService.findByFeedback(anyString())).thenReturn(mockQuestions);
		when(searchingSortService.sortList(any())).thenReturn(listFull);
		when(searchingSortService.findSearchedItemsOnPage(any(), eq(1))).thenReturn(list);
		when(dTOService.findLevelByName(anyString())).thenReturn(mockLevel);

		SearchQuestionResponseDTO result = adminService.searchQuestion(request);

		assertEquals(2, result.getQuestions().size());

		QuestionFoundInSearchDTO response1 = result.getQuestions().get(0);
		assertEquals(11, response1.getId());
		assertEquals("Java", response1.getCategory());
		assertEquals("INTERMEDIATE", response1.getLevel());
		assertEquals("Sample question 1", response1.getQuestionContent());
		assertEquals(Arrays.asList("Sample answer 1", "Answer 2"), response1.getAnswers());

		QuestionFoundInSearchDTO response2 = result.getQuestions().get(1);
		assertEquals(12, response2.getId());
		assertEquals("Java", response2.getCategory());
		assertEquals("INTERMEDIATE", response2.getLevel());
		assertEquals("Sample question 2", response2.getQuestionContent());
		assertEquals(Arrays.asList("Sample answer 2", "Huston"), response2.getAnswers());
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_searchQuestion_emptySearchFields() {
		setUpQuestions();

		request.setSearchFields(new ArrayList<>());

		when(questionService.findByQuestionContentContainsIgnoreCaseAndLockedIsFalse(anyString()))
				.thenReturn(mockQuestions);
		when(questionService.findByAnswers(anyString())).thenReturn(mockQuestions);
		when(questionService.findByFeedback(anyString())).thenReturn(mockQuestions);
		when(searchingSortService.sortList(any())).thenReturn(listFull);
		when(searchingSortService.findSearchedItemsOnPage(any(), eq(1))).thenReturn(list);
		when(dTOService.findLevelByName(anyString())).thenReturn(mockLevel);

		SearchQuestionResponseDTO result = adminService.searchQuestion(request);

		assertEquals(2, result.getQuestions().size());

		QuestionFoundInSearchDTO response1 = result.getQuestions().get(0);
		assertEquals(11, response1.getId());
		assertEquals("Java", response1.getCategory());
		assertEquals("INTERMEDIATE", response1.getLevel());
		assertEquals("Sample question 1", response1.getQuestionContent());
		assertEquals(Arrays.asList("Sample answer 1", "Answer 2"), response1.getAnswers());

		QuestionFoundInSearchDTO response2 = result.getQuestions().get(1);
		assertEquals(12, response2.getId());
		assertEquals("Java", response2.getCategory());
		assertEquals("INTERMEDIATE", response2.getLevel());
		assertEquals("Sample question 2", response2.getQuestionContent());
		assertEquals(Arrays.asList("Sample answer 2", "Huston"), response2.getAnswers());
	}

	// ---------------------------------------------------------------------------------------------
	@Test
	public void test_searchQuestion_throwExceptionWithNoQuestions() {
		setUpQuestions();
		request.setSearchFields(Arrays.asList("answers"));

		Level mockLevel = Level.INTERMEDIATE;

		when(dTOService.findLevelByName(anyString())).thenReturn(mockLevel);
		assertThrows(AdminAccesException.class, () -> {
			adminService.searchQuestion(request);
		});
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_updateQuestion_Success() {
		setUpQuestions();
		Integer questionId = 1;
		Question existingQuestion = new Question();
		existingQuestion.setCategory(category);

		when(categoryService.findByName("Java")).thenReturn(category);
		when(questionRepository.findById(questionId)).thenReturn(Optional.of(existingQuestion));
		when(dTOService.questionEntityToResponse(any())).thenReturn(questionResponseDTO);

		QuestionResponseDTO responseDTO = adminService.updateQuestion(questionId, questionRequestDTO);

		verify(questionRepository, times(2)).save(any());
		assertNotNull(responseDTO);
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_updateQuestion_InvalidQuestionId() {
		Integer questionId = 10;
		when(questionRepository.findById(questionId)).thenReturn(Optional.empty());

		assertThrows(AdminAccesException.class, () -> {
			adminService.updateQuestion(questionId, questionRequestDTO);
		});
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_searchUser() {
		setUpUsers();

		requestUser.setSearchFields(Arrays.asList("first_name", "last_name", "email"));

		when(tagService.findByName(anyString())).thenReturn(optTag);
		when(userService.findByFirstNameContainsIgnoreCase(anyString())).thenReturn(mockUsers);
		when(userService.findByLastNameContainsIgnoreCase(anyString())).thenReturn(mockUsers);
		when(userService.findByEmailContainsIgnoreCase(anyString())).thenReturn(mockUsers);
		when(searchingSortService.sortList(any())).thenReturn(listFull);
		when(searchingSortService.findSearchedItemsOnPage(any(), eq(3))).thenReturn(list);

		SearchUserResponseDTO result = adminService.searchUser(requestUser);

		assertEquals(2, result.getUsers().size());
		UserFoundInSearchDTO response1 = result.getUsers().get(0);
		assertEquals(11, response1.getId());
		assertEquals("First name of: 11", response1.getFirstName());
		assertEquals("Last name of: 11", response1.getLastName());
		assertEquals("Email of: 11", response1.getEmail());

		UserFoundInSearchDTO response2 = result.getUsers().get(1);
		assertEquals(12, response2.getId());
		assertEquals("First name of: 12", response2.getFirstName());
		assertEquals("Last name of: 12", response2.getLastName());
		assertEquals("Email of: 12", response2.getEmail());
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_searchUser_emptySearchField() {
		setUpUsers();

		requestUser.setSearchFields(new ArrayList<>());

		when(tagService.findByName(anyString())).thenReturn(optTag);
		when(userService.findByFirstNameContainsIgnoreCase(anyString())).thenReturn(mockUsers);
		when(userService.findByLastNameContainsIgnoreCase(anyString())).thenReturn(mockUsers);
		when(userService.findByEmailContainsIgnoreCase(anyString())).thenReturn(mockUsers);
		when(searchingSortService.sortList(any())).thenReturn(listFull);
		when(searchingSortService.findSearchedItemsOnPage(any(), eq(3))).thenReturn(list);

		SearchUserResponseDTO result = adminService.searchUser(requestUser);

		assertEquals(2, result.getUsers().size());
		UserFoundInSearchDTO response1 = result.getUsers().get(0);
		assertEquals(11, response1.getId());
		assertEquals("First name of: 11", response1.getFirstName());
		assertEquals("Last name of: 11", response1.getLastName());
		assertEquals("Email of: 11", response1.getEmail());

		UserFoundInSearchDTO response2 = result.getUsers().get(1);
		assertEquals(12, response2.getId());
		assertEquals("First name of: 12", response2.getFirstName());
		assertEquals("Last name of: 12", response2.getLastName());
		assertEquals("Email of: 12", response2.getEmail());
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_searchUser_ThrowsExceptionWithNoUsers() {
		setUpUsers();
		requestUser.setSearchFields(new ArrayList<>());

		List<User> mockUsers = new ArrayList<>();

		when(userService.findByFirstNameContainsIgnoreCase(anyString())).thenReturn(mockUsers);
		when(userService.findByLastNameContainsIgnoreCase(anyString())).thenReturn(mockUsers);
		when(userService.findByEmailContainsIgnoreCase(anyString())).thenReturn(mockUsers);

		assertThrows(AdminAccesException.class, () -> {
			adminService.searchUser(requestUser);
		});
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_giveTagToUser_SuccessfullyGivesTagToUser() {

		User user = new User();
		String tagName = "TestTag";
		Tag tag = new Tag(tagName);

		when(dTOService.findOrCreateTag(tagName)).thenReturn(tag);

		ConfirmationResponseDTO response = adminService.giveTagToUser(user, tagName);

		assertEquals("Tag has been added to user.", response.getConfirmationMessage());
		verify(userRepository, times(1)).save(user);
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_changeFirstNameAccept_SuccessfullyChangesFirstName() {

		NotificationFirstName nameNote = new NotificationFirstName();
		User user = new User();
		nameNote.setUser(user);
		nameNote.setName("NewFirstName");

		when(notificationService.findById(anyInt())).thenReturn(nameNote);

		ChangeNameResponseDTO response = adminService.changeFirstNameAccept(1);

		// Assert
		assertEquals("First name has been changed.", response.getConfirmationMessage());
		verify(userRepository, times(1)).save(user);
		verify(notificationService, times(1)).delete(1);
		verify(userService, times(1)).buildClaim(user);
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_changLastNameAccept_SuccessfullyChangesLastName() {

		NotificationLastName nameNote = new NotificationLastName();
		User user = new User();
		nameNote.setUser(user);
		nameNote.setName("NewLastName");

		when(notificationService.findById(anyInt())).thenReturn(nameNote);

		ChangeNameResponseDTO response = adminService.changeLastNameAccept(2);

		assertEquals("Last name has been changed.", response.getConfirmationMessage());
		verify(userRepository, times(1)).save(user);
		verify(notificationService, times(1)).delete(2);
		verify(userService, times(1)).buildClaim(user);
	}
}