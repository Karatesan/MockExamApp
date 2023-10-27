import React from "react";

const QuestionContentInput = ({ placeholder, type, id, onChange, value, styles, isRequired }) => {
  return (
    <div>
      <input
        placeholder={placeholder}
        type={type}
        value={value}
        onChange={(e) => {
          onChange(e);
        }}
        id={id}
        name={id}
        className={` w-[800px] h-[50px] border-2 border-primary bg-transparent rounded-[20px] px-3 text-white outline-none focus:border-white ${styles}`}
      />
    </div>
  );
};

export default QuestionContentInput;
