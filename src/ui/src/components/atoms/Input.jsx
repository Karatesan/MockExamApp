import React from "react";

const Input = ({ placeholder, type, id, onChange, value, styles, isRequired, maxLength }) => {
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
        maxLength={maxLength}
        className={` w-[350px] h-[50px] border-2 border-primary bg-transparent rounded-[20px] px-3 text-white outline-none focus:border-white ${styles}`}
      />
    </div>
  );
};

export default Input;
