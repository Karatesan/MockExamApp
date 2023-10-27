import React from "react";

//Button consists of 2 values - title and variation. Inside the component that is <Button title="" variation={0/1}> you can put whatever text you like. Variation 0 = no bg, 1 = color.
const Button = ({ title, variation, buttonSize, handleClick, id, styles, isDisabled=false }) => {
  return (
    <button
      id={id}
      disabled={isDisabled}
      onClick={() => handleClick()}
      className={`relative button-z-index ${styles} ${
        variation === 1
          ? "bg-[#7518ff] button-hover1"
          : "bg-transparent button-hover2"
      } ${
        buttonSize === 1
          ? "lg:mt-3 md:mt-2 lg:mb-3 md:mb-2 lg:w-[260px] md:w-[200px] lg:h-[54px] md:h-[48px] lg:rounded-[30px] md:rounded-[25px]"
          : "mr-2 w-[110px] h-[36px] rounded-[20px]"
      } `}
    >
      <p
        className={`relative z-10 text-white font-bold ${
          buttonSize === 1 ? "lg:text-[24px] md:text-[17px]" : "text-[18px]"
        } px-4`}
      >
        {title}
      </p>
    </button>
  );
};

export default Button;
