package com.fdmgroup.MockExam.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountIdRoleRequestDTO extends AccountIdRequestDTO {
private String roleName;
}
