package com.fdmgroup.MockExam.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fdmgroup.MockExam.dto.ConfirmationResponseDTO;
import com.fdmgroup.MockExam.dto.IdFoundInSearchDTO;
import com.fdmgroup.MockExam.dto.NotificationDetailsResponseDTO;
import com.fdmgroup.MockExam.dto.NotificationListResponseDTO;
import com.fdmgroup.MockExam.dto.NotificationRequestDTO;
import com.fdmgroup.MockExam.dto.NotificationResponseDTO;
import com.fdmgroup.MockExam.dto.QuestionResponseDTO;
import com.fdmgroup.MockExam.dto.ReportQuestionRequestDTO;
import com.fdmgroup.MockExam.exceptions.AdminAccesException;
import com.fdmgroup.MockExam.model.Category;
import com.fdmgroup.MockExam.model.ExamAccess;
import com.fdmgroup.MockExam.model.Notification;
import com.fdmgroup.MockExam.model.NotificationAccount;
import com.fdmgroup.MockExam.model.NotificationExam;
import com.fdmgroup.MockExam.model.NotificationFirstName;
import com.fdmgroup.MockExam.model.NotificationLastName;
import com.fdmgroup.MockExam.model.NotificationQuestion;
import com.fdmgroup.MockExam.model.Question;
import com.fdmgroup.MockExam.model.User;
import com.fdmgroup.MockExam.repositories.ExamAccessRepository;
import com.fdmgroup.MockExam.repositories.NotificationRepository;

@ExtendWith(MockitoExtension.class)

public class test_NotificationService {
	@InjectMocks
	private NotificationSeviceImpl notificationService;

	@Mock
	NotificationRepository notificationRepository;
	@Mock
	QuestionService questionService;
	@Mock
	DTOService dTOService;
	@Mock
	SearchingSortService searchingSortService;
	@Mock
	ExamAccessRepository examAccessRepository;
	@Mock
	MailService mailService;
	NotificationRequestDTO request;
	User user;
	List<Notification> notifications;
	Notification note;
	String categoryName = "Java";

	Category category = new Category(categoryName);
	Question question;

	@BeforeEach
	public void setUp() {
		note = new Notification();
		request = new NotificationRequestDTO();
		user = new User();
		notifications = new ArrayList<>();
		question = new Question();

		question.setCategory(category);
		user.setEmail("testEmail");
		user.setFirstName("testFirst");
		user.setLastName("testLast");
		user.setId(1);
		user.setVerified(true);
		user.setLocked(false);

		request.setPage(1);

	}

