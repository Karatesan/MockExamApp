import React from "react";
import Button from "./Button";

const ErrorBox = ({ error, hideWindow }) => {
  return (
    <div className=" flex-col rounded-md shadow-md fixed bg-slate-600 top-[50%] left-[50%] w-[400px] border-2 border-solid border-red-500 text-red-500 text-[25px] font-semibold text-center -translate-x-[50%] -translate-y-[50%] p-5">
      <p className="mb-4">{error}</p>
      <div
        onClick={() => {
          hideWindow(null);
        }}
      >
        <Button title="Ok" />
      </div>
    </div>
  );
};

export default ErrorBox;
