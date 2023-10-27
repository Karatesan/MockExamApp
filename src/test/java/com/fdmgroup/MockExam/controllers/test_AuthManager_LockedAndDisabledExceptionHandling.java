package com.fdmgroup.MockExam.controllers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fdmgroup.MockExam.dto.AuthenticationRequestDTO;
import com.fdmgroup.MockExam.dto.RegisterRequestDTO;
import com.fdmgroup.MockExam.model.User;


@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class test_AuthManager_LockedAndDisabledExceptionHandling {

	
	@Autowired
	private MockMvc mvc;
	@Autowired
    private ObjectMapper objectMapper;
	
	@MockBean
	UserDetailsService userDetailsService;
	
	static String validJwtToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QHRlc3QuY29tIiwiaWF0IjoxNjkyMDIwOTIxLCJleHAiOjE3OTIwMjIzNjF9.R3OqXtVwU5DdANVB8Qk8w1MFvYkdJFSOqnqsG3jVNtU";
	
	RegisterRequestDTO request;
	AuthenticationRequestDTO authRequest;
	User user;
	String firstName;
	String lastName;
	String email;
	String password;
	UserDetails userDetails;
	List<GrantedAuthority> authorities;
	GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_TRAINEE");
	
	@BeforeEach
	public void setUp() {
		firstName ="testname_firstname";
		lastName ="testname_lastname";
		email = "test@test.com";
		password = "testPassword";
		authorities = new ArrayList<>();
		authorities.add(authority);
		 userDetails = new org.springframework.security.core.userdetails.User(email, password, false, true, true, true, authorities);
		
		
		authRequest = new AuthenticationRequestDTO(email,password);
	
	}

	@Test
	@WithMockUser(username= "test@test.com" , password = "testPassword")
	void test_authenticate_throwsUserNotEnabledException() throws JsonProcessingException, Exception {
		when(userDetailsService.loadUserByUsername(email)).thenReturn(userDetails);
		
		mvc.perform(post("/api/authentication/authenticate")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(authRequest)))
				.andExpect(status().isUnauthorized())
				.andExpect(content().string("Your account needs to be verified!"));
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	@WithMockUser(username= "test@test.com" , password = "testPassword")
	void test_authenticate_throwsUserAccountLockedException() throws JsonProcessingException, Exception {
		
		userDetails = new org.springframework.security.core.userdetails.User(email, password, true, true, true, false, authorities);
		when(userDetailsService.loadUserByUsername(email)).thenReturn(userDetails);
		
		mvc.perform(post("/api/authentication/authenticate")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(authRequest)))
				.andExpect(status().isUnauthorized())
				.andExpect(content().string("Your account has been locked! \n Please contact an Admin if you want to unlock it!"));
	}
}
