import React, { useEffect, useState } from "react";
import Navbar from "../molecules/Navbar";
import { mainPageTexts } from "../../assets/constants";
import Button from "../atoms/Button";
import SignUpBox from "./SignUpBox";
import styles from "../../../style";

const Main = () => {
  const [words, setWords] = useState(mainPageTexts);
  const [index, setIndex] = useState(0);
  const [signUpBox, setSignUpBox] = useState(false);
  

  const signUpPop = () => {
    setSignUpBox((current) => !current);
  }

  useEffect(() => {
    const lastIndex = words.length - 1;
    if (index < 0) {
      setIndex(lastIndex);
    }
    if (index > lastIndex) {
      setIndex(0);
    }
  }, [index, words]);

  useEffect(() => {
    let slider = setInterval(() => {
      setIndex(index + 1);
    }, 3000);
    return () => clearInterval(slider);
  }, [index]);
  return (
    <>
      {signUpBox && <SignUpBox setSignUpBox={setSignUpBox} />}
      <div className={`${styles.paddingX} ${styles.flexCenter} justify-center`}>
        <div className={`${styles.boxWidth}`}>
      <div className="flex flex-col justify-center items-center lg:mt-2 md:mt-5 relative">
        <h1 className="lg:text-[100px] md:text-[92px] font-bold text-white z-10">
          <span className="bg-[#1A1A1A]">MAKE LEARNING</span> <br />
          A <br />
          <span className="bg-[#1A1A1A]">EXPERIENCE</span>
        </h1>
        {words.map((word, wordIndex) => {
          let position = "nextWord";
          wordIndex === index
            ? (position = "activeWord")
            : wordIndex === index - 1 || (index === 0 && words.length - 1)
            ? (position = "lastWord")
            : (position = "lastWord");
          return (
            <span
              key={word}
              className={`${position} main__word top-[28%] lg:left-[30%] md:left-[15%] main__textStroke lg:text-[100px] md:text-[92px] font-bold`}
            >
              {word}
            </span>
          );
        })}
        <div className="lg:w-[800px] md:w-[740px]">
          <Button id="CreateAccountButton" title="Create an account!" variation={1} buttonSize={1} handleClick={signUpPop}/>
        </div>
      </div>
      </div>
      </div>
    </>
  );
};

export default Main;
