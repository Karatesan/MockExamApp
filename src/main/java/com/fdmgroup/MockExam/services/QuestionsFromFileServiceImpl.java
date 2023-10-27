package com.fdmgroup.MockExam.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFPicture;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fdmgroup.MockExam.dto.ConfirmationResponseDTO;
import com.fdmgroup.MockExam.dto.QuestionRequestDTO;
import com.fdmgroup.MockExam.exceptions.InvalidDocFileException;
import com.fdmgroup.MockExam.exceptions.InvalidXlsFileException;
import com.fdmgroup.MockExam.model.Category;
import com.fdmgroup.MockExam.model.Question;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * The class implements the QuestionsFromFileService interface. Provides a
 * method to add questions from a file and save them in the database. It parses
 * Excel and Word documents to extract question and answer data, along with
 * related information.
 */
@Data
@AllArgsConstructor
@Service
public class QuestionsFromFileServiceImpl implements QuestionsFromFileService {

	private AdminService adminService;
	private UserService userService;
	private CategoryService categoryService;
	private DTOService dTOService;
	private QuestionService questionService;

	@Override
	public ConfirmationResponseDTO addQuestionsFroMFile(MultipartFile file, MultipartFile answersFile,
			boolean accessToAllUsers) {
		String category = "";
		List<String> feedback = new ArrayList<>();
		List<String> answers = new ArrayList<>();
		List<String> correctAnswersABC = new ArrayList<>();
		List<String> correctAnswersStrings = new ArrayList<>();
		List<String> levels = new ArrayList<>();
		List<String> tagsExcel = new ArrayList<>();
		List<String> tags = new ArrayList<>();
		List<QuestionRequestDTO> questionsToAdd = new ArrayList<>();
		int questionCounter = 0;
		boolean badQuestion = false;

		/**
		 * This 'try' part is for get which answers are correct.
		 */
		try (XSSFWorkbook workbook = new XSSFWorkbook(answersFile.getInputStream())) {

			int sheetIndex = 1;
			int columnIndex = 2;
			int startRow = 2;

			for (int rowNum = startRow; rowNum <= workbook.getSheetAt(sheetIndex).getLastRowNum(); rowNum++) {
				Row row = workbook.getSheetAt(sheetIndex).getRow(rowNum);
				if (row != null) {
					Cell answerCell = row.getCell(columnIndex);
					if (answerCell != null) {
						correctAnswersABC.add(answerCell.getStringCellValue());
					}
					Cell levelCell = row.getCell(columnIndex + 1);
					if (levelCell != null) {
						levels.add(levelCell.getStringCellValue());
					}
					Cell tagsCell = row.getCell(columnIndex + 2);
					if (tagsCell != null) {
						tagsExcel.add(tagsCell.getStringCellValue());
					}
				}
			}

		} catch (Exception e1) {

			throw new InvalidXlsFileException("Invalid xls/xlsx file.");
		}
		/**
		 * Next 'try' part is responsible for getting questions: content, answers,
		 * images and feedback.
		 */
		try (XWPFDocument document = new XWPFDocument(file.getInputStream())) {

			List<XWPFParagraph> paragraphs = document.getParagraphs();
			byte[] imageBytes = null;
			String imageName = "Empty Image";
			category = paragraphs.get(0).getText();

			for (int i = 1; i < paragraphs.size(); i++) {
				XWPFParagraph paragraph = paragraphs.get(i);
				if (paragraph.getNumFmt() != null && paragraph.getNumFmt().equals("decimal")) {
					badQuestion=false;

					String questionContent = paragraph.getText();

					i++;
					paragraph = paragraphs.get(i);

					/*
					 * Next 10 lines is for check if there is an image for question and set up
					 * values for returning image.
					 */
					List<XWPFRun> runs = paragraph.getRuns();
					boolean hasImage = false;
					for (XWPFRun run : runs) {
						List<XWPFPicture> picturesRun = run.getEmbeddedPictures();
						for (XWPFPicture picture : picturesRun) {
							imageName = picture.getPictureData().getFileName();
							imageBytes = picture.getPictureData().getData();
							hasImage = true;
						}
					}

					paragraph = paragraphs.get(i);

					while (paragraphs.get(++i).getText().trim().isEmpty()) {
					}

					answers = new ArrayList<>();
					feedback = new ArrayList<>();
					paragraph = paragraphs.get(i);
					/**
					 * This 'while' part is checking and adding answers and feedback.
					 */
					while (paragraph.getNumFmt() != null) {

						if (paragraph.getNumFmt().equals("upperLetter"))
							answers.add(paragraph.getText());

						if (paragraph.getNumFmt().equals("lowerLetter"))
							feedback.add(paragraph.getText());

						i++;

						if (i < paragraphs.size())
							paragraph = paragraphs.get(i);
						else
							break;
					}

					correctAnswersStrings = new ArrayList<>();

					String[] listStr = correctAnswersABC.get(questionCounter).replace(" ", "").split(",");

					if (listStr.length > 0) {

						for (String string : listStr) {

							correctAnswersStrings.add(answers.get(string.charAt(0) - 65));

						}
					} else {
						throw new InvalidXlsFileException(
								"Invalid xls/xlsx file. Check question number: " + questionCounter++);
					}

//					tags = new ArrayList<>();
					tags = Arrays.asList(tagsExcel.get(questionCounter).split(", "));

					/**
					 * Create question for return with or without an image.
					 */
					System.out.println(questionCounter);
					if (hasImage) {
						
						MultipartFile imageFile = new MockMultipartFile(imageName, imageName, "image", imageBytes);
						QuestionRequestDTO question = new QuestionRequestDTO(category, levels.get(questionCounter),
								questionContent, answers, feedback, correctAnswersStrings, imageFile,tags);
						Question newQuestion = dTOService.questionRequestToEntity(question);
						List<Question> found = questionService.findByQuestionContent(questionContent);
						System.out.println("check");
						for(Question foundQ : found) {
							if(newQuestion.getAnswers().equals(foundQ.getAnswers()) &&
									newQuestion.getFeedback().equals(foundQ.getFeedback()) &&
									newQuestion.getCategory().equals(foundQ.getCategory()) &&
									newQuestion.getCorrectAnswers().equals(foundQ.getCorrectAnswers()) &&
									newQuestion.getLevel().equals(foundQ.getLevel()) &&
									newQuestion.getTags().equals(foundQ.getTags()) 
//									&&
//									newQuestion.getImage().equals(foundQ.getImage())
									)
							{	
								badQuestion=true;
							throw new InvalidDocFileException("Invalid doc/docx file. Question number: " + questionCounter +" already exists");
							}
						}
						questionsToAdd.add(question);

					} else {
						QuestionRequestDTO question = new QuestionRequestDTO(category, levels.get(questionCounter),
								questionContent, answers, feedback, correctAnswersStrings, tags);
						
						Question newQuestion = dTOService.questionRequestToEntity(question);
						List<Question> found = questionService.findByQuestionContent(questionContent);
						for(Question foundQ : found) {
							if(newQuestion.getAnswers().equals(foundQ.getAnswers()) &&
									newQuestion.getFeedback().equals(foundQ.getFeedback()) &&
									newQuestion.getCategory().equals(foundQ.getCategory()) &&
									newQuestion.getCorrectAnswers().equals(foundQ.getCorrectAnswers()) &&
									newQuestion.getLevel().equals(foundQ.getLevel()) &&
									newQuestion.getTags().equals(foundQ.getTags()))
								{
								badQuestion=true;
							throw new InvalidDocFileException("Invalid doc/docx file. Question number: " + questionCounter +" already exists");
								}
						}
						questionsToAdd.add(question);
					}
					questionCounter++;

				}
			}
		} catch (Exception e) {
			if (questionCounter == 0)
				throw new InvalidDocFileException("Invalid doc/docx file.");
			else
				if(badQuestion) 								
					throw new InvalidDocFileException("Invalid doc/docx file. Question number: " + questionCounter++ +" already exists");
				else
				throw new InvalidDocFileException("Invalid doc/docx file. Check question number: " + questionCounter++);

		}

		for (QuestionRequestDTO quest : questionsToAdd) {
			adminService.addQuestion(quest);
		}
		Category categoryEntity = categoryService.findOrCreateCategory(category);

		if (accessToAllUsers)
			userService.giveAccessToUsers(categoryEntity);

		return ConfirmationResponseDTO.builder().confirmationMessage("Questions have been added.").build();
	}

}
