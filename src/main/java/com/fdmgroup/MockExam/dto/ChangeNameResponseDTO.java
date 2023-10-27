package com.fdmgroup.MockExam.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangeNameResponseDTO {
	
	String confirmationMessage;
	String token;

}
