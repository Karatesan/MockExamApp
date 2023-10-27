import React from "react";
import Input from "../atoms/Input";
import Button from "../atoms/Button";
import { useState } from "react";
import PacmanLoader from "react-spinners/ClipLoader";
import authService from "../../services/auth.service";
import { GiCancel } from "react-icons/gi";
import HookErrorComponent from "../atoms/HookErrorComponent";
import HookInput from "../atoms/HookInput";
import { FormProvider, useForm } from "react-hook-form";
import { email_validation } from "../../utils/InputValidations";

const ResetPass = ({ setResetPass }) => {
  const [isLoading, setIsLoading] = useState(false);
  const [response, setResponse] = useState(null);
  const [error, setError] = useState(null);
  const [errors, setErrors] = useState([]);

  const methods = useForm();

  const submitResetRequest = methods.handleSubmit(async (data) => {
    console.log("trying");
      error && setError(null);
      setIsLoading(true);
      return authService
        .resetPassword(data.email)
        .then((response) => {
          if (response.status === 200) {
            setIsLoading(false);

            setResponse(
              response.data.responseMessage
            );
          }
        })
        .catch((error) => {
          setIsLoading(false);
          setError("Could not send request!");
        });
    
  });

  return (
    <div className="signUp__background signUp__glass flex justify-center items-center">
      <div className="w-[700px] h-[450px] bg-[#1A1A1AEF] rounded-[20px] flex flex-col items-center relative">
        <button
          className="mt-4 absolute right-[20px] text-primary text-[30px]"
          title={"Cancel"}
          onClick={() => setResetPass(false)}
        >
          <GiCancel />
        </button>
        <h2 className="text-[60px] text-white my-10">Request password reset</h2>
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
        {error && (
          <div className="w-max text-center ">
            <p id="error" className="text-red-700 text-[18px]">{error}</p>
          </div>
        )}
        {response && (
          <div className="w-[500px] text-center">
            <p id="success" className="text-green-700 text-[18px]">{response}</p>
            <div className="mt-[30px]">
            <Button id={"ToLoginButton"} title={"Back to Login"} variation={1} buttonSize={1} handleClick={() => setResetPass(false)} type="submit" />
            </div>
          </div>
        )}

        {!response && (
          <FormProvider {...methods}>
            <div id="input box">
              <form
                onSubmit={e => e.preventDefault()}
                noValidate
                autoComplete="off"
                className="container"
              >
                {errors && (
                  <div className="w-[500px] flex flex-col text-center">
                    {Object.keys(errors).map((singleError) => (
                      <HookErrorComponent key={JSON.stringify(singleError)} errorMessage={errors[singleError]["message"]} id={JSON.stringify(singleError)}/>
                    ))}
                  </div>
                )}
                <div className="h-[20px] my-[20px] flex flex-col items-center justify-between">
                  <HookInput
                    setErrors={setErrors}
                    {...email_validation}
                  />
                </div>
                <div className="flex flex-col justify-between items-center mt-[50px] h-[150px]">
                  <Button id={"SendRequestButton"} title={"Send request!"} variation={1} buttonSize={1} handleClick={submitResetRequest} type="submit" />
                </div>
              </form>
            </div>
          </FormProvider>
        )}
      </div>
    </div>

  );
};

export default ResetPass;
