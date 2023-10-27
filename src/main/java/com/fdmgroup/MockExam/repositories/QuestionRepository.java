package com.fdmgroup.MockExam.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fdmgroup.MockExam.model.Category;
import com.fdmgroup.MockExam.model.Question;
import com.fdmgroup.MockExam.model.Question.Level;
import com.fdmgroup.MockExam.model.Tag;

/**
 * Repository interface for managing Question entities in the database. Extends
 * JpaRepository to inherit basic CRUD operations.
 */
@Repository
public interface QuestionRepository extends JpaRepository<Question, Integer> {

//	Question save(Question question);

	Optional<Question> findById(int id);

	/**
	 * Find questions by their associated category.
	 * 
	 * @param cat The Category object representing the category of questions to
	 *            retrieve.
	 * @return A list of Question objects associated with the provided category.
	 */
	List<Question> findByCategory(Category cat);

	List<Question> findByLevel(Level level);

	/**
	 * Find a specified number of random questions from the database.
	 * 
	 * @param numberOfQuestions The number of random questions to retrieve.
	 * @return A list of random Question objects limited by the provided number.
	 */
	@Query(value = "Select * FROM exam ORDER BY RAND() LIMIT :numberOfQuestions", nativeQuery = true)
	List<Question> findRandomQuestions(@Param("numberOfQuestions") int numberOfQuestions);

	List<Question> findByLocked(boolean bool);

//	@Query(value = "SELECT DISTINCT q.* FROM question q " + "LEFT JOIN question_tags qt ON q.id = qt.question_id "
//			+ "LEFT JOIN tag t ON qt.tags_id = t.id " + 
//			"WHERE"
//			+ " LOWER(q.question_content) LIKE LOWER(CONCAT('%', :question_content, '%')) "
//			+ "OR (:answers = '%' OR LOWER(q.answers) LIKE LOWER(CONCAT('%', :answers, '%')))"
//			+ "OR (:feedback = '%' OR LOWER(q.feedback) LIKE LOWER(CONCAT('%', :feedback, '%'))) "
//			+ "OR LOWER(t.name) LIKE LOWER(CONCAT('%', :tag_name, '%'))"
//			, nativeQuery = true)
//	List<Question> findByQuestionContentOrAnswersOrFeedbackOrTagName(@Param("question_content") String question_content,
//			@Param("answers") String answers, @Param("feedback") String feedback, @Param("tag_name") String tag_name);

	List<Question> findByQuestionContentContainsIgnoreCaseAndLockedIsFalse(String string);
	
	List<Question> findByQuestionContentContainsIgnoreCase(String string);
	List<Question> findByQuestionContent(String string);

	@Query(value = "SELECT q.* FROM question q WHERE"
			+ " LOWER(q.answers) LIKE LOWER(CONCAT('%', :string, '%'))" + " AND q.locked = false", nativeQuery = true)
	List<Question> findByAnswers(String string);

	@Query(value = "SELECT q.* FROM question q WHERE"
			+ " LOWER(q.feedback) LIKE LOWER(CONCAT('%', :string, '%'))" + " AND q.locked = false", nativeQuery = true)
	List<Question> findByFeedback(String string);

	@Query(value = "SELECT q.* FROM question q "
			+ "LEFT JOIN question_tags qt ON q.id = qt.question_id "
			+ "LEFT JOIN tag t ON qt.tags_id = t.id "
			+ "WHERE LOWER(t.name) LIKE LOWER(CONCAT('%', :string, '%'))" + " AND q.locked = false", nativeQuery = true)
	List<Question> findByTags(String string);
	
	List<Question> findByTagsContains(Tag tag);
}
