package com.fdmgroup.MockExam.services;

import org.springframework.web.multipart.MultipartFile;

import com.fdmgroup.MockExam.dto.ConfirmationResponseDTO;

/**
 * Interface to define the contract for the QuestionsFromFileService.
 * Provides a method for adding questions from a file and saving them in the database.
 */
public interface QuestionsFromFileService {

	/**
	 * Adds multiple questions from a file to the system.
	 * @param file The main file containing question content.
	 * @param answersFile The file containing correct answers and levels.
	 * @param accessToAllUsers 
	 * @return A QuestionRequestDTO representing the added question.
	 */
	ConfirmationResponseDTO addQuestionsFroMFile(MultipartFile file, MultipartFile answersFile, boolean accessToAllUsers);

}
