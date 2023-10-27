package com.fdmgroup.MockExam.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.fdmgroup.MockExam.comperators.SearchIdComperator;
import com.fdmgroup.MockExam.dto.AccountIdRequestDTO;
import com.fdmgroup.MockExam.dto.AccountIdRoleRequestDTO;
import com.fdmgroup.MockExam.dto.ChangeNameResponseDTO;
import com.fdmgroup.MockExam.dto.ConfirmationResponseDTO;
import com.fdmgroup.MockExam.dto.IdFoundInSearchDTO;
import com.fdmgroup.MockExam.dto.NotificationRequestDTO;
import com.fdmgroup.MockExam.dto.NotificationResponseDTO;
import com.fdmgroup.MockExam.dto.QuestionFoundInSearchDTO;
import com.fdmgroup.MockExam.dto.QuestionRequestDTO;
import com.fdmgroup.MockExam.dto.QuestionResponseDTO;
import com.fdmgroup.MockExam.dto.SearchQuestionRequestDTO;
import com.fdmgroup.MockExam.dto.SearchQuestionResponseDTO;
import com.fdmgroup.MockExam.dto.SearchUserRequestDTO;
import com.fdmgroup.MockExam.dto.SearchUserResponseDTO;
import com.fdmgroup.MockExam.dto.UserFoundInSearchDTO;
import com.fdmgroup.MockExam.exceptions.AdminAccesException;
import com.fdmgroup.MockExam.model.Category;
import com.fdmgroup.MockExam.model.Image;
import com.fdmgroup.MockExam.model.Question;
import com.fdmgroup.MockExam.model.Question.Level;
import com.fdmgroup.MockExam.model.Role;
import com.fdmgroup.MockExam.model.Tag;
import com.fdmgroup.MockExam.model.User;
import com.fdmgroup.MockExam.model.Notification;
import com.fdmgroup.MockExam.model.NotificationFirstName;
import com.fdmgroup.MockExam.model.NotificationLastName;
import com.fdmgroup.MockExam.model.NotificationQuestion;
import com.fdmgroup.MockExam.repositories.ImageRepository;
import com.fdmgroup.MockExam.repositories.NotificationRepository;
import com.fdmgroup.MockExam.repositories.QuestionRepository;
import com.fdmgroup.MockExam.repositories.UserRepository;
import com.fdmgroup.MockExam.validators.ObjectValidator;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
	private final UserRepository userRepository;
	private final QuestionService questionService;
	private final UserService userService;
	private final TagService tagService;
	private final QuestionRepository questionRepository;
	private final ImageRepository imageRepository;
	private final CategoryService categoryService;
	private final ObjectValidator<QuestionRequestDTO> questionRequestDTOValidator;
	private final DTOService dTOService;
	private final SearchingSortService searchingSortService;
	private final NotificationService notificationService;

	@Override
	public ConfirmationResponseDTO blockAccount(AccountIdRequestDTO request, boolean blockIt) {
		try {
			Optional<User> userOptional = userRepository.findById(request.getId());
			User user = userOptional.get();
			user.setLocked(blockIt);
			userRepository.save(user);
		} catch (Exception e) {
			throw new EntityNotFoundException();
		}
		if (blockIt)
			return ConfirmationResponseDTO.builder().confirmationMessage("Account has been blocked.").build();
		else
			return ConfirmationResponseDTO.builder().confirmationMessage("Account has been unblocked.").build();

	}

	// ----------------------------------------------------------------------------------

	@Override
	public ConfirmationResponseDTO deleteAccount(int id) {
		Optional<User> userOptional = userRepository.findById(id);

		if (userOptional.isPresent()) {
			User user = userOptional.get();

			user.setEmail("deletedAccount" + user.getId() + "@mail.com");
			user.setUserImage(null);
			user.setPassword("deletedPassword");
			userRepository.save(user);

			return ConfirmationResponseDTO.builder().confirmationMessage("Account has been deleted.").build();

		} else
			throw new EntityNotFoundException();
	}

	// ----------------------------------------------------------------------------------

	@Override
	public QuestionResponseDTO addQuestion(QuestionRequestDTO question) {
		Category category = categoryService.findByName(question.getCategory());
		if (category == null) {
			category = new Category(question.getCategory());
			categoryService.saveCategory(category);
		}
		questionRequestDTOValidator.validate(question);
		Question newQuestion = dTOService.questionRequestToEntity(question);

		questionRepository.save(newQuestion);
		QuestionResponseDTO responseDTO = dTOService.questionEntityToResponse(newQuestion);

		return responseDTO;
	}

	// ----------------------------------------------------------------------------------

	@Override
	public ConfirmationResponseDTO changeAccountRole(AccountIdRoleRequestDTO request) {
		try {
			Optional<User> userOptional = userRepository.findById(request.getId());

			User user = userOptional.get();
			Role role = dTOService.findRoleByName(request.getRoleName());
			user.setRole(role);
			userRepository.save(user);
			return ConfirmationResponseDTO.builder().confirmationMessage("Role has been changed.").build();
		} catch (Exception e) {
			throw new EntityNotFoundException();
		}

	}

	// ----------------------------------------------------------------------------------

	@Override
	public QuestionResponseDTO updateQuestion(Integer questionId, QuestionRequestDTO question) {
		Optional<Question> optQuestion = questionRepository.findById(questionId);
		if (optQuestion.isPresent()) {
			QuestionResponseDTO responseDTO = addQuestion(question);

			deleteQuestion(questionId);

			return responseDTO;
		} else
			throw new AdminAccesException("Invalid question id.");
	}

	// ----------------------------------------------------------------------------------

	@Override
	public ConfirmationResponseDTO deleteQuestion(Integer id) {

		Optional<Question> questionOptional = questionRepository.findById(id);
		if (questionOptional.isPresent()) {
			try {
				Question question = questionOptional.get();

				Image image = question.getImage();
				if (!question.getImage().getName().equals("Empty Image"))
					imageRepository.deleteById(image.getId());
				questionRepository.deleteById(id);
			} catch (Exception e) {
				Question question = questionOptional.get();
				question.setLocked(true);
				questionRepository.save(question);
				return ConfirmationResponseDTO.builder()
						.confirmationMessage("Question has been locked, because it has been used in exam.").build();

			}

		} else {
			throw new EntityNotFoundException();
		}

		return ConfirmationResponseDTO.builder().confirmationMessage("Question has been deleted.").build();
	}

	// ----------------------------------------------------------------------------------

