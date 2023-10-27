package com.fdmgroup.MockExam.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fdmgroup.MockExam.dto.ChangeFirstNameRequestDTO;
import com.fdmgroup.MockExam.dto.ChangeLastNameRequestDTO;

import com.fdmgroup.MockExam.dto.ChangeNameResponseDTO;
import com.fdmgroup.MockExam.dto.ChangePasswordRequestDTO;
import com.fdmgroup.MockExam.dto.ChangePasswordResponseDTO;
import com.fdmgroup.MockExam.dto.ConfirmationResponseDTO;
import com.fdmgroup.MockExam.dto.DeleteMyAccountRequestDTO;
import com.fdmgroup.MockExam.dto.ImageResponseDTO;
import com.fdmgroup.MockExam.exceptions.InvalidPasswordOrEmailException;
import com.fdmgroup.MockExam.model.Role;
import com.fdmgroup.MockExam.model.User;
import com.fdmgroup.MockExam.services.AuthenticationService;
import com.fdmgroup.MockExam.services.JwtServiceImpl;
import com.fdmgroup.MockExam.services.UserService;

@SpringBootTest
@AutoConfigureMockMvc
public class test_UserController {

	@Autowired
	private UserController controller;

	@Autowired
	private MockMvc mvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private AuthenticationService service;
	@MockBean
	private AuthenticationManager authManager;
	@MockBean
	private UserService userService;
	static JwtServiceImpl jwtService;

	static String validJwtToken = "It will be generated during @BeforeAll";

	@BeforeAll
	public static void setUp() {
		jwtService = new JwtServiceImpl();
		User user = new User();
		user.setEmail("test@test.com");
		user.setPassword("testPassword");
		user.setRole(Role.ADMIN);
		validJwtToken = jwtService.generateToken(user);
	}

	@Test
	void test_controller_notNull() {
		assertThat(controller).isNotNull();
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	@WithMockUser(username = "test@test.com", password = "testPassword")
	void test_changePassword_handCorrectRequestAndHeaderToService() throws Exception {

		String responseMessage = "Password change successful";
		ChangePasswordRequestDTO request = new ChangePasswordRequestDTO("oldPassword", "newPassword", "newPassword");
		ChangePasswordResponseDTO response = ChangePasswordResponseDTO.builder().responseMessage(responseMessage)
				.build();

		when(service.changePassword(request, "Bearer " + validJwtToken)).thenReturn(response);
		mvc.perform(put("/api/users/changePassword").header("Authorization", "Bearer " + validJwtToken)
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect((jsonPath("responseMessage").value(responseMessage)));
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	@WithMockUser(username = "test@test.com", password = "testPassword")
	public void test_changeFirstName_handCorrectRequestAndHeaderToService() throws Exception {
		ChangeFirstNameRequestDTO requestDTO = new ChangeFirstNameRequestDTO();
		requestDTO.setFirstname("NewFirstName");
		String requestJson = objectMapper.writeValueAsString(requestDTO);

		ChangeNameResponseDTO responseDTO = new ChangeNameResponseDTO();
		responseDTO.setConfirmationMessage("First name changed to: NewFirstName");
		when(userService.changeFirstName(eq(requestDTO), anyString())).thenReturn(responseDTO);

		mvc.perform(put("/api/users/changeFirstname").contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer " + validJwtToken).content(requestJson)).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("confirmationMessage").value("First name changed to: NewFirstName"));
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	@WithMockUser(username = "test@test.com", password = "testPassword")
	public void test_changeLastName_handCorrectRequestAndHeaderToService() throws Exception {
		ChangeLastNameRequestDTO requestDTO = new ChangeLastNameRequestDTO();
		requestDTO.setLastname("NewLastName");
		String requestJson = objectMapper.writeValueAsString(requestDTO);

		ChangeNameResponseDTO responseDTO = new ChangeNameResponseDTO();
		responseDTO.setConfirmationMessage("Last name changed to: NewLastName");
		when(userService.changeLastName(eq(requestDTO), anyString())).thenReturn(responseDTO);

		mvc.perform(put("/api/users/changeLastname").contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer " + validJwtToken).content(requestJson)).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("confirmationMessage").value("Last name changed to: NewLastName"));
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	@WithMockUser(username = "test@test.com", password = "testPassword")
	public void test_blockMyAccount_UserDisabled() throws Exception {
		DeleteMyAccountRequestDTO requestDTO = new DeleteMyAccountRequestDTO();
		requestDTO.setPassword("Test123");
		User user = new User();
		user.setEmail("email@test.com");
		when(userService.getUserFromHeader(anyString())).thenReturn(user);
		when(userService.blockMyAccount(anyString(), anyString())).thenThrow(DisabledException.class);

		mvc.perform(put("/api/users/blockMyAccount").contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer " + validJwtToken)
				.content(objectMapper.writeValueAsString(requestDTO))).andExpect(status().isForbidden());
	}

	// ---------------------------------------------------------------------------------------------

//    @Test
//    public void test_blockMyAccount_Success() throws Exception {
//        DeleteMyAccountRequestDTO requestDTO = new DeleteMyAccountRequestDTO();
//        requestDTO.setPassword("validPassword");
//
//        User user = new User();
//        user.setEmail("email@test.com");
//        user.setPassword("abc");
//        ConfirmationResponseDTO responseDTO = new ConfirmationResponseDTO();
//        responseDTO.setConfirmationMessage("Account blocked successfully");
//        UsernamePasswordAuthenticationToken authenticate = new UsernamePasswordAuthenticationToken(user.getEmail(),user.getPassword());
//        when(authManager.authenticate(any())).thenReturn(authenticate);
//        when(userService.getUserFromHeader(anyString())).thenReturn(user);
//
//        Mockito.when(userService.blockMyAccount(validJwtToken, requestDTO.getPassword()))
//               .thenReturn(responseDTO);
//
//        mvc.perform(MockMvcRequestBuilders
//                .put("/api/users/blockMyAccount")
//                .header("Authorization", validJwtToken)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content("{\"password\":\"validPassword\"}"))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("confirmationMessage")
//                .value("Account blocked successfully"));
//    }

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_blockMyAccount_InvalidPassword() throws Exception {
		DeleteMyAccountRequestDTO requestDTO = new DeleteMyAccountRequestDTO();
		requestDTO.setPassword("invalidPassword");

		User user = new User();
		user.setEmail("email@test.com");
		when(userService.getUserFromHeader(anyString())).thenReturn(user);
		Mockito.doThrow(InvalidPasswordOrEmailException.class).when(authManager)
				.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class));

		mvc.perform(MockMvcRequestBuilders.put("/api/users/blockMyAccount").header("Authorization", validJwtToken)
				.contentType(MediaType.APPLICATION_JSON).content("{\"password\":\"invalidPassword\"}"))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_blockMyAccount_AccountLocked() throws Exception {
		DeleteMyAccountRequestDTO requestDTO = new DeleteMyAccountRequestDTO();
		requestDTO.setPassword("validPassword");

		User user = new User();
		user.setEmail("email@test.com");
		when(userService.getUserFromHeader(anyString())).thenReturn(user);
		Mockito.doThrow(LockedException.class).when(authManager)
				.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class));

		mvc.perform(MockMvcRequestBuilders.put("/api/users/blockMyAccount").header("Authorization", validJwtToken)
				.contentType(MediaType.APPLICATION_JSON).content("{\"password\":\"validPassword\"}"))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}
}
