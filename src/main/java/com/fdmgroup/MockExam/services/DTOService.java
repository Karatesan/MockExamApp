package com.fdmgroup.MockExam.services;

import java.util.List;

import com.fdmgroup.MockExam.dto.AnswerRequestDTO;
import com.fdmgroup.MockExam.dto.AnswerResponseDTO;
import com.fdmgroup.MockExam.dto.CreateExamResponseDTO;
import com.fdmgroup.MockExam.dto.ExamRequestDTO;
import com.fdmgroup.MockExam.dto.ExamResponseDTO;
import com.fdmgroup.MockExam.dto.NotificationResponseDTO;
import com.fdmgroup.MockExam.dto.QuestionRequestDTO;
import com.fdmgroup.MockExam.dto.QuestionResponseDTO;
import com.fdmgroup.MockExam.model.Answer;
import com.fdmgroup.MockExam.model.Exam;
import com.fdmgroup.MockExam.model.Notification;
import com.fdmgroup.MockExam.model.Question;
import com.fdmgroup.MockExam.model.Question.Level;
import com.fdmgroup.MockExam.model.Role;
import com.fdmgroup.MockExam.model.Tag;

/**
 * Interface to define the contract for the DTOService. Provides methods for
 * converting DTOs to entities and vice versa.
 */
public interface DTOService {

	/**
	 * Converts a question entity to a question response DTO.
	 * 
	 * @param question The question entity to convert.
	 * @return The corresponding question response DTO.
	 */
	QuestionResponseDTO questionEntityToResponse(Question question);

	/**
	 * Converts a question request DTO to a question entity.
	 * 
	 * @param questionDTO The question request DTO to convert.
	 * @return The corresponding question entity.
	 */
	Question questionRequestToEntity(QuestionRequestDTO questionDTO);

	/**
	 * Converts an answer entity to an answer response DTO.
	 * 
	 * @param answer The answer entity to convert.
	 * @return The corresponding answer response DTO.
	 */
	AnswerResponseDTO answerEntityToResponse(Answer answer);

	/**
	 * Converts an answer request DTO to an answer entity.
	 * 
	 * @param answerDTO The answer request DTO to convert.
	 * @return The corresponding answer entity.
	 */
	Answer answerRequestToEntity(AnswerRequestDTO answerDTO);

	/**
	 * Converts an exam entity to an exam response DTO for completed exams.
	 * 
	 * @param exam The exam entity to convert.
	 * @return The corresponding exam response DTO for completed exams.
	 */
	ExamResponseDTO examEntityToResponse_complete(Exam exam);

	/**
	 * Converts an exam request DTO to an exam entity for completed exams.
	 * 
	 * @param examRequestDTO The exam request DTO to convert.
	 * @return The corresponding exam entity for completed exams.
	 */
	Exam examRequestToEntity_complete(ExamRequestDTO examDTO);

	/**
	 * Converts a list of questions to a create exam response DTO.
	 * 
	 * @param questions The list of questions to convert.
	 * @return The corresponding create exam response DTO.
	 */
	CreateExamResponseDTO examEntityToResponse_create(List<Question> questions);

	/**
	 * Finds a question level by its name.
	 * 
	 * @param levelName The name of the level to find.
	 * @return The corresponding question level.
	 */
	Level findLevelByName(String levelName);

	Role findRoleByName(String roleName);

	Tag findOrCreateTag(String tagName);

	List<String> convertListTagsToString(List<Tag> tags);

}
