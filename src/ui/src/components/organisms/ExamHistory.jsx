import React, { useState, useEffect } from "react";
import styles from "../../../style";
import axios from "axios";
import authHeader from "../../services/auth-header";
import Loader from "../atoms/Loader";
import Button from "../atoms/Button";
import testPageService from "../../services/testPage.service";
import { GiCheckMark } from "react-icons/gi";
import { HiXMark } from "react-icons/hi2";
import { GoMoveToEnd } from "react-icons/go";
import ReviewExam from "./ReviewExam";
import { GiCancel } from "react-icons/gi";

const ExamHistory = ({ setHistoryWindow }) => {
  const [exams, setExams] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState(false);
  const [showReview, setShowReview] = useState(null);

  useEffect(() => {
    axios
      .get("http://localhost:8080/api/exam/allExamsFromUser", {
        headers: authHeader(),
      })
      .then((response) => {
        setExams(response.data);
        setIsLoading(false);
      })
      .catch((error) => {
        setError(error.response.data);
        setIsLoading(false);
      });
  }, []);

  const calculateAnsweredQuestions = (answersDTO) => {
    let counter = 0;

    answersDTO.forEach((element) => {
      if (element.givenAnswer.length > 0) ++counter;
    });
    return counter;
  };

  return (
    <div className="bg-[#FFFFFF22] w-full h-[78.5vh] rounded-[20px] relative overflow-hidden">
      {showReview === null && (
        <button
          className=" absolute right-[20px] top-[20px] text-primary text-[30px] z-20"
          title={"Cancel"}
          onClick={() => setHistoryWindow(false)}
        >
          <GiCancel />
        </button>
      )}
      <div className={`content ${showReview !== null ? "slide-in" : "exam"}`}>
        {showReview !== null && (
          <ReviewExam exam={exams[showReview]} closeWindow={setShowReview} />
        )}
      </div>
      <div
        className={`content ${
          showReview !== null ? "slide-out" : ""
        } bg-[#FFFFFF22] pt-10 rounded-[20px] flex flex-col items-center overflow-auto`}
        id="examHistory"
      >
        <h1 className="text-white text-[50px] mb-10">Completed exams</h1>
        {isLoading && <Loader isLoading={isLoading} />}
        <div id="table-container" className="overflow-auto">
          <table id="table" className="">
            <thead className="text-white font-semibold font-poppins">
              <tr>
                <th rowSpan={2}>Date</th>
                <th colSpan={5}>Score</th>
              </tr>
              <tr>
                <th>Answered Questions</th>
                <th rowSpan={1}>Correct Answers</th>
                <th rowSpan={1}>Exam type</th>
                <th>Is it passed?</th>
                <th>Review exam!</th>
              </tr>
            </thead>
            <tbody>
              {exams &&
                exams.map((exam, index) => {
                  const { examDate, answersDTO } = exam;
                  console.log(answersDTO);
                  const score = testPageService.calculateScore(answersDTO);
                  const numberOfQuestions = answersDTO.length;
                  const result = score + "/" + numberOfQuestions;
                  const category = answersDTO[0].questionResponseDTO.category;
                  const percentage = testPageService.calculatePercentage(
                    score,
                    numberOfQuestions
                  );
                  const date = new Date(examDate).toLocaleString();
                  const numberOfAnsweredQuestions =
                    calculateAnsweredQuestions(answersDTO) +
                    "/" +
                    numberOfQuestions;

                  return (
                    <tr
                      key={index}
                      className={`text-white text-center ${
                        index % 2 === 0 ? "bg-[#ffffff25]" : "bg-[#ffffff11]"
                      } hover:bg-[#ffffff44]`}
                    >
                      <td>{date}</td>
                      <td>{numberOfAnsweredQuestions}</td>
                      <td>
                        {result} ({percentage}%)
                      </td>
                      <td>{category}</td>
                      <td className="">
                        {percentage > 70 ? (
                          <GiCheckMark
                            size={20}
                            className="mx-auto text-green-700"
                          />
                        ) : (
                          <HiXMark size={30} className="mx-auto text-red-800" />
                        )}
                      </td>
                      <td className="">
                        <button onClick={() => setShowReview(index)}>
                          <GoMoveToEnd size={20} />
                        </button>
                      </td>
                    </tr>
                  );
                })}
            </tbody>
          </table>
        </div>
        {/* {exams.length>0 && <ReviewExam exam={exams[0]} /> } */}
      </div>
    </div>
  );
};

export default ExamHistory;
