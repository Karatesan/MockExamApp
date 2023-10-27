package com.fdmgroup.MockExam.services;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.fdmgroup.MockExam.dto.ConfirmationResponseDTO;
import com.fdmgroup.MockExam.exceptions.InvalidCategoryException;
import com.fdmgroup.MockExam.model.Category;
import com.fdmgroup.MockExam.model.Notification;
import com.fdmgroup.MockExam.model.NotificationQuestion;
import com.fdmgroup.MockExam.model.Question;
import com.fdmgroup.MockExam.model.Question.Level;
import com.fdmgroup.MockExam.model.Tag;
import com.fdmgroup.MockExam.model.User;
import com.fdmgroup.MockExam.repositories.NotificationRepository;
import com.fdmgroup.MockExam.repositories.QuestionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

/**
 * The class implements the QuestionService interface. Provides methods to find,
 * add, delete and filter questions.
 */
@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

	private final QuestionRepository questionRepository;
	private final CategoryService categoryService;

	@Override
	public List<Question> findAllQuestions() {
		return questionRepository.findAll();
	}

	// ----------------------------------------------------------------------------------

	@Override
	public Question findById(Integer id) {
		Optional<Question> questionOptional = questionRepository.findById(id);
		if (!questionOptional.isPresent())
			throw new EntityNotFoundException();
		return questionOptional.get();
	}

	// ----------------------------------------------------------------------------------

	@Override
	public List<Question> findAllQuestionsInCategory(String category) {
		Category cat = categoryService.findByName(category);
		if (cat == null)
			throw new InvalidCategoryException("Requested catogory does not exist");

		List<Question> questionsFound = questionRepository.findByCategory(cat);
		questionsFound.removeIf(question -> question.getLocked());
		return questionsFound;
	}

	// ----------------------------------------------------------------------------------

	@Override
	public List<Question> filterQuestionsByLevel(List<Question> questions, Level level) {
		return questions.stream().filter(question -> question.getLevel().equals(level)).collect(Collectors.toList());
	}

	// ----------------------------------------------------------------------------------

	@Override
	public List<Question> findRandomQuestions(List<Question> questions, int numberOfQuestions) {
		Collections.shuffle(questions);
		int examSize = Math.min(numberOfQuestions, questions.size());
		return questions.subList(0, examSize);
	}


	
	@Override
	public List<Question> findByQuestionContentContainsIgnoreCaseAndLockedIsFalse(String searchText){
		return	questionRepository.findByQuestionContentContainsIgnoreCaseAndLockedIsFalse(searchText);
		
	}
	
	@Override
	public List<Question> findByAnswers(String searchText){
		return questionRepository.findByAnswers(searchText);
		
	}
	
	@Override
	public List<Question> findByFeedback( String searchText){
		return questionRepository.findByFeedback(searchText);
		
	}
	
	@Override
	public List<Question> findByTags(String searchText){
		return questionRepository.findByTags(searchText);		
		
	}

	@Override
	public List<Question> findByTagsContains(Tag tag) {
		return questionRepository.findByTagsContains(tag);		

	}

//	@Override
//	public List<Question> findByQuestionContentContainsIgnoreCase(String searchText) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public ConfirmationResponseDTO flagQuestion(User user, int id, String comment) {
//		// TODO Auto-generated method stub
//		return null;
//	}
@Override
public List<Question> findByQuestionContent(String string){
	return questionRepository.findByQuestionContent(string);		

}
}
