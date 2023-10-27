import React from "react";
import Button from "./Button";
import { useNavigate } from "react-router";

const ConfirmDeleteQuestion = ({ message, confirm,isExamCanceled, cancel }) => {
  return (
    
    <div className="signUp__background signUp__glass flex justify-center items-center">
    <div
      className={`flex-col flex rounded-[20px] shadow-md fixed bg-[#1a1a1aaa] top-[50%] left-[50%] w-[400px] border-[1px] border-solid border-[#CC3242] text-[#CC3242] text-[25px] font-semibold text-center -translate-x-[50%] -translate-y-[50%] p-5`}
    >
      <p className="mb-4">{message}</p>
      <div>
        <Button title="Ok" handleClick={confirm} />
        <Button title="Cancel" handleClick={cancel} />
      </div>
    </div>
    </div>
  );
};

export default ConfirmDeleteQuestion;