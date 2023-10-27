package com.fdmgroup.MockExam.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fdmgroup.MockExam.dto.AccountIdRequestDTO;
import com.fdmgroup.MockExam.dto.AccountIdRoleRequestDTO;
import com.fdmgroup.MockExam.dto.ChangeNameResponseDTO;
import com.fdmgroup.MockExam.dto.ConfirmationResponseDTO;
import com.fdmgroup.MockExam.dto.ImageResponseDTO;
import com.fdmgroup.MockExam.dto.ListOfUsersIdDTO;
import com.fdmgroup.MockExam.dto.NotificationDetailsResponseDTO;
import com.fdmgroup.MockExam.dto.NotificationListResponseDTO;
import com.fdmgroup.MockExam.dto.NotificationRequestDTO;
import com.fdmgroup.MockExam.dto.QuestionRequestDTO;
import com.fdmgroup.MockExam.dto.QuestionResponseDTO;
import com.fdmgroup.MockExam.dto.UserFoundInSearchDTO;
import com.fdmgroup.MockExam.model.Category;
import com.fdmgroup.MockExam.model.Image;
import com.fdmgroup.MockExam.model.Question;
import com.fdmgroup.MockExam.model.Role;
import com.fdmgroup.MockExam.model.Tag;
import com.fdmgroup.MockExam.model.User;
import com.fdmgroup.MockExam.services.AdminService;
import com.fdmgroup.MockExam.services.CategoryService;
import com.fdmgroup.MockExam.services.JwtServiceImpl;
import com.fdmgroup.MockExam.services.NotificationService;
import com.fdmgroup.MockExam.services.QuestionService;
import com.fdmgroup.MockExam.services.TagService;
import com.fdmgroup.MockExam.services.UserService;
import com.fdmgroup.MockExam.utils.ImageConverter;

import com.fasterxml.jackson.core.type.TypeReference;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
public class test_AdminController {

	@MockBean(name = "categoryService")
	CategoryService categoryService;

	@MockBean
	AdminService adminService;
	@MockBean
	QuestionService questionService;
	@MockBean
	TagService tagService;
	@MockBean
	UserService userService;
	@MockBean
NotificationService notificationService;
	@Autowired
    private ObjectMapper objectMapper;

	
	@Autowired
	MockMvc mvc;
	
	JwtServiceImpl jwtService;

	
	Category category;
	Image image;
	Question question;
	ImageResponseDTO imageRes;
	QuestionResponseDTO questionResponseDTO;
	QuestionRequestDTO questionRequestDTO;
	QuestionRequestDTO questionRequestDTO2;
	QuestionRequestDTO imageQuestionRequestDTO;
	static String validJwtToken = "It will be generated during @BeforeAll";
	String email = "admin@adminmail.admin";
	Image image2;
	MockMultipartFile imageFile;
	
	List<Question> questions;
	
	
	
	@BeforeAll
	public void setUp() throws IOException {
		jwtService = new JwtServiceImpl();
		User user = new User();
		user.setEmail("test@test.com");
		user.setPassword("testPassword");
		user.setRole(Role.ADMIN);
		validJwtToken = jwtService.generateToken(user);
		
		category = new Category("Java");
		image = new Image("Empty Image", 0, new byte[0]);
		image.setId(1);
		imageRes = new ImageResponseDTO();
		imageRes.setId(1);
		imageRes.setName("Empty Image");
		imageRes.setSize(0);
		
		objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		List<Tag> tags = new ArrayList<>();
		tags.add(new Tag("Java"));
		tags.add(new Tag("Testing"));
		question = new Question(category, Question.Level.BEGINNER, "What is your favorite dependency?",
				Arrays.asList("JPA", "JUnit", "Spring", "Hibernate"),
				Arrays.asList("Incorrect", "Correct", "Correct", "Incorrect"), Arrays.asList("JUnit", "Spring"), image, tags);

		questions = Arrays.asList(question);
		questionResponseDTO = new QuestionResponseDTO("Java", "beginner", "What is your favorite dependency?",
				Arrays.asList("JPA", "JUnit", "Spring", "Hibernate"),
				Arrays.asList("Incorrect", "Correct", "Correct", "Incorrect"), Arrays.asList("JUnit", "Spring"),
				imageRes, Arrays.asList("Java", "Testing"));
		imageFile = new MockMultipartFile("file", "image.jpg", "image/jpeg", "images data".getBytes()) ;
		image2 = ImageConverter.convertMultiPartFileToImage(imageFile);
		
		questionRequestDTO = new QuestionRequestDTO("Java", "beginner", "What is your favorite dependency?",
				Arrays.asList("JPA", "JUnit", "Spring", "Hibernate"),
				Arrays.asList("Incorrect", "Correct", "Correct", "Incorrect"), Arrays.asList("JUnit", "Spring"), 
				Arrays.asList("JUnit", "Spring"));		
		questionRequestDTO2 = new QuestionRequestDTO("Java", "beginner", "What is your favorite dependency?",
						Arrays.asList("JPA", "JUnit", "Spring", "Hibernate"),
						Arrays.asList("Incorrect", "Correct", "Correct", "Incorrect"), Arrays.asList("JUnit", "Spring"));
		
	}

