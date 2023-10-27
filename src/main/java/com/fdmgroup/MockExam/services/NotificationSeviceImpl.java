package com.fdmgroup.MockExam.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.fdmgroup.MockExam.dto.CategoryResponseDTO;
import com.fdmgroup.MockExam.dto.ConfirmationResponseDTO;
import com.fdmgroup.MockExam.dto.ListOfUsersIdDTO;
import com.fdmgroup.MockExam.dto.NotificationDetailsResponseDTO;
import com.fdmgroup.MockExam.dto.NotificationListResponseDTO;
import com.fdmgroup.MockExam.dto.NotificationRequestDTO;
import com.fdmgroup.MockExam.dto.NotificationResponseDTO;
import com.fdmgroup.MockExam.dto.QuestionResponseDTO;
import com.fdmgroup.MockExam.dto.ReportQuestionRequestDTO;
import com.fdmgroup.MockExam.dto.UserFoundInSearchDTO;
import com.fdmgroup.MockExam.exceptions.AdminAccesException;
import com.fdmgroup.MockExam.exceptions.UserAccesException;
import com.fdmgroup.MockExam.model.Category;
import com.fdmgroup.MockExam.model.ExamAccess;
import com.fdmgroup.MockExam.model.Notification;
import com.fdmgroup.MockExam.model.NotificationAccount;
import com.fdmgroup.MockExam.model.NotificationExam;
import com.fdmgroup.MockExam.model.NotificationFirstName;
import com.fdmgroup.MockExam.model.NotificationLastName;
import com.fdmgroup.MockExam.model.NotificationQuestion;
import com.fdmgroup.MockExam.model.User;
import com.fdmgroup.MockExam.repositories.ExamAccessRepository;
import com.fdmgroup.MockExam.repositories.NotificationRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationSeviceImpl implements NotificationService {
	private final NotificationRepository notificationRepository;
	private final QuestionService questionService;
	private final DTOService dTOService;
	private final SearchingSortService searchingSortService;
	private final ExamAccessRepository examAccessRepository;
	private final MailService mailService;

	@Override
	public Notification findById(int id) {
		return notificationRepository.findById(id).orElseThrow();
	}

	// ---------------------------------------------------------------------------------------------

	@Override
	public NotificationListResponseDTO seeNotification(NotificationRequestDTO request, User admin) {
		List<Notification> notes = notificationRepository.findAll();
		notes = removeAdmin(notes, admin);
		notes.addAll(addHandleBy(admin));
		switch (request.getAction()) {
		case "Delete_account":
			notes = filterDeleteAccount(notes);
			break;
		case "Reporting_question":
			notes = filterReportQuestion(notes);
			break;
		case "Exam_access":
			notes = filterAccessExam(notes);
			break;
		case "First_name":
			notes = filterFirstName(notes);
			break;
		case "Last_name":
			notes = filterLastName(notes);
			break;
		case "All":
			break;
		}

		if (notes.size() == 0)
			throw new AdminAccesException("There is no notifications fulfiling the requiments.");

		List<NotificationResponseDTO> listWithMessages = getMessageForNotification(notes);
		if (listWithMessages.size() > 1)
			listWithMessages = searchingSortService.sortList(listWithMessages);

		if (listWithMessages.size() > 5)
			listWithMessages = searchingSortService.findSearchedItemsOnPage(listWithMessages, request.getPage());
		return new NotificationListResponseDTO(notes.size(), listWithMessages);
	}

	@Override
	public List<Notification> addHandleBy(User admin) {
		return notificationRepository.findByHandleBy(admin);
	}

	// ---------------------------------------------------------------------------------------------
	@Override
	public List<Notification> removeAdmin(List<Notification> list, User admin) {
		return list.stream().filter(note -> !note.getAdmins().contains(admin)).collect(Collectors.toList());
	}

	@Override
	public List<NotificationResponseDTO> getMessageForNotification(List<Notification> notes) {
		List<NotificationResponseDTO> returning = new ArrayList<>();
		for (Notification note : notes) {
			Category category = new Category();
			User user = note.getUser();
			String userString = " " + user.getFirstName() + " " + user.getLastName() + " (" + user.getEmail() + ") ";
			if (note instanceof NotificationExam) {

				NotificationExam notificationExam = (NotificationExam) note;
				category = notificationExam.getCategory();
				String string = "User:" + userString + "wants to get access to exam in category: "
						+ category.getCategoryName();

				returning.add(new NotificationResponseDTO(note.getId(), string));

			} else if (note instanceof NotificationQuestion) {

				NotificationQuestion notificationQuestion = (NotificationQuestion) note;
				category = notificationQuestion.getQuestion().getCategory();

				String string = "User:" + userString + "reported a question from Exam: " + category.getCategoryName();

				returning.add(new NotificationResponseDTO(note.getId(), string));

			} else if (note instanceof NotificationAccount) {

				String string = "User:" + userString + "wants to delete account.";

				returning.add(new NotificationResponseDTO(note.getId(), string));

			} else if (note instanceof NotificationFirstName) {
				NotificationFirstName nameNote = (NotificationFirstName) note;

				String string = "User:" + userString + "wants to change first name to: " + nameNote.getName();

				returning.add(new NotificationResponseDTO(note.getId(), string));

			} else if (note instanceof NotificationLastName) {
				NotificationLastName nameNote = (NotificationLastName) note;

				String string = "User:" + userString + "wants to change last name to: " + nameNote.getName();

				returning.add(new NotificationResponseDTO(note.getId(), string));

			}
		}

		return returning;
	}

	// ---------------------------------------------------------------------------------------------

	@Override
	public List<Notification> filterDeleteAccount(List<Notification> notes) {
		notes.removeIf(note -> !(note instanceof NotificationAccount));
		return notes;
	}

	// ---------------------------------------------------------------------------------------------

	@Override
	public List<Notification> filterReportQuestion(List<Notification> notes) {
		notes.removeIf(note -> !(note instanceof NotificationQuestion));
		return notes;
	}

	// ---------------------------------------------------------------------------------------------

	@Override
	public List<Notification> filterAccessExam(List<Notification> notes) {
		notes.removeIf(note -> !(note instanceof NotificationExam));
		return notes;
	}

	@Override
	public List<Notification> filterFirstName(List<Notification> notes) {
		notes.removeIf(note -> !(note instanceof NotificationFirstName));
		return notes;
	}

	@Override
	public List<Notification> filterLastName(List<Notification> notes) {
		notes.removeIf(note -> !(note instanceof NotificationLastName));
		return notes;
	}

	// ---------------------------------------------------------------------------------------------

	@Override
	public ConfirmationResponseDTO askForExamAccess(User user, Category category) {
		if (!category.equals(null)) {
			Optional<ExamAccess> access = examAccessRepository.findByUser(user);
			if (access.isPresent()) {
				if (access.get().getCategories().contains(category))
					throw new UserAccesException("You already have access to this exam.");
			}
			List<NotificationExam> myNotes = findMyExamAccessRequest(user);
			for (NotificationExam note : myNotes) {

				if (category.equals(note.getCategory()))
					throw new UserAccesException("You have already requested access to that exam...");
			}

			NotificationExam notificationExam = new NotificationExam(user, category);

			notificationRepository.save(notificationExam);
			return ConfirmationResponseDTO.builder().confirmationMessage("Request for exam access has been sent.")
					.build();
		} else
			throw new UserAccesException("Wrong category name.");

	}

	// ---------------------------------------------------------------------------------------------

	@Override
	public List<NotificationExam> findMyExamAccessRequest(User user) {
		List<Notification> list = notificationRepository.findByUser(user);
		 List<NotificationExam> returning = list.stream()
			        .filter(note -> note instanceof NotificationExam)
			        .map(note -> (NotificationExam) note)
			        .collect(Collectors.toList());
		return returning;
	}

	// ---------------------------------------------------------------------------------------------

	@Override
	public void deleteAccount(User user) {
		NotificationAccount note = new NotificationAccount(user);
		notificationRepository.save(note);
	}

	// ---------------------------------------------------------------------------------------------

	@Override
	public ConfirmationResponseDTO reportQuestion(User user, ReportQuestionRequestDTO request) {
		NotificationQuestion note = new NotificationQuestion(user, questionService.findById(request.getQuestionId()),
				request.getComment());
		notificationRepository.save(note);
		return ConfirmationResponseDTO.builder().confirmationMessage("Question has been reported.").build();
	}

	// ---------------------------------------------------------------------------------------------

	@Override
	public NotificationDetailsResponseDTO notificationDetails(NotificationRequestDTO request, User admin) {

		NotificationDetailsResponseDTO returning = new NotificationDetailsResponseDTO();
		int id = request.getId();
		Notification note = findById(id);
		note.addToAdmins(admin);
		notificationRepository.save(note);
		User user = note.getUser();
		UserFoundInSearchDTO userDTO = new UserFoundInSearchDTO(user.getId(), user.getFirstName(), user.getLastName(),
				user.getEmail(), user.getLocked(), user.getVerified(),
				dTOService.convertListTagsToString(user.getTags()));

		if (note instanceof NotificationExam) {
			NotificationExam notificationExam = (NotificationExam) note;
			returning = new NotificationDetailsResponseDTO("Exam_access", id, userDTO,
					new CategoryResponseDTO(notificationExam.getCategory().getCategoryName()));
		} else if (note instanceof NotificationQuestion) {
			NotificationQuestion notificationQuestion = (NotificationQuestion) note;
			returning = new NotificationDetailsResponseDTO("Reporting_question", id, userDTO,
					dTOService.questionEntityToResponse(notificationQuestion.getQuestion()),
					notificationQuestion.getComment());
		} else if (note instanceof NotificationFirstName) {
			NotificationFirstName notificationName = (NotificationFirstName) note;
			returning = new NotificationDetailsResponseDTO("First_name", id, userDTO, notificationName.getName());
		} else if (note instanceof NotificationLastName) {
			NotificationLastName notificationName = (NotificationLastName) note;
			returning = new NotificationDetailsResponseDTO("Last_name", id, userDTO, notificationName.getName());
		} else
			returning = new NotificationDetailsResponseDTO("Delete_account", id, userDTO);

		return returning;
	}

	// ---------------------------------------------------------------------------------------------

	@Override
	public ConfirmationResponseDTO handle(int id, User admin) {
		Notification note = findById(id);
		note.setHandleBy(admin);
		notificationRepository.save(note);
		return ConfirmationResponseDTO.builder().confirmationMessage("Notification handling is in process now.")
				.build();
	}

	// ---------------------------------------------------------------------------------------------

	@Override
	public ConfirmationResponseDTO delete(int id) {
		Notification note = findById(id);
		notificationRepository.delete(note);
		return ConfirmationResponseDTO.builder().confirmationMessage("Notification has been rejected.").build();
	}

	// ---------------------------------------------------------------------------------------------

	@Override
	public ConfirmationResponseDTO approved(int id) {
		delete(id);
		return ConfirmationResponseDTO.builder().confirmationMessage("Notification has been approved.").build();
	}

	// ---------------------------------------------------------------------------------------------

	@Override
	public ConfirmationResponseDTO examAccessAccept(int id) {

		Notification note = findById(id);
		User user = note.getUser();
		NotificationExam notificationExam = (NotificationExam) note;
		Category category = notificationExam.getCategory();

		giveAccessToUser(user, category);

		delete(id);
		mailService.sendEmail(mailService.examAccess(user, category.getCategoryName()));

		return ConfirmationResponseDTO.builder().confirmationMessage("Access to exams has been granted.").build();
	}

	public void examAccessGiveAccess(User user, Category category) {

	}

	// ---------------------------------------------------------------------------------------------

	@Override
	public QuestionResponseDTO acceptQuestionReporting(int questionId, int noteId) {
		QuestionResponseDTO questionDTO = dTOService.questionEntityToResponse(questionService.findById(questionId));
		questionDTO.setNoteId(noteId);
		return questionDTO;
	}

	@Override
	public boolean updateQuestion_deleteNotification(Integer noteId) {
		try {
			Notification note = findById(noteId);
			NotificationQuestion notification = (NotificationQuestion) note;

			mailService.sendEmail(mailService.questionReport(notification.getUser(),
					notification.getQuestion().getCategory().getCategoryName()));
			delete(noteId);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public boolean deleteAccount_deleteNotification(int noteId) {
		try {
			Notification note = findById(noteId);
			mailService.sendEmail(mailService.deleteAccount(note.getUser()));
			delete(noteId);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public ConfirmationResponseDTO giveAccessToUser(User user, Category category) {
		ExamAccess access = new ExamAccess();
		if (examAccessRepository.findByUser(user).isPresent())
			access = examAccessRepository.findByUser(user).get();
		else
			access = new ExamAccess(user);

		if (!access.getCategories().contains(category))
			access.addToCategories(category);

		examAccessRepository.save(access);

		return ConfirmationResponseDTO.builder().confirmationMessage("Access to exam has been granted.").build();
	}

	@Override
	public Notification changeFirstName_notificationRequest(User user, String firstname) {
		return notificationRepository.save(new NotificationFirstName(user, firstname));

	}

	@Override
	public Notification changeLastName_notificationRequest(User user, String lastname) {
		return notificationRepository.save(new NotificationLastName(user, lastname));
	}

}
