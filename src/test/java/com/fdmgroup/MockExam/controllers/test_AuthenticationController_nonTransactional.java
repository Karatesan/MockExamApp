package com.fdmgroup.MockExam.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fdmgroup.MockExam.dto.AuthenticationRequestDTO;
import com.fdmgroup.MockExam.dto.RegisterRequestDTO;
import com.fdmgroup.MockExam.model.User;
import com.fdmgroup.MockExam.model.VerificationToken;
import com.fdmgroup.MockExam.repositories.VerificationTokenRepository;
import com.fdmgroup.MockExam.services.JwtService;
import com.fdmgroup.MockExam.services.UserService;
import com.fdmgroup.MockExam.services.VerificationTokenService;

import jakarta.transaction.Transactional;


@SpringBootTest
@AutoConfigureMockMvc

public class test_AuthenticationController_nonTransactional {
	
	@Autowired
	private AuthenticationController controller;
	
	@Autowired
	private MockMvc mvc;
	
	@Mock
	User mockUser;
	
	@Mock
	UserService mockService;
	
	@Mock
	VerificationTokenRepository verificationTokenRepository;
	
	@Autowired
    private ObjectMapper objectMapper;
	
	@Autowired
	JwtService jwtService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	VerificationTokenService verificationTokenService;
	
	private RegisterRequestDTO request;
	String email = "newtest@test.com";
	private AuthenticationRequestDTO requestAuth;
	
	@BeforeEach
	void setUp() {
		request = new RegisterRequestDTO(
				"testname_firstname",
				"testname_lastname",
				email,
				"testPassword",
				"testPassword");
		requestAuth=new AuthenticationRequestDTO(email, "testPassword");
	}
	

	// ---------------------------------------------------------------------------------------------

	@Test
	void test_ControllerNotNull() {
		assertThat(controller).isNotNull();
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	@Transactional
	void test_registerUserDoesNotCreateUserAndThrowsCorrectExceptionWithIncorrectEmailPassedIn() throws JsonProcessingException, Exception {
		
		request.setEmail("notanemail");
		
		MvcResult mvcresult = mvc.perform(post("/api/authentication/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isBadRequest())
				.andReturn();
		
		assertEquals("{\"email\":[\"Not a valid email address\"]}", mvcresult.getResponse().getContentAsString());	
	}
	
	// ---------------------------------------------------------------------------------------------
	
	@Test
	@Transactional
	void test_registerUserDoesNotCreateUserAndThrowsCorrectExceptionWithIncorrectFirstNamePassedIn() throws JsonProcessingException, Exception {
		request.setFirstname("testtesttesttesttesttesttesttesttesttesttesttesttee");
		
		MvcResult mvcresult = mvc.perform(post("/api/authentication/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isBadRequest())
				.andReturn();
		
		assertEquals("{\"firstName\":[\"First names cannot be longer than 50 characters!\"]}", mvcresult.getResponse().getContentAsString());
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	void test_registerUserDoesNotCreateUserAndThrowsCorrectExceptionWithIncorrectlastNamePassedIn() throws JsonProcessingException, Exception {
		request.setLastname("testtesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttestt");
		
		MvcResult mvcresult = mvc.perform(post("/api/authentication/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isBadRequest())
				.andReturn();
		
		assertEquals("{\"lastName\":[\"Last names cannot be longer than 100 characters!\"]}", mvcresult.getResponse().getContentAsString());
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	@Transactional
	void test_authenticateDoesNotAuthenticateUserAndThrowsExceptionWhenIncorrectEmailIsPassedIn() throws JsonProcessingException, Exception {
		
		MvcResult mvcRegister = mvc.perform(post("/api/authentication/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isOk()) 				
				.andReturn();
		
		AuthenticationRequestDTO authRequest = new AuthenticationRequestDTO("nottest@test.com", "testPassword");
		
		MvcResult mvcresult = mvc.perform(post("/api/authentication/authenticate")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(authRequest)))
				.andExpect(status().isUnauthorized())
				.andReturn();
		
		MockHttpServletResponse resultRequest =  mvcresult.getResponse();
		
		assertEquals("E-Mail or Password invalid!",resultRequest.getContentAsString());
		
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	void test_verify_verifiesUserWhenCalledWithExistingTokenAsParam() throws JsonProcessingException, Exception {
		
		MvcResult mvcRegister = mvc.perform(post("/api/authentication/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isOk()) 				.andReturn();
		
		User user = userService.findByEmail(email).get();
		VerificationToken token = new VerificationToken();
		List<VerificationToken> tokens = new ArrayList<>();
		tokens.add(token);
		when(verificationTokenRepository.findByUser(user)).thenReturn(tokens);
		List<VerificationToken> tokenList = verificationTokenService.findByUser(user);
		VerificationToken verificationToken = tokenList.get(0);		
		
		MvcResult mvcresult = mvc.perform(get("/api/authentication/confirm?token=" +  verificationToken.getToken()))
				.andExpect(status().isOk())
				.andReturn();
		
		MockHttpServletResponse resultRequest =  mvcresult.getResponse();
		
		assertEquals("{\"confirmationMessage\":\"Email account is verified now!\"}",resultRequest.getContentAsString());

		assertEquals(1,tokenList.size());
		
		user = userService.findByEmail(email).get();
		
		assertTrue(user.getVerified());
		
		verificationTokenService.delete(verificationToken);
		
		userService.delete(user);
		
	}
}