	// ---------------------------------------------------------------------------------------------
	
	@Test
	@WithMockUser(username= "test@test.com" , password = "testPassword", authorities = {"ROLE_ADMIN"})
	public void test_addQuestion_TriesToSaveQuestionAndCategoryWithProperValues() throws Exception {
		
		
		when(adminService.addQuestion(Mockito.any())).thenReturn(questionResponseDTO);
		when(categoryService.findByName(category.getCategoryName())).thenReturn(category);
		
		mvc.perform(
				MockMvcRequestBuilders.multipart("/api/admin/addQuestion")
				.file(imageFile)
				.param("category", "Java")
				.param("level","beginner")
				.param("questionContent", "What is your favorite dependency?")
				.param("answers","[JPA, JUnit, Spring, Hibernate]")
				.param("correctAnswers", "[JUnit, Spring]")
				.param("feedback", "[Incorrect, Correct, Correct, Incorrect]")
				.param("tags", "[Java, Testing]")
.param("accessToAllUsers", "true")
				.header("Authorization", "Bearer " + validJwtToken)
				)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.category").value("Java"))
				.andExpect(jsonPath("$.answers[0]").value("JPA"))
				.andExpect(jsonPath("$.answers[1]").value("JUnit")).andExpect(jsonPath("$.answers[2]").value("Spring"))
				.andExpect(jsonPath("$.answers[3]").value("Hibernate"))
				.andExpect(jsonPath("$.correctAnswers[0]").value("JUnit"))
				.andExpect(jsonPath("$.correctAnswers[1]").value("Spring")).andExpect(jsonPath("$.image").exists())
				.andExpect(jsonPath("$.feedback[0]").value("Incorrect"))
				.andExpect(jsonPath("$.feedback[1]").value("Correct"))
				.andExpect(jsonPath("$.feedback[2]").value("Correct"))
				.andExpect(jsonPath("$.feedback[3]").value("Incorrect"))
				.andExpect(jsonPath("$.tags[0]").value("Java"))
				.andExpect(jsonPath("$.tags[1]").value("Testing"))
				.andExpect(jsonPath("$.level").value("beginner"))
				.andExpect(jsonPath("$.questionContent").value("What is your favorite dependency?"));	
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	@WithMockUser(username= "test@test.com" , password = "testPassword", roles = {"ADMIN"})
	public void test_addQuestion_TriesToSaveQuestionAndCategoryWithProperValuesAndNewCategory() throws Exception {
			
		when(adminService.addQuestion(Mockito.any())).thenReturn(questionResponseDTO);
		when(categoryService.findByName(category.getCategoryName())).thenReturn(null);
		when(categoryService.saveCategory(category)).thenReturn(category);
		mvc.perform(
				MockMvcRequestBuilders.multipart("/api/admin/addQuestion")
				.file(imageFile)
				.param("category", "Java")
				.param("level","beginner")
				.param("questionContent", "What is your favorite dependency?")
				.param("answers","[JPA, JUnit, Spring, Hibernate]")
				.param("correctAnswers", "[JUnit, Spring]")
				.param("feedback", "[Incorrect, Correct, Correct, Incorrect]")
				.param("tags", "[Java, Testing]")
				.param("accessToAllUsers", "true")

				.header("Authorization", "Bearer " + validJwtToken)
				)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.category").value("Java"))
				.andExpect(jsonPath("$.answers[0]").value("JPA"))
				.andExpect(jsonPath("$.answers[1]").value("JUnit")).andExpect(jsonPath("$.answers[2]").value("Spring"))
				.andExpect(jsonPath("$.answers[3]").value("Hibernate"))
				.andExpect(jsonPath("$.correctAnswers[0]").value("JUnit"))
				.andExpect(jsonPath("$.correctAnswers[1]").value("Spring")).andExpect(jsonPath("$.image").exists())
				.andExpect(jsonPath("$.feedback[0]").value("Incorrect"))
				.andExpect(jsonPath("$.feedback[1]").value("Correct"))
				.andExpect(jsonPath("$.feedback[2]").value("Correct"))
				.andExpect(jsonPath("$.feedback[3]").value("Incorrect"))
				.andExpect(jsonPath("$.tags[0]").value("Java"))
				.andExpect(jsonPath("$.tags[1]").value("Testing"))
				.andExpect(jsonPath("$.level").value("beginner"))
				.andExpect(jsonPath("$.questionContent").value("What is your favorite dependency?"));	
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	@WithMockUser(username= "test@test.com" , password = "testPassword", authorities = {"ROLE_ADMIN"})
	public void test_deleteQuestion_questionHasBeenDeleted() throws Exception {
		
		when(adminService.deleteQuestion(1)).thenReturn(ConfirmationResponseDTO.builder().confirmationMessage("Question has been deleted.").build());
		mvc.perform(delete("/api/admin/deleteQuestion").param("id", "1")
				.header("Authorization", "Bearer " + validJwtToken))
		.andExpect(status().isOk()).andExpect(jsonPath("confirmationMessage").value("Question has been deleted."));
		verify(adminService, times(1)).deleteQuestion(1);
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	@WithMockUser(username= "test@test.com" , password = "testPassword", authorities = {"ROLE_ADMIN"})
	public void test_deleteQuestion_questionHasBeenLocked() throws Exception {
		
		when(adminService.deleteQuestion(1)).thenReturn(ConfirmationResponseDTO.builder().confirmationMessage("Question has been locked, because it has been use in exam.").build());
		mvc.perform(delete("/api/admin/deleteQuestion").param("id", "1")
				.header("Authorization", "Bearer " + validJwtToken))
		.andExpect(status().isOk()).andExpect(jsonPath("confirmationMessage").value("Question has been locked, because it has been use in exam."));
		verify(adminService, times(1)).deleteQuestion(1);
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	@WithMockUser(username = "test@test.com", password = "testPassword", authorities = {"ROLE_ADMIN"})
	public void test_lockAccount() throws Exception {
	    AccountIdRequestDTO requestDTO = new AccountIdRequestDTO();
	    requestDTO.setId(123); 

	    when(adminService.blockAccount(requestDTO, true))
	            .thenReturn(ConfirmationResponseDTO.builder().confirmationMessage("Account has been blocked.").build());

	    mvc.perform(put("/api/admin/lockAccount")
	            .header("Authorization", "Bearer " + validJwtToken)
	            .contentType(MediaType.APPLICATION_JSON)
	            .content("{\"id\": 123}")
	    )
	            .andExpect(status().isOk())
	            .andExpect(jsonPath("confirmationMessage").value("Account has been blocked."));

	    verify(adminService, times(1)).blockAccount(requestDTO, true);
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	@WithMockUser(username = "test@test.com", password = "testPassword", authorities = {"ROLE_ADMIN"})
	public void test_unlockAccount() throws Exception {
		  AccountIdRequestDTO requestDTO = new AccountIdRequestDTO();
		    requestDTO.setId(123); 
		    when(adminService.blockAccount(any(AccountIdRequestDTO.class), eq(false)))
	            .thenReturn(ConfirmationResponseDTO.builder().confirmationMessage("Account has been unblocked.").build());

	    mvc.perform(put("/api/admin/unblockAccount")
	            .header("Authorization", "Bearer " + validJwtToken)
	            .contentType(MediaType.APPLICATION_JSON)
	            .content("{\"id\": 123}")
	    )
	            .andExpect(status().isOk())
	            .andExpect(jsonPath("confirmationMessage").value("Account has been unblocked."));

	    verify(adminService, times(1)).blockAccount(requestDTO, false);
	}

	// ---------------------------------------------------------------------------------------------

//	@Test
//	@WithMockUser(username = "test@test.com", password = "testPassword", authorities = {"ROLE_ADMIN"})
//	public void test_deleteAccount() throws Exception {
//		AccountIdRequestDTO requestDTO = new AccountIdRequestDTO();
//	    requestDTO.setId(123); 
//	    Mockito.when(adminService.deleteAccount(any(AccountIdRequestDTO.class)))
//	            .thenReturn(ConfirmationResponseDTO.builder().confirmationMessage("Account has been deleted.").build());
//
//	    mvc.perform(delete("/api/admin/deleteAccount")
//	            .header("Authorization", "Bearer " + validJwtToken)
//	            .contentType(MediaType.APPLICATION_JSON)
//	            .content("{\"id\": 123}")
//	    )
//	            .andExpect(status().isOk())
//	            .andExpect(jsonPath("confirmationMessage").value("Account has been deleted."));
//
//	    verify(adminService, times(1)).deleteAccount(requestDTO);
//	}	
	@Test
	@WithMockUser(username = "test@test.com", password = "testPassword", authorities = {"ROLE_ADMIN"})
	public void test_changeAccountRole() throws Exception {
		AccountIdRoleRequestDTO requestDTO = new AccountIdRoleRequestDTO();
	    requestDTO.setId(123); 
	    requestDTO.setRoleName("ADMIN");
	    when(adminService.changeAccountRole(any(AccountIdRoleRequestDTO.class)))
	            .thenReturn(ConfirmationResponseDTO.builder().confirmationMessage("Role has been changed.").build());

	    mvc.perform(put("/api/admin/changeAccountRole")
	            .header("Authorization", "Bearer " + validJwtToken)
	            .contentType(MediaType.APPLICATION_JSON)
	            .content("{\"id\": 123, \"roleName\": \"ADMIN\"}")
	    )
	            .andExpect(status().isOk())
	            .andExpect(jsonPath("confirmationMessage").value("Role has been changed."));

	    verify(adminService, times(1)).changeAccountRole(requestDTO);
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	@WithMockUser(username = "test@test.com", password = "testPassword", authorities = {"ROLE_ADMIN"})
	public void test_deleteQuestion() throws Exception {
	
	    when(adminService.deleteQuestion(any()))
	            .thenReturn(ConfirmationResponseDTO.builder().confirmationMessage("Question has been deleted.").build());

	    mvc.perform(delete("/api/admin/deleteQuestion")
				.param("id", "123")

	            .header("Authorization", "Bearer " + validJwtToken)

	    )
	            .andExpect(status().isOk())
	            .andExpect(jsonPath("confirmationMessage").value("Question has been deleted."));

	    verify(adminService, times(1)).deleteQuestion(123);
	}	

	// ---------------------------------------------------------------------------------------------

	@Test
	@WithMockUser(username = "test@test.com", password = "testPassword", authorities = {"ROLE_ADMIN"})
	public void test_getQuestionById() throws Exception {
	    when(questionService.findById(123)).thenReturn(question); 

	    mvc.perform(get("/api/admin/getQuestion")
	            .param("id", "123")
	            .header("Authorization", "Bearer " + validJwtToken)
	    )
	            .andExpect(status().isOk())
	        	.andExpect(jsonPath("$.category").value("Java"))
				.andExpect(jsonPath("$.answers[0]").value("JPA"))
				.andExpect(jsonPath("$.answers[1]").value("JUnit")).andExpect(jsonPath("$.answers[2]").value("Spring"))
				.andExpect(jsonPath("$.answers[3]").value("Hibernate"))
				.andExpect(jsonPath("$.correctAnswers[0]").value("JUnit"))
				.andExpect(jsonPath("$.correctAnswers[1]").value("Spring")).andExpect(jsonPath("$.image").exists())
				.andExpect(jsonPath("$.feedback[0]").value("Incorrect"))
				.andExpect(jsonPath("$.feedback[1]").value("Correct"))
				.andExpect(jsonPath("$.feedback[2]").value("Correct"))
				.andExpect(jsonPath("$.feedback[3]").value("Incorrect"))
				.andExpect(jsonPath("$.level").value("beginner"))
				.andExpect(jsonPath("$.questionContent").value("What is your favorite dependency?")); 

	    verify(questionService, times(1)).findById(123);
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	@WithMockUser(username = "test@test.com", password = "testPassword", authorities = {"ROLE_ADMIN"})
	public void test_changeFirstNameAccept() throws Exception {
	    int accountId = 123;

	    when(adminService.changeFirstNameAccept(accountId))
	            .thenReturn(ChangeNameResponseDTO.builder().confirmationMessage("First name has been changed.").build());

	    mvc.perform(put("/api/admin/changeFirstNameAccept")
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(objectMapper.writeValueAsString(AccountIdRequestDTO.builder().id(accountId).build()))
	            .header("Authorization", "Bearer " + validJwtToken)
	    )
	            .andExpect(status().isOk())
	            .andExpect(jsonPath("confirmationMessage").value("First name has been changed."));

	    verify(adminService, times(1)).changeFirstNameAccept(accountId);
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	@WithMockUser(username = "test@test.com", password = "testPassword", authorities = {"ROLE_ADMIN"})
	public void test_changeLastNameAccept() throws Exception {
	    int accountId = 123;

	    when(adminService.changeLastNameAccept(accountId))
	            .thenReturn(ChangeNameResponseDTO.builder().confirmationMessage("Last name has been changed.").build());

	    mvc.perform(put("/api/admin/changeLastNameAccept")
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(objectMapper.writeValueAsString(AccountIdRequestDTO.builder().id(accountId).build()))
	            .header("Authorization", "Bearer " + validJwtToken)
	    )
	            .andExpect(status().isOk())
	            .andExpect(jsonPath("confirmationMessage").value("Last name has been changed."));

	    verify(adminService, times(1)).changeLastNameAccept(accountId);
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	@WithMockUser(username = "test@test.com", password = "testPassword", authorities = {"ROLE_ADMIN"})
	public void test_getAllTags() throws Exception {
	    List<Tag> mockTags = Arrays.asList(new Tag("Tag1"), new Tag("Tag2")); 

	    when(tagService.getAllTags()).thenReturn(mockTags);

	    MvcResult result = mvc.perform(get("/api/admin/getAllTags")
	            .header("Authorization", "Bearer " + validJwtToken)
	    )
	            .andExpect(status().isOk())
	            .andReturn();

	    String responseContent = result.getResponse().getContentAsString();
	    List<String> responseTags = objectMapper.readValue(responseContent, new TypeReference<List<String>>() {});

	    assertTrue(responseTags.contains("Tag1"));
	    assertTrue(responseTags.contains("Tag2"));

	    verify(tagService, times(1)).getAllTags();
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	@WithMockUser(username = "test@test.com", password = "testPassword", authorities = {"ROLE_ADMIN"})
	public void test_giveAccessToUsers() throws Exception {
	    Category category = new Category(); 
	    when(categoryService.findByName("Java")).thenReturn(category);

	    List<Integer> userIdList = Arrays.asList(1, 2, 3);
	    ListOfUsersIdDTO request = new ListOfUsersIdDTO();
	    request.setCategoryName("Java");
	    request.setList(userIdList);

	    ConfirmationResponseDTO confirmationResponse = ConfirmationResponseDTO.builder()
	            .confirmationMessage("Access to exam has been granted.")
	            .build();
	    when(userService.giveAccessToUsers(userIdList, category)).thenReturn(confirmationResponse);

	    MvcResult result = mvc.perform(post("/api/admin/giveAccessToUsers")
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(objectMapper.writeValueAsString(request))
	            .header("Authorization", "Bearer " + validJwtToken)
	    )
	            .andExpect(status().isOk())
	            .andReturn();

	    String responseContent = result.getResponse().getContentAsString();
	    ConfirmationResponseDTO responseDTO = objectMapper.readValue(responseContent, ConfirmationResponseDTO.class);

	    assertEquals("Access to exam has been granted.", responseDTO.getConfirmationMessage());

	    verify(categoryService, times(1)).findByName("Java");
	    verify(userService, times(1)).giveAccessToUsers(userIdList, category);
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	@WithMockUser(username = "test@test.com", password = "testPassword", authorities = {"ROLE_ADMIN"})
	public void test_notificationDelete() throws Exception {
	    int notificationId = 123;

	    when(notificationService.delete(notificationId))
	            .thenReturn(ConfirmationResponseDTO.builder().confirmationMessage("Notification has been rejected.").build());

	    mvc.perform(delete("/api/admin/notificationDelete")
	            .param("id", String.valueOf(notificationId))
	            .header("Authorization", "Bearer " + validJwtToken)
	    )
	            .andExpect(status().isOk())
	            .andExpect(jsonPath("confirmationMessage").value("Notification has been rejected."));

	    verify(notificationService, times(1)).delete(notificationId);
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	@WithMockUser(username = "test@test.com", password = "testPassword", authorities = {"ROLE_ADMIN"})
	public void test_examAccessAccept() throws Exception {
		 int accountId = 123; 

		    when(notificationService.examAccessAccept(accountId))
		            .thenReturn(ConfirmationResponseDTO.builder().confirmationMessage("Access to exams has been granted.").build());

		    mvc.perform(post("/api/admin/examAccessAccept")
		            .contentType(MediaType.APPLICATION_JSON)
		            .content(objectMapper.writeValueAsString(AccountIdRequestDTO.builder().id(accountId).build()))
		            .header("Authorization", "Bearer " + validJwtToken)
		    )
		            .andExpect(status().isOk())
		            .andExpect(jsonPath("confirmationMessage").value("Access to exams has been granted."));

		    verify(notificationService, times(1)).examAccessAccept(accountId);
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	@WithMockUser(username = "test@test.com", password = "testPassword", authorities = {"ROLE_ADMIN"})
	public void test_giveTagToUser() throws Exception {
	    int userId = 123;
	    String tagName = "Java";

	    when(adminService.giveTagToUser(userService.findById(userId), tagName))
	            .thenReturn(ConfirmationResponseDTO.builder().confirmationMessage("Tag has been added to user.").build());

	    mvc.perform(post("/api/admin/giveTagToUser")
	            .param("id", String.valueOf(userId))
	            .param("tagName", tagName)
	            .header("Authorization", "Bearer " + validJwtToken)
	    )
	            .andExpect(status().isOk())
	            .andExpect(jsonPath("confirmationMessage").value("Tag has been added to user."));

	    verify(adminService, times(1)).giveTagToUser(userService.findById(userId), tagName);
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	@WithMockUser(username = "test@test.com", password = "testPassword", authorities = {"ROLE_ADMIN"})
	public void test_notificationHandle() throws Exception {
	    int notificationId = 123;

	    User admin = new User();
	    when(userService.getUserFromHeader(Mockito.anyString())).thenReturn(admin);

	    when(notificationService.handle(notificationId, admin))
	            .thenReturn(ConfirmationResponseDTO.builder().confirmationMessage("Notification handling is in process now.").build());

	    mvc.perform(put("/api/admin/notificationHandle")
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(objectMapper.writeValueAsString(AccountIdRequestDTO.builder().id(notificationId).build()))
	            .header("Authorization", "Bearer " + validJwtToken)
	    )
	            .andExpect(status().isOk())
	            .andExpect(jsonPath("confirmationMessage").value("Notification handling is in process now."));

	    verify(userService, times(1)).getUserFromHeader(Mockito.anyString());
	    verify(notificationService, times(1)).handle(notificationId, admin);
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	@WithMockUser(username = "test@test.com", password = "testPassword", authorities = {"ROLE_ADMIN"})
	public void test_acceptQuestionReporting() throws Exception {
	    int questionId = 123;
	    int noteId = 456;

	    User admin = new User();
	    when(userService.getUserFromHeader(Mockito.anyString())).thenReturn(admin);

	    when(notificationService.handle(noteId, admin))
	            .thenReturn(ConfirmationResponseDTO.builder().confirmationMessage("Notification handling is in process now.").build());

	    when(notificationService.acceptQuestionReporting(questionId, noteId))
	            .thenReturn(questionResponseDTO);

	    mvc.perform(get("/api/admin/acceptQuestionReporting")
	            .param("questionId", String.valueOf(questionId))
	            .param("noteId", String.valueOf(noteId))
	            .header("Authorization", "Bearer " + validJwtToken)
	    )
	            .andExpect(status().isOk())
	        	.andExpect(jsonPath("$.category").value("Java"))
				.andExpect(jsonPath("$.answers[0]").value("JPA"))
				.andExpect(jsonPath("$.answers[1]").value("JUnit")).andExpect(jsonPath("$.answers[2]").value("Spring"))
				.andExpect(jsonPath("$.answers[3]").value("Hibernate"))
				.andExpect(jsonPath("$.correctAnswers[0]").value("JUnit"))
				.andExpect(jsonPath("$.correctAnswers[1]").value("Spring")).andExpect(jsonPath("$.image").exists())
				.andExpect(jsonPath("$.feedback[0]").value("Incorrect"))
				.andExpect(jsonPath("$.feedback[1]").value("Correct"))
				.andExpect(jsonPath("$.feedback[2]").value("Correct"))
				.andExpect(jsonPath("$.feedback[3]").value("Incorrect"))
				.andExpect(jsonPath("$.tags[0]").value("Java"))
				.andExpect(jsonPath("$.tags[1]").value("Testing"))
				.andExpect(jsonPath("$.level").value("beginner"))
				.andExpect(jsonPath("$.questionContent").value("What is your favorite dependency?"));

	    verify(userService, times(1)).getUserFromHeader(Mockito.anyString());
	    verify(notificationService, times(1)).handle(noteId, admin);
	    verify(notificationService, times(1)).acceptQuestionReporting(questionId, noteId);
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	@WithMockUser(username = "test@test.com", password = "testPassword", authorities = {"ROLE_ADMIN"})
	public void test_notificationDetails() throws Exception {
	    User admin = new User(); 
	    when(userService.getUserFromHeader(Mockito.anyString())).thenReturn(admin);
	
	    UserFoundInSearchDTO userDTO = new UserFoundInSearchDTO(11,"firstName","lastName","email@email.email",
	    		false,true, Arrays.asList("tag1","tag2"));
	    
	    NotificationRequestDTO request = new NotificationRequestDTO("Delete_account",1); 

	    NotificationDetailsResponseDTO responseDTO = new NotificationDetailsResponseDTO("Delete_account",10,userDTO);
	    when(notificationService.notificationDetails(request, admin)).thenReturn(responseDTO);

	    mvc.perform(post("/api/admin/notificationDetails")
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(objectMapper.writeValueAsString(request))
	            .header("Authorization", "Bearer " + validJwtToken)
	    )
	            .andExpect(status().isOk())
	        	.andExpect(jsonPath("$.action").value("Delete_account"))
	        	.andExpect(jsonPath("$.id").value("10"))
	        	.andExpect(jsonPath("$.user.id").value("11"))
	        	.andExpect(jsonPath("$.user.firstName").value("firstName"))
	        	.andExpect(jsonPath("$.user.lastName").value("lastName"))
	        	.andExpect(jsonPath("$.user.email").value("email@email.email"))
	        	.andExpect(jsonPath("$.user.locked").value("false"))
	        	.andExpect(jsonPath("$.user.verified").value("true"))
	        	.andExpect(jsonPath("$.user.tags[0]").value("tag1"))
	        	.andExpect(jsonPath("$.user.tags[1]").value("tag2"));

	    verify(userService, times(1)).getUserFromHeader(Mockito.anyString());
	    verify(notificationService, times(1)).notificationDetails(request, admin);
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	@WithMockUser(username = "test@test.com", password = "testPassword", authorities = {"ROLE_ADMIN"})
	public void test_seeNotifications() throws Exception {
	    User admin = new User(); 
	    Mockito.when(userService.getUserFromHeader(Mockito.anyString())).thenReturn(admin);

	    NotificationRequestDTO request = new NotificationRequestDTO();

	    NotificationListResponseDTO responseDTO = new NotificationListResponseDTO(); // Replace with a valid response object.
	    Mockito.when(notificationService.seeNotification(request, admin)).thenReturn(responseDTO);

	    mvc.perform(post("/api/admin/seeNotifications")
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(objectMapper.writeValueAsString(request))
	            .header("Authorization", "Bearer " + validJwtToken)
	    )
	            .andExpect(status().isOk());

	    verify(userService, times(1)).getUserFromHeader(Mockito.anyString());
	    verify(notificationService, times(1)).seeNotification(request, admin);
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	@WithMockUser(username = "test@test.com", password = "testPassword", authorities = {"ROLE_ADMIN"})
	public void test_deleteAccount() throws Exception {
	    int userId = 123; 
	    int noteId = 456; 

	    when(notificationService.deleteAccount_deleteNotification(noteId)).thenReturn(true);

	    when(adminService.deleteAccount(userId))
	            .thenReturn(ConfirmationResponseDTO.builder().confirmationMessage("Account has been deleted.").build());

	    mvc.perform(delete("/api/admin/deleteAccount")
	            .param("userId", String.valueOf(userId))
	            .param("noteId", String.valueOf(noteId))
	            .header("Authorization", "Bearer " + validJwtToken)
	    )
	            .andExpect(status().isOk())
	            .andExpect(jsonPath("confirmationMessage").value("Account has been deleted."));

	    verify(notificationService, times(1)).deleteAccount_deleteNotification(noteId);
	    verify(adminService, times(1)).deleteAccount(userId);
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	@WithMockUser(username = "test@test.com", password = "testPassword", authorities = {"ROLE_ADMIN"})
	public void test_updateQuestion() throws Exception {
		Integer notificationId = 456;
		int questionId = 123;

		when(adminService.updateQuestion(any(), any())).thenReturn(questionResponseDTO);
		when(categoryService.findByName(category.getCategoryName())).thenReturn(category);


	    mvc.perform(put("/api/admin/updateQuestion")
				.param("category", "Java")
				.param("level","beginner")
				.param("questionContent", "What is your favorite dependency?")
				.param("answers","[JPA, JUnit, Spring, Hibernate]")
				.param("correctAnswers", "[JUnit, Spring]")
				.param("feedback", "[Incorrect, Correct, Correct, Incorrect]")
				.param("tags", "[Java, Testing]")
				.param("accessToAllUsers", "true")
	            .header("Authorization", "Bearer " + validJwtToken)
	    )
	            .andExpect(status().isOk())
	            .andExpect(jsonPath("$.category").value("Java"))
				.andExpect(jsonPath("$.answers[0]").value("JPA"))
				.andExpect(jsonPath("$.answers[1]").value("JUnit")).andExpect(jsonPath("$.answers[2]").value("Spring"))
				.andExpect(jsonPath("$.answers[3]").value("Hibernate"))
				.andExpect(jsonPath("$.correctAnswers[0]").value("JUnit"))
				.andExpect(jsonPath("$.correctAnswers[1]").value("Spring")).andExpect(jsonPath("$.image").exists())
				.andExpect(jsonPath("$.feedback[0]").value("Incorrect"))
				.andExpect(jsonPath("$.feedback[1]").value("Correct"))
				.andExpect(jsonPath("$.feedback[2]").value("Correct"))
				.andExpect(jsonPath("$.feedback[3]").value("Incorrect"))
				.andExpect(jsonPath("$.tags[0]").value("Java"))
				.andExpect(jsonPath("$.tags[1]").value("Testing"))
				.andExpect(jsonPath("$.level").value("beginner"))
				.andExpect(jsonPath("$.questionContent").value("What is your favorite dependency?"));	

	    verify(notificationService, times(1)).updateQuestion_deleteNotification(notificationId);
	    verify(categoryService, times(1)).findOrCreateCategory(category.getCategoryName());
	    
	        verify(userService, times(1)).giveAccessToUsers(any());
	    
	    verify(adminService, times(1)).updateQuestion(questionId, any(QuestionRequestDTO.class));
	}
	
	
}
