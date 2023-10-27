package com.fdmgroup.MockExam.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fdmgroup.MockExam.dto.AuthenticationRequestDTO;
import com.fdmgroup.MockExam.dto.AuthenticationResponseDTO;
import com.fdmgroup.MockExam.dto.ChangePasswordRequestDTO;
import com.fdmgroup.MockExam.dto.ChangePasswordResponseDTO;
import com.fdmgroup.MockExam.dto.ConfirmationResponseDTO;
import com.fdmgroup.MockExam.dto.PasswordResetRequestDTO;
import com.fdmgroup.MockExam.dto.PasswordResetResponseDTO;
import com.fdmgroup.MockExam.dto.RegisterRequestDTO;
import com.fdmgroup.MockExam.dto.RequestPasswordResetRequestDTO;
import com.fdmgroup.MockExam.dto.RequestPasswordResetResponseDTO;
import com.fdmgroup.MockExam.dto.ResendLinkResponseDTO;
import com.fdmgroup.MockExam.services.AuthenticationService;
import com.fdmgroup.MockExam.services.TimeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/authentication")
@RequiredArgsConstructor

public class AuthenticationController {
	
	private final AuthenticationService service;
	private final TimeService timeService;

	@PostMapping("/register")
	public ResponseEntity<AuthenticationResponseDTO> register(
			@RequestBody RegisterRequestDTO request){
		return ResponseEntity.ok(service.register(request,timeService.now()));
		
	}
	
	@PostMapping("/authenticate")
	public ResponseEntity<AuthenticationResponseDTO> authenticate(
			@RequestBody AuthenticationRequestDTO request){
		return ResponseEntity.ok(service.authenticate(request));
}
	
	@RequestMapping(path = "confirm")
	public ResponseEntity<ConfirmationResponseDTO> verify(
			@RequestParam("token") String token) {	
		return ResponseEntity.ok(service.verifyToken(token,timeService.now()));
	}
	
	@RequestMapping(path = "resendLink")
	public ResponseEntity<ResendLinkResponseDTO> resendLink(
			@RequestParam("token") String token) {	
		return ResponseEntity.ok(service.resendVerificationLink(token,timeService.now()));
	}
	
	@PostMapping("/passwordReset")
	public ResponseEntity<RequestPasswordResetResponseDTO> requestPasswordReset (@RequestBody RequestPasswordResetRequestDTO request)
	{			
		return ResponseEntity.ok(service.requestPasswordReset(request,timeService.now()));	
	}
	
	@PutMapping(path="resettingPassword")
	public ResponseEntity<PasswordResetResponseDTO> resetPassword (@RequestBody PasswordResetRequestDTO request, @RequestParam("token") String token){
		return ResponseEntity.ok(service.resetPassword(request, token,timeService.now()));
	}
	
	@PostMapping("/changePassword")
	public ResponseEntity<ChangePasswordResponseDTO> changePassword(@RequestBody ChangePasswordRequestDTO request, @RequestHeader("Authorization") String header){
		return ResponseEntity.ok(service.changePassword(request, header));
	}

}
