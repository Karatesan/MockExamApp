package com.fdmgroup.MockExam.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fdmgroup.MockExam.dto.AuthenticationResponseDTO;
import com.fdmgroup.MockExam.dto.ChangeFirstNameRequestDTO;
import com.fdmgroup.MockExam.dto.ChangeLastNameRequestDTO;
import com.fdmgroup.MockExam.dto.ChangeNameResponseDTO;
import com.fdmgroup.MockExam.dto.ConfirmationResponseDTO;
import com.fdmgroup.MockExam.dto.ImageResponseDTO;
import com.fdmgroup.MockExam.exceptions.EmailAlreadyExistsException;
import com.fdmgroup.MockExam.model.Category;
import com.fdmgroup.MockExam.model.Notification;
import com.fdmgroup.MockExam.model.NotificationFirstName;
import com.fdmgroup.MockExam.model.NotificationLastName;
import com.fdmgroup.MockExam.model.Question;
import com.fdmgroup.MockExam.model.Role;
import com.fdmgroup.MockExam.model.User;
import com.fdmgroup.MockExam.model.UserImage;
import com.fdmgroup.MockExam.repositories.UserRepository;
import com.fdmgroup.MockExam.utils.ImageConverter;

import jakarta.validation.Valid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * This class implements the UserService interface and provides functionality
 * for user management and related operations.
 */
@Service
public class UserServiceImpl implements UserService {
	@Autowired
	UserRepository userRepository;
	@Autowired
	NotificationService notificationService;

	@Autowired
	JwtService jwtService;

	/**
	 * Constructs a UserServiceImpl instance with a UserRepository dependency.
	 * 
	 * @param userRepository The repository used for user data access.
	 */
	@Autowired
	public UserServiceImpl(UserRepository userRepository, JwtService jwtService, NotificationService notificationService) {
		super();
		this.userRepository = userRepository;
		this.jwtService = jwtService;
		this.notificationService = notificationService;
	}

	@Override
	public String generateToken() {

		String token = UUID.randomUUID().toString();

		return token;
	}

	@Override
	public User save(User user) {
		return userRepository.save(user);
	}

	@Override
	public User verificateUser(User user) {
		user.setVerified(true);
		return save(user);
	}

	@Override
	public User register(@Valid User user) {
		Optional<User> userOpt = userRepository.findByEmail(user.getEmail());
		if (userOpt.isPresent()) {
			throw new EmailAlreadyExistsException("A user with this email already exists.");
		}

		return save(user);
	}
	
	@Override
	public List<User> findAll(){
		return userRepository.findAll();
	}

	@Override
	public Optional<User> findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public int enableUser(String email) {
		return userRepository.enableUser(email);

	}

	@Override
	public User updatePassword(String password, User user) {
		user.setPassword(password);
		return userRepository.save(user);
	}

	@Override
	public void delete(User user) {
		userRepository.delete(user);
	}

	@Override
	public ChangeNameResponseDTO changeFirstName(ChangeFirstNameRequestDTO request, String header) {
		User user = getUserFromHeader(header);
		var jwtToken = buildClaim(getUserFromHeader(header));

		notificationService.changeFirstName_notificationRequest(user, request.getFirstname());
		
		return ChangeNameResponseDTO.builder().confirmationMessage("First name change to: " + request.getFirstname()+" has been requested.")
				.token(jwtToken).build();
	}

	
	@Override
	public String buildClaim(User user) {
		Map<String, Object> extraClaim = new HashMap<String, Object>();
		extraClaim.put("firstname", user.getFirstName());
		extraClaim.put("lastname", user.getLastName());
		extraClaim.put("role", user.getRole());

		var jwtToken = jwtService.generateToken(extraClaim, user);
		AuthenticationResponseDTO.builder().token(jwtToken).build();
		return jwtToken;
	}
	
	@Override
	public ChangeNameResponseDTO changeLastName(ChangeLastNameRequestDTO request, String header) {
		User user = getUserFromHeader(header);
		var jwtToken = buildClaim(user);
		
		notificationService.changeLastName_notificationRequest(user, request.getLastname());

		return ChangeNameResponseDTO.builder().confirmationMessage("Last name change to: " + request.getLastname()+" has been requested.")
				.token(jwtToken).build();
	}
	
	@Override
	public ConfirmationResponseDTO addImage(String header, UserImage image) {
		User user = getUserFromHeader(header);
		user.setUserImage(image);
		userRepository.save(user);
		return ConfirmationResponseDTO.builder().confirmationMessage("Image has been added.").build();
	}

	@Override
	public ConfirmationResponseDTO deleteImage(String header) {
		User user = getUserFromHeader(header);
		user.setUserImage(null);
		userRepository.save(user);
		return ConfirmationResponseDTO.builder().confirmationMessage("Image has been removed.").build();
	}

	@Override
	public ImageResponseDTO getImage(String header) {
		User user = getUserFromHeader(header);

		UserImage image = user.getUserImage();
		return ImageConverter.convertImageToResponseImage(image);
	}

	@Override
	public ConfirmationResponseDTO blockMyAccount(String header, String password) {
		User user = getUserFromHeader(header);

		user.setLocked(true);
		userRepository.save(user);
		notificationService.deleteAccount(user);

		return ConfirmationResponseDTO.builder()
				.confirmationMessage("Account has been locked.\nPlease wait for administration to delete.").build();
	}

	@Override
	public User getUserFromHeader(String header) {
		final String userEmail = jwtService.extractUserEmailFromHeader(header);
		return findByEmail(userEmail).orElseThrow();
	}

	@Override
	public List<User> findByFirstNameContainsIgnoreCase(String string) {
		return userRepository.findByFirstNameContainsIgnoreCase(string);
	}

	@Override
	public List<User> findByLastNameContainsIgnoreCase(String string) {
		return userRepository.findByLastNameContainsIgnoreCase(string);
	}

	@Override
	public List<User> findByEmailContainsIgnoreCase(String string) {
		return userRepository.findByEmailContainsIgnoreCase(string);
	}

	@Override
	public List<User> findByTags(String string) {
		return userRepository.findByTags(string);
	}

	@Override
	public User findById(int id) {
		return userRepository.findById(id).orElseThrow();
	}

	@Override
	public ConfirmationResponseDTO giveAccessToUsers(List<Integer> list, Category category) {
		for (int i : list) {
			try {
				User user = findById(i);
				notificationService.giveAccessToUser(user, category);
			} catch (Exception e) {
			}
		}
		return ConfirmationResponseDTO.builder().confirmationMessage("Access to exam has been granted.").build();

	}

	@Override
	public ConfirmationResponseDTO giveAccessToUsers(Category category) {
		List<Integer> list = findAll().stream()
				.filter(user -> user.getRole() == Role.TRAINEE)
			    .map(User::getId)
			    .collect(Collectors.toList());
		return giveAccessToUsers(list, category);
	}


}
