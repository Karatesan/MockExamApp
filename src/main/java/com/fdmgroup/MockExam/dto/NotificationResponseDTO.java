package com.fdmgroup.MockExam.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class NotificationResponseDTO extends IdFoundInSearchDTO {
private String message;

public NotificationResponseDTO(int id, String message) {
	super(id);
	this.message = message;
}

}
