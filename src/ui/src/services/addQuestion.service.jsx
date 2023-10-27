class addQuestionService {
  //HELPER FUNCTIONS ------------------------------------------------------------

  isErrorPresent(type, questionError) {
    return questionError && questionError.some((e) => e.type === type);
  }

  removeError(type, questionError) {
    let newQuestionErrors = questionError.filter((e) => e.type !== type);
    return newQuestionErrors;
  }

  removeErrorIfPresent(type, questionError, setQuestionError) {
    if (this.isErrorPresent(type,questionError)) this.removeError(type, questionError, setQuestionError);
  }

  isImage(file) {
    if (file && file.type.startsWith("image/")) {
      return true;
    }
    return false;
  }

  deleteImage(setQuestion, question, setCurrentFocus, setIsImageValid) {
    if (question.file) {
      setCurrentFocus("none")
      setQuestion({ ...question, file: null });
      setIsImageValid(false)
    }
  }

  isCorrectAnswerPresent(answers){
    let isCorrectAnswerPresent = false;
    answers.forEach((a) => {
      if (a !== "") isCorrectAnswerPresent = true;
    });
    return isCorrectAnswerPresent;
  }

  //INPUT HANDLERS---------------------------------------------------------

  handleFileInput(
    event,
    setCurrentFocus,
    setQuestion,
    question,
  ) {
    setCurrentFocus("file");
    const imageFile = event.target.files[0];
      setQuestion({ ...question, file: imageFile });
      event.target.value = null;
      console.log(imageFile);

  }

  handleNewCategoryInput(
    e,
    setCurrentFocus,
    question,
    setQuestion,
    setNewCategory,
    questionError,
     setQuestionError
  ) {
    setCurrentFocus("category");
    this.removeErrorIfPresent("category", questionError, setQuestionError);
    const newCategoryValue = e.target.value;
    setQuestion({ ...question, category: newCategoryValue });
    if (newCategoryValue.trim() === "") {
      setNewCategory(false);
    } else {
      setNewCategory(true);
    }
  }

  handleBasicInput(e, name, setCurrentFocus, question, setQuestion, questionError, setQuestionError) {
    this.removeErrorIfPresent(name, questionError, setQuestionError);
    setCurrentFocus(name);
    setQuestion({ ...question, [name]: e.target.value });
  }


}

export default new addQuestionService();
