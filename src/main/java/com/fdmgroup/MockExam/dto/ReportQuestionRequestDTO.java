package com.fdmgroup.MockExam.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReportQuestionRequestDTO {
private int questionId;
private String comment;
}
