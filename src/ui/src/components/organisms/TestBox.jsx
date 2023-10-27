import React, { useEffect, useState } from "react";
import Input from "../atoms/Input";
import Button from "../atoms/Button";
import PropTypes from "prop-types";
import axios from "axios";
import authHeader from "../../services/auth-header";
import { useNavigate } from "react-router";
import { GiCancel } from "react-icons/gi";
import { levels as allLevels } from "../../assets/constants";



const TestBox = ({ setTestBox }) => {
  const [selectedCategory, setSelectedCategory] = useState("");
  const [selectedLevel,setSelectedLevel] = useState("");
  const [categories, setCategories] = useState([]);
  const [levels,setLevels] = useState(allLevels)
  const [categoryError,setCategoryError] = useState(null)
  const [levelError,setLevelError] = useState(null)
  const navigate = useNavigate();

  useEffect(() => {
    axios
      .get("http://localhost:8080/api/questions/allCategories", {
        headers: authHeader(),
      })
      .then((response) => {
        setCategories(response.data); // Update the state with fetched data
      })
      .catch((error) => {
        console.error(error.response.data);
      });

  }, []);

  const validateSelection = (name,value)=>{
    if(value==="") return `${name} is required.`
    else return null;
  }

  const handleError = ()=>{
    setCategoryError(validateSelection("Category", selectedCategory));
    setLevelError(validateSelection("Difficulty",selectedLevel));
  }
  
  const handleStartMockExam = () => {
    handleError();
    if(selectedCategory && selectedLevel){
    setTestBox(false);
    navigate(
      `/test?category=${encodeURIComponent(selectedCategory)}&level=${encodeURIComponent(selectedLevel)}&exam=mock`
    );
    window.location.reload();
    }
  };

  const handleStartRandomQuestions = () => {
    handleError();
    if(selectedCategory && selectedLevel){
    setTestBox(false);
    navigate(
      `/test?category=${encodeURIComponent(selectedCategory)}&level=${encodeURIComponent(selectedLevel)}&exam=questions`
    );
    window.location.reload();
    }
  };

  return (
    <div className="signUp__background signUp__glass flex justify-center items-center">
      <div className="w-[600px] lg:h-[700px] bg-[#1A1A1AEF] rounded-[20px] flex flex-col items-center relative">
      <button
          className="mt-4 absolute right-[20px] text-primary text-[30px]"
          title={"Cancel"}
          onClick={() => setTestBox(false)}
        >
          <GiCancel />
        </button>
        <h2 className="text-[60px] text-white mt-10 mb-3">Pick Your Exam</h2>
        <div id="errorBox" className=" h-[70px]">
          <ul className="list-disc text-red-600 text-[18px] font-poppins font-semibold">
        {categoryError && <li><p className="">{categoryError} </p></li>}
        {levelError && <li><p className="">{levelError} </p></li>}
        </ul>
        </div>
        <div className=" lg:mt-[40px] flex flex-col items-center justify-evenly">
          <div id="selectors_container" className="flex items-center justify-between h-[150px] w-full px-14">
            <div id="category-container" className="text-center">
          <label 
            htmlFor="categoryDropdown"
            className="text-[20px] text-white font-poppins mb "
          >
            Category
          </label>
          <div className="testBox_select mt-2">
            <select
              className="w-[200px] h-[40px] rounded-[20px] text-white border-[1px] border-primary transition-all bg-transparent pl-3 outline-none focus:border-[#7d35ea] appearance-none"
              id="categoryDropdown"
              value={selectedCategory}
              onChange={(e) => {
                if(categoryError!==null)setCategoryError(null)
                setSelectedCategory(e.target.value)}}
            >
              <option value="">Select a category</option>
              {categories.map((category, index) => {
                return (
                  <option key={index} value={category}>
                    {category}
                  </option>
                );
              })}
            </select>
          </div>
          </div>
          <div id="level-container" className="text-center">
          <label 
            htmlFor="levelDropdown"
            className="text-[20px] text-white font-poppins mb-2 "
          >
            Difficulty
          </label>
          <div className="testBox_select mt-2">
            <select
              className="w-[200px] h-[40px] rounded-[20px] text-white border-[1px] border-primary transition-all bg-transparent pl-3 outline-none focus:border-[#7d35ea] appearance-none"
              id="levelDropdown"
              value={selectedLevel}
              onChange={(e) => {
                if(levelError!==null)setLevelError(null)
                setSelectedLevel(e.target.value)}}
            >
              <option value="">Select difficulty</option>
              {levels.map((level, index) => {
                return (
                  <option key={index} value={level}>
                    {level}
                  </option>
                );
              })}
            </select>
            </div>
            </div>
          </div>
          <div className="flex justify-between mt-10 md:mb-6 w-[550px]">
            <div
              className="flex flex-col justify-start w-[270px] p-4 select-exam cursor-pointer"
              onClick={handleStartMockExam}
            >
              <p className="text-white text-[20px]">
                Mock Exam
              </p>
              <ul className="flex flex-col text-white ml-3 list-disc">
                <li>20 questions in category</li>
                <li>Timer 20 minutes</li>
                <li>Score at the end of the exam</li>
                <li>Exam saved in profile</li>
              </ul>
            </div>
            <div
              className="flex flex-col justify-start w-[270px] p-4 select-exam cursor-pointer"
              onClick={handleStartRandomQuestions}
            >
              <p className="text-white text-[20px]">
                Random Questions
              </p>
              <ul className="text-white ml-3 list-disc">
                <li>Practise tool</li>
                <li>Random questions in category</li>
                <li>No timer</li>
                <li>Feedback after each answer</li>
              </ul>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default TestBox;
