package com.fdmgroup.MockExam.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import com.fdmgroup.MockExam.dto.ConfirmationResponseDTO;
import com.fdmgroup.MockExam.dto.QuestionRequestDTO;
import com.fdmgroup.MockExam.dto.QuestionResponseDTO;
import com.fdmgroup.MockExam.exceptions.InvalidDocFileException;
import com.fdmgroup.MockExam.exceptions.InvalidXlsFileException;
import com.fdmgroup.MockExam.model.Category;
import com.fdmgroup.MockExam.model.Image;
import com.fdmgroup.MockExam.model.Question;
import com.fdmgroup.MockExam.model.Question.Level;
import com.fdmgroup.MockExam.model.Tag;

@ExtendWith(MockitoExtension.class)

public class test_QuestionsFromFileService {
	@InjectMocks
	private QuestionsFromFileServiceImpl questionsFromFileService;
	@Mock
	private AdminService adminService;
	@Mock
	private UserService userService;
	@Mock
	private CategoryService categoryService;
	@Mock
	private DTOService dTOService;
	@Mock
	private QuestionService questionService;

	Question question;
	Question question2;
	List<Question> list;
	List<String> answers;
	List<String> feedback;
	List<String> correctAnswersStrings;
	Category category = new Category("Java");
	Tag tag = new Tag("Java");
	Tag tag2 = new Tag("Java");
	List<Tag> tags;
	List<Tag> tags2;
	Image image;

	@BeforeEach
	public void setUp() {
		list = new ArrayList<>();
		tags = new ArrayList<>();
		answers = new ArrayList<>();
		feedback = new ArrayList<>();
		correctAnswersStrings = new ArrayList<>();
		tags.add(tag);
		answers.add("Who knows");
		answers.add("No one knows");
		answers.add("Nobody knows");
		answers.add("Somebody knows");
		feedback.add("Good");
		feedback.add("Bad");
		feedback.add("Bad");
		feedback.add("Bad");
		correctAnswersStrings.add("Who knows");
		image= new Image("image.jpg", 20, "image data".getBytes());
		question = new Question(category, Level.BEGINNER, "What is the meaning of Java?", answers, feedback,
				correctAnswersStrings, tags);
		question2 = new Question(category, Level.BEGINNER, "What is the meaning of UNIX?", answers, feedback,
				correctAnswersStrings, tags2);
	}

	@Test
	public void testAddQuestionsFromFiles_goodFiles_noRepeatingQuestions() throws IOException {
		byte[] docxContent = getClass().getResourceAsStream("/test.docx").readAllBytes();
		byte[] xlsxContent = getClass().getResourceAsStream("/test.xlsx").readAllBytes();
		MockMultipartFile docxFile = new MockMultipartFile("file", "test.docx",
				"application/vnd.openxmlformats-officedocument.wordprocessingml.document", docxContent);
		MockMultipartFile xlsxFile = new MockMultipartFile("answersFile", "test.xlsx",
				"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", xlsxContent);
		
		list.add(question2);
		
		when(adminService.addQuestion(Mockito.any(QuestionRequestDTO.class))).thenReturn(new QuestionResponseDTO());
		when(dTOService.questionRequestToEntity(any())).thenReturn(question);
		when(questionService.findByQuestionContent(anyString())).thenReturn(list);
		
		ConfirmationResponseDTO response = questionsFromFileService.addQuestionsFroMFile(docxFile, xlsxFile, true);

		verify(adminService, Mockito.times(2)).addQuestion(Mockito.any(QuestionRequestDTO.class));

		assertEquals("Questions have been added.", response.getConfirmationMessage());
	}

	// ---------------------------------------------------------------------------------------------
	@Test
	public void testAddQuestionsFromFiles_goodFiles_repeatingQuestions() throws IOException {
		byte[] docxContent = getClass().getResourceAsStream("/test.docx").readAllBytes();
		byte[] xlsxContent = getClass().getResourceAsStream("/test.xlsx").readAllBytes();
		MockMultipartFile docxFile = new MockMultipartFile("file", "test.docx",
				"application/vnd.openxmlformats-officedocument.wordprocessingml.document", docxContent);
		MockMultipartFile xlsxFile = new MockMultipartFile("answersFile", "test.xlsx",
				"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", xlsxContent);
		
		list.add(question);
		
		assertThrows(InvalidXlsFileException.class, () -> {
			questionsFromFileService.addQuestionsFroMFile(docxFile, docxFile, true);
		});
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void testAddQuestionsFromFiles_wrongXlsFile() throws IOException {
		byte[] docxContent = getClass().getResourceAsStream("/test.docx").readAllBytes();
		MockMultipartFile docxFile = new MockMultipartFile("file", "test.docx",
				"application/vnd.openxmlformats-officedocument.wordprocessingml.document", docxContent);

		assertThrows(InvalidXlsFileException.class, () -> {
			questionsFromFileService.addQuestionsFroMFile(docxFile, docxFile, true);
		});
	}

	// ---------------------------------------------------------------------------------------------

	@Test
	public void testAddQuestionsFromFiles_wrongDocFile() throws IOException {
		byte[] xlsxContent = getClass().getResourceAsStream("/test.xlsx").readAllBytes();
		MockMultipartFile xlsxFile = new MockMultipartFile("answersFile", "test.xlsx",
				"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", xlsxContent);

		assertThrows(InvalidDocFileException.class, () -> {
			questionsFromFileService.addQuestionsFroMFile(xlsxFile, xlsxFile, true);
		});
	}
}
