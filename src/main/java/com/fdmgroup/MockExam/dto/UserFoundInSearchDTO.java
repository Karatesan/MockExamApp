package com.fdmgroup.MockExam.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserFoundInSearchDTO extends IdFoundInSearchDTO {
//private int id;
private String firstName;
private String lastName;
private String email;
private boolean locked;
private boolean verified;
private List<String> tags;

public UserFoundInSearchDTO(int id,String firstName, String lastName, String email, boolean locked, boolean verified,
		List<String> tags) {
	super(id);
	this.firstName = firstName;
	this.lastName = lastName;
	this.email = email;
	this.locked = locked;
	this.verified = verified;
	this.tags = tags;
}



}
