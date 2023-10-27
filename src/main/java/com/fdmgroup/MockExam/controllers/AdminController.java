package com.fdmgroup.MockExam.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fdmgroup.MockExam.dto.AccountIdRequestDTO;
import com.fdmgroup.MockExam.dto.AccountIdRoleRequestDTO;
import com.fdmgroup.MockExam.dto.ChangeNameResponseDTO;
import com.fdmgroup.MockExam.dto.ConfirmationResponseDTO;
import com.fdmgroup.MockExam.dto.ExamAccessRequestDTO;
import com.fdmgroup.MockExam.dto.ListOfUsersIdDTO;
import com.fdmgroup.MockExam.dto.NotificationDetailsResponseDTO;
import com.fdmgroup.MockExam.dto.NotificationListResponseDTO;
import com.fdmgroup.MockExam.dto.NotificationRequestDTO;
import com.fdmgroup.MockExam.dto.QuestionRequestDTO;
import com.fdmgroup.MockExam.dto.QuestionResponseDTO;
import com.fdmgroup.MockExam.dto.SearchQuestionRequestDTO;
import com.fdmgroup.MockExam.dto.SearchQuestionResponseDTO;
import com.fdmgroup.MockExam.dto.SearchUserRequestDTO;
import com.fdmgroup.MockExam.dto.SearchUserResponseDTO;
import com.fdmgroup.MockExam.dto.SuggestionsResponseDTO;
import com.fdmgroup.MockExam.model.Category;
import com.fdmgroup.MockExam.model.Question;
import com.fdmgroup.MockExam.model.Tag;
import com.fdmgroup.MockExam.model.User;
import com.fdmgroup.MockExam.services.AdminService;
import com.fdmgroup.MockExam.services.CategoryService;
import com.fdmgroup.MockExam.services.NotificationService;
import com.fdmgroup.MockExam.services.QuestionService;
import com.fdmgroup.MockExam.services.QuestionsFromFileService;
import com.fdmgroup.MockExam.services.TagService;
import com.fdmgroup.MockExam.services.UserService;
import com.fdmgroup.MockExam.utils.QuestionConverter;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/api/admin")
@RestController
public class AdminController {

	private final AdminService adminService;
	private final QuestionService questionService;
	private final CategoryService categoryService;
	private final NotificationService notificationService;
	private final QuestionsFromFileService questionsFromFile;
	private final UserService userService;
	private final TagService tagService;

	@PutMapping("/lockAccount")
	public ResponseEntity<ConfirmationResponseDTO> lockAccount(@RequestHeader("Authorization") String header,
			@RequestBody AccountIdRequestDTO request) {
		return ResponseEntity.ok(adminService.blockAccount(request, true));
	}

	// ---------------------------------------------------------------------------------------------

	@PutMapping("/unblockAccount")
	public ResponseEntity<ConfirmationResponseDTO> unlockAccount(@RequestHeader("Authorization") String header,
			@RequestBody AccountIdRequestDTO request) {
		return ResponseEntity.ok(adminService.blockAccount(request, false));
	}

	// ---------------------------------------------------------------------------------------------

	@DeleteMapping("/deleteAccount")
	public ResponseEntity<ConfirmationResponseDTO> deleteAccount(@RequestParam int userId, @RequestParam int noteId) {
		notificationService.deleteAccount_deleteNotification(noteId);
		return ResponseEntity.ok(adminService.deleteAccount(userId));

	}

	// ---------------------------------------------------------------------------------------------

	@PutMapping("/changeAccountRole")
	public ResponseEntity<ConfirmationResponseDTO> changeAccountRole(@RequestHeader("Authorization") String header,
			@RequestBody AccountIdRoleRequestDTO request) {
		return ResponseEntity.ok(adminService.changeAccountRole(request));

	}

	// ---------------------------------------------------------------------------------------------

