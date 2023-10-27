import React from "react";
import Button from "../atoms/Button";
import { useState } from "react";
import { useNavigate, useParams } from "react-router";
import PacmanLoader from "react-spinners/ClipLoader";
import authService from "../../services/auth.service";
import { GiCancel } from "react-icons/gi";
import { FormProvider, useForm } from "react-hook-form";
import HookInput from "../atoms/HookInput";
import { confirm_password_validation, password_validation } from "../../utils/InputValidations";
import HookErrorComponent from "../atoms/HookErrorComponent";
import HookPasswordInput from "../atoms/HookPasswordInput";

const ResettingPasswordBox = () => {
  const [errors, setErrors] = useState([]);
  const [error, setError] = useState(null);
  const [response, setResponse] = useState(null);
  const [isLoading, setIsLoading] = useState(false);
  const params = useParams();

  const navigate = useNavigate();
  const methods = useForm();

  const submitResettingRequest = methods.handleSubmit(async (data) => {
    setError(null);
    setIsLoading(true);
    console.log(data)
    return authService
      .resettingPassword(data, params.token)
      .then((response) => {
        if (response.status === 200) {
          setIsLoading(false);
          error && setError(null);
          setResponse(response.data.responseMessage);
        }
      })
      .catch((error) => {
        setIsLoading(false);
        setError(error.response.data);
      }
      );
  });

  return (
    <div className="signUp__background signUp__glass flex justify-center items-center">
      <div className="w-[500px] h-[550px] bg-[#1A1A1AEF] rounded-[20px] flex flex-col items-center relative">
        <button
          className="mt-4 absolute right-[20px] text-primary text-[30px]"
          title={"Cancel"}
          onClick={() => navigate("/")}
        >
          <GiCancel />
        </button>
        <h2 className="text-[60px] text-white mt-10 mb-3">Reset password</h2>
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
        {response && !error && (
          <div className="w-[500px] text-center">
            <p id="success" className="text-green-700 text-[18px]  mt-[50px]">{response}</p>
            <div className="flex flex-col justify-between items-center mt-[50px] h-[150px]">
            <Button id={"GetStartedButton"} title={"Get started!"} variation={1} buttonSize={1} handleClick={() => navigate("/")} type="submit" />
            </div>
          </div>
        )}

        {error && !response && (
          <div className="w-max text-center ">
            <p id="error" className="text-red-700 text-[18px]">{error}</p>
          </div>
        )}
        {!response &&
          <FormProvider {...methods}>
            <div className="resettingPasswordForm">
              <form
                onSubmit={e => e.preventDefault()}
                noValidate
                autoComplete="off"
                className=""
              >
                {errors && (
                  <div className="w-[500px] flex flex-col text-center">
                    {Object.keys(errors).map((singleError) => (
                      <HookErrorComponent key={JSON.stringify(singleError)} errorMessage={errors[singleError]["message"]} id={JSON.stringify(singleError)} />
                    ))}
                  </div>
                )}
                <div className="h-[200px] flex flex-col items-center justify-evenly">
                  <HookPasswordInput setErrors={setErrors} {...password_validation} />
                  <HookPasswordInput setErrors={setErrors} {...confirm_password_validation} />
                </div >
                <div className="flex flex-col justify-between items-center h-[150px]">
                  <Button id={"SubmitPasswordButton"} title={"Submit password!"} variation={1} buttonSize={1} handleClick={submitResettingRequest} type="submit" />
                </div>
              </form>
            </div>
          </FormProvider>
        }
      </div>
    </div>
  );
};

export default ResettingPasswordBox;