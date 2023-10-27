package com.fdmgroup.MockExam.services;

import java.util.List;
import java.util.Optional;

import com.fdmgroup.MockExam.model.Question;
import com.fdmgroup.MockExam.model.Tag;

public interface TagService {

	List<Tag> getAllTags();

	Optional<Tag> findByNameContains(String tag);

	Optional<Tag> findByName(String tag);

	Tag newTag(String tag);

	List<Tag> getListOfTagsFromListOfQuestions(List<Question> listOfQuestions);

}