	@PostMapping("/addQuestion")
	public QuestionResponseDTO addQuestion(@RequestParam(name = "category", required = false) String category,
			@RequestParam("level") String level, @RequestParam("questionContent") String questionContent,
			@RequestParam("answers") List<String> answers, @RequestParam("correctAnswers") List<String> correctAnswers,
			@RequestParam("feedback") List<String> feedback,
			@RequestParam(name = "file", required = false) MultipartFile file,
			@RequestParam(name = "tags", required = false) List<String> tags,
			@RequestParam("accessToAllUsers") boolean accessToAllUsers) {

		Category categoryEntity = categoryService.findOrCreateCategory(category);

		QuestionRequestDTO newQuestion = new QuestionRequestDTO(category, level, questionContent, answers, feedback,
				correctAnswers, file, tags);
		QuestionResponseDTO response = adminService.addQuestion(newQuestion);

		if (accessToAllUsers)
			userService.giveAccessToUsers(categoryEntity);

		return response;
	}

	// ---------------------------------------------------------------------------------------------

	@PostMapping("/questionsFromFile")
	public ResponseEntity<ConfirmationResponseDTO> addQuestionsFroMFile(@RequestParam("file") MultipartFile file,
			@RequestParam("answersFile") MultipartFile answersFile,
			@RequestParam("accessToAllUsers") boolean accessToAllUsers) {

		return ResponseEntity.ok(questionsFromFile.addQuestionsFroMFile(file, answersFile, accessToAllUsers));
	}

	// ---------------------------------------------------------------------------------------------

	@PutMapping("/updateQuestion")
	public QuestionResponseDTO updateQuestion(@RequestParam("questionId") Integer questionId,
			@RequestParam("category") String category, @RequestParam("level") String level,
			@RequestParam("questionContent") String questionContent, @RequestParam("answers") List<String> answers,
			@RequestParam("correctAnswers") List<String> correctAnswers,
			@RequestParam("feedback") List<String> feedback,
			@RequestParam(name = "file", required = false) MultipartFile file,
			@RequestParam(name = "tags", required = false) List<String> tags,
			@RequestParam(name = "noteId", required = false) Integer notificationId,
			@RequestParam("accessToAllUsers") boolean accessToAllUsers) {

		QuestionRequestDTO newQuestion = new QuestionRequestDTO(category, level, questionContent, answers, feedback,
				correctAnswers, file, tags);
		try {
			notificationService.updateQuestion_deleteNotification(notificationId);
		} catch (Exception e) {
		}
		Category categoryEntity = categoryService.findOrCreateCategory(category);
		if (accessToAllUsers)
			userService.giveAccessToUsers(categoryEntity);

		return adminService.updateQuestion(questionId, newQuestion);
	}

	// ---------------------------------------------------------------------------------------------

	@DeleteMapping("/deleteQuestion")
	public ResponseEntity<ConfirmationResponseDTO> deleteQuestion(@RequestParam Integer id) {
		return ResponseEntity.ok(adminService.deleteQuestion(id));
	}

	// ---------------------------------------------------------------------------------------------

	@GetMapping(path = "getQuestion")
	public QuestionResponseDTO getQuestionById(@RequestParam("id") Integer id) {
		Question question = questionService.findById(id);
		QuestionResponseDTO responseQuestion = QuestionConverter.questionEntityToResponse(question);
		return responseQuestion;
	}

	// ---------------------------------------------------------------------------------------------

	@PostMapping("/searchQuestion")
	public ResponseEntity<SearchQuestionResponseDTO> searchQuestion(@RequestBody SearchQuestionRequestDTO request) {
		return ResponseEntity.ok(adminService.searchQuestion(request));
	}

	// ---------------------------------------------------------------------------------------------

	@GetMapping("/searchUser")
	public ResponseEntity<SearchUserResponseDTO> searchUser(@RequestBody SearchUserRequestDTO request) {
		return ResponseEntity.ok(adminService.searchUser(request));
	}

	// ---------------------------------------------------------------------------------------------

	@GetMapping("/suggestions")
	public ResponseEntity<SuggestionsResponseDTO> getSuggestions(@RequestParam String sugsestion) {
		return null;
	}

