package com.fdmgroup.MockExam.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fdmgroup.MockExam.dto.CreateExamRequestDTO;

import com.fdmgroup.MockExam.model.Exam;
import com.fdmgroup.MockExam.model.Question;
import com.fdmgroup.MockExam.model.User;
import com.fdmgroup.MockExam.repositories.ExamRepository;

/**
 * The class implements the ExamService interface.
 * Provides methods to generate an exam for user and save it after completion.
 */
@Service
public class ExamServiceImpl implements ExamService {

	private final ExamRepository examRepository;
	private final QuestionService questionService;
	private final DTOService dTOService;
	private final JwtService jwtService;
	private final UserService userService;

	/**
     * Constructs a new ExamServiceImpl instance.
     * @param examRepository The repository for managing exams.
     * @param questionService The service for managing questions.
     * @param dTOService The service for handling DTOs.
     * @param jwtService The service for JWT token operations.
     * @param userService The service for managing users.
	 */
	@Autowired
	public ExamServiceImpl(ExamRepository examRepository, QuestionService questionService, DTOService dTOService,
			JwtService jwtService, UserService userService) {
		super();
		this.examRepository = examRepository;
		this.questionService = questionService;
		this.dTOService = dTOService;
		this.jwtService = jwtService;
		this.userService = userService;
	}

	@Override
	public List<Question> takeExam(CreateExamRequestDTO request) {
		List<Question> questions = questionService.findAllQuestionsInCategory(request.getCategory());
		questions = questionService.filterQuestionsByLevel(questions, dTOService.findLevelByName(request.getLevel()));
		return questionService.findRandomQuestions(questions, 20);
	}

	@Override
	public Exam completeExam(String header, Exam exam, LocalDateTime time) {	
		String userEmail = jwtService.extractUserEmailFromHeader(header);
		System.err.println(userEmail);
		
		User user = userService.findByEmail(userEmail).orElseThrow();
		exam.setUser(user);
		exam.setExamDate(time);
		return examRepository.save(exam);
	}

	@Override
	public List<Exam> findAllExamsFromUser(User user) {
		List<Exam> exams = examRepository.findAllByUser(user);
		return exams;
	}
}
