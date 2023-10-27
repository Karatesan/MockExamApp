package com.fdmgroup.MockExam.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fdmgroup.MockExam.model.Tag;
@Repository
public interface TagRepository extends JpaRepository<Tag, Integer>{

	public Optional<Tag> findByName(String tag);


	public Optional<Tag> findByNameContainsIgnoreCase(String tag);

}
