import React, { useState } from "react";
import { GiCancel } from "react-icons/gi";
import Input from "../atoms/Input";
import Button from "../atoms/Button";
import { BiCloudUpload } from "react-icons/bi";
import axios from "axios";
import authHeader from "../../services/auth-header";
import Loader from "../atoms/Loader";

const AddQuestionFromFile = ({
  setQuestionFromFileBox,
  questionFromFileBox,
  setQuestionFile,
  questionFile,
  setAnswersFile,
  answersFile,
  setQuestionAdded,
  questionAdded,
}) => {
  const [questionError, setQuestionError] = useState(null);
  const [answerError, setAnswerError] = useState(null);
  const [cantSubmitError, setCantSubmitError] = useState(null);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState(null);
  const [accessToAllUsers, setAccessToAllUsers] = useState(false);

  const isAnswersFileValid = answersFile && !answerError;
  const isQuestionFileValid = questionFile && !questionError;

  const canSubmit = isAnswersFileValid && isQuestionFileValid;

  const handleUploadQuestionsFile = (event) => {
    const file = event.target.files[0];
    if (file) {
      if (
        file.type ===
          "application/vnd.openxmlformats-officedocument.wordprocessingml.document" || // For docx
        file.type === "application/msword"
      ) {
        // For doc
        setQuestionFile(file);
        setQuestionError(null);
      } else {
        setQuestionError("Not a valid Word file");
      }
    }
  };

  const handleUploadAnswersFile = (event) => {
    const file = event.target.files[0];
    if (file) {
      if (
        file.type ===
          "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" || // For XLSX
        file.type === "application/vnd.ms-excel"
      ) {
        // For XLS
        setAnswerError(null);
        setAnswersFile(file);
      } else {
        setAnswerError("Not a valid Excel file");
      }
    }
  };

  const handleSubmit = () => {
    console.log("handlujemy");
    if (!canSubmit) setCantSubmitError("Upload files first!");
    else {
      setCantSubmitError(null);
      setIsLoading(true);
      axios
        .post(
          "http://localhost:8080/api/admin/questionsFromFile",
          {
            file: questionFile,
            answersFile: answersFile,
            accessToAllUsers: accessToAllUsers
          },
          {
            headers: {
              ...authHeader(),
              "Content-Type": "multipart/form-data",
            },
          }
        )
        .then((response) => {
          if (response.request.status === 200) {
            setQuestionAdded("Questions added!");
            setTimeout(() => {
              setIsLoading(false);
              setQuestionAdded(false);
              clearAllMessages();
            }, 2000);
          }
        })
        .catch((error) => {
          setError(error);
        });
    }
  };

  const clearAllMessages = () => {
    setQuestionError(null);
    setAnswerError(null);
    setCantSubmitError(null);
    setAnswersFile(null);
    setQuestionFile(null);
    setAccessToAllUsers(false);
    document.getElementById("checkbox").checked = false;
  };

  return (
    <div className="w-[400px] min-h-[500px] absolute bg-[#1A1A1AEF] z-50 flex flex-col items-center rounded-[20px] ">
      <button
        className="mt-4 absolute right-[20px] text-primary text-[30px]"
        title={"Cancel"}
        onClick={() => {
          setQuestionFromFileBox(false);
          clearAllMessages();
        }}
      >
        <GiCancel />
      </button>

      <h2 className="text-[40px] text-white mt-5 mb-10">Add Files </h2>
      <div className="h-[100px] w-[300px]">
        {isLoading && <Loader size={50} isLoading={isLoading} />}
        {questionAdded && (
          <p className="text-green-600 text-center text-[20px]">
            {questionAdded}
          </p>
        )}
        {error && (
          <p className="text-red-600 text-center text-[20px]">
            Oops! Something went wrong!
          </p>
        )}
        <ul className="list-disc text-red-600 text-[18px] font-poppins font-semibold">
          {questionError && <li>{questionError}</li>}
          {answerError && <li>{answerError}</li>}
          {cantSubmitError && <li>{cantSubmitError}</li>}
        </ul>
      </div>
      <div className="mb-10 text-center flex flex-col w-full items-center h-[300px]">
        <div id="accessCheckBox"
          className="mb-2">
                          <label htmlFor="NewCategoryAccessibility" className="ml-8">
                  <input
                    type="checkbox"
                    id="checkbox"
                    name="NewCategoryAccessibility"
                    value={accessToAllUsers}
                    onChange={(event) => {
                      setAccessToAllUsers(event.target.checked)
                    }}
                  />
                  <span className="text-white ml-4">
                    Give access to this category of exam to everyone?
                  </span>
                </label>

        </div>
        <div
          id="input_questionsFile_container"
          className={` flex ${
            isQuestionFileValid
              ? "border-[2px] border-green-600"
              : questionError
              ? "border-[2px] border-red-600"
              : "border-[2px] border-primary"
          } w-[300px] rounded-[30px] px-2 mb-4`}
        >
          <input
            className={`hidden`}
            id="question_file_input"
            name="question_file_input"
            type="file"
            onChange={(event) => handleUploadQuestionsFile(event)}
          />
          <label
            className={`flex justify-center items-center cursor-pointer`}
            htmlFor="question_file_input"
          >
            <BiCloudUpload className="text-[50px] text-primary hover:text-[#8231fdd4] transition-all" />
            <span className="text-[20px] font-bold text-white ml-4">
              Upload Questions File
            </span>
          </label>
        </div>
        <div
          id="input_answersFile_container"
          className={` flex ${
            isAnswersFileValid
              ? "border-[2px] border-green-600"
              : answerError
              ? "border-[2px] border-red-600"
              : "border-[2px] border-primary"
          } w-[300px] rounded-[30px] px-2 mb-4`}
        >
          <input
            className={`hidden`}
            id="answer_file_input"
            name="answer_file_input"
            type="file"
            onChange={(event) => handleUploadAnswersFile(event)}
          />
          <label
            className={`flex justify-center items-center cursor-pointer`}
            htmlFor="answer_file_input"
          >
            <BiCloudUpload className="text-[50px] text-primary hover:text-[#8231fdd4] transition-all" />
            <span className="text-[20px] font-bold text-white ml-4">
              Upload Answers File
            </span>
          </label>
        </div>
        <div className="flex items-center justify-center mt-10">
          <Button
            title={"Submit"}
            variation={0}
            buttonSize={1}
            type="submit"
            handleClick={handleSubmit}
          />
        </div>
      </div>
    </div>
  );
};

export default AddQuestionFromFile;
