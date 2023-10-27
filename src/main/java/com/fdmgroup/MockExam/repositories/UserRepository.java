package com.fdmgroup.MockExam.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fdmgroup.MockExam.model.Question;
import com.fdmgroup.MockExam.model.User;

/**
 * Repository interface for managing User entities in the database. Extends
 * JpaRepository to inherit basic CRUD operations.
 */
@Repository
@Transactional(readOnly = true)
public interface UserRepository extends JpaRepository<User, Integer> {

	/**
	 * Find a User entity by their email address.
	 * 
	 * @param email The email address associated with the user to search for.
	 * @return An Optional containing the User entity matching the provided email,
	 *         or an empty Optional if not found.
	 */
	Optional<User> findByEmail(String email);

	/**
	 * Enable a user by updating their verified status.
	 * 
	 * @param email The email address of the user to enable.
	 * @return The number of affected rows in the database after the update.
	 */
	@Transactional
	@Modifying
	@Query("UPDATE User e SET e.verified = TRUE WHERE e.email = ?1")
	int enableUser(String email);

	@Query(value = "SELECT DISTINCT u.* FROM exam_user u " + "LEFT JOIN exam_user_tags ut ON u.id = ut.exam_user_id "
			+ "LEFT JOIN tag t ON qt.tags_id = t.id "
			+ "WHERE LOWER(u.first_name) LIKE LOWER(CONCAT('%', :first_name, '%')) "
			+ "OR LOWER(u.last_name) LIKE LOWER(CONCAT('%', :last_name, '%')) "
			+ "OR LOWER(u.email) LIKE LOWER(CONCAT('%', :email, '%')) "
			+ "OR LOWER(t.name) LIKE LOWER(CONCAT('%', :tag_name, '%'))", nativeQuery = true)
	List<User> findByFirstNameOrLastNameOrEmailOrTags(String first_name, String last_name, String email,
			String tag_name);
	

	
	List<User> findByFirstNameContainsIgnoreCase(String string);
	
	List<User> findByLastNameContainsIgnoreCase(String string);
	
	List<User> findByEmailContainsIgnoreCase(String string);
	
	@Query(value = "SELECT u.* FROM exam_user u "
			+ "LEFT JOIN exam_user_tags ut ON u.id = ut.exam_user_id "
			+ "LEFT JOIN tag t ON ut.tags_id = t.id "
			+ "WHERE LOWER(t.name) LIKE LOWER(CONCAT('%', :string, '%'))", nativeQuery = true)
	List<User> findByTags(String string);

}
