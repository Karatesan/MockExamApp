import React, { useState, useEffect, useRef } from "react";
import authHeader from "../../services/auth-header";
import { GiCancel } from "react-icons/gi";
import Input from "../atoms/Input";
import Button from "../atoms/Button";
import axios from "axios";
import { AiTwotoneEdit } from "react-icons/ai";
import { BiCloudUpload } from "react-icons/bi";
import { RiDeleteBin6Fill } from "react-icons/ri";
import testPageService from "../../services/testPage.service";
import questionService from "../../services/question.service";
import styles from "../../../style";
import { useNavigate, useParams } from "react-router";
import AddAnswerBox from "../molecules/AddAnswerBox";
import AnswerList from "../molecules/AnswerList";
import addQuestionService from "../../services/addQuestion.service";
import addQuestionValidations from "../../services/addQuestionValidations";
import AddQuestionFromFile from "../molecules/AddQuestionFromFile";
import QuestionContentInput from "../atoms/QuestionContentInput";

const initialData = {
  questionId: null,
  category: "",
  level: "",
  questionContent: "",
  answers: [],
  feedback: [],
  correctAnswers: [],
  file: null,
  tags: null,
  accessToAllUsers: false,
};

const emptyAnswerForm = {
  answer: "",
  feedback: "",
  isCorrect: true,
};

const levels = ["BEGINNER", "INTERMEDIATE", "EXPERT"];

