package com.fdmgroup.MockExam.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.fdmgroup.MockExam.model.Question;
import com.fdmgroup.MockExam.model.Tag;
import com.fdmgroup.MockExam.repositories.NotificationRepository;
import com.fdmgroup.MockExam.repositories.QuestionRepository;
import com.fdmgroup.MockExam.repositories.TagRepository;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {
	private final TagRepository tagRepository;
	
	@Override
	public List<Tag> getAllTags(){
		return tagRepository.findAll();
	}

	@Override
	public Optional<Tag> findByNameContains(String tag) {
		return tagRepository.findByNameContainsIgnoreCase(tag);
	}

	@Override
	public Tag newTag(String tag) {
		return tagRepository.save(new Tag(tag));
	}

	@Override
	public Optional<Tag> findByName(String tag) {
		return tagRepository.findByName(tag);

	}

	@Override
	public List<Tag> getListOfTagsFromListOfQuestions(List<Question> listOfQuestions) {
		List<Tag> list = new ArrayList<>();
		for (Question question : listOfQuestions) {
			list.addAll(question.getTags());
		}
		Set<Tag> newTags = new HashSet<>(list);
		list.clear();
		list.addAll(newTags);
		return list;
	}

}