	@Test
	public void test_findById() {

		int id = 1;
		Mockito.when(notificationRepository.findById(id)).thenReturn(Optional.of(note));
		Notification result = notificationService.findById(id);

		assertEquals(note, result);
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_seeNotification_deleteAccount() {
		request.setAction("Delete_account");
		note = new NotificationAccount(user);
		notifications.add(note);
		Mockito.when(notificationRepository.findAll()).thenReturn(notifications);

		NotificationListResponseDTO responseDTO = notificationService.seeNotification(request, user);

		assertEquals(notifications.size(), responseDTO.getList().size());
		assertEquals(responseDTO.getList().get(0).getMessage(),
				"User: testFirst testLast (testEmail) wants to delete account.");
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_seeNotification_examAccess() {
		request.setAction("Exam_access");
		note = new NotificationExam(user, category);
		notifications.add(note);
		Mockito.when(notificationRepository.findAll()).thenReturn(notifications);

		NotificationListResponseDTO responseDTO = notificationService.seeNotification(request, user);

		assertEquals(notifications.size(), responseDTO.getList().size());
		assertEquals(responseDTO.getList().get(0).getMessage(),
				"User: testFirst testLast (testEmail) wants to get access to exam in category: " + categoryName);
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_seeNotification_questionReport() {
		request.setAction("Reporting_question");
		note = new NotificationQuestion(user, question, "Bad question");
		notifications.add(note);
		Mockito.when(notificationRepository.findAll()).thenReturn(notifications);

		NotificationListResponseDTO responseDTO = notificationService.seeNotification(request, user);

		assertEquals(notifications.size(), responseDTO.getList().size());
		assertEquals(responseDTO.getList().get(0).getMessage(),
				"User: testFirst testLast (testEmail) reported a question from Exam: " + categoryName);
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_seeNotification_firstName() {
		request.setAction("First_name");
		note = new NotificationFirstName(user, "newName");
		notifications.add(note);
		Mockito.when(notificationRepository.findAll()).thenReturn(notifications);

		NotificationListResponseDTO responseDTO = notificationService.seeNotification(request, user);

		assertEquals(notifications.size(), responseDTO.getList().size());
		assertEquals(responseDTO.getList().get(0).getMessage(),
				"User: testFirst testLast (testEmail) wants to change first name to: newName");
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_seeNotification_lastName() {
		request.setAction("Last_name");
		note = new NotificationLastName(user, "newName");
		notifications.add(note);
		Mockito.when(notificationRepository.findAll()).thenReturn(notifications);

		NotificationListResponseDTO responseDTO = notificationService.seeNotification(request, user);

		assertEquals(notifications.size(), responseDTO.getList().size());
		assertEquals(responseDTO.getList().get(0).getMessage(),
				"User: testFirst testLast (testEmail) wants to change last name to: newName");
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_seeNotification_moreNotifications() {
		User admin = new User();
		request.setAction("All");
		request.setPage(1);
		notifications = new ArrayList<>();

		List<IdFoundInSearchDTO> sorted = new ArrayList<>();
		List<IdFoundInSearchDTO> sortedEnd = new ArrayList<>();
		
		for (int i = 0; i < 10; i++) {
			notifications.add(new NotificationExam(user, category));
			sorted.add(new IdFoundInSearchDTO(i));
		}
		for (int i = 0; i < 5; i++) {
			sortedEnd.add(new IdFoundInSearchDTO(i));
		}
		
		when(notificationRepository.findAll()).thenReturn(notifications);
		when(searchingSortService.sortList(any())).thenReturn(sorted);
		when(searchingSortService.findSearchedItemsOnPage(any(), eq(1))).thenReturn(sortedEnd);
		
		NotificationListResponseDTO notificationResponses = notificationService.seeNotification(request, admin);

		assertEquals(5, notificationResponses.getList().size());
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_seeNotification_zeroNotifications() {
		request.setAction("All");

		Mockito.when(notificationRepository.findAll()).thenReturn(new ArrayList<>());

        assertThrows(AdminAccesException.class, () -> {
        	notificationService.seeNotification(request, user);
        });

	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_addHandleBy() {
		List<Notification> notificationsHandledByAdmin = notificationService.addHandleBy(user);

		List<Notification> expectedNotifications = notifications.stream()
				.filter(note -> note.getAdmins().contains(user)).collect(Collectors.toList());

		assertEquals(expectedNotifications, notificationsHandledByAdmin);
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_getMessageForNotification() {
		notifications = new ArrayList<>();

		notifications.add(new NotificationExam(user, category));
		notifications.add(new NotificationQuestion(user, question, "Bad question"));
		notifications.add(new NotificationAccount(user));
		notifications.add(new NotificationFirstName(user, "Frodo"));
		notifications.add(new NotificationLastName(user, "Baggins"));

		List<NotificationResponseDTO> notificationResponses = notificationService
				.getMessageForNotification(notifications);

		assertEquals(5, notificationResponses.size());
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_filterDeleteAccount() {

		List<Notification> notifications = new ArrayList<>(List.of(new NotificationAccount(user),
				new NotificationExam(user, category), new NotificationAccount(user),
				new NotificationQuestion(user, question, "Bad question"), new NotificationAccount(user)));

		List<Notification> filteredNotifications = notificationService.filterDeleteAccount(notifications);

		long countNonAccountNotifications = filteredNotifications.stream()
				.filter(notification -> !(notification instanceof NotificationAccount)).count();
		assertEquals(0, countNonAccountNotifications);
		assertEquals(3, filteredNotifications.size());
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_filterReportQuestion() {

		List<Notification> notifications = new ArrayList<>(
				List.of(new NotificationQuestion(user, question, "Bad question"), new NotificationAccount(user),
						new NotificationQuestion(user, question, "Bad question"), new NotificationExam(user, category),
						new NotificationQuestion(user, question, "Bad question"),
						new NotificationQuestion(user, question, "Bad question")));

		List<Notification> filteredNotifications = notificationService.filterReportQuestion(notifications);

		long countNullQuestions = filteredNotifications.stream()
				.filter(notification -> (notification instanceof NotificationQuestion)
						&& ((NotificationQuestion) notification).getQuestion() == null)
				.count();
		assertEquals(0, countNullQuestions);
		assertEquals(4, filteredNotifications.size());

	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_filterAccessExam() {
		List<Notification> notifications = new ArrayList<>(
				List.of(new NotificationExam(user, category), new NotificationAccount(user),
						new NotificationExam(user, category), new NotificationQuestion(user, question, "Bad question"),
						new NotificationExam(user, category), new NotificationExam(user, category)));

		List<Notification> filteredNotifications = notificationService.filterAccessExam(notifications);

		long countNullCategories = filteredNotifications.stream()
				.filter(notification -> (notification instanceof NotificationExam)
						&& ((NotificationExam) notification).getCategory() == null)
				.count();

		assertEquals(0, countNullCategories);
		assertEquals(4, filteredNotifications.size());
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_filterFirstName() {
		List<Notification> notifications = new ArrayList<>(List.of(new NotificationExam(user, category),
				new NotificationAccount(user), new NotificationFirstName(user, "newName"),
				new NotificationQuestion(user, question, "Bad question"), new NotificationFirstName(user, "newName"),
				new NotificationFirstName(user, "newName")));

		List<Notification> filteredNotifications = notificationService.filterFirstName(notifications);

		long countNullName = filteredNotifications.stream()
				.filter(notification -> (notification instanceof NotificationFirstName)
						&& ((NotificationFirstName) notification).getName() == null)
				.count();

		assertEquals(0, countNullName);
		assertEquals(3, filteredNotifications.size());
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_filterLastName() {
		List<Notification> notifications = new ArrayList<>(List.of(new NotificationExam(user, category),
				new NotificationAccount(user), new NotificationLastName(user, "newName"),
				new NotificationQuestion(user, question, "Bad question"), new NotificationLastName(user, "newName"),
				new NotificationLastName(user, "newName")));

		List<Notification> filteredNotifications = notificationService.filterLastName(notifications);

		long countNullName = filteredNotifications.stream()
				.filter(notification -> (notification instanceof NotificationLastName)
						&& ((NotificationLastName) notification).getName() == null)
				.count();

		assertEquals(0, countNullName);
		assertEquals(3, filteredNotifications.size());
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_askForExamAccess() {
		Category category = new Category("testCategory");
		note = new NotificationExam(user, category);

		when(notificationRepository.save(note)).thenReturn(note);

		ConfirmationResponseDTO response = notificationService.askForExamAccess(user, category);

		verify(notificationRepository, times(1)).save(note);
		assertEquals("Request for exam access has been sent.", response.getConfirmationMessage());
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_askForExamAccess_alreadyHaveAnAccess() {
		Category category = new Category("testCategory");
		note = new NotificationExam(user, category);
		ExamAccess access = new ExamAccess(user);
		access.addToCategories(category);
		when(examAccessRepository.findByUser(user)).thenReturn(Optional.of(access));

		ConfirmationResponseDTO response = notificationService.askForExamAccess(user, category);

		verify(notificationRepository, times(0)).save(note);
		assertEquals("You have access to this exam.", response.getConfirmationMessage());
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_deleteAccount() {

		note = new NotificationAccount(user);

		when(notificationRepository.save(note)).thenReturn(note);

		notificationService.deleteAccount(user);

		verify(notificationRepository, times(1)).save(note);
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_reportQuestion() {
		ReportQuestionRequestDTO request = new ReportQuestionRequestDTO();
		request.setQuestionId(1);
		request.setComment("Some message.");

		when(questionService.findById(request.getQuestionId())).thenReturn(question);

		note = new NotificationQuestion(user, question, request.getComment());

		when(notificationRepository.save(note)).thenReturn(note);

		ConfirmationResponseDTO response = notificationService.reportQuestion(user, request);

		verify(questionService, times(1)).findById(request.getQuestionId());
		verify(notificationRepository, times(1)).save(note);
		assertEquals("Question has been reported.", response.getConfirmationMessage());
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_notificationDetails_examAccess() {
		NotificationRequestDTO request = new NotificationRequestDTO();
		request.setId(1);

		note = new NotificationExam(user, category);
		when(notificationRepository.findById(request.getId())).thenReturn(Optional.of(note));

		NotificationDetailsResponseDTO response = notificationService.notificationDetails(request, user);

		verify(notificationRepository, times(1)).save(note);
		assertEquals(false, response.isHandling());
		assertEquals("Exam_access", response.getAction());

	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_notificationDetails_deleteAccount() {
		NotificationRequestDTO request = new NotificationRequestDTO();
		request.setId(1);

		note = new NotificationAccount(user);
		when(notificationRepository.findById(request.getId())).thenReturn(Optional.of(note));

		NotificationDetailsResponseDTO response = notificationService.notificationDetails(request, user);

		verify(notificationRepository, times(1)).save(note);
		assertEquals(false, response.isHandling());
		assertEquals("Delete_account", response.getAction());
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_notificationDetails_questionReport() {
		NotificationRequestDTO request = new NotificationRequestDTO();
		request.setId(1);

		note = new NotificationQuestion(user, question,
				"Java is better than Python, C++, C# and JavaScript all together");
		when(notificationRepository.findById(request.getId())).thenReturn(Optional.of(note));

		NotificationDetailsResponseDTO response = notificationService.notificationDetails(request, user);

		verify(notificationRepository, times(1)).save(note);
		assertEquals(false, response.isHandling());
		assertEquals("Reporting_question", response.getAction());

	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_notificationDetails_FirstName() {
		NotificationRequestDTO request = new NotificationRequestDTO();
		request.setId(1);

		note = new NotificationFirstName(user, "newName");
		when(notificationRepository.findById(request.getId())).thenReturn(Optional.of(note));

		NotificationDetailsResponseDTO response = notificationService.notificationDetails(request, user);

		verify(notificationRepository, times(1)).save(note);
		assertEquals(false, response.isHandling());
		assertEquals("First_name", response.getAction());
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_notificationDetails_lastName() {
		NotificationRequestDTO request = new NotificationRequestDTO();
		request.setId(1);

		note = new NotificationLastName(user, "newName");
		when(notificationRepository.findById(request.getId())).thenReturn(Optional.of(note));

		NotificationDetailsResponseDTO response = notificationService.notificationDetails(request, user);

		verify(notificationRepository, times(1)).save(note);
		assertEquals(false, response.isHandling());
		assertEquals("Last_name", response.getAction());
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_handle() {
		// Arrange
		int notificationId = 123;
		note = new NotificationExam(user, category);
		when(notificationRepository.findById(notificationId)).thenReturn(Optional.of(note));

		ConfirmationResponseDTO response = notificationService.handle(notificationId, user);

		verify(notificationRepository, times(1)).save(note);
		assertEquals(user, note.getHandleBy());
		assertEquals("Notification handling is in process now.", response.getConfirmationMessage());
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_delete() {
		int notificationId = 1;
		note = new Notification();
		when(notificationRepository.findById(notificationId)).thenReturn(Optional.of(note));

		ConfirmationResponseDTO response = notificationService.delete(notificationId);

		verify(notificationRepository, times(1)).delete(note);
		assertEquals("Notification has been rejected.", response.getConfirmationMessage());
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_approved() {
		int notificationId = 123;
		note = new NotificationExam(user, category);
		when(notificationRepository.findById(notificationId)).thenReturn(Optional.of(note));

		ConfirmationResponseDTO response = notificationService.approved(notificationId);

		verify(notificationRepository, times(1)).delete(note);
		assertEquals("Notification has been approved.", response.getConfirmationMessage());

	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_removeAdmin() {
		User admin = new User();
		note.addToAdmins(admin);
		Notification note2 = new Notification();
		note2.addToAdmins(user);
		notifications.add(note2);

		List<Notification> result = notificationService.removeAdmin(notifications, admin);

		List<Notification> expectedNotifications = result.stream().filter(note -> !note.getAdmins().contains(admin))
				.collect(Collectors.toList());

		assertEquals(1, result.size());
		assertEquals(expectedNotifications, result);
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_acceptQuestionReporting() {
		int questionId = 1;
		int noteId = 2;

		question.setId(questionId);

		QuestionResponseDTO expectedDTO = new QuestionResponseDTO();
		expectedDTO.setNoteId(noteId);

		when(questionService.findById(questionId)).thenReturn(question);
		when(dTOService.questionEntityToResponse(question)).thenReturn(expectedDTO);

		QuestionResponseDTO resultDTO = notificationService.acceptQuestionReporting(questionId, noteId);

		assertEquals(expectedDTO, resultDTO);

		verify(questionService).findById(questionId);
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_examAccessAccept_SuccessfullyGrantsExamAccess() {

		int noteId = 1;
		Notification notificationExam = new NotificationExam(user, category);

		when(notificationRepository.findById(noteId)).thenReturn(Optional.of(notificationExam));

		ConfirmationResponseDTO response = notificationService.examAccessAccept(noteId);

		assertEquals("Access to exams has been granted.", response.getConfirmationMessage());
		verify(mailService, times(1)).examAccess(user, "Java");
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_updateQuestion_deleteNotification_returnTrue() {
		int noteId = 1;

		NotificationQuestion notification = new NotificationQuestion(user, question, "comment");

		when(notificationRepository.findById(noteId)).thenReturn(Optional.of(notification));

		boolean actual = notificationService.updateQuestion_deleteNotification(noteId);
		verify(mailService).questionReport(user, question.getCategory().getCategoryName());
		verify(notificationRepository).delete(notification);
		assertEquals(true, actual);
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_updateQuestion_deleteNotification_returnFalse() {
		int noteId = 1;

		when(notificationRepository.findById(noteId)).thenReturn(Optional.empty());

		boolean actual = notificationService.updateQuestion_deleteNotification(noteId);

		assertEquals(false, actual);
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_deleteAccount_deleteNotification_returnTrue() {
		int noteId = 2;

		Notification notification = new NotificationAccount(user);

		when(notificationRepository.findById(noteId)).thenReturn(Optional.of(notification));

		boolean actual = notificationService.deleteAccount_deleteNotification(noteId);
		verify(mailService).deleteAccount(user);
		verify(notificationRepository).delete(notification);
		assertEquals(true, actual);

	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_deleteAccount_deleteNotification_returnFalse() {
		int noteId = 2;

		when(notificationRepository.findById(noteId)).thenReturn(Optional.empty());

		boolean actual = notificationService.deleteAccount_deleteNotification(noteId);

		assertEquals(false, actual);

	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_giveAccessToUser_SuccessfullyGrantsAccess() {

		ExamAccess examAccess = new ExamAccess(user);

		when(examAccessRepository.findByUser(user)).thenReturn(Optional.of(examAccess));

		ConfirmationResponseDTO response = notificationService.giveAccessToUser(user, category);

		assertEquals("Access to exam has been granted.", response.getConfirmationMessage());
		verify(examAccessRepository, times(1)).save(examAccess);
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_changeFirstName_notificationRequest() {
		notificationService.changeFirstName_notificationRequest(user, "newName");
		verify(notificationRepository).save(any());
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_changeLastName_notificationRequest() {
		notificationService.changeLastName_notificationRequest(user, "newName");
		verify(notificationRepository).save(any());
	}

	// ---------------------------------------------------------------------------------------------

	   @Test
	    public void test_findMyExamAccessRequest() {
	    
	        Notification notification1 = new Notification();
	        NotificationExam notification2 = new NotificationExam();
	        Notification notification3 = new Notification();
	        notifications.add(notification1);
	        notifications.add(notification2);
	        notifications.add(notification3);

	        Mockito.when(notificationRepository.findByUser(user)).thenReturn(notifications);

	        List<NotificationExam> result = notificationService.findMyExamAccessRequest(user);

	        assertEquals(1, result.size());
	        assertEquals(notification2, result.get(0));
	    }
	   
}
