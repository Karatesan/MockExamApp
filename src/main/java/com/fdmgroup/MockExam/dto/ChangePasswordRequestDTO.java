package com.fdmgroup.MockExam.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordRequestDTO{
	
	private String confirmPassword;
	private String oldPassword;
	private String password;
	
	
}
