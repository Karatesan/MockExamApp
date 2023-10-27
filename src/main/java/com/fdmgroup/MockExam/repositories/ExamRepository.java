package com.fdmgroup.MockExam.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.fdmgroup.MockExam.model.Exam;
import com.fdmgroup.MockExam.model.User;

/**
 * Repository interface for managing Exam entities in the database.
 * Extends JpaRepository to inherit basic CRUD operations.
 */
@Repository
public interface ExamRepository extends JpaRepository<Exam, Integer> {

	List<Exam> findAllByUser(User user);

}
