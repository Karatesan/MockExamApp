package com.fdmgroup.MockExam.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchQuestionRequestDTO {
private String searchText;
private String category;
private String level;
private List<String> searchFields;
// tags: list or just one?
private String tag;

private int page;
}
