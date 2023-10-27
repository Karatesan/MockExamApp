package com.fdmgroup.MockExam.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fdmgroup.MockExam.dto.CreateExamRequestDTO;

import com.fdmgroup.MockExam.model.Exam;
import com.fdmgroup.MockExam.model.Question;
import com.fdmgroup.MockExam.model.User;

/**
 * Interface to define the contract for the ExamService.
 * Provides methods for managing exams.
 */
@Service
public interface ExamService {
	
    /**
     * Generates a list of questions for an exam based on the request DTO.
     * @param request The DTO containing exam request details.
     * @return A list of questions for the exam.
     */
	List<Question> takeExam(CreateExamRequestDTO request);
	
    /**
     * Completes and saves an exam for a user.
     * @param header The header containing user information.
     * @param exam The completed exam to be saved.
     * @param time The time stamp of when the exam was completed.
     * @return The saved exam.
     */
	Exam completeExam(String header, Exam exam, LocalDateTime time);

	List<Exam> findAllExamsFromUser(User user);
	
}
