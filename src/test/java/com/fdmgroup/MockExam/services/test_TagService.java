package com.fdmgroup.MockExam.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fdmgroup.MockExam.model.Question;
import com.fdmgroup.MockExam.model.Tag;
import com.fdmgroup.MockExam.repositories.TagRepository;

@ExtendWith(MockitoExtension.class)

public class test_TagService {
	@InjectMocks
	private TagServiceImpl tagService;
	@Mock
	TagRepository tagRepository;
	String tagName = "TestTag";
	Tag tag = new Tag(tagName);
	List<Tag> list;

@BeforeEach
public void setUp() {
	list = new ArrayList<>();
	list.add(tag);

	
}
	@Test
	public void test_findByNameContainsIgnoreCases() {
		when(tagRepository.findByNameContainsIgnoreCase(tagName)).thenReturn(Optional.of(tag));

		Optional<Tag> result = tagService.findByNameContains(tagName);

		verify(tagRepository).findByNameContainsIgnoreCase(tagName);
		assertTrue(result.isPresent());
		assertEquals(tag, result.get());
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_newTag() {
		when(tagRepository.save(tag)).thenReturn(tag);

		Tag result = tagService.newTag(tagName);

		verify(tagRepository).save(tag);
		assertNotNull(result);
		assertEquals(tagName, result.getName());
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_findByName() {
		when(tagRepository.findByName(tagName)).thenReturn(Optional.of(tag));

		Optional<Tag> result = tagService.findByName(tagName);

		verify(tagRepository).findByName(tagName);

		assertTrue(result.isPresent());
		assertEquals(tag, result.get());
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_findAll() {
		when(tagRepository.findAll()).thenReturn(list);
		List<Tag> actual = tagService.getAllTags();
		assertEquals(list, actual);
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void test_getListOfTagsFromListOfQuestions() {
		Question question1 = new Question();
		Question question2 = new Question();
		Tag tag1 = new Tag("newTag1");
		Tag tag2 = new Tag("newTag2");
		list.add(tag1);
		list.add(tag2);
		List<Tag> list1 = new ArrayList<>();
		List<Tag> list2 = new ArrayList<>();
		list1.add(tag);
		list2.add(tag);
		list1.add(tag1);
		list2.add(tag2);
		question1.setTags(list1);
		question2.setTags(list2);
		List<Question> questions = new ArrayList<>();
		questions.add(question1);
		questions.add(question2);
		
		List<Tag> actual = tagService.getListOfTagsFromListOfQuestions(questions);
		
		assertEquals(3, actual.size());
		assertTrue(actual.contains(tag));
		assertTrue(actual.contains(tag1));
		assertTrue(actual.contains(tag2));
	}
}
