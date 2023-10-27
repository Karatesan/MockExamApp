import React from "react";
import { AiTwotoneEdit } from "react-icons/ai";
import { RiDeleteBin6Fill } from "react-icons/ri";

const AnswerList = ({ question, handleGoToEditAnswer, deleteAnswer }) => {
  return question.answers.map((answer, index) => {
    return (
      <div
        key={index}
        className="border-[1px] rounded-[20px] border-[#FFFFFF22] px-4 w-[450px] h-[100px] relative pr-[40px] mb-2"
      >
        <button
          className="mt-2 absolute right-[10px] text-primary text-[30px]"
          title={"Edit"}
          onClick={() => {
            handleGoToEditAnswer(index);
          }}
        >
          <AiTwotoneEdit size="25" />
        </button>
        <button
          className="mt-2 absolute right-[40px] text-primary text-[30px]"
          title={"Delete"}
          onClick={() => {
            deleteAnswer(index);
          }}
        >
          <RiDeleteBin6Fill size="25" />
        </button>
        <div className="flex text-white">
          <span className="mr-2 font-semibold">Answer:</span>
          <span className="flex lg:w-[400px] md:w-[300px] md:max-h-[50px] overflow-y-hidden hide_scrollbar italic">
            {answer}
          </span>
        </div>
        <div className="flex text-white">
          <span className="mr-2 font-semibold">Feedback: </span>
          <span className="flex lg:w-[400px] md:w-[300px] md:max-h-[25px] overflow-y-hidden hide_scrollbar italic">
            {question.feedback[index]}
          </span>
        </div>
        <p className="text-white font-semibold">
          {
          question.correctAnswers.includes(answer) ? (
            <span className="text-green-400">This answer is correct.</span>
          ) : (
            <span className="text-red-400">This answer is incorrect.</span>
          )}
        </p>
      </div>
    );
  });
};

export default AnswerList;
