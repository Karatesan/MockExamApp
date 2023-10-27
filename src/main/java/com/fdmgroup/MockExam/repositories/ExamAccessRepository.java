package com.fdmgroup.MockExam.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fdmgroup.MockExam.model.ExamAccess;
import com.fdmgroup.MockExam.model.User;

@Repository
public interface ExamAccessRepository extends JpaRepository<ExamAccess, Integer>{

	Optional<ExamAccess> findByUser(User user);

}
