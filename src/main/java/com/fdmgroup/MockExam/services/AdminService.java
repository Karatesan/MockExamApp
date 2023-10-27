package com.fdmgroup.MockExam.services;

import java.util.List;

import com.fdmgroup.MockExam.dto.AccountIdRequestDTO;
import com.fdmgroup.MockExam.dto.AccountIdRoleRequestDTO;
import com.fdmgroup.MockExam.dto.ChangeNameResponseDTO;
import com.fdmgroup.MockExam.dto.ConfirmationResponseDTO;
import com.fdmgroup.MockExam.dto.IdFoundInSearchDTO;
import com.fdmgroup.MockExam.dto.NotificationRequestDTO;
import com.fdmgroup.MockExam.dto.NotificationResponseDTO;
import com.fdmgroup.MockExam.dto.QuestionRequestDTO;
import com.fdmgroup.MockExam.dto.QuestionResponseDTO;
import com.fdmgroup.MockExam.dto.SearchQuestionRequestDTO;
import com.fdmgroup.MockExam.dto.SearchQuestionResponseDTO;
import com.fdmgroup.MockExam.dto.SearchUserRequestDTO;
import com.fdmgroup.MockExam.dto.SearchUserResponseDTO;
import com.fdmgroup.MockExam.dto.UserFoundInSearchDTO;
import com.fdmgroup.MockExam.dto.QuestionFoundInSearchDTO;
import com.fdmgroup.MockExam.model.Question;
import com.fdmgroup.MockExam.model.User;

public interface AdminService {

	/**
	 * Blocks a user account based on the provided AccountIdRequestDTO and returns a confirmation response.
	 * @param request The AccountIdRequestDTO containing the user's account ID to be blocked.
	 * @return A ConfirmationResponseDTO indicating the success of the account blocking operation.
	 */
	ConfirmationResponseDTO blockAccount(AccountIdRequestDTO request, boolean blockIt);

	/**
	 * TO DO:
	 * @param request
	 * @return
	 */
	ConfirmationResponseDTO deleteAccount(int id);
	
    /**
     * Adds a new question to the repository.
     * @param question The question request DTO containing question details.
     * @return A question response DTO for the added question.
     */
	QuestionResponseDTO addQuestion(QuestionRequestDTO question);

	ConfirmationResponseDTO changeAccountRole(AccountIdRoleRequestDTO request);

	QuestionResponseDTO updateQuestion(Integer questionId, QuestionRequestDTO newQuestion);

	  /**
     * Deletes a question by its ID, including its associated image.
     * @param id The ID of the question to delete.
     */
	ConfirmationResponseDTO deleteQuestion(Integer id);
	
//	List<Question> findAllLockedQuestions();
	
	List<User> findAllUsers();

	SearchQuestionResponseDTO searchQuestion(SearchQuestionRequestDTO request);

	SearchUserResponseDTO searchUser(SearchUserRequestDTO request);

	ConfirmationResponseDTO giveTagToUser(User user, String tagName);
	ChangeNameResponseDTO changeFirstNameAccept(int id);

	ChangeNameResponseDTO changeLastNameAccept(int id);
//
//
//	
//
//
//	Question findById(Integer id);


}
