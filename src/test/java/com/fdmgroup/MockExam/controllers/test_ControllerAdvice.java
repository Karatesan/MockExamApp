package com.fdmgroup.MockExam.controllers;


import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fdmgroup.MockExam.dto.AuthenticationRequestDTO;
import com.fdmgroup.MockExam.dto.ChangePasswordRequestDTO;
import com.fdmgroup.MockExam.dto.PasswordResetRequestDTO;
import com.fdmgroup.MockExam.dto.RegisterRequestDTO;
import com.fdmgroup.MockExam.model.PasswordResetToken;
import com.fdmgroup.MockExam.model.Role;
import com.fdmgroup.MockExam.model.User;
import com.fdmgroup.MockExam.model.VerificationToken;
import com.fdmgroup.MockExam.repositories.UserRepository;
import com.fdmgroup.MockExam.services.PasswordResetTokenService;
import com.fdmgroup.MockExam.services.VerificationTokenService;



@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class test_ControllerAdvice {
	
	@MockBean()
	private UserRepository userRepo;
	
	@MockBean
	VerificationTokenService  verificationTokenService;
	
	@MockBean(name="passwordResetTokenService")
	PasswordResetTokenService passwordResetTokenService;
	
	@Autowired
	private MockMvc mvc;
	@Autowired
    private ObjectMapper objectMapper;
	
	static String validJwtToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QHRlc3QuY29tIiwiaWF0IjoxNjkyMDIwOTIxLCJleHAiOjE3OTIwMjIzNjF9.R3OqXtVwU5DdANVB8Qk8w1MFvYkdJFSOqnqsG3jVNtU";
	
	RegisterRequestDTO request;
	AuthenticationRequestDTO authRequest;
	User user;
	String firstName;
	String lastName;
	String email;
	String password;
	
	
	@BeforeEach
	public void setUp() {
		firstName ="testname_firstname";
		lastName ="testname_lastname";
		email = "anothertest@test.com";
		password = "testPassword";
		 request = new RegisterRequestDTO(
					firstName,
					lastName,
					email,
					password,
					password);
		 user = new User(email, firstName,lastName,password, Role.TRAINEE);
		authRequest = new AuthenticationRequestDTO(email,password);
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	void test_register_ThrowsEmailAlreadyExistsExceptionIfAlreadyExistingUserIsCreated() throws JsonProcessingException, Exception {
		
		when(userRepo.findByEmail(email)).thenReturn(Optional.of(user));


		mvc.perform(post("/api/authentication/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request)))
						.andExpect(status().isConflict())
						.andExpect(content().string("A user with this email already exists."));
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	void test_register_ThrowsPasswordsNotIdenticalException_whenPasswordsAreNotIdentical() throws JsonProcessingException, Exception {
		request.setConfirmPassword("differentPassword");

		mvc.perform(post("/api/authentication/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request)))
						.andExpect(status().isBadRequest())
						.andExpect(content().string("Password and confirm password are not identical!"));
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	@WithMockUser(username= "test@test.com" , password = "testPassword")
	void test_changePassword_ThrowsIfPasswordsAreNotIdentical() throws Exception {
		
		ChangePasswordRequestDTO request = new ChangePasswordRequestDTO("oldPassword","newPassword","notnewPassword");
		
		mvc.perform(put("/api/users/changePassword")
				.header("Authorization", "Bearer " + validJwtToken)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isBadRequest())
				.andExpect(content().string("New password and confirm password are not identical!"));

	}
	

	// ---------------------------------------------------------------------------------------------

	
	@Test
	void test_register_ConstraintViolaton_whenHandedIncorrectEmail() throws JsonProcessingException, Exception {
		request.setEmail("notanemail");

		mvc.perform(post("/api/authentication/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request)))
						.andExpect(status().isBadRequest())
						.andExpect(jsonPath("email").value("Not a valid email address"));
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	void test_authenticate_throwsException() throws Exception {
		mvc.perform(post("/api/authentication/authenticate")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(authRequest)))
		.andExpect(status().isUnauthorized())
		.andExpect(content().string("E-Mail or Password invalid!"));
	}	

	// ---------------------------------------------------------------------------------------------

	@Test
	void test_verify_throws_TokenExpiredException() throws Exception{
		String token = "someToken";
		VerificationToken verificationToken = new VerificationToken();
		LocalDateTime time = LocalDateTime.now();
		User verifyUser = new User();

		verifyUser.setVerified(false);
		verificationToken.setToken(token);
		verificationToken.setConfirmedAt(null);
		verificationToken.setUser(verifyUser);
		verificationToken.setExpiresAt(LocalDateTime.now().minusMinutes(10));
		when(verificationTokenService.getToken(token)).thenReturn(Optional.of(verificationToken));
		System.err.println(verificationToken.toString());
		
		
		mvc.perform(get("/api/authentication/confirm?token=" +  verificationToken.getToken()))
				.andExpect(status().isConflict())
				.andExpect(content().string("Verification token is expired!"));
	
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	void test_resettingPassword_throws_TokenExpiredException() throws Exception{
		PasswordResetRequestDTO passwordResetRequest = 
				new PasswordResetRequestDTO("resettedPassword", "resettedPassword");
		
		String token = "someToken";
		PasswordResetToken passwordResetToken = new PasswordResetToken();
		LocalDateTime time = LocalDateTime.now();
		User resetPasswordUser = new User();

		resetPasswordUser.setVerified(false);
		passwordResetToken.setToken(token);
		passwordResetToken.setResetAt(null);
		passwordResetToken.setUser(resetPasswordUser);
		passwordResetToken.setExpiresAt(LocalDateTime.now().minusMinutes(10));
		when(passwordResetTokenService.getToken(token)).thenReturn(Optional.of(passwordResetToken));
		System.err.println(passwordResetToken.toString());
		
		
		mvc.perform(put("/api/authentication/resettingPassword?token="+passwordResetToken.getToken())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(passwordResetRequest)))
				.andExpect(status().isConflict())
				.andExpect(content().string("Password reset token is expired!"));
	
	}

}
		
	

