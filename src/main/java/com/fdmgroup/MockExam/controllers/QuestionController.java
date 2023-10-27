package com.fdmgroup.MockExam.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.fdmgroup.MockExam.dto.QuestionResponseDTO;
import com.fdmgroup.MockExam.model.Category;
import com.fdmgroup.MockExam.model.Question;
import com.fdmgroup.MockExam.model.User;
import com.fdmgroup.MockExam.model.Question.Level;
import com.fdmgroup.MockExam.model.Role;
import com.fdmgroup.MockExam.services.AuthenticationService;
import com.fdmgroup.MockExam.services.CategoryService;
import com.fdmgroup.MockExam.services.DTOService;
import com.fdmgroup.MockExam.services.NotificationService;
import com.fdmgroup.MockExam.services.QuestionService;
import com.fdmgroup.MockExam.services.UserService;
import com.fdmgroup.MockExam.utils.QuestionConverter;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/questions")
public class QuestionController {

	private final QuestionService questionService;
	private final CategoryService categoryService;
	private final UserService userService;
	private final DTOService dTOService;

//	@Autowired
//	public QuestionController(QuestionService questionService, CategoryService categoryService, 
//			DTOService dTOService) {
//		super();
//		this.questionService = questionService;
//		this.categoryService = categoryService;
//
//		this.dTOService = dTOService;
//
//	}
	// ---------------------------------------------------------------------------------------------

	@GetMapping("/all")
	public List<QuestionResponseDTO> getAllQuestions() {
		List<Question> questions = questionService.findAllQuestions();
		List<QuestionResponseDTO> responseQuestions = QuestionConverter.mapList(questions,
				QuestionConverter::questionEntityToResponse);
		return responseQuestions;
	}

	// ---------------------------------------------------------------------------------------------

//	@GetMapping("/{id}")
//	public ResponseEntity<QuestionResponseDTO> getQuestionById(@PathParam(value = "id") Integer id){
//		System.out.println("dsadsadsa");
//		Optional<Question> question =  questionService.findById(id);
//		QuestionResponseDTO responseQuestion = new QuestionResponseDTO(); 
//		if(question.isPresent()) {
//		responseQuestion = QuestionConverter.questionEntityToResponse(question.get());
//		}
//		return ResponseEntity.ok(responseQuestion);	
//		}
//	
	// ---------------------------------------------------------------------------------------------

	@GetMapping("/itemsInCategory")
	public List<QuestionResponseDTO> getAllQuestionsInCategory(@RequestParam(value = "category") String category) {
		List<Question> questions = questionService.findAllQuestionsInCategory(category);
		List<QuestionResponseDTO> responseQuestions = QuestionConverter.mapList(questions,
				QuestionConverter::questionEntityToResponse);
		return responseQuestions;
	}

	// ---------------------------------------------------------------------------------------------

	// FOR REACT FRONTEND
//	fetch(`/api/exams/random?category=${category}&numberOfQuestions=${numberOfQuestions}`)
//    .then(response => response.json())
//    .then(data => {
//        // Do something with the random questions data
//        console.log(data);
//    })
//    .catch(error => {
//        // Handle any errors that occur during the request
//        console.error("Error fetching random questions:", error);
//    });

	// ---------------------------------------------------------------------------------------------

	@GetMapping("/randomItemsInCategory")
	public ResponseEntity<List<QuestionResponseDTO>> getRandomQuestions(@RequestParam("category") String category,
			@RequestParam("numberOfQuestions") int numberOfQuestions) {

		List<Question> randomQuestions = questionService
				.findRandomQuestions(questionService.findAllQuestionsInCategory(category), numberOfQuestions);

		// List<Question> randomQuestions =
		// questionService.findRandomQuestions(category, numberOfQuestions);
		List<QuestionResponseDTO> responseQuestions = QuestionConverter.mapList(randomQuestions,
				QuestionConverter::questionEntityToResponse);
		return ResponseEntity.ok(responseQuestions);
	}

	// ---------------------------------------------------------------------------------------------
	@GetMapping("/randomItemsInCategoryAndLevel")
	public ResponseEntity<List<QuestionResponseDTO>> getRandomQuestionsWithLevel(
			@RequestParam("category") String category,
			@RequestParam(name = "numberOfQuestions", required = false) String numberOfQuestions,
			@RequestParam("level") String level) {
		Level levelQuestion = dTOService.findLevelByName(level);
		List<Question> allQuestions = questionService.findAllQuestionsInCategory(category);
		int num = numberOfQuestions != null ? Integer.parseInt(numberOfQuestions) : allQuestions.size();

		List<Question> randomQuestions = questionService
				.findRandomQuestions(questionService.filterQuestionsByLevel(allQuestions, levelQuestion), num);
		// List<Question> randomQuestions =
		// questionService.findRandomQuestionsWithLevel(category, num, level);

		List<QuestionResponseDTO> responseQuestions = QuestionConverter.mapList(randomQuestions,
				QuestionConverter::questionEntityToResponse);
		System.out.println(responseQuestions);
		return ResponseEntity.ok(responseQuestions);
	}

	// ---------------------------------------------------------------------------------------------

	@GetMapping("/allCategories")
	public List<String> findAllCategories(@RequestHeader("Authorization") String header) {
		User user = userService.getUserFromHeader(header);
		List<Category> categories = categoryService.findAllCategories(user);
		return categories.stream().map(category -> category.getCategoryName()).collect(Collectors.toList());
	}
	@GetMapping("/allCategoriesForAll")
	public List<String> findAllCategories() {
	
		List<Category> categories = categoryService.findAllCategories();
		return categories.stream().map(category -> category.getCategoryName()).collect(Collectors.toList());
	}

}
