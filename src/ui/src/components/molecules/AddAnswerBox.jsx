import React from "react";
import { GiCancel } from "react-icons/gi";
import Input from "../atoms/Input";
import Button from "../atoms/Button";

const AddAnswerBox = ({
  setMultiInputBox,
  title,
  multiInputBox,
  answerError,
  currentAnswerAdded,
  setcurrentAnswerAdded,
  handleSubmitAnswer,
}) => {
  return (
    <div className="w-[400px] min-h-[500px] absolute bg-[#1A1A1AEF] z-50 flex flex-col items-center rounded-[20px] ">
      <button
        className="mt-4 absolute right-[20px] text-primary text-[30px]"
        title={"Cancel"}
        onClick={() => setMultiInputBox(false)}
      >
        <GiCancel />
      </button>
      <h2 className="text-[40px] text-white mt-5 mb-10">{title} </h2>
      {answerError && (
        <p className="text-red-700 text-[18px] mb-2">{answerError}</p>
      )}
      <div className="mb-10 text-center flex flex-col ">
        <Input
          type="text"
          id="answer"
          required={true}
          placeholder="Type your answer..."
          value={currentAnswerAdded.answer}
          onChange={(e) =>
            setcurrentAnswerAdded({
              ...currentAnswerAdded,
              answer: e.target.value,
            })
          }
          styles="mb-3 "
          maxLength={200}
        />
        <Input
          type="text"
          id="feedback"
          placeholder="Type your feedback..."
          value={currentAnswerAdded.feedback}
          onChange={(e) =>
            setcurrentAnswerAdded({
              ...currentAnswerAdded,
              feedback: e.target.value,
            })
          }
          styles="mb-3 "
          maxLength={500}
        />
        <div className="flex ml-1 mt-3">
          <input
            type="checkbox"
            name="isCorrect"
            id="isCorrect"
            defaultChecked={currentAnswerAdded.isCorrect != ""}
            onChange={(e) =>
              setcurrentAnswerAdded({
                ...currentAnswerAdded,
                isCorrect: e.target.checked,
              })
            }
          />
          <label className="text-white ml-2" htmlFor="isCorrect">
            Is this answer correct?
          </label>
        </div>
        <div className="flex items-center justify-center mt-10">
          <Button
            title={"Submit"}
            variation={0}
            buttonSize={1}
            type="submit"
            handleClick={handleSubmitAnswer}
          />
        </div>
      </div>
    </div>
  );
};

export default AddAnswerBox;
