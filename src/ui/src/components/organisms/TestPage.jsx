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
import examService from "../../services/exam.service";

const generateInitialAnswers = (questions, set) => {
  const initialArray = [];
  for (let i = 0; i < questions.length; i++) {
    const innerArray = new Array(questions[i].answers.length).fill(0); // Initialize with default values
    initialArray.push(innerArray);
  }
  return initialArray;
};

const TestPage = () => {
  //These lines allowe the user to pick just one answer.
  const [questions, setQuestions] = useState([]);
  const [error, setError] = useState(null);
  const [isLoading, setIsLoading] = useState(true);
  const [questionIndex, setQuestionIndex] = useState(0);
  const [studentAnswers, setStudentAnswers] = useState([]);
  const [displayQuestionNavPanel, setDisplayQUestionNavPanel] = useState(false);
  const [examFinished, setExamFinished] = useState(false);
  const [imageWindow, setImageWindow] = useState(false);
  const [confirmWindow, setConfirmWindow] = useState(false);
  const [score, setScore] = useState();
  const [showFeedback, setShowFeedback] = useState(false);
  const [examStarted, setExamStarted] = useState(false);

  //console.log(unsureAnswer);

  const location = useLocation();
  const params = new URLSearchParams(location.search);
  const examType = params.get("exam");
  const navigate = useNavigate();

  const hasPrevious = questionIndex > 0;
  const isDataPrepared = !isLoading && studentAnswers.length > 0;
  const unsureQuestions = isDataPrepared
    ? testPageService.countQuestions(studentAnswers, "unsure")
    : 0;
  const answeredQuestions = isDataPrepared
    ? testPageService.countQuestions(studentAnswers, "answered")
    : 0;
  const numberOfQuestions = isDataPrepared ? questions.length : 0;
  const hasNext = questionIndex < numberOfQuestions - 1;
  const time = 20;
  const category = params.get("category");
  const level = params.get("level");
  const examPassThreshold = 75;

  useEffect(() => {
    // Fetch data when the component mounts randomItemsInCategory?category=Business+Analysis&numberOfQuestions=20
    const url =
      examType === "mock"
        ? `http://localhost:8080/api/exam/takeExam?category=${params.get(
            "category"
          )}&level=${params.get("level")}`
        : `http://localhost:8080/api/questions/randomItemsInCategoryAndLevel?category=${params.get(
            "category"
          )}&level=${params.get("level")}`;
    axios
      .get(url, { headers: authHeader() })
      .then((response) => {
        setQuestions(
          examType === "mock" ? response.data.questionsDTO : response.data
        );
        setIsLoading(false);
      })
      .catch((error) => {
        setError(error.response.data);
        setIsLoading(false);
      });
  }, [isLoading]);

  useEffect(() => {
    setStudentAnswers(() => generateInitialAnswers(questions));
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
    if (examType !== "mock") setShowFeedback(null);
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
    for (let i = 0; i < newArray[questionIndex].length; ++i) {
      if (newArray[questionIndex][i] === 2) newArray[questionIndex][i] = 0;
    }
    setStudentAnswers(newArray);
  };

  //--------------------------------------------------------------------------------

  const handleRightClick = (questionIndex, answerIndex, event) => {
    event.preventDefault();
    const newArray = [...studentAnswers];
    const currentVal = newArray[questionIndex][answerIndex];
    newArray[questionIndex][answerIndex] = currentVal === 2 ? 0 : 2;
    for (let i = 0; i < newArray[questionIndex].length; ++i) {
      if (newArray[questionIndex][i] === 1) newArray[questionIndex][i] = 0;
    }
    setStudentAnswers(newArray);
  };

  //--------------------------------------------------------------------------------

  const handleFinishExam = () => {
    const transformedStudentAnswers = studentAnswers.map((answer, index) => {
      const givenAnswer = testPageService.answersIndexToStrings(
        answer,
        index,
        questions
      );
      const questionId = questions[index].id;
      return { questionId, givenAnswer };
    });
    if (examType === "mock") {
      axios
        .post(
          "http://localhost:8080/api/exam/completeExam",
          {
            answers: transformedStudentAnswers,
          },
          { headers: authHeader() }
        )
        .then((response) => {
          let studentScore = testPageService.calculateScore(
            response.data.answersDTO
          );
          setScore(studentScore);
        });
      setExamFinished(true);
    } else {
    }
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

  //console.log(questions[questionIndex].correctAnswers);

  //----------------------------------------------------------------------------

  return (
    <>
      {examFinished && (
        <div className="fixed top-[50%] left-[50%] -translate-x-[200px] -translate-y-[200px] z-50">
          <ScoreWIndow
            passThreshold={examPassThreshold}
            score={score}
            numberOfQuestions={numberOfQuestions}
            handleConfirm={() => navigate("/main")}
            handleSeeCheckAnswers="dasda"
          />
        </div>
      )}
      {!examStarted && (
        <div className="absolute top-[50%] left-[50%] -translate-x-[275px] -translate-y-[350px] z-50">
          <TakingExamInfo
            isMultiple={testPageService.areMultipleChoiceQuestionsPresent(
              questions
            )}
            startExam={setExamStarted}
            category={category}
            level={level}
            numberOfQuestions={numberOfQuestions}
            time={time}
            examType={examType}
          />
        </div>
      )}

      <div className={`mt-5 w-full flex items-center justify-center`}>
        {(examFinished || confirmWindow || imageWindow || !examStarted) && (
          <div className="overlay" />
        )}

        {confirmWindow && (
          <div className="z-30">
            <ConfirmWindow
              message={
                messages.find((message) => message.type === confirmWindow)
                  .message
              }
              confirm={() => handleConfirmWindow(confirmWindow === "cancel")}
              cancel={() => setConfirmWindow(null)}
            />
          </div>
        )}
        {error && <ErrorBox error={error} hideWindow={setError} />}
        {
          /*Loader w czasei wczytywania danych z db, nie jest wycentrowany -  -translate-x-[50px] -translate-y-[50px] cos nei dziala z tym kompoenntem - tymczasowe */
          isLoading && (
            <div className=" flex justify-center items-center">
              <PacmanLoader
                color={`#FFFFFF33`}
                size={100}
                loading={isLoading}
                className=" fixed top-[50%] left-[50%] -translate-x-[300px] -translate-y-[100px] "
              />
            </div>
          )
        }
        {isDataPrepared && (
          <>
            {examType === "mock" && (
              <TestPageSidePanel
                setDisplayQUestionNavPanel={setDisplayQUestionNavPanel}
                displayQuestionNavPanel={displayQuestionNavPanel}
                questions={questions}
                setQuestionIndex={setQuestionIndex}
                studentAnswers={studentAnswers}
                questionIndex={questionIndex}
              />
            )}

            <div className="bg-[#FFFFFF22] mb-4 lg:w-[1200px] md:w-[1000px] rounded-[20px] flex flex-col items-center justify-evenly relative">
              <button
                className=" absolute right-[20px] top-[20px] text-primary text-[30px]"
                title={"Cancel"}
                onClick={() => setConfirmWindow("cancel")}
              >
                <GiCancel />
              </button>
              {examStarted && (
                <TestPageInformationBar
                  questionIndex={questionIndex}
                  numberOfQuestions={numberOfQuestions}
                  handleFinishExam={handleFinishExam}
                  answeredQuestions={answeredQuestions}
                  time={time * 60}
                  examType={examType}
                />
              )}
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
              <QuestionCard
                question={questions[questionIndex]}
                studentAnswers={studentAnswers[questionIndex]}
                questionIndex={questionIndex}
                handleSelectAnswer={handleSelectAnswer}
                isFeedbackShowed={showFeedback}
                extra_lines={examService.maxNumberOfLines(
                  questions[questionIndex]
                )}
                handleRightClick={handleRightClick}
              />
              <div
                className={`mb-5 flex w-[30%] ${
                  examType === "mock" ? "justify-between" : "justify-center"
                } mt-[-30px]`}
              >
                {examType === "mock" && (
                  <Button
                    handleClick={handlePreviousQuestionClick}
                    title="Previous"
                  />
                )}

                <Button handleClick={handleNextQuestionClick} title="Next" />
              </div>
              <div>
                {examType === "mock" && (
                  <Button
                    handleClick={() => {
                      if (answeredQuestions === numberOfQuestions) {
                        setConfirmWindow("finish");
                      } else {
                        if (
                          unsureQuestions + answeredQuestions ===
                          numberOfQuestions
                        )
                          setConfirmWindow("unsure");
                        else {
                          if (unsureQuestions === 0)
                            setConfirmWindow("unansweredQuestions");
                          else setConfirmWindow("unansweredAndUnsureQuestions");
                        }
                      }
                    }}
                    title="Submit!"
                    buttonSize={1}
                  />
                )}
                {examType !== "mock" && (
                  <Button
                    handleClick={() => setShowFeedback(true)}
                    title="Check Answer!"
                    buttonSize={1}
                  />
                )}
              </div>
            </div>
          </>
        )}
      </div>
    </>
  );
};

export default TestPage;
