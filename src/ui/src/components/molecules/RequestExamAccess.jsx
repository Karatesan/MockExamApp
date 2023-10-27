import React, { useEffect, useState } from "react";
import axios from "axios";
import authHeader from "../../services/auth-header";
import { GiCancel } from "react-icons/gi";
import Button from "../atoms/Button";

const RequestExamAccess = ({ setRequestExamWindow }) => {
  const [userCategories, setUserCategories] = useState([]);
  const [allPossibleCategories, setAllPossibleCategories] = useState([]);
  const [selectedCategory, setSelectedCategory] = useState(null);
  const [categoryError, setCategoryError] = useState(null);
  const [confirmation, setConfirmation] = useState(null);

  useEffect(() => {
    axios
      .get("http://localhost:8080/api/questions/allCategories", {
        headers: authHeader(),
      })
      .then((response) => {
        setUserCategories(response.data); // Update the state with fetched data
      })
      .catch((error) => {
        console.error(error.response.data);
      });

    axios
      .get("http://localhost:8080/api/questions/allCategoriesForAll", {
        headers: authHeader(),
      })
      .then((response) => {
        setAllPossibleCategories(response.data); // Update the state with fetched data
      })
      .catch((error) => {
        console.error(error.response.data);
      });
  }, []);

  //when i use post endpoint with requestParam and want to send data in url string, I have to add after url null
  //which indicates that body in null and parameters are in string
  const handleRequest = () => {
    if (selectedCategory === null) setCategoryError("Select a category!");
    else {
      axios
        .post(
          `http://localhost:8080/api/users/askForExamAccess?categoryString=${selectedCategory}`,
          null,
          { headers: authHeader() }
        )
        .then((response) => {
          if (response.request.status === 200) {
            setConfirmation("Your request has been sent!");
            setTimeout(() => {
              setConfirmation(null);
              setSelectedCategory(null);
            }, 2000);
          }
        }).catch((error) => {
          setCategoryError(error.response.data)
          setTimeout(() => {
            setCategoryError(null);
            setSelectedCategory(null);
          }, 5000);
        })
        ;
    }
  };

  console.log(selectedCategory);

  return (
    <div className="signUp__background signUp__glass flex justify-center items-center relative">
      <div className="w-[600px] lg:h-[700px] bg-[#1A1A1AEF] rounded-[20px] flex flex-col items-center relative">
        <button
          className="mt-4 absolute right-[20px] text-primary text-[30px]"
          title={"Cancel"}
          onClick={() => setRequestExamWindow(false)}
        >
          <GiCancel />
        </button>
        <h2 className="text-[30px] text-white mt-10 mb-3">
          Your current exam categories{" "}
        </h2>
        <div className="flex flex-wrap w-[500px] justify-center bg-[#2a253198] p-4 rounded-2xl h-[200px] overflow-y-auto">
          {userCategories.length === 0 && (
            <div className="flex items-center text-white py-2 px-4 border-2 border-solid border-gray bg-[#8951e23d] rounded-xl h-[50px] ">
              You don't have access to any exams yet!
            </div>
          )}
          {userCategories.map((category, index) => (
            <div
              key={index}
              className="flex items-center text-white py-2 px-4 border-2 border-solid border-gray mr-2 mb-2 h-[50px] bg-[#8951e23d] rounded-xl"
            >
              {category}
            </div>
          ))}
        </div>
        <h2 className="text-[30px] text-white mt-5 mb-3">
          Request access to new exam category
        </h2>
        <div className="h-[50px]">
          {categoryError && (
            <p className="text-red-600 text-[18px] font-poppins font-semibold">
              {categoryError}
            </p>
          )}
          {confirmation && (
            <p className="text-green-600 text-center text-[20px]">
              {confirmation}
            </p>
          )}
        </div>
        <div className="flex flex-wrap w-[500px] justify-center bg-[#2a253198] p-4 rounded-2xl h-[200px] overflow-y-auto">
          {allPossibleCategories
            .filter((item) => !userCategories.includes(item))
            .map((category, index) => (
              <div
                key={index}
                onClick={() => {
                  if (categoryError !== null) setCategoryError(null);
                  setSelectedCategory(category);
                }}
                className={`text-white py-2 px-2 mr-2 mb-2 cursor-pointer h-[50px] ${
                  selectedCategory === category
                    ? "border-2 border-solid border-[#11f71d86] bg-[#8851e298]"
                    : "border-2 border-solid border-gray bg-[#8951e23d]"
                }  rounded-xl flex items-center`}
              >
                {category}
              </div>
            ))}
        </div>
        <div className="absolute bottom-5">
          <Button title="Request" handleClick={handleRequest} />
        </div>
      </div>
    </div>
  );
};

export default RequestExamAccess;
