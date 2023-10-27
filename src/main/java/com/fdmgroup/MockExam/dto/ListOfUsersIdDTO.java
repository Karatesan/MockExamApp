package com.fdmgroup.MockExam.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@AllArgsConstructor
@Data
@NoArgsConstructor
public class ListOfUsersIdDTO {
private List<Integer> list;
private String categoryName;
}