//	@Override
//	public List<Question> findAllLockedQuestions() {
//		return questionRepository.findByLocked(true);
//	}

	// ----------------------------------------------------------------------------------

	@Override
	public List<User> findAllUsers() {
		return userRepository.findAll();
	}

	// ----------------------------------------------------------------------------------

	@Override
	public SearchQuestionResponseDTO searchQuestion(SearchQuestionRequestDTO request) {
		List<String> searchFileds = request.getSearchFields();

		List<QuestionFoundInSearchDTO> list = new ArrayList<>();
		List<Question> found = new ArrayList<>();

		String searchText = request.getSearchText();
		String category = request.getCategory();
		String levelName = request.getLevel();

		if (searchFileds.isEmpty()) {
			found.addAll(questionService.findByQuestionContentContainsIgnoreCaseAndLockedIsFalse(searchText));
			found.addAll(questionService.findByAnswers(searchText));
			found.addAll(questionService.findByFeedback(searchText));

		} else {
			for (String field : searchFileds) {
				if (searchFileds.contains(field)) {
					switch (field) {
					case "question_content":
						found.addAll(
								questionService.findByQuestionContentContainsIgnoreCaseAndLockedIsFalse(searchText));
						break;
					case "answers":
						found.addAll(questionService.findByAnswers(searchText));
						break;
					case "feedback":
						found.addAll(questionService.findByFeedback(searchText));
						break;
					default:
					}
				}
			}
		}

		Set<Question> uniqueQuestions = new HashSet<>(found);
		found.clear();
		found.addAll(uniqueQuestions);

		if (!category.isEmpty()) {
			found = found.stream().filter(question -> question.getCategory().getCategoryName().equals(category))
					.distinct().collect(Collectors.toList());
		}

		if (!levelName.isEmpty()) {
			Level level = dTOService.findLevelByName(levelName);
			found = found.stream().filter(question -> question.getLevel().equals(level)).distinct()
					.collect(Collectors.toList());
		}
//		//for one tag:

//		if(!request.getTag().isEmpty()) {
//			try {
//		Tag tag = tagService.findByName(request.getTag()).get();
//		found = found.stream().filter(question -> question.getTags().contains(tag)).distinct()
//			.collect(Collectors.toList());
//			}
//			catch(Exception e) {
//				throw new AdminAccesException("Invalid tag.");
//			}
//		}

//		// for List of tags:
//		if(!tagsString.isEmpty()) {
////			// for OR
////			List<Question> questionsTags = new ArrayList<>();
//			for(String string : tagsString) {
//				try {
//			Tag tag = tagService.findByName(string).get();
//			
////			// for AND
////			found = found.stream().filter(question -> question.getTags().contains(tag)).distinct()
////					.collect(Collectors.toList());
////			// for OR
////			questionsTags.addAll(questionService.findByTagsContains(tag));
//			
//				}catch(Exception e) {}
////				// for OR
////				found = found.stream().distinct().filter(questionsTags::contains).collect(Collectors.toList());
//
//			}
//		}

		if (found.size() > 0) {
			for (Question question : found) {
				list.add(new QuestionFoundInSearchDTO(question.getId(), question.getCategory().getCategoryName(),
						question.getLevel().toString(), question.getQuestionContent(), question.getAnswers(),
						question.getFeedback(), question.getCorrectAnswers(), question.getLocked(),
						dTOService.convertListTagsToString(question.getTags())));
			}
			System.out.println(list);

			SearchQuestionResponseDTO questions = new SearchQuestionResponseDTO();
			questions.setTotalNumber(list.size());

			list = searchingSortService.sortList(list);
			System.out.println(list);

			if (list.size() > 5) {
				list = searchingSortService.findSearchedItemsOnPage(list, request.getPage());
			}
			questions.setQuestions(list);

			return questions;
		} else
			throw new AdminAccesException("There are no questions fulfilling the requirements.");
	}

	// ----------------------------------------------------------------------------------

	@Override
	public SearchUserResponseDTO searchUser(SearchUserRequestDTO request) {
		List<UserFoundInSearchDTO> list = new ArrayList<>();
		List<User> found = new ArrayList<>();

		List<String> searchFileds = request.getSearchFields();
		String searchText = request.getSearchText();

		if (searchFileds.isEmpty()) {
			found.addAll(userService.findByFirstNameContainsIgnoreCase(searchText));
			found.addAll(userService.findByLastNameContainsIgnoreCase(searchText));
			found.addAll(userService.findByEmailContainsIgnoreCase(searchText));
//			found.addAll(userService.findByTags(searchText));

		} else {
			for (String field : searchFileds) {
				if (searchFileds.contains(field)) {
					switch (field) {
					case "first_name":
						found.addAll(userService.findByFirstNameContainsIgnoreCase(searchText));
						break;
					case "last_name":
						found.addAll(userService.findByLastNameContainsIgnoreCase(searchText));
						break;
					case "email":
						found.addAll(userService.findByEmailContainsIgnoreCase(searchText));
						break;
//					case "tags":
//						found.addAll(userService.findByTags(searchText));
//						break;
					default:
					}
				}
			}
		}
		Set<User> uniqueUsers = new HashSet<>(found);
		found.clear();
		found.addAll(uniqueUsers);

		if (!request.getTag().isEmpty()) {
			try {
				Tag tag = tagService.findByName(request.getTag()).get();
				found = found.stream().filter(user -> user.getTags().contains(tag)).distinct()
						.collect(Collectors.toList());
			} catch (Exception e) {
				throw new AdminAccesException("Invalid tag.");
			}
		}

		if (found.size() > 0) {
			for (User user : found) {
				list.add(new UserFoundInSearchDTO(user.getId(), user.getFirstName(), user.getLastName(),
						user.getEmail(), user.getLocked(), user.getLocked(),
						dTOService.convertListTagsToString(user.getTags())));
			}
			SearchUserResponseDTO users = new SearchUserResponseDTO();
			users.setTotalNumber(list.size());

			list = searchingSortService.sortList(list);

			if (list.size() > 5) {
				list = searchingSortService.findSearchedItemsOnPage(list, request.getPage());
			}
			users.setUsers(list);
			return users;
		} else
			throw new AdminAccesException("There is no users fulfilling the requiments.");
	}

	@Override
	public ConfirmationResponseDTO giveTagToUser(User user, String tagName) {
		Tag tag = dTOService.findOrCreateTag(tagName);
		user.addToTags(tag);
		userRepository.save(user);
		return ConfirmationResponseDTO.builder().confirmationMessage("Tag has been added to user.").build();
	}

	@Override
	public ChangeNameResponseDTO changeFirstNameAccept(int id) {
		NotificationFirstName nameNote = (NotificationFirstName) notificationService.findById(id);
		User user = nameNote.getUser();
		user.setFirstName(nameNote.getName());
		userRepository.save(user);
		notificationService.delete(id);
		String jwtToken = userService.buildClaim(user);
		return ChangeNameResponseDTO.builder().confirmationMessage("First name has been changed.").token(jwtToken)
				.build();
	}

	@Override
	public ChangeNameResponseDTO changeLastNameAccept(int id) {
		NotificationLastName nameNote = (NotificationLastName) notificationService.findById(id);
		User user = nameNote.getUser();
		user.setLastName(nameNote.getName());
		userRepository.save(user);
		notificationService.delete(id);
		String jwtToken = userService.buildClaim(user);
		return ChangeNameResponseDTO.builder().confirmationMessage("Last name has been changed.").token(jwtToken)
				.build();
	}
}
