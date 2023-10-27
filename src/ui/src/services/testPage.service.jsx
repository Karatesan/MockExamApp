class testPageService {
  isAnswerCorrect(studentAnswers, correctAnswers) {
    if (
      studentAnswers === null ||
      studentAnswers.length !== correctAnswers.length
    )
      return false;
    else {
      studentAnswers.sort();
      correctAnswers.sort();
      for (let u = 0; u < studentAnswers.length; ++u) {
        if (studentAnswers[u] !== correctAnswers[u]) return false;
      }
      return true;
    }
  }

  calculateScore(data) {
    let score = 0;
    for (let i = 0; i < data.length; ++i) {
      const studentAnswers = data[i].givenAnswer;
      const correctAnswers = data[i].questionResponseDTO.correctAnswers;
      if (this.isAnswerCorrect(studentAnswers, correctAnswers)) score++;
    }
    return score;
  }

  // -----------------------------------------------------------------------

  calculatePercentage(score, numOfQuestions) {
    return Math.round((score / numOfQuestions) * 100);
  }

  // -----------------------------------------------------------------------

  getBase64Url(image) {
    return `data:image/webp;base64,${image}`;
  }

  // -----------------------------------------------------------------------

  isQuestionAnsweredOrUnsure(answers, type) {
    //2 stands for unsure, 1 for answered, 0 not touched
    for (let i = 0; i < answers.length; ++i)
      if (answers[i] === type) return true;
    return false;
  }

  isQuestionUnsure(answers) {
    for (let i = 0; i < answers.length; ++i) if (answers[i] === 2) return true;
    return false;
  }

  areMultipleChoiceQuestionsPresent(questions) {
    let multiple = false;
    for (let i = 0; i < questions.length; ++i) {
      if (questions[i].correctAnswers.length > 1) multiple = true;
    }
    return multiple;
  }

  // -----------------------------------------------------------------------

  countQuestions(answers, type) {
    console.log(type);
    let numType = 0;
    if (type === "answered") numType = 1;
    if (type === "unsure") numType = 2;
    let counter = 0;
    if (answers !== null) {
      for (let i = 0; i < answers.length; ++i) {
        if (this.isQuestionAnsweredOrUnsure(answers[i], numType)) ++counter;
      }
    }
    return counter;
  }

  // -----------------------------------------------------------------------

  isExamPassed(data, threshold) {
    const score = this.calculateScore(data);
    const numberOfQuestions = data.length;
    return this.calculatePercentage(score, numberOfQuestions) > threshold;
  }

  // -----------------------------------------------------------------------

  answersIndexToStrings(answers, index, questions) {
    const questionAnswers = questions[index].answers;
    let answersStrings = [];
    for (let i = 0; i < answers.length; ++i) {
      if (answers[i] === 1) answersStrings.push(questionAnswers[i]);
    }
    return answersStrings;
  }

  // -----------------------------------------------------------------------
}
export default new testPageService();
