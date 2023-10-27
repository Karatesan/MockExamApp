package com.fdmgroup.MockExam.services;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.fdmgroup.MockExam.dto.ConfirmationResponseDTO;
import com.fdmgroup.MockExam.dto.ListOfUsersIdDTO;
import com.fdmgroup.MockExam.dto.NotificationDetailsResponseDTO;
import com.fdmgroup.MockExam.dto.NotificationListResponseDTO;
import com.fdmgroup.MockExam.dto.NotificationRequestDTO;
import com.fdmgroup.MockExam.dto.NotificationResponseDTO;
import com.fdmgroup.MockExam.dto.QuestionResponseDTO;
import com.fdmgroup.MockExam.dto.ReportQuestionRequestDTO;
import com.fdmgroup.MockExam.model.Category;
import com.fdmgroup.MockExam.model.Notification;
import com.fdmgroup.MockExam.model.NotificationAccount;
import com.fdmgroup.MockExam.model.NotificationExam;
import com.fdmgroup.MockExam.model.NotificationFirstName;
import com.fdmgroup.MockExam.model.User;

public interface NotificationService {

	Notification findById(int id);

	NotificationListResponseDTO seeNotification(NotificationRequestDTO request, User admin);

	List<Notification> removeAdmin(List<Notification> list, User admin);

	List<Notification> filterDeleteAccount(List<Notification> notes);

	List<Notification> filterAccessExam(List<Notification> notes);

	List<Notification> filterReportQuestion(List<Notification> notes);
	
	List<Notification> filterFirstName(List<Notification> notes);
	
	List<Notification> filterLastName(List<Notification> notes);

	ConfirmationResponseDTO askForExamAccess(User user, Category category);

	List<NotificationResponseDTO> getMessageForNotification(List<Notification> notes);

	void deleteAccount(User user);

	ConfirmationResponseDTO reportQuestion(User user, ReportQuestionRequestDTO request);

	NotificationDetailsResponseDTO notificationDetails(NotificationRequestDTO request, User admin);

	ConfirmationResponseDTO handle(int id, User admin);

	ConfirmationResponseDTO delete(int id);

	ConfirmationResponseDTO approved(int id);

	QuestionResponseDTO acceptQuestionReporting(int questionId, int noteId);

//	ConfirmationResponseDTO examAccessAccept(User findById, Category findByName, int noteId);

	ConfirmationResponseDTO examAccessAccept(int id);

	boolean updateQuestion_deleteNotification(Integer notificationId);

	boolean deleteAccount_deleteNotification(int noteId);

	ConfirmationResponseDTO giveAccessToUser(User user, Category category);

	Notification changeFirstName_notificationRequest(User user, String firstname);

	Notification changeLastName_notificationRequest(User user, String lastname);

	List<Notification> addHandleBy(User admin);

	List<NotificationExam> findMyExamAccessRequest(User user);




}
