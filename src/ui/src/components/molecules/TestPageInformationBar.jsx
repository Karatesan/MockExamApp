import React from "react";
import Timer from "../atoms/Timer";

const TestPageInformationBar = ({
  questionIndex,
  numberOfQuestions,
  handleFinishExam,
  answeredQuestions,
  time,
  examType
}) => {
  return (
    <div id="topPanel" className="flex justify-between lg:w-[1000px] md:w-[900px] mt-5 ">
      <h3 className="text-white flex-1 text-left text-[20px] ">
        Question {questionIndex + 1 + "/" + numberOfQuestions}
      </h3>
      {examType === "mock" && <Timer

        className="flex-1 text-center"
        initialTimeInSeconds={time}
        onTimerEnd={handleFinishExam}
      />
      }
      <h3 className="text-white flex-1 text-right text-[20px] ">
        Questions answered: {answeredQuestions + "/" + numberOfQuestions}
      </h3>
    </div>
  );
};

export default TestPageInformationBar;
