package com.fdmgroup.MockExam.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fdmgroup.MockExam.dto.AuthenticationRequestDTO;
import com.fdmgroup.MockExam.dto.AuthenticationResponseDTO;
import com.fdmgroup.MockExam.dto.ConfirmationResponseDTO;
import com.fdmgroup.MockExam.dto.PasswordResetRequestDTO;
import com.fdmgroup.MockExam.dto.PasswordResetResponseDTO;
import com.fdmgroup.MockExam.dto.RegisterRequestDTO;
import com.fdmgroup.MockExam.dto.RequestPasswordResetRequestDTO;
import com.fdmgroup.MockExam.dto.RequestPasswordResetResponseDTO;
import com.fdmgroup.MockExam.dto.ResendLinkResponseDTO;
import com.fdmgroup.MockExam.model.Role;
import com.fdmgroup.MockExam.model.User;
import com.fdmgroup.MockExam.repositories.UserRepository;
import com.fdmgroup.MockExam.services.AuthenticationService;
import com.fdmgroup.MockExam.services.AuthenticationServiceImpl;
import com.fdmgroup.MockExam.services.JwtService;
import com.fdmgroup.MockExam.services.UserService;

import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class test_AuthenticationController {
	
	@MockBean
	private AuthenticationService mockService;

	@MockBean(name ="userRepo")
	private UserRepository userRepo;
	
	@Autowired
	private MockMvc mvc;
	@Autowired
    private ObjectMapper objectMapper;
	
	RegisterRequestDTO request;
	ArgumentCaptor<String> stringCaptor;
	ArgumentCaptor<LocalDateTime> timeCaptor;
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
		 stringCaptor  = ArgumentCaptor.forClass(String.class);
		 timeCaptor  = ArgumentCaptor.forClass(LocalDateTime.class);
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	void test_verify_verifiesUserWhenCalledWithExistingTokenAsParam() throws JsonProcessingException, Exception {

	ConfirmationResponseDTO response = new ConfirmationResponseDTO("called");
		
	String testTokenString = "testToken";	
	LocalDateTime time = LocalDateTime.now();
	when(mockService.verifyToken(stringCaptor.capture(),timeCaptor.capture())).thenReturn(response);	
		
	mvc.perform(get("/api/authentication/confirm?token=" +  testTokenString))
			.andExpect(status().isOk())
			.andExpect(jsonPath("confirmationMessage").value("called"));
	
	assertTrue(stringCaptor.getValue().equals(testTokenString));
	assertTrue(timeCaptor.getValue().isBefore(time.plusSeconds(1)));
	assertTrue(timeCaptor.getValue().isAfter(time.minusSeconds(1)));
	
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	void test_resendLink_resendsEmailWithLinkVerificationWhenCalledWithExpiredToken() throws JsonProcessingException, Exception {
		ResendLinkResponseDTO response = new ResendLinkResponseDTO("resent");
		
		String oldToken = "oldToken";
		LocalDateTime time = LocalDateTime.now();
		when(mockService.resendVerificationLink(stringCaptor.capture(),timeCaptor.capture())).thenReturn(response);
		
		mvc.perform(get("/api/authentication/resendLink?token=" +  oldToken))
		.andExpect(status().isOk())
		.andExpect(jsonPath("resendLinkMessage").value("resent"));
		
		assertTrue(stringCaptor.getValue().equals(oldToken));
		assertTrue(timeCaptor.getValue().isBefore(time.plusSeconds(1)));
		assertTrue(timeCaptor.getValue().isAfter(time.minusSeconds(1)));
		
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	void test_register_CreatesUserAndReturnsValidTokenWithCorrectUserPassedIn() throws JsonProcessingException, Exception {

		AuthenticationResponseDTO response = AuthenticationResponseDTO.builder().token("Created Token").build();
		LocalDateTime time = LocalDateTime.now();
		
		ArgumentCaptor<RegisterRequestDTO> requestCaptor = ArgumentCaptor.forClass(RegisterRequestDTO.class);
		
		when(mockService.register(requestCaptor.capture(),timeCaptor.capture())).thenReturn(response);

	
		mvc.perform(post("/api/authentication/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("token").value("Created Token"));
		
		assertTrue(timeCaptor.getValue().isBefore(time.plusSeconds(1)));
		assertTrue(timeCaptor.getValue().isAfter(time.minusSeconds(1)));
		assertEquals(requestCaptor.getValue(), request);
		
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	void test_authenticate_sendsCorrectUserEmailAndPasswordToAuthenticationServiceLayerWhenAuthenticationRequestIsSendIn() throws JsonProcessingException, Exception {
		AuthenticationRequestDTO authRequest = new AuthenticationRequestDTO("testemail", "authTestPassword");
		
		AuthenticationResponseDTO response = AuthenticationResponseDTO.builder().token("Created Token").build();
		
		when(mockService.authenticate(authRequest)).thenReturn(response);
	
		mvc.perform(post("/api/authentication/authenticate")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(authRequest)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("token").value("Created Token"));
		
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	void test_requestPasswordRequest_sendsSendsCorrectPasswordRequestRequestToServiceLayerAndReturnsCorrectPasswordResetResponse() throws JsonProcessingException, Exception {
		String email = "testReset@test.com";
		LocalDateTime time = LocalDateTime.now();
		
		ArgumentCaptor<RequestPasswordResetRequestDTO> requestCaptor = ArgumentCaptor.forClass(RequestPasswordResetRequestDTO.class);
		
		RequestPasswordResetRequestDTO resetRequest =new RequestPasswordResetRequestDTO(email);
		String responseMessage="Password Reset Mail was send to " + email + "!";
		
		RequestPasswordResetResponseDTO resetResponse = new RequestPasswordResetResponseDTO(responseMessage);
		
		when(mockService.requestPasswordReset(requestCaptor.capture(), timeCaptor.capture())).thenReturn(resetResponse);
		
		mvc.perform(post("/api/authentication/passwordReset")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(resetRequest)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("responseMessage").value(responseMessage));
		
		assertTrue(timeCaptor.getValue().isBefore(time.plusSeconds(1)));
		assertTrue(timeCaptor.getValue().isAfter(time.minusSeconds(1)));
		assertEquals(requestCaptor.getValue(), resetRequest);
	}
	
	// ---------------------------------------------------------------------------------------------
	
	@Test
	void test_resetPassword_resetsPasswordForwardsCorrectRequestPasswordResetRequestAndTokenReceivesCorrectRequestPasswordResetResponseFromService() throws JsonProcessingException, Exception {
		
		String token = "testtoken";
		PasswordResetRequestDTO passwordResetRequest = 
				new PasswordResetRequestDTO("resettedPassword", "resettedPassword");
		ArgumentCaptor<PasswordResetRequestDTO> resetCaptor = ArgumentCaptor.forClass(PasswordResetRequestDTO.class);
		
		LocalDateTime time = LocalDateTime.now();
		
		PasswordResetResponseDTO passwordResetResponse = new PasswordResetResponseDTO("Password change successful!");
		
		when(mockService.resetPassword(resetCaptor.capture(), stringCaptor.capture(), timeCaptor.capture())).thenReturn(passwordResetResponse);
		
		mvc.perform(put("/api/authentication/resettingPassword?token=testtoken")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(passwordResetRequest)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("responseMessage").value("Password change successful!"));
		
		assertTrue(timeCaptor.getValue().isBefore(time.plusSeconds(1)));
		assertTrue(timeCaptor.getValue().isAfter(time.minusSeconds(1)));
		assertEquals(resetCaptor.getValue(), passwordResetRequest);
		assertTrue(stringCaptor.getValue().equals(token));
	}
}
