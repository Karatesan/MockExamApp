package com.fdmgroup.MockExam.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import com.fdmgroup.MockExam.dto.AnswerRequestDTO;
import com.fdmgroup.MockExam.dto.AnswerResponseDTO;
import com.fdmgroup.MockExam.dto.CreateExamResponseDTO;
import com.fdmgroup.MockExam.dto.ExamRequestDTO;
import com.fdmgroup.MockExam.dto.ExamResponseDTO;
import com.fdmgroup.MockExam.dto.ImageResponseDTO;
import com.fdmgroup.MockExam.dto.NotificationResponseDTO;
import com.fdmgroup.MockExam.dto.QuestionRequestDTO;
import com.fdmgroup.MockExam.dto.QuestionResponseDTO;
import com.fdmgroup.MockExam.model.Answer;
import com.fdmgroup.MockExam.model.Exam;
import com.fdmgroup.MockExam.model.Image;
import com.fdmgroup.MockExam.model.Notification;
import com.fdmgroup.MockExam.model.Question;
import com.fdmgroup.MockExam.model.Question.Level;
import com.fdmgroup.MockExam.model.Role;
import com.fdmgroup.MockExam.model.Tag;
import com.fdmgroup.MockExam.repositories.QuestionRepository;
import com.fdmgroup.MockExam.utils.ImageConverter;

/**
 * The class implements the DTOService interface. Provides methods to convert
 * question, answer and exam DTOs to entities and vice versa.
 */
@Service
public class DTOServiceImpl implements DTOService {

	private ImageService imageService;
	private CategoryService categoryService;
	private TagService tagService;
	private QuestionRepository questionRepository;

	/**
	 * Constructs a new DTOServiceImpl instance.
	 * 
	 * @param imageService       The service for managing images.
	 * @param categoryService    The service for managing categories.
	 * @param tagService    The service for managing categories.
	 * @param questionRepository The repository for managing questions.
	 */
	public DTOServiceImpl(ImageService imageService, CategoryService categoryService,TagService tagService,
			QuestionRepository questionRepository) {
		super();
		this.imageService = imageService;
		this.categoryService = categoryService;
		this.tagService = tagService;

		this.questionRepository = questionRepository;
	}

	// ----------------------------------QUESTION----------------------------------

	@Override
	public QuestionResponseDTO questionEntityToResponse(Question question) {

		ImageResponseDTO imageResponseDTO = new ImageResponseDTO();
		try {
			imageResponseDTO = ImageConverter.convertImageToResponseImage(question.getImage());
		} catch (Exception e) {
		}
		List<String> tags = convertListTagsToString(question.getTags());

		String category = question.getCategory().getCategoryName();
		String level = question.getLevel().toString().toLowerCase();
		QuestionResponseDTO response = new QuestionResponseDTO(question.getId(), category, level,
				question.getQuestionContent(), question.getAnswers(), question.getFeedback(),
				question.getCorrectAnswers(), imageResponseDTO, tags);

		return response;
	}

	@Override
	public Question questionRequestToEntity(QuestionRequestDTO questionDTO) {

		Question.Level questionLevel = findLevelByName(questionDTO.getLevel());
		Image newImage = new Image();

		if (questionDTO.getImageFile() == null || questionDTO.getImageFile().isEmpty()) {
			newImage = imageService.findByName("Empty Image");
			if (newImage == null) {
				newImage = imageService.save(new Image("Empty Image", 0, new byte[0]));

			}
		} else {

			try {
				newImage = ImageConverter.convertMultiPartFileToImage(questionDTO.getImageFile());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		List<Tag> tags = new ArrayList<>();
		for (String tag : questionDTO.getTags()) {

			Tag newTag = findOrCreateTag(tag);
			tags.add(newTag);
		}
		Question newQuestion = new Question(categoryService.findByName(questionDTO.getCategory()), questionLevel,
				questionDTO.getQuestionContent(), questionDTO.getAnswers(), questionDTO.getFeedback(),
				questionDTO.getCorrectAnswers(), newImage, tags);
		return newQuestion;
	}

	// ----------------------------------ANSWER----------------------------------

	@Override
	public AnswerResponseDTO answerEntityToResponse(Answer answer) {

		Question question = answer.getQuestion();
		QuestionResponseDTO questionDTO = questionEntityToResponse(question);
		return new AnswerResponseDTO(answer.getId(), questionDTO, answer.getGivenAnswer());
	}

	@Override
	public Answer answerRequestToEntity(AnswerRequestDTO answerDTO) {
		Optional<Question> questionOptional = questionRepository.findById(answerDTO.getQuestionId());
		Question question = questionOptional.orElse(null);
		return new Answer(question, answerDTO.getGivenAnswer());
	}

	// ----------------------------------EXAM
	// COMPLETE----------------------------------

	@Override
	public ExamResponseDTO examEntityToResponse_complete(Exam exam) {

		List<AnswerResponseDTO> answers = exam.getAnswers().stream().map(this::answerEntityToResponse)
				.collect(Collectors.toList());

		return new ExamResponseDTO(exam.getId(), exam.getExamDate().toString(), answers);
	}

	@Override
	public Exam examRequestToEntity_complete(ExamRequestDTO examRequestDTO) {
		System.out.println("complete");
		List<Answer> answers = examRequestDTO.getAnswers().stream().map(this::answerRequestToEntity)
				.collect(Collectors.toList());
		System.out.println(answers);
		return new Exam(answers);

	}

	// ----------------------------------EXAM
	// CREATE----------------------------------

	@Override
	public CreateExamResponseDTO examEntityToResponse_create(List<Question> questions) {
		List<QuestionResponseDTO> questionsResponse = new ArrayList<>();
		for (Question quest : questions) {
			questionsResponse.add(questionEntityToResponse(quest));
		}
		return new CreateExamResponseDTO(questionsResponse);
	}

	// ----------------------------------ADDITIONAL----------------------------------

	@Override
	public Level findLevelByName(String levelName) {
		Level[] levels = Level.values();
		Level questionLevel = Level.BEGINNER;
		for (Question.Level level : levels) {
			if (level.toString().toLowerCase().equals(levelName.toLowerCase()))
				questionLevel = level;
		}
		return questionLevel;
	}

	// ----------------------------------ADDITIONAL----------------------------------

	@Override
	public Role findRoleByName(String roleName) {
		Role[] roles = Role.values();
		Role returningRole = Role.TRAINEE;
		for (Role role : roles) {
			if (role.toString().toLowerCase().equals(roleName.toLowerCase()))
				returningRole = role;
		}
		return returningRole;
	}

	@Override
	public Tag findOrCreateTag(String tagName) {
		Optional<Tag> tag = tagService.findByName(tagName);
		if (tag.isPresent())
			return tag.get();
		else
			return tagService.newTag(tagName);
	}

	@Override
	public List<String> convertListTagsToString(List<Tag> tags) {
		List<String> strings = new ArrayList<>();
		for(Tag tag : tags) {
			strings.add(tag.getName());
		}
		return strings;
	}

}
