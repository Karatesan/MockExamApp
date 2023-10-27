package com.fdmgroup.MockExam.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.web.multipart.MultipartFile;

import com.fdmgroup.MockExam.dto.ImageResponseDTO;
import com.fdmgroup.MockExam.dto.QuestionRequestDTO;
import com.fdmgroup.MockExam.dto.QuestionResponseDTO;
import com.fdmgroup.MockExam.model.Category;
import com.fdmgroup.MockExam.model.Image;
import com.fdmgroup.MockExam.model.Question;
import com.fdmgroup.MockExam.model.Tag;

//Probably to delete

public class QuestionConverter {
	
	public static Question questionRequestToEntity(QuestionRequestDTO question, Category category, MultipartFile image, List<Tag> tags) {
		
		
		Question.Level questionLevel=null;
		Image newImage = new Image();
		Question.Level[] levels = Question.Level.values();
			for(Question.Level level : levels) {
				if(level.toString().equals(question.getLevel())) questionLevel = level;
			}
			if(image!=null) {
		try {
			newImage = ImageConverter.convertMultiPartFileToImage(image);
		} catch (IOException e) {
			e.printStackTrace();
		}
			}
		
		Question newQuestion = new Question(category, 
											questionLevel, 
											question.getQuestionContent(), 
											question.getAnswers(), 
											question.getFeedback(), 
											question.getCorrectAnswers(),
											newImage,
											tags);
		return newQuestion;
		
	}

	
	public static QuestionResponseDTO questionEntityToResponse(Question question) {
		
		ImageResponseDTO imageResponseDTO = ImageConverter.convertImageToResponseImage(question.getImage());
		String category = question.getCategory().getCategoryName();
		String level = question.getLevel().toString().toLowerCase();
		List<String> tags = new ArrayList<>();
		for(Tag tag : question.getTags()) {
			tags.add(tag.toString());
		}
		QuestionResponseDTO response = new QuestionResponseDTO(question.getId(), 
															   category, 
															   level, 
															   question.getQuestionContent(), 
															   question.getAnswers(), 
															   question.getFeedback(),	
															   question.getCorrectAnswers(), 
															   imageResponseDTO,
															   tags);
		
		return response;
	}
	

	
	 public static <T, R> List<R> mapList(List<T> list, Function<T, R> mapper) {
	        return list.stream()
	                .map(mapper)
	                .collect(Collectors.toList());
	    }
}
