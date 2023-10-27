package com.fdmgroup.MockExam.services;

import java.util.List;

import com.fdmgroup.MockExam.dto.ConfirmationResponseDTO;
import com.fdmgroup.MockExam.model.Question;
import com.fdmgroup.MockExam.model.Question.Level;
import com.fdmgroup.MockExam.model.Tag;
import com.fdmgroup.MockExam.model.User;

/**
 * Interface to define the contract for the QuestionService. Provides methods
 * for managing questions.
 */
public interface QuestionService {

	/**
	 * Retrieves a list of all questions.
	 * 
	 * @return A list containing all questions in the repository.
	 */
	List<Question> findAllQuestions();

	/**
	 * Finds all questions in a specific category.
	 * 
	 * @param category The name of the category to search for.
	 * @return A list of questions in the specified category.
	 */
	List<Question> findAllQuestionsInCategory(String category);

	/**
	 * Finds a question by its ID.
	 * 
	 * @param id The ID of the question to retrieve.
	 * @return The question with the specified ID.
	 */
	Question findById(Integer id);

	/**
	 * Selects a random subset of questions from a list.
	 * 
	 * @param questions         The list of questions to select from.
	 * @param numberOfQuestions The number of random questions to select.
	 * @return A list containing the randomly selected questions.
	 */
	List<Question> findRandomQuestions(List<Question> questions, int numberOfQuestions);

	/**
	 * Filters a list of questions by their level.
	 * 
	 * @param questions The list of questions to filter.
	 * @param level     The level to filter by.
	 * @return A filtered list of questions with the specified level.
	 */
	List<Question> filterQuestionsByLevel(List<Question> questions, Level level);

//	List<Question> findByQuestionContentContainsIgnoreCase(String searchText);
//	ConfirmationResponseDTO flagQuestion(User user, int id, String comment);
	
	List<Question> findByQuestionContentContainsIgnoreCaseAndLockedIsFalse(String searchText);

	List<Question> findByAnswers(String searchText);

	List<Question> findByFeedback(String searchText);

	List<Question> findByTags(String searchText);

	List<Question> findByTagsContains(Tag tag);

	List<Question> findByQuestionContent(String string);

}
