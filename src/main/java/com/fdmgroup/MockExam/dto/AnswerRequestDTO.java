package com.fdmgroup.MockExam.dto;

import java.util.List;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnswerRequestDTO {
private Integer questionId;
private List<String> givenAnswer;
}
