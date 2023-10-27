import React, { useState } from "react";
import Button from "../atoms/Button";
import Timer from "../atoms/Timer";
import testPageService from "../../services/testPage.service";
import logo from "../../assets/logos/fdm_logo.png";
import { questionletters } from "../../assets/constants";
import { AiFillLock, AiFillUnlock } from "react-icons/ai";

const QuestionCard = ({
  question,
  handleSelectAnswer,
  studentAnswers,
  questionIndex,
  isFeedbackShowed,
  extra_lines,
  handleRightClick
}) => {
  const [activeFeedback, setActiveFeedback] = useState(null);
  const [lockFeedback, setLockFeedback] = useState(false);
  const heighnumbers = [200+extra_lines*60,300+extra_lines*50,350+extra_lines*50]
  const heightPossibilitiesArray = ["h-[150px]","h-[200px]","h-[300px]","h-[350px]","h-[400px]"];
  const heightChoice = ["h-["+heighnumbers[0]+"px]","h-["+heighnumbers[1]+"px]","h-["+heighnumbers[2]+"px]"];
  const AnswerHeight = ["h-[65px]","h-[80px]","h-[100px]"]
  
  const feedback = question.feedback.map((f, index) => {
    if (f && f !== "") {
      return f;
    } else {
      return "No feedback here!";
    }
  });

  const isAnswerCorrect = (answer, correctAnswers) => {
    return correctAnswers.includes(answer);
  };

  return (
    <>
      <div className="relative overflow-auto">
        <div
          id="feedback_container"
          className={`question-card-feedback absolute top-[-250px] bg-gray flex flex-col justify-center items-center rounded-2xl ${
            isFeedbackShowed && activeFeedback !== null ? "visible" : ""
          }`}
        >
          <p className="text-white">{feedback[activeFeedback]}</p>
        </div>

        <div id="question" className="w-[1000px] mb-5 mx-auto">
          <h2
            className={`text-white font-semibold ${
              question.questionContent.length < 100
                ? "text-[22px]"
                : "text-[18px]"
            } mb-5 lg:ml-0 md:ml-8 `}
          >
            {question.questionContent}
          </h2>
          <div className="bg-[#FFFFFF33] lg:w-[1000px] md:w-[850px] md:mx-auto h-[1px] mb-0 " />
        </div>
        <div
          className={`grid grid-cols-2 ${heightPossibilitiesArray[Math.ceil(question.answers.length/2 - 1)+ extra_lines]} mb-3 `}
        >
          {question.answers.map((answer, index) => {
            return (
              <div
                key={index}
                onMouseEnter={() => {
                  if (!lockFeedback) setActiveFeedback(index);
                }}
                onMouseLeave={() => {
                  if (!lockFeedback) setActiveFeedback(null);
                }}
                onClick={(event) => {
                  if (!isFeedbackShowed)
                    handleSelectAnswer(questionIndex, index);
                  else {
                    setLockFeedback(!lockFeedback);
                  }
                }}
                onContextMenu={(event) =>
                  handleRightClick(questionIndex, index, event)
                }
                className={`${
                  studentAnswers[index] === 1
                    ? "text__selectedAnswer"
                    : studentAnswers[index] === 2
                    ? "text__unsureAnswer"
                    : "border-none"
                } flex justify-start items-center test__answer bg-[#FFFFFF33] lg:w-[500px] ${AnswerHeight[extra_lines]} rounded-[20px] mx-[20px] mb-2 relative `}
              >
                {console.log(heighnumbers)}
                <div
                  className={`flex justify-center items-center rounded-full w-[39px] h-[39px]  ${
                    isFeedbackShowed
                      ? isAnswerCorrect(answer, question.correctAnswers)
                        ? "bg-[#309942]"
                        : "bg-[#CC3242]"
                      : "bg-gray"
                  } text-white font-semibold ml-3`}
                >
                  {questionletters[index]}
                </div>
                <p className="text-white ml-4 max-w-[400px] text-[17px]">
                  {answer}
                </p>
                {activeFeedback === index &&
                  isFeedbackShowed &&
                  (lockFeedback ? (
                    <AiFillLock className="text-white absolute right-4" />
                  ) : (
                    <AiFillUnlock className="text-white absolute right-4" />
                  ))}
              </div>
            );
          })}
        </div>
      </div>
    </>
  );
};

export default QuestionCard;
