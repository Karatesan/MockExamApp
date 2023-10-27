import React from "react";
import styles from "../../../style";
import Button from "../atoms/Button";

const TakingExamInfo = ({
  startExam,
  category,
  level,
  numberOfQuestions,
  time,
  isMultiple,
  examType,
}) => {
  return (
    <>
      <div className={`${styles.boxWidth}`}>
        <div className="bg-[#1A1A1AEF] w-[550px] lg:h-[700px] mt-16 rounded-[20px] flex flex-col items-center justify-center relative ">
          <h2 className="text-[40px] font-semibold font-poppins text-white text-center">
            You are about to take an exam:{" "}
          </h2>
          <div className="w-[80%] my-5">
            <span className="text-[30px] text-white">
              Topic:{" "}
              <span className="ml-6 font-thin italic underline decoration-primary">
                {category}
              </span>
            </span>
            <br />
            <span className="text-[30px] text-white">
              Difficulty:{" "}
              <span className="ml-6 font-thin italic underline decoration-primary">
                {level}
              </span>
            </span>
            <br />
            <span className="text-[30px] text-white">
              Number of Questions:{" "}
              <span className="ml-6 font-thin italic underline decoration-primary">
                {numberOfQuestions}
              </span>
            </span>
            <br />
            {examType === "mock" && (
              <div>
                <span className="text-[30px] text-white">
                  Time:{" "}
                  <span className="ml-6 font-thin italic underline decoration-primary">
                    {time}min
                  </span>
                </span>
              </div>
            )}
          </div>
          <h3 className="text-[40px] font-semibold text-white text-center mt-5">
            Goodluck!
          </h3>
          <div className="mt-5">
            <Button
              title="Let's start!"
              variation={2}
              buttonSize={1}
              handleClick={() => startExam(true)}
            />
          </div>
          <p className="text-white">
            This exam {isMultiple ? "does" : "doesn't"} have multiple choice
            questions.{" "}
          </p>
          <p className="text-white">
            Right click on answer to flag it as unsure.
          </p>
        </div>
      </div>
    </>
  );
};

export default TakingExamInfo;