const UpdateQuestion = (props) => {
  const [question, setQuestion] = useState(initialData);
  const [categories, setCategories] = useState([]);
  const [multiInputBox, setMultiInputBox] = useState(false);
  const [newCategory, setNewCategory] = useState(false);
  const [currentAnswerAdded, setcurrentAnswerAdded] = useState(emptyAnswerForm);
  const [editAnswer, setEditAnswer] = useState(null);
  const [answerError, setAnswerError] = useState(null);
  const [questionError, setQuestionError] = useState([]);
  const [questionFromFileBox, setQuestionFromFileBox] = useState(false);
  const [questionAdded, setQuestionAdded] = useState(false);
  const [isImageValid, setIsImageValid] = useState(false);
  const [imageNotEmpty, setImageNotEmpty] = useState(false);
  const [updatedImage, setUpdatedImage] = useState(false);
  const [answerBoxTitle, setAnswerBoxTitle] = useState("Add Answer");

  const { id } = useParams();
  const navigate = useNavigate();
  const firstRender = useRef(true);
  const currentFocus = useRef(null);

  const loadQuestion = () => {
    questionService.getQuestion(id).then((response) => {
      console.log(response);
      setQuestion({
        questionId: response.data.id,
        category: response.data.category,
        level: response.data.level,
        questionContent: response.data.questionContent,
        answers: response.data.answers,
        feedback: response.data.feedback,
        correctAnswers: response.data.correctAnswers,
        file: response.data.image,
        tags: response.data.tags,
        accessToAllUsers: false,
      });
      if (response.data.image.size > 0) {
        setImageNotEmpty(true);
      }
      console.log(question.file.base64image === undefined);
    });
  };

  const clearAll = () => {
    setQuestionFromFileBox(false);
    setQuestionAdded(false);
    setQuestion(initialData);
    setIsImageValid(false);
    setNewCategory(false);
    setImageNotEmpty(false);
  };

  /*---------------------------------------------------------------------------------*/

  useEffect(() => {
    axios
      .get("http://localhost:8080/api/questions/allCategories", {
        headers: authHeader(),
      })
      .then((response) => {
        setCategories(response.data); // Update the state with fetched data
      })
      .catch((error) => {
        console.error(error.response.data);
      });
  }, [questionAdded]);

  useEffect(() => {
    loadQuestion();
  }, []);
  /*---------------------------------------------------------------------------------*/

  useEffect(() => {
    if (!firstRender.current) {
      validateQuestion(question, currentFocus.current, questionError);
    } else {
      if (categories !== null) {
        firstRender.current = false;
      }
    }
  }, [question]);

  /*---------------------------------------------------------------------------------*/

  const handleQuestionUpload = async () => {
    setCurrentFocus("submit");
    const errors = validateQuestion(
      question,
      currentFocus.current,
      questionError
    );
    console.log(questionError);
    if (errors.length === 0) {
      try {
        const correctAnswers = question.correctAnswers.filter(
          (answer) => answer !== ""
        );

        const formData = new FormData();
        formData.append("questionId", question.questionId);
        formData.append("file", question.file);
        formData.append("category", question.category);
        formData.append("level", question.level);
        formData.append("questionContent", question.questionContent);
        formData.append("accessToAllUsers", question.accessToAllUsers);
        formData.append("tags", []);
        question.answers.forEach((answer) => {
          formData.append("answers", answer);
        });
        question.feedback.forEach((feedback) => {
          formData.append("feedback", feedback);
        });
        correctAnswers.forEach((correctAnswer) => {
          formData.append("correctAnswers", correctAnswer);
        });

        const response = await fetch(
          "http://localhost:8080/api/admin/updateQuestion",
          {
            method: "PUT",
            body: formData,
            headers: authHeader(),
          }
        );

        setQuestionAdded("The question has been updated in the database!");

        setTimeout(() => {
          clearAll();
          navigate("/main");
        }, 2000);

        firstRender.current = true;

        // Handle the response as needed
      } catch (error) {
        console.log(error);
      }
    }
  };

  const setCurrentFocus = (type) => {
    currentFocus.current = type;
  };

  /*---------------------------------------------------------------------------------*/

  //HANDLE INPUTS --------------------------------------------------------------------------------

  /*VALIDATIONS ---------------------------------------------------------------------------------*/

  const validateQuestion = (question, currentFocus, questionError) => {
    const errors = addQuestionValidations.validate(
      question,
      currentFocus,
      questionError
    );
    if (
      !addQuestionService.isErrorPresent("file", errors) &&
      !addQuestionService.isErrorPresent("fileSize", errors) &&
      question.file
    ) {
      setIsImageValid(true);
      if (question.file.size > 0) {
        setImageNotEmpty(true);
      }
    }
    setQuestionError(errors);
    return errors;
  };

  const fillAnswerForEdit = (index) => {
    const answer = {
      answer: question.answers[index],
      feedback: question.feedback[index],
      isCorrect: question.correctAnswers[index] != "",
    };
    return answer;
  };

  const handleSubmitAnswer = () => {
    if (currentAnswerAdded.answer === "") {
      setAnswerError("The answer cannot be empty");
    } else {
      if (editAnswer !== null) handleEditAnswer(editAnswer);
      else handleAddAnswer();
    }
  };

  const handleGoToEditAnswer = (index) => {
    setcurrentAnswerAdded(fillAnswerForEdit(index));
    setEditAnswer(index);
    setAnswerBoxTitle("Edit Answer");
    setMultiInputBox(true);
  };

  const deleteAnswer = (index) => {
    setCurrentFocus("answers");
    const newAnswers = [...question.answers];
    newAnswers.splice(index, 1);
    const correctAnswerIndex = question.correctAnswers.findIndex(
      (e) => e === question.answers[index]
    );
    const newCorrecyAnswers = [...question.correctAnswers];
    if (correctAnswerIndex >= 0) {
      newCorrecyAnswers.splice(correctAnswerIndex, 1);
    }
    const newFeedback = [...question.feedback];
    newFeedback.splice(index, 1);
    setQuestion({
      ...question,
      answers: newAnswers,
      correctAnswers: newCorrecyAnswers,
      feedback: newFeedback,
    });
  };

  /*---------------------------------------------------------------------------------------------------*/

  const handleAddAnswer = () => {
    setCurrentFocus("answers");
    const updatedAnswerArray = [...question.answers, currentAnswerAdded.answer];
    const updatedFeedbackArray = [
      ...question.feedback,
      currentAnswerAdded.feedback
        ? currentAnswerAdded.feedback
        : "No feedback for this answer!",
    ];
    let updatedCorrectAnswersArray = [...question.correctAnswers];
    updatedCorrectAnswersArray = [
      ...updatedCorrectAnswersArray,
      currentAnswerAdded.isCorrect ? currentAnswerAdded.answer : "",
    ];
    setQuestion({
      ...question,
      answers: updatedAnswerArray,
      feedback: updatedFeedbackArray,
      correctAnswers: updatedCorrectAnswersArray,
    });
    setcurrentAnswerAdded(emptyAnswerForm);
    setMultiInputBox(false);
    setAnswerError(null);
  };

  /*---------------------------------------------------------------------------------*/

  const handleEditAnswer = (index) => {
    setCurrentFocus("answers");
    console.log("jestem w edicie");
    console.log(currentFocus.current);
    let updatedAnswerArray = [...question.answers];
    updatedAnswerArray[index] = currentAnswerAdded.answer;

    let updatedFeedbackArray = [...question.feedback];
    updatedFeedbackArray[index] = currentAnswerAdded.feedback
      ? currentAnswerAdded.feedback
      : "No feedback for this answer!";

    let updatedCorrectAnswersArray = [...question.correctAnswers];
    updatedCorrectAnswersArray[index] = currentAnswerAdded.isCorrect
      ? currentAnswerAdded.answer
      : "";
    setQuestion({
      ...question,
      answers: updatedAnswerArray,
      feedback: updatedFeedbackArray,
      correctAnswers: updatedCorrectAnswersArray,
    });
    setcurrentAnswerAdded(emptyAnswerForm);
    setEditAnswer(null);
    setMultiInputBox(false);
    setAnswerError(null);
  };

  /*---------------------------------------------------------------------------------*/

  return (
    <div className={`${styles.flexCenter}`}>
      <div className="bg-[#FFFFFF22] w-full p-4 lg:h-[78.5vh] rounded-[20px] flex flex-col items-center justify-center relative">
        <button
          className=" absolute right-[20px] top-[20px] text-primary text-[30px] z-20"
          title={"Cancel"}
          onClick={() => navigate("/main")}
        >
          <GiCancel />
        </button>
        {/* Okno dodawania odpowiedzi, feedbacku do pytania----------------------------------------------------------*/}
        {multiInputBox && (
          <AddAnswerBox
            setMultiInputBox={setMultiInputBox}
            title={answerBoxTitle}
            multiInputBox={multiInputBox}
            answerError={answerError}
            currentAnswerAdded={currentAnswerAdded}
            setcurrentAnswerAdded={setcurrentAnswerAdded}
            handleSubmitAnswer={handleSubmitAnswer}
          />
        )}
        {/* ----------------------------------------------------------------------------------------------*/}
        <div
          className={`w-full lg:min-h-[700px] rounded-[20px] flex flex-col items-center relative `}
        >
          <h2 className="lg:text-[50px] md:text-[38px] text-white mt-2">
            Edit Question
          </h2>
          <div
            id="form_container"
            className="flex flex-col w-full justify-center"
          >
            {/*ERROR HANDLING --------------------------------------------------------------------------------------- */}
            <div className="absolute lg:w-[400px] h-[300px] lg:right-[30px] md:right-[5px] lg:top-[100px] md:top-[125px] pl-8">
              {questionError.length > 0 && (
                <ul className="list-disc text-red-600 lg:text-[18px] md:text-[14px] font-poppins font-semibold">
                  {questionError.map((e, index) => {
                    return (
                      <li key={index} className="mb-1">
                        {e.message}
                      </li>
                    );
                  })}
                </ul>
              )}
              {questionAdded && !questionFromFileBox && (
                <p className="text-green-700 font-poppins ">{questionAdded}</p>
              )}
            </div>
            {/*--------------------------------------------------------------------------------------- */}

            <div
              id="pick_category_container"
              className="flex items-center w-full lg:my-2 ml-5"
            >
              <select
                className="w-[200px] h-[50px] rounded-[20px] text-white border-[1px] border-primary transition-all bg-transparent pl-3 outline-none focus:border-[#7d35ea] appearance-none mr-4"
                id="dropdown"
                value={question.category}
                onChange={(e) =>
                  addQuestionService.handleBasicInput(
                    e,
                    "category",
                    setCurrentFocus,
                    question,
                    setQuestion,
                    questionError,
                    setQuestionError
                  )
                }
                disabled={newCategory}
              >
                <option value="">Select a category</option>
                {categories.map((category, index) => {
                  return (
                    <option key={index} value={category}>
                      {category}
                    </option>
                  );
                })}
              </select>
              <p className="text-[20px] text-white font-poppins text-center mr-4">
                or
              </p>
              <Input
                type="text"
                id="category"
                value={question.category}
                onChange={(e) =>
                  addQuestionService.handleNewCategoryInput(
                    e,
                    setCurrentFocus,
                    question,
                    setQuestion,
                    setNewCategory,
                    questionError,
                    setQuestionError
                  )
                }
                placeholder="Create new one..."
                styles="mb-0"
              />
            </div>
            <div className="lg:my-5 md:my-2 ml-5">
              <select
                className="w-[200px] h-[50px] rounded-[20px] text-white border-[1px] border-primary transition-all bg-transparent pl-3 outline-none focus:border-[#7d35ea] appearance-none mr-4"
                id="dropdown"
                value={question.level.toUpperCase()}
                onChange={(e) =>
                  addQuestionService.handleBasicInput(
                    e,
                    "level",
                    setCurrentFocus,
                    question,
                    setQuestion,
                    questionError,
                    setQuestionError
                  )
                }
              >
                <option value="">Select difficulty</option>
                {levels.map((level, index) => {
                  return (
                    <option key={index} value={level}>
                      {level}
                    </option>
                  );
                })}
              </select>
              {newCategory && (
                <label htmlFor="NewCategoryAccessibility" className="ml-8">
                  <input
                    type="checkbox"
                    name="NewCategoryAccessibility"
                    disabled={!newCategory}
                    onChange={(event) => {
                      setQuestion({
                        ...question,
                        accessToAllUsers: event.target.checked,
                      });
                    }}
                  />
                  <span className="text-white ml-4">
                    Give access to this category of exam to everyone?
                  </span>
                </label>
              )}
            </div>
            <QuestionContentInput
              type="text"
              id="questionContnent"
              value={question.questionContent}
              onChange={(e) =>
                addQuestionService.handleBasicInput(
                  e,
                  "questionContent",
                  setCurrentFocus,
                  question,
                  setQuestion,
                  questionError,
                  setQuestionError
                )
              }
              placeholder="Question"
              styles="my-2 ml-5"
            />
            {/*IMAGE INPUT ---------------------------------------------------------------------------------------------------*/}

            <div className="flex items-center my-2 ml-5">
              <div
                id="input_image_container"
                className="lg:w-[250px] md:w-[230px] flex lg:mr-10 md:mr-8 border-[2px] border-primary rounded-[30px] pl-4"
              >
                <input
                  disabled={isImageValid && imageNotEmpty}
                  id="file_input"
                  name="file_input"
                  type="file"
                  onChange={(event) => {
                    addQuestionService.handleFileInput(
                      event,
                      setCurrentFocus,
                      setQuestion,
                      question,
                      setQuestionError,
                      questionError,
                      validateQuestion
                    );
                  }}
                  className={`hidden`}
                />
                <label
                  className={`flex justify-center items-center cursor-pointer ${
                    isImageValid && imageNotEmpty ? "opacity-20" : ""
                  }`}
                  htmlFor="file_input"
                >
                  <BiCloudUpload className="text-[50px] text-primary hover:text-[#8231fdd4] transition-all" />
                  <span className="lg:text-[20px] md:text-[18px] font-bold text-white ml-4">
                    Upload Image
                  </span>
                </label>
              </div>

              {/*IMAGE INPUT ---------------------------------------------------------------------------------------------------*/}

              <div className="flex">
                <Button
                  styles="lg:mr-10 md:mr-2"
                  title={"Input Answer"}
                  variation={0}
                  buttonSize={1}
                  type="submit"
                  handleClick={() => {
                    setMultiInputBox(true);
                    setAnswerBoxTitle("Add Answer");
                  }}
                />
              </div>
            </div>
            <div className="h-[1px] w-[calc(100%-50px)] bg-[#FFFFFF22] mx-auto mb-3" />
            <div className="flex justify-evenly lg:items-center h-[200px]">
              {imageNotEmpty && isImageValid && (
                <div className="relative">
                  <img
                    className="max-w-[400px] max-h-[200px] rounded-[20px]"
                    src={
                      question.file.base64image === undefined
                        ? URL.createObjectURL(question.file)
                        : "data:image/png;base64," + question.file.base64image
                    }
                  />
                  <div
                    className="absolute w-full h-full top-0 opacity-0 hover:opacity-100 flex items-center justify-center cursor-pointer"
                    onClick={() =>
                      addQuestionService.deleteImage(
                        setQuestion,
                        question,
                        setCurrentFocus,
                        setIsImageValid
                      )
                    }
                  >
                    <RiDeleteBin6Fill
                      size="50"
                      color="#8231fdd4"
                      className=""
                    />
                  </div>
                </div>
              )}

              <div className="lg:w-[500px] lg:h-[200px] md:h-[135px] overflow-y-scroll hide_scrollbar">
                {/*Lsit of answers with feedbacks -----------------------------------------------------------------------------------------*/}
                <AnswerList
                  question={question}
                  handleGoToEditAnswer={handleGoToEditAnswer}
                  deleteAnswer={deleteAnswer}
                />
                {/*-----------------------------------------------------------------------------------------*/}
              </div>
            </div>
          </div>
          <div className="absolute bottom-1">
            <Button
              title={"Submit!"}
              variation={2}
              buttonSize={1}
              type="submit"
              handleClick={(event) => handleQuestionUpload(event)}
            />
          </div>
        </div>
      </div>
    </div>
  );
};

export default UpdateQuestion;
