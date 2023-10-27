class examService {
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

  isQuestionAnswered(answers) {
    for (let i = 0; i < answers.length; ++i) if (answers[i] === 1) return true;
    return false;
  }

  // -----------------------------------------------------------------------

  countAnsweredQuestions(answers) {
    let counter = 0;
    if (answers !== null) {
      for (let i = 0; i < answers.length; ++i) {
        if (this.isQuestionAnswered(answers[i])) ++counter;
      }
    }
    return counter;
  }

  // -----------------------------------------------------------------------

  isExamPassed(score, numberOfQuestions, threshold) {
    return this.calculatePercentage(score, numberOfQuestions) > threshold;
  }

  getTextWidth(text, font) {
    const canvas = this.getTextWidth.canvas || (this.getTextWidth.canvas = document.createElement("canvas"));
    const context = canvas.getContext("2d");
    context.font = font;
    console.log("font: " + context.font);
    const metrics = context.measureText(text);
    return metrics.width;
  }

  determineHeightInLines(answer) {
    return getTextWidth(answer, 'normal 17px sans-serif');
  }

  maxNumberOfLines(question){
    var extra_lines=0;
    
    question.answers.map((answer) => {
      let number_of_lines=this.getTextWidth(answer,'normal 17px sans-serif')/350;
    
    if((number_of_lines)>2 && extra_lines===0){extra_lines=1;}
    if((number_of_lines)>3.5) {extra_lines=2;}
  })
  return extra_lines;
  }


}
export default new examService();
