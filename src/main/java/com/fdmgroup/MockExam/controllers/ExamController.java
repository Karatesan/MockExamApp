package com.fdmgroup.MockExam.controllers;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.fdmgroup.MockExam.dto.CreateExamRequestDTO;
import com.fdmgroup.MockExam.dto.CreateExamResponseDTO;
import com.fdmgroup.MockExam.dto.ExamRequestDTO;
import com.fdmgroup.MockExam.dto.ExamResponseDTO;
import com.fdmgroup.MockExam.exceptions.NoQuestionException;
import com.fdmgroup.MockExam.model.Exam;
import com.fdmgroup.MockExam.model.Question;
import com.fdmgroup.MockExam.model.User;
import com.fdmgroup.MockExam.services.DTOService;
import com.fdmgroup.MockExam.services.ExamService;
import com.fdmgroup.MockExam.services.JwtService;
import com.fdmgroup.MockExam.services.TimeService;
import com.fdmgroup.MockExam.services.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/exam")
@RequiredArgsConstructor

public class ExamController {

	private final ExamService examService;
	private final DTOService dTOService;
	private final JwtService jwtService;
	private final UserService userService;

	private final TimeService timeService;

	@GetMapping("/takeExam")
	public CreateExamResponseDTO takeExam(@ModelAttribute("CreateExamRequestDTO") CreateExamRequestDTO request) {

		List<Question> questions = examService.takeExam(request);
		if(questions.size()>0)
		return dTOService.examEntityToResponse_create(questions);
		else throw new NoQuestionException("There is no questions fulfiling the requiments");

	}

	@PostMapping("/completeExam")
	public ExamResponseDTO completeExam(@RequestBody ExamRequestDTO request,
			@RequestHeader("Authorization") String header) {
		Exam exam = dTOService.examRequestToEntity_complete(request);
		Exam examCompleted = examService.completeExam(header, exam, timeService.now());
		return dTOService.examEntityToResponse_complete(examCompleted);
	}
	
	@GetMapping("/allExamsFromUser")
	List<ExamResponseDTO> findAllExamsFromUser(@RequestHeader("Authorization") String header){
		
		String userEmail = jwtService.extractUserEmailFromHeader(header);
		Optional<User> user = userService.findByEmail(userEmail);
		List<Exam>exams = examService.findAllExamsFromUser(user.get());
		List<ExamResponseDTO> DTOExams = exams.stream().map(exam->dTOService.examEntityToResponse_complete(exam)).collect(Collectors.toList());
		
		return DTOExams;
	}
}