	// ---------------------------------------------------------------------------------------------

	@PostMapping("/seeNotifications")
	public ResponseEntity<NotificationListResponseDTO> seeNotifications(@RequestHeader("Authorization") String header,
			@RequestBody NotificationRequestDTO request) {
		User admin = userService.getUserFromHeader(header);
		return ResponseEntity.ok(notificationService.seeNotification(request, admin));
	}

	// ---------------------------------------------------------------------------------------------

	@PostMapping("/notificationDetails")
	public ResponseEntity<NotificationDetailsResponseDTO> notificationDetails(
			@RequestHeader("Authorization") String header, @RequestBody NotificationRequestDTO request) {
		User admin = userService.getUserFromHeader(header);
		return ResponseEntity.ok(notificationService.notificationDetails(request, admin));
	}

	// ---------------------------------------------------------------------------------------------

	@GetMapping("/acceptQuestionReporting")
	public ResponseEntity<QuestionResponseDTO> acceptQuestionReporting(@RequestHeader("Authorization") String header,
			@RequestParam int questionId, @RequestParam int noteId) {
		User admin = userService.getUserFromHeader(header);

		notificationService.handle(noteId, admin);
		return ResponseEntity.ok(notificationService.acceptQuestionReporting(questionId, noteId));
	}

	// ---------------------------------------------------------------------------------------------

	@PutMapping("/notificationHandle")
	public ResponseEntity<ConfirmationResponseDTO> notificationHandle(@RequestHeader("Authorization") String header,
			@RequestBody AccountIdRequestDTO request) {
		User admin = userService.getUserFromHeader(header);
		return ResponseEntity.ok(notificationService.handle(request.getId(), admin));
	}

	// ---------------------------------------------------------------------------------------------

	@DeleteMapping("/notificationDelete")
	public ResponseEntity<ConfirmationResponseDTO> notificationDelete(@RequestParam int id) {
		return ResponseEntity.ok(notificationService.delete(id));
	}

	// ---------------------------------------------------------------------------------------------

	@PostMapping("/examAccessAccept")
	public ResponseEntity<ConfirmationResponseDTO> examAccessAccept(@RequestBody AccountIdRequestDTO request) {
		return ResponseEntity.ok(notificationService.examAccessAccept(request.getId()));
	}

	// ---------------------------------------------------------------------------------------------

	@PostMapping("/giveTagToUser")
	public ResponseEntity<ConfirmationResponseDTO> giveTagToUser(@RequestParam int id, @RequestParam String tagName) {
		return ResponseEntity.ok(adminService.giveTagToUser(userService.findById(id), tagName));
	}

	// ---------------------------------------------------------------------------------------------

	@PostMapping("/giveAccessToUsers")
	public ResponseEntity<ConfirmationResponseDTO> giveAccessToUsers(@RequestBody ListOfUsersIdDTO request) {
		Category category = categoryService.findByName(request.getCategoryName());

		return ResponseEntity.ok(userService.giveAccessToUsers(request.getList(), category));
	}

	// ---------------------------------------------------------------------------------------------

	@GetMapping("/getAllTags")
	public List<String> getAllTags() {
		List<Tag> tags = tagService.getAllTags();
		return tags.stream().map(tag -> tag.getName()).collect(Collectors.toList());

	}

	// ---------------------------------------------------------------------------------------------

	@PutMapping("/changeFirstNameAccept")
	public ResponseEntity<ChangeNameResponseDTO> changeFirstNameAccept(@RequestBody AccountIdRequestDTO request) {
		return ResponseEntity.ok(adminService.changeFirstNameAccept(request.getId()));
	}

	// ---------------------------------------------------------------------------------------------

	@PutMapping("/changeLastNameAccept")
	public ResponseEntity<ChangeNameResponseDTO> changeLastNameAccept(@RequestBody AccountIdRequestDTO request) {
		return ResponseEntity.ok(adminService.changeLastNameAccept(request.getId()));
	}
}
