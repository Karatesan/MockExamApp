import { useEffect, useState } from "react";
import { questionletters } from "../../assets/constants";
import logo from "../../assets/logos/fdm_logo.png";
import axios from "axios";
import ErrorBox from "../atoms/ErrorBox";
import PacmanLoader from "react-spinners/ClipLoader";
import Button from "../atoms/Button";
import Timer from "../atoms/Timer";
import authHeader from "../../services/auth-header";
import { useLocation, useNavigate } from "react-router";
import ConfirmWindow from "../atoms/ConfirmWindow";
import testPageService from "../../services/testPage.service";
import TestPageSidePanel from "../molecules/TestPageSidePanel";
import ScoreWIndow from "../atoms/ScoreWIndow";
import TestPageInformationBar from "../molecules/TestPageInformationBar";
import QuestionCard from "../molecules/QuestionCard";
import TakingExamInfo from "./TakingExamInfo";
import { GiCancel } from "react-icons/gi";
import { testPageConfirmMessages as messages } from "../..//assets/constants";
import Loader from "../atoms/Loader";
import { FaExclamationCircle } from "react-icons/fa";
import ReportQuestion from "../molecules/ReportQuestion";
import examService from "../../services/exam.service";


const generateInitialAnswers = (questions) => {
  const initialArray = [];
  for (let i = 0; i < questions.length; i++) {
    const innerArray = new Array(questions[i].answers.length).fill(0); // Initialize with default values
    initialArray.push(innerArray);
  }

  return initialArray;
};

