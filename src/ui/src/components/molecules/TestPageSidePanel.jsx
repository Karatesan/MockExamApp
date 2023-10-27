import React from "react";
import testPageService from "../../services/testPage.service";

const TestPageSidePanel = ({
  setDisplayQUestionNavPanel,
  displayQuestionNavPanel,
  questions,
  setQuestionIndex,
  studentAnswers,
  questionIndex
}) => {
  return (
    <div
      id="sidePanel"
      onMouseEnter={() => {
        setDisplayQUestionNavPanel(true);
      }}
      onMouseLeave={() => setDisplayQUestionNavPanel(false)}
      className={`lg:flex flex-col items-center z-10 rounded-l-[20px] question-nav-panel md:hidden absolute w-[250px] h-[650px]
    ${
      displayQuestionNavPanel ? "visible" : ""
    } shadow-lg custom-scrollbar py-6 bg-[#1a1a1a]`}
    >
      {questions.map((question, index) => {
        return (
          <div
            onClick={() => {
              setQuestionIndex(index);
            }}
            key={question.id}
            className={`w-full ${
              testPageService.isQuestionAnsweredOrUnsure(
                studentAnswers[index],
                1
              )
                ? "bg-[#7d35ea]"
                : testPageService.isQuestionAnsweredOrUnsure(
                    studentAnswers[index],
                    2
                  )
                ? "bg-[#8ca019]"
                : ""
            } ${
              index === questionIndex
                ? "border-[1px] border-solid border-[#7d35ea]"
                : ""
            } hover:cursor-pointer hover:${
              testPageService.isQuestionAnsweredOrUnsure(
                studentAnswers[index],
                1
              )
                ? "bg-[#7d35ea]"
                : "bg-[#ffffff17]"
            }
          } p-2 
        `}
          >
            <p className="truncate text-white font-poppins text-center">
              Question {index + 1}
            </p>
          </div>
        );
      })}
      <h4 className="text-[24px] text-white absolute -rotate-90 bottom-[70px] -left-[40px]">
        QUESTIONS
      </h4>
    </div>
  );
};

export default TestPageSidePanel;
