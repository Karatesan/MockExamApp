package com.fdmgroup.MockExam.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDetailsResponseDTO {
private int id;
private QuestionResponseDTO question;
private CategoryResponseDTO category;
private UserFoundInSearchDTO user;
private String action;
private String comment;
private boolean handling;
public NotificationDetailsResponseDTO(String action,int id, UserFoundInSearchDTO user) {
	super();
	this.action = action;
	this.id = id;
	this.user = user;
}
public NotificationDetailsResponseDTO(String action,int id, UserFoundInSearchDTO user, CategoryResponseDTO category) {
	super();
	this.action = action;
	this.id = id;
	this.category = category;
	this.user = user;
}
public NotificationDetailsResponseDTO(String action,int id,  UserFoundInSearchDTO user,QuestionResponseDTO question, String comment) {
	super();
	this.action = action;
	this.id = id;
	this.question = question;
	this.user = user;
	this.comment = comment;
}
public NotificationDetailsResponseDTO(String action, int id, UserFoundInSearchDTO user,  String comment) {
	super();
	this.id = id;
	this.user = user;
	this.action = action;
	this.comment = comment;
}

}
