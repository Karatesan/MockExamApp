package com.fdmgroup.MockExam.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fdmgroup.MockExam.dto.ChangeFirstNameRequestDTO;
import com.fdmgroup.MockExam.dto.ChangeLastNameRequestDTO;
import com.fdmgroup.MockExam.dto.ChangeNameResponseDTO;
import com.fdmgroup.MockExam.dto.ChangePasswordRequestDTO;
import com.fdmgroup.MockExam.dto.ChangePasswordResponseDTO;
import com.fdmgroup.MockExam.dto.ConfirmationResponseDTO;
import com.fdmgroup.MockExam.dto.DeleteMyAccountRequestDTO;
import com.fdmgroup.MockExam.dto.ImageResponseDTO;
import com.fdmgroup.MockExam.dto.ReportQuestionRequestDTO;
import com.fdmgroup.MockExam.exceptions.InvalidPasswordOrEmailException;
import com.fdmgroup.MockExam.exceptions.UserAccountLockedException;
import com.fdmgroup.MockExam.exceptions.UserDisabledException;
import com.fdmgroup.MockExam.model.Category;
import com.fdmgroup.MockExam.model.NotificationExam;
import com.fdmgroup.MockExam.model.User;
import com.fdmgroup.MockExam.services.AuthenticationService;
import com.fdmgroup.MockExam.services.CategoryService;
import com.fdmgroup.MockExam.services.NotificationService;
import com.fdmgroup.MockExam.services.UserService;
import com.fdmgroup.MockExam.services.QuestionService;
import com.fdmgroup.MockExam.utils.ImageConverter;

import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/users")
@RestController
public class UserController {

	private final AuthenticationManager authManager;
	private final UserService userService;
	private final NotificationService notificationService;
	private final AuthenticationService authenticationService;
	private final QuestionService questionService;
	private final CategoryService categoryService;
	private User user = new User();

	@PutMapping("/changePassword")
	public ResponseEntity<ChangePasswordResponseDTO> changePassword(@RequestBody ChangePasswordRequestDTO request,
			@RequestHeader("Authorization") String header) {
		return ResponseEntity.ok(authenticationService.changePassword(request, header));
	}

	// ---------------------------------------------------------------------------------------------

	@PutMapping("/changeFirstname")
	public ResponseEntity<ChangeNameResponseDTO> changeFirstname(@RequestBody ChangeFirstNameRequestDTO request,
			@RequestHeader("Authorization") String header) {
//		userService.changeFirstName(request, header);

		return ResponseEntity.ok(userService.changeFirstName(request, header));
	}

	// ---------------------------------------------------------------------------------------------

	@PutMapping("/changeLastname")
	public ResponseEntity<ChangeNameResponseDTO> changeLastname(@RequestBody ChangeLastNameRequestDTO request,
			@RequestHeader("Authorization") String header) {
//		userService.changeLastName(request, header);

		return ResponseEntity.ok(userService.changeLastName(request, header));
	}

	// ---------------------------------------------------------------------------------------------

	@PutMapping("/addImage")
	public ResponseEntity<ConfirmationResponseDTO> addImage(@RequestHeader("Authorization") String header,
			@RequestParam(name = "image") MultipartFile image) throws IOException {
		return ResponseEntity.ok(userService.addImage(header, ImageConverter.convertMultiPartFileToUserImage(image)));
	}

	// ---------------------------------------------------------------------------------------------

	@PutMapping("/deleteImage")
	public ResponseEntity<ConfirmationResponseDTO> deleteImage(@RequestHeader("Authorization") String header) {
		return ResponseEntity.ok(userService.deleteImage(header));
	}

	// ---------------------------------------------------------------------------------------------

	@GetMapping("/getImage")
	public ResponseEntity<ImageResponseDTO> getImage(@RequestHeader("Authorization") String header) {
		return ResponseEntity.ok(userService.getImage(header));
	}

	// ---------------------------------------------------------------------------------------------

	@PutMapping("/blockMyAccount")
	public ResponseEntity<ConfirmationResponseDTO> blockMyAccount(@RequestHeader("Authorization") String header,
			@RequestBody DeleteMyAccountRequestDTO request) {
//		User 
		user = userService.getUserFromHeader(header);
		try {
			authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), request.getPassword()));
		} catch (BadCredentialsException e) {
			throw new InvalidPasswordOrEmailException("Password invalid!");
		} catch (DisabledException e) {
			throw new UserDisabledException("Your account needs to be verified!");
		} catch (LockedException e) {
			throw new UserAccountLockedException("Your account has already been locked!");
		}
		return ResponseEntity.ok(userService.blockMyAccount(header, request.getPassword()));

	}

	// ---------------------------------------------------------------------------------------------

	@PostMapping("/askForExamAccess")
	private ResponseEntity<ConfirmationResponseDTO> askForExamAccess(@RequestHeader("Authorization") String header,
			@RequestParam String categoryString) {
//		User 
		user = userService.getUserFromHeader(header);
		Category category = categoryService.findByName(categoryString);
		return ResponseEntity.ok(notificationService.askForExamAccess(user, category));
	}

	// ---------------------------------------------------------------------------------------------

	@PostMapping("/reportQuestion")
	private ResponseEntity<ConfirmationResponseDTO> reportQuestion(@RequestHeader("Authorization") String header,
			@RequestBody ReportQuestionRequestDTO request) {
//		User 
		user = userService.getUserFromHeader(header);
		return ResponseEntity.ok(notificationService.reportQuestion(user, request));
	}

	// ---------------------------------------------------------------------------------------------

	@GetMapping("/seeMyExamAccessRequest")
	public List<NotificationExam> seeMyExamAccessRequest(@RequestHeader("Authorization") String header) {
		user = userService.getUserFromHeader(header);
		return notificationService.findMyExamAccessRequest(user);
	}
}
