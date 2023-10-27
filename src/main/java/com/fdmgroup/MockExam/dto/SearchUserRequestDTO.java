package com.fdmgroup.MockExam.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchUserRequestDTO {
	private String searchText;
	private List<String> searchFields;
	private int page;
	private String tag;
}
