import React from "react";
import testPageService from "../../services/testPage.service";
import Button from "./Button";
import { useNavigate } from "react-router-dom";

const ScoreWIndow = ({
  score,
  numberOfQuestions,
  handleConfirm,
  passThreshold,
}) => {
  const navigate = useNavigate();
  const percentages = testPageService.calculatePercentage(
    score,
    numberOfQuestions
  );
  const isPassed = percentages >= passThreshold;

  const navigateToHistory = () => {
    sessionStorage.setItem("history", "true");
    navigate("/main");
  };

  return (
    <div className="bg-[#1a1a1add] border-[1px] rounded-[20px] border-solid border-[#7d35ea] flex flex-col items-center justify-evenly w-[450px] h-[400px]">
      
      <p className=" text-[25px] font-semibold text-white ">
        You scored {score} out of {numberOfQuestions} ({percentages}%).
      </p>
      <p
        className={`text-[30px] ${
          isPassed ? "text-green-700" : "text-red-700"
        }`}
      >
        {isPassed ? "Congratulations! You Passed!" : "Sorry, You Did Not Pass."}
      </p>
      <div>
        <Button
          title="Check Answers"
          handleClick={navigateToHistory}
          buttonSize={1}
        />
      </div>
      <div>
        <Button title="Confirm" handleClick={() => navigate("/main")} />
      </div>
    </div>
  );
};

export default ScoreWIndow;
