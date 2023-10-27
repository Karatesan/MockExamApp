import addQuestionService from "./addQuestion.service";
import { useEffect } from "react";

class addQuestionValidations {

  validateCategory(question, questionError) {

    const isCategoryErrorPresent = addQuestionService.isErrorPresent("category", questionError)
    const isCategoryValid = question.category === "" ? false : true
    let errors = (isCategoryErrorPresent && isCategoryValid) ? addQuestionService.removeError("category", questionError) : (questionError) ? [...questionError] : []

    if (!isCategoryErrorPresent && !isCategoryValid)
      errors.push({
        type: "category",
        message: "Select existing or create new CATEGORY.",
      });
    return errors;
  }
  validateLevel(question, questionError) {

    const isLevelErrorPresent = addQuestionService.isErrorPresent("level", questionError)
    const isLevelValid = question.level === "" ? false : true
    let errors = (isLevelErrorPresent && isLevelValid) ? addQuestionService.removeError("level", questionError) : (questionError) ? [...questionError] : []

    if (!isLevelErrorPresent && !isLevelValid)
      errors.push({
        type: "level",
        message: "Select question DIFFICULTY.",
      });
    return errors;
  }
  validateQuestionContent(question, questionError) {

    const isQuestionContentErrorPresent = addQuestionService.isErrorPresent("questionContent", questionError)
    const isQuestionContentValid = question.questionContent === "" ? false : true
    let errors = (isQuestionContentErrorPresent && isQuestionContentValid) ? addQuestionService.removeError("questionContent", questionError) : (questionError) ? [...questionError] : []

    if (!isQuestionContentErrorPresent && !isQuestionContentValid)
      errors.push({
        type: "questionContent",
        message: "QUESTION cannot be empty.",
      });
    return errors;
  }
  validateAnswers(question, questionError) {

    const isCorrectAnswerPresent = addQuestionService.isCorrectAnswerPresent(question.correctAnswers);
    const isAnswerNumbersErrorPresent = addQuestionService.isErrorPresent("answerNumbers", questionError);
    const isAnswerErrorPresent = addQuestionService.isErrorPresent("answers", questionError);
    const isCorrectAnswerErrorPresent = addQuestionService.isErrorPresent("correctAnswers", questionError);
    let errorArray = questionError ? [...questionError] : []
    if (isAnswerErrorPresent && question.answers.length >= 2)
      errorArray = addQuestionService.removeError("answers", errorArray);
    if (isCorrectAnswerErrorPresent && isCorrectAnswerPresent)
      errorArray = addQuestionService.removeError("correctAnswers", errorArray);
    if (isAnswerNumbersErrorPresent && question.answers.length <= 8) {
      errorArray = addQuestionService.removeError("answerNumbers", errorArray);
    }
    if (question.answers.length < 2 && !isAnswerErrorPresent) {
      errorArray.push({
        type: "answers",
        message: "There must be at least two ANSWERS",
      })
    }
    if (!isCorrectAnswerPresent && !isCorrectAnswerErrorPresent) {
      errorArray.push({
        type: "correctAnswers",
        message: "There must be at least one CORRECT ANSWER",
      })
    
      }
    if (!isAnswerNumbersErrorPresent && question.answers.length > 8) {
        console.log("Why are we not here?")
        errorArray.push({
          type: "answerNumbers",
          message: "There cannot be more than 8 answers",
        })
    }
    return errorArray;
  }
  validateFile(question, questionError) {

    const maxFileSize = 1000000;

    const isFileFormatErrorPresent = addQuestionService.isErrorPresent("file", questionError)
    const isFileSizeErrorPresent = addQuestionService.isErrorPresent("fileSize", questionError)
    const isFileEmptyErrorPresent = addQuestionService.isErrorPresent("emptyFile", questionError)
    const isFileSizeValid = question.file.size < maxFileSize;
    const isFormatValid = addQuestionService.isImage(question.file);
    let errorArray = questionError ? [...questionError] : []
    if (isFileFormatErrorPresent && isFormatValid) errorArray = addQuestionService.removeError("file", errorArray);
    if (isFileSizeErrorPresent && isFileSizeValid) errorArray = addQuestionService.removeError("fileSize", errorArray);
    if (!isFileFormatErrorPresent && !isFormatValid)
      errorArray.push({
        type: "file",
        message: "Wrong FILE type. File must be an IMAGE.",
      });

    if (!isFileSizeErrorPresent && !isFileSizeValid)
      errorArray.push({
        type: "fileSize",
        message: "File size cannot exceed 1mb.",
      });
    return errorArray;
  }

  validateAll(question) {

    let validationErrors = [];
    validationErrors.push(...this.validateCategory(question));
    validationErrors.push(...this.validateLevel(question));
    validationErrors.push(...this.validateQuestionContent(question));
    validationErrors = [...validationErrors, ...this.validateAnswers(question)]
    return validationErrors;
  }

  validate(question, field, questionError) {
    let validationErrors = [];
    switch (field) {

      case "submit": {
        validationErrors = this.validateAll(question)
        break;
      }
      case "category": {
        validationErrors = [...validationErrors, ...this.validateCategory(question, questionError)]
        break;
      }
      case "level": {
        validationErrors = [...this.validateLevel(question, questionError)];
        break;
      }
      case "questionContent": {
        validationErrors = [...this.validateQuestionContent(question, questionError)];
        break;
      }
      case "answers": {
        validationErrors = [...validationErrors, ...this.validateAnswers(question, questionError)]
        break;
      }
      case "file": {
        validationErrors = [...this.validateFile(question, questionError)];
        break;
      }
      case "none": {
        break;
      }
      default: {
        validationErrors = [...questionError]
      }
    }

    const cleanedErrors = validationErrors.filter(error => error != null);
    return cleanedErrors;
  }
}

export default new addQuestionValidations;