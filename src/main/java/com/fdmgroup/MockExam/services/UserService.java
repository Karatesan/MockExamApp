package com.fdmgroup.MockExam.services;

import com.fdmgroup.MockExam.dto.ChangeFirstNameRequestDTO;
import com.fdmgroup.MockExam.dto.ChangeLastNameRequestDTO;
import com.fdmgroup.MockExam.dto.ChangeNameResponseDTO;
import com.fdmgroup.MockExam.dto.ConfirmationResponseDTO;
import com.fdmgroup.MockExam.dto.ImageResponseDTO;
import com.fdmgroup.MockExam.model.Category;
import com.fdmgroup.MockExam.model.Question;
import com.fdmgroup.MockExam.model.User;
import com.fdmgroup.MockExam.model.UserImage;

import java.util.List;
import java.util.Optional;

/**
 * Interface to define the contract for the UserService. It provides methods for
 * user management, authentication, and password-related operations.
 */
public interface UserService {

	/**
	 * Generates a token for user authentication and verification purposes.
	 * 
	 * @return A generated authentication token.
	 */
	String generateToken();

	/**
	 * Saves a user entity in the database.
	 * 
	 * @param user The user entity to be saved.
	 * @return The saved user entity.
	 */
	User save(User user);

	/**
	 * Verifies a user's account.
	 * 
	 * @param user The user to be verified.
	 * @return The verified user entity.
	 */
	User verificateUser(User user);

	/**
	 * Registers a new user.
	 * 
	 * @param user The user entity representing the new user.
	 * @return The registered user entity.
	 */
	User register(User user);

	/**
	 * Finds a user by their email address.
	 * 
	 * @param email The email address of the user to be found.
	 * @return An optional containing the user if found, or empty if not found.
	 */
	Optional<User> findByEmail(String email);
	User findById(int id);

	/**
	 * Enables a user's account based on their email address.
	 * 
	 * @param email The email address of the user to enable.
	 * @return The number 1 if successful.
	 */
	int enableUser(String email);

	/**
	 * Updates a user's password.
	 * 
	 * @param password The new password to set for the user.
	 * @param user     The user whose password is being updated.
	 * @return The updated user entity.
	 */
	User updatePassword(String password, User user);

	/**
	 * Deletes a user entity from the database.
	 * 
	 * @param user The user entity to be deleted.
	 */
	void delete(User user);

	/**
	 * Changes the first name for the user identified by the provided JWT header,
	 * then generates new JWT token with new first name.
	 * 
	 * @param request The request containing new first name.
	 * @param header  The JWT token in the header identifying the user.
	 * @return A response indicating the success of the first name change.
	 */
	ChangeNameResponseDTO changeFirstName(ChangeFirstNameRequestDTO request, String header);

	/**
	 * Changes the last name for the user identified by the provided JWT header,
	 * then generates new JWT token with new last name.
	 * 
	 * @param request The request containing new last name.
	 * @param header  The JWT token in the header identifying the user.
	 * @return A response indicating the success of the last name change.
	 */
	ChangeNameResponseDTO changeLastName(ChangeLastNameRequestDTO request, String header);

	/**
	 * Adds an image to the user's profile and returns a confirmation response.
	 * 
	 * @param header The authorization header containing user information.
	 * @param image  The user image to be added.
	 * @return A ConfirmationResponseDTO indicating the success of the image
	 *         addition.
	 */
	ConfirmationResponseDTO addImage(String header, UserImage image);

	/**
	 * Deletes the user's profile image and returns a confirmation response.
	 * 
	 * @param header The authorization header containing user information.
	 * @return A ConfirmationResponseDTO indicating the success of the image
	 *         deletion.
	 */
	ConfirmationResponseDTO deleteImage(String header);

	/**
	 * Retrieves the user's profile image and returns it as an ImageResponseDTO.
	 * 
	 * @param header The authorization header containing user information.
	 * @return An ImageResponseDTO containing the user's profile image information.
	 */
	ImageResponseDTO getImage(String header);

	/**
	 * Blocks the user's account and returns a confirmation response.
	 * 
	 * @param header The authorization header containing user information.
	 * @return A ConfirmationResponseDTO indicating that the user's account has been
	 *         blocked.
	 */
	ConfirmationResponseDTO blockMyAccount(String header, String password);

	/**
	 * Retrieves a user based on the information extracted from the provided
	 * authorization header.
	 * 
	 * @param header The authorization header containing user information.
	 * @return The User object associated with the provided header.
	 */
	User getUserFromHeader(String header);

	List<User> findByFirstNameContainsIgnoreCase(String string);

	List<User> findByLastNameContainsIgnoreCase(String string);

	List<User> findByEmailContainsIgnoreCase(String string);

	List<User> findByTags(String string);

	 ConfirmationResponseDTO giveAccessToUsers(List<Integer> list, Category category);

	ConfirmationResponseDTO giveAccessToUsers(Category categoryEntity);

	List<User> findAll();

	String buildClaim(User user);



}
