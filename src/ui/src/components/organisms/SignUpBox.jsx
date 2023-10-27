import React, { useEffect } from "react";
import Input from "../atoms/Input";
import Button from "../atoms/Button";
import { useState } from "react";
import authService from "../../services/auth.service";
import PacmanLoader from "react-spinners/ClipLoader";
import ErrorComponent from "../atoms/ErrorComponent";
import { GiCancel } from 'react-icons/gi';
import { FormProvider, useForm } from "react-hook-form";
import HookInput from "../atoms/HookInput";
import { confirm_password_validation, email_validation, first_name_validation, last_name_validation, password_validation } from "../../utils/InputValidations";
import HookErrorComponent from "../atoms/HookErrorComponent";
import HookPasswordInput from "../atoms/HookPasswordInput";

const SignUpBox = ({ setSignUpBox }) => {
  const [isLoading, setIsLoading] = useState(false);
  const [response, setResponse] = useState(null);
  const [error, setError] = useState(null);
  const [errors, setErrors] = useState([]);
  const [errorList, setErrorList] = useState([]);
  const [errorFields, setErrorFields] = useState([]);

  const methods = useForm();

  const fieldList = ["firstname", "lastname", "email", "password", "confirmPassword"];

  const resetResponses = () => {
    errorFields.forEach(
      (element) => {
        document.getElementById(element).style.outline = "none";
      });
    error && setError(null);
    response && setResponse(null);
    errorList && setErrorList([]);
    errorFields && setErrorFields([]);
  };

  useEffect(() => {
    Object.keys(errors).forEach((element) => {
      document.getElementById(element).style.outline = "2px solid red";
    },
      fieldList.forEach((element) => {
        if ((Object.keys(errors).find(i => i=== element)) === undefined) {
          document.getElementById(element).style.outline = "none";
        }
      }
      ))
  });

  const submitRegister = methods.handleSubmit(async (data) => {
    resetResponses();
    setIsLoading(true);
    return authService
      .register(data)
      .then((response) => {
        if (response.status === 200) {
          setIsLoading(false);
          error && setError(null);
          setResponse(
            "Your account has been created. We have sent a verification link to the email address you provided."
          );
          //setSignUpBox(false);
        }
      })
      .catch((error) => {
        setIsLoading(false);
        if (typeof error.response.data == "string") {
          setError(error.response.data);
        } else {
          setErrorList(error.response.data);
          setErrorFields(Object.keys(error.response.data));
          Object.keys(error.response.data).forEach(
            (element) => {
              document.getElementById(element).style.outline = "2px solid red";
            });
        }
      });
  });

  return (
    <div className="signUp__background signUp__glass flex justify-center items-center">
      <div className="w-[550px] lg:h-[700px] bg-[#1A1A1AEF] rounded-[20px] flex flex-col items-center relative">
        <button
          className="mt-4 absolute right-[20px] text-primary text-[30px]"
          title={"Cancel"}
          onClick={() => {
            resetResponses();
            setSignUpBox(false);
          }}
        ><GiCancel /></button>
        <h2 className="text-[60px] text-white mt-10">Sign Up</h2>
        {
          isLoading && (
            <div className=" flex justify-center items-center">
              <PacmanLoader
                color={`#FFFFFF33`}
                size={50}
                loading={isLoading}
                className=""
              />
            </div>
          )
        }
        {response && (
          <div className="w-[500px] text-center">
            <p id="success" className="text-green-700 text-[18px]">{response}</p>
          </div>
        )}
        {error && (
          <div className="w-max text-center ">
            <p id="error" className="text-red-700 text-[18px]">{error}</p>
          </div>
        )}

        {!(Object.keys(errorList).length == 0) && (
          <div id="errorList">
            {Object.entries(errorList).map((singleError) => (

              <ErrorComponent key={JSON.stringify(singleError)} errorMessage={{ errorMessage: Object.values(singleError)[1]}} />

            ))}
            <div />
          </div>
        )

        }
 {errors && (
              <div className="w-[500px] flex flex-col text-center">
                {Object.keys(errors).map((singleError) => (
                  <HookErrorComponent key={JSON.stringify(singleError)} errorMessage={errors[singleError]["message"]} id={JSON.stringify(singleError)} />
                ))}
              </div>
            )}

        <FormProvider {...methods}>
          <form
            onSubmit={e => e.preventDefault()}
            noValidate
            autoComplete="off"
            className="container"
          >
           
            <div className="lg:my-[50px] md:my-[30px] flex flex-col items-center justify-between">

              <HookInput
                setErrors={setErrors}
                {...first_name_validation}
              />
              <HookInput
                setErrors={setErrors}
                {...last_name_validation}
              />
              <HookInput
                setErrors={setErrors}
                {...email_validation}
              />
              <HookPasswordInput
                setErrors={setErrors}
                {...password_validation}
              />
              <HookPasswordInput
                setErrors={setErrors}
                {...confirm_password_validation}
              />
            </div>
            {!response && (
              <div className="flex flex-col justify-between items-center mb-10">
                <Button
                  id="SignUpButton"
                  title={"Sign Up!"}
                  variation={1}
                  buttonSize={1}
                  type="submit"
                  handleClick={submitRegister}
                />
              </div>
            )}
          </form>
        </FormProvider>
        {response && (
          <Button
            id={"ConfirmButton"}
            title={"Confirm"}
            variation={1}
            buttonSize={1}
            type="submit"
            handleClick={() => {
              resetResponses();
              setSignUpBox(false);
            }}
          />
        )}
      </div>
    </div>
  );
};

export default SignUpBox;
