package com.fdmgroup.MockExam.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExamAccessRequestDTO extends AccountIdRequestDTO {
private String category;
private int noteId;
public ExamAccessRequestDTO(String category) {
	super();
	this.category = category;
}

}
