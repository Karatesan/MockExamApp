package com.fdmgroup.MockExam.services;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fdmgroup.MockExam.dto.CreateExamRequestDTO;
import com.fdmgroup.MockExam.model.Exam;
import com.fdmgroup.MockExam.model.Question;
import com.fdmgroup.MockExam.model.Question.Level;
import com.fdmgroup.MockExam.model.User;
import com.fdmgroup.MockExam.repositories.ExamRepository;

@ExtendWith(MockitoExtension.class)
public class test_ExamService {
	@InjectMocks
	private ExamServiceImpl examService;

	@Mock
	private ExamRepository examRepository;
	@Mock
	private JwtService jwtService;
	@Mock
	private UserService userService;
	@Mock
	private QuestionService questionService;
	@Mock
	private DTOService dTOService;

	// ---------------------------------------------------------------------------------------------
	Exam exam;
	User user;
	String header = "header";
	String userEmail = "test@example.com";
	LocalDateTime time;
	@BeforeEach
	public void setUp() {
		 exam = new Exam();
		 user = new User();
		 time = LocalDateTime.now();
			user.setEmail(userEmail);
			exam.setUser(user);
			exam.setExamDate(time);
	}
	@Test
	public void test_completeExam() {
		when(jwtService.extractUserEmailFromHeader(header)).thenReturn(userEmail);
		when(userService.findByEmail(userEmail)).thenReturn(Optional.of(user));
		when(examRepository.save(any(Exam.class))).thenReturn(exam);

		Exam result = examService.completeExam(header, new Exam(),time);
		System.out.println(result);
		assertNotNull(result);

		verify(jwtService, times(1)).extractUserEmailFromHeader(header);
		verify(userService, times(1)).findByEmail(userEmail);
		verify(examRepository, times(1)).save(any(Exam.class));
		assertNotNull(result.getExamDate());
		assertEquals(user, result.getUser());
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_takeExam() {
		CreateExamRequestDTO request = new CreateExamRequestDTO();
		request.setCategory("Java");
		request.setLevel("Beginner");
		Level level = Level.BEGINNER;

		List<Question> questionsAll = new ArrayList<>();
		List<Question> questionsLevel = new ArrayList<>();
		List<Question> questionsRandom = new ArrayList<>();

		when(questionService.findAllQuestionsInCategory(request.getCategory())).thenReturn(questionsAll);
		when(dTOService.findLevelByName(request.getLevel())).thenReturn(level);
		when(questionService.filterQuestionsByLevel(questionsAll, level)).thenReturn(questionsLevel);
		when(questionService.findRandomQuestions(questionsLevel, 20)).thenReturn(questionsRandom);

		List<Question> result = examService.takeExam(request);

		assertNotNull(result);
		assertEquals(questionsRandom, result);
		verify(questionService, times(1)).findAllQuestionsInCategory(request.getCategory());
		verify(dTOService, times(1)).findLevelByName(request.getLevel());
		verify(questionService, times(1)).filterQuestionsByLevel(questionsAll, level);
		verify(questionService, times(1)).findRandomQuestions(questionsLevel, 20);
	}

	// ---------------------------------------------------------------------------------------------

	  @Test
	    public void test_findAllExamsFromUser() {
		  List<Exam> examList = new ArrayList<>();
		  examList.add(exam);
		  doReturn(examList).when(examRepository).findAllByUser(user);
		  List<Exam> result = examService.findAllExamsFromUser(user);
		  assertEquals(examList, result);
	       }
}