const ReviewExam = ({ exam, closeWindow }) => {
  //These lines allowe the user to pick just one answer.
  const [questions, setQuestions] = useState([]);
  const [error, setError] = useState(null);
  const [isLoading, setIsLoading] = useState(true);
  const [questionIndex, setQuestionIndex] = useState(0);
  const [studentAnswers, setStudentAnswers] = useState([]);
  const [displayQuestionNavPanel, setDisplayQUestionNavPanel] = useState(false);
  const [imageWindow, setImageWindow] = useState(false);
  const [confirmWindow, setConfirmWindow] = useState(false);
  const [score, setScore] = useState();
  const [examStarted, setExamStarted] = useState(false);
  const [reportQuestionWindow,setReportQuestionWindow] = useState(false);

  const examType = "questions";
  const navigate = useNavigate();

  const hasPrevious = questionIndex > 0;
  const isDataPrepared = !isLoading && studentAnswers.length > 0;
  const answeredQuestions = isDataPrepared
    ? testPageService.countQuestions(studentAnswers, "answered")
    : 0;
  const numberOfQuestions = isDataPrepared ? questions.length : 0;
  const hasNext = questionIndex < numberOfQuestions - 1;
  const time = 20;

  useEffect(() => {
    if (exam) {
      const questions = exam.answersDTO.map((answer) => {
        return {
          id: answer.questionResponseDTO.id,
          category: answer.questionResponseDTO.category,
          level: answer.questionResponseDTO.level,
          questionContent: answer.questionResponseDTO.questionContent,
          answers: answer.questionResponseDTO.answers,
          feedback: answer.questionResponseDTO.feedback,
          image: answer.questionResponseDTO.image,
          correctAnswers: answer.questionResponseDTO.correctAnswers,
        };
      });
      setIsLoading(false);
      setQuestions(questions);
    }
  }, []);

  useEffect(() => {
    if (questions[0]) {
      var emptyAnswers = generateInitialAnswers(questions);
      for (let i = 0; i < emptyAnswers.length; ++i) {
        var studentAnswerss = exam.answersDTO[i].givenAnswer;
        var allAnswers = questions[i].answers;
        for (let u = 0; u < emptyAnswers[i].length; ++u) {
          if (studentAnswerss.includes(allAnswers[u])) emptyAnswers[i][u] = 1;
        }
      }
      setStudentAnswers(emptyAnswers);
    }
  }, [questions]);

  //Handlers -----------------------------------------------------------------------

  const handleImageWindowToggle = () => {
    if (!imageWindow) {
      setImageWindow(true);
    } else {
      setImageWindow(false);
    }
  };

  // -----------------------------------------------------------------------

  function handleNextQuestionClick() {
    if (hasNext) {
      setQuestionIndex(questionIndex + 1);
    } else {
      setQuestionIndex(0);
    }
  }

  //--------------------------------------------------------------------------------

  function handlePreviousQuestionClick() {
    if (hasPrevious) {
      setQuestionIndex(questionIndex - 1);
    } else {
      setQuestionIndex(numberOfQuestions - 1);
    }
  }

  //--------------------------------------------------------------------------------

  const handleSelectAnswer = (questionIndex, answerIndex) => {
    const newArray = [...studentAnswers];
    const currentVal = newArray[questionIndex][answerIndex];
    newArray[questionIndex][answerIndex] = currentVal === 1 ? 0 : 1;
    setStudentAnswers(newArray);
  };

  //----------------------------------------------------------------------------

  const handleConfirmWindow = (examCanceled) => {
    if (!examCanceled) {
      setConfirmWindow(null);
      setExamFinished(true);
      handleFinishExam();
    } else {
      navigate("/main");
    }
  };

  //----------------------------------------------------------------------------
  return (
    <>
      <div className={`w-full h-full flex items-center justify-center`}>
        {error && <ErrorBox error={error} hideWindow={setError} />}
        {isLoading && <Loader isLoading={isLoading} />}
        {isDataPrepared && (
          <>
            {/* <TestPageSidePanel
                setDisplayQUestionNavPanel={setDisplayQUestionNavPanel}
                displayQuestionNavPanel={displayQuestionNavPanel}
                questions={questions}
                setQuestionIndex={setQuestionIndex}
                studentAnswers={studentAnswers}
                questionIndex={questionIndex}
              /> */}

            <div className="bg-[#242222ea] w-full h-full rounded-[20px] flex flex-col items-center justify-evenly relative">
              <div
                id="topPanel"
                className="flex justify-between lg:w-[1000px] md:w-[900px] mt-5 "
              >
                <h3 className="text-white flex-1 text-left text-[20px] mt-4">
                  Question {questionIndex + 1 + "/" + numberOfQuestions}
                </h3>
                <Button 
              title={"Report Question"}
              variation={1}
              buttonSize={1}
              handleClick={() =>setReportQuestionWindow(true)}
              />
                <h3 className="text-white flex-1 text-right text-[20px] mt-4">
                  Questions answered:{" "}
                  {answeredQuestions + "/" + numberOfQuestions}
                </h3>
              </div>
              <button
                className=" absolute right-[20px] top-[20px] text-primary text-[30px]"
                title={"Cancel"}
                onClick={() => closeWindow(null)}
              >
                <GiCancel />
              </button>
              
              {/* {examType === "mock" && examStarted && (
                <TestPageInformationBar
                  questionIndex={questionIndex}
                  numberOfQuestions={numberOfQuestions}
                  handleFinishExam={handleFinishExam}
                  answeredQuestions={answeredQuestions}
                  time={time*60}
                />
              )} */}
              <div className="flex flex-row">
              <div
                id="imageContainer"
                className="w-[500px] h-[300px] relative mt-4"
                onClick={() => handleImageWindowToggle()}
              >
                <div
                  className={`top-[-100px] left-[-150px] image-container absolute ${
                    imageWindow ? "visible" : ""
                  } `}
                  onClick={(e) => {
                    e.stopPropagation(); // Stop event propagation
                    handleImageWindowToggle();
                  }}
                >
                  <img
                    src={testPageService.getBase64Url(
                      questions[questionIndex].image.base64image
                    )}
                    className="object-contain w-[100%] h-[100%]"
                  />
                </div>

                {!imageWindow &&
                questions[questionIndex].image.name === "Empty Image" ? (
                  <img
                    src={logo}
                    className="w-[100%] h-[100%] object-contain"
                  />
                ) : (
                  <img
                    src={testPageService.getBase64Url(
                      questions[questionIndex].image.base64image
                    )}
                    className="object-contain w-[100%] h-[100%]"
                  />
                )}
              </div>
              </div>
              <QuestionCard
                question={questions[questionIndex]}
                studentAnswers={studentAnswers[questionIndex]}
                questionIndex={questionIndex}
                handleSelectAnswer={handleSelectAnswer}
                isFeedbackShowed={true}
                extra_lines={examService.maxNumberOfLines(questions[questionIndex])}
              />
              <div
                className={`mb-5 flex w-[30%] ${
                  examType === "mock" ? "justify-between" : "justify-center"
                } mt-[-30px]`}
              >
                <div className="absolute left-[20px] top-[25vh]">
                <Button
                  handleClick={handlePreviousQuestionClick}
                  title="Previous"
                />
                </div>
                <div className="absolute right-[20px] top-[25vh]">
                <Button handleClick={handleNextQuestionClick} title="Next" />
                </div>       
              </div>
              
              

{reportQuestionWindow && <ReportQuestion 
              setReportQuestionWindow={setReportQuestionWindow}
              question_id={questions[questionIndex].id}
              />}
              
            </div>
          </>
        )}
      </div>
    </>
  );
};

export default ReviewExam;
