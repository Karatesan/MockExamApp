import React, { useState } from "react";
import Input from "../atoms/Input";
import HookInput from "../atoms/HookInput";
import Button from "../atoms/Button";
import ResetPass from "./ResetPass";
import authService from "../../services/auth.service";
import PropTypes from "prop-types";
import { useNavigate } from "react-router";
import jwtDecode from "jwt-decode";
import { GiCancel } from "react-icons/gi";
import {
  email_validation,
  password_validation,
} from "../../utils/InputValidations";
import { FormProvider, useForm } from "react-hook-form";
import ErrorComponent from "../atoms/ErrorComponent";
import HookErrorComponent from "../atoms/HookErrorComponent";
import HookPasswordInput from "../atoms/HookPasswordInput";
import NoValidationPasswordInput from "../atoms/NoValidationPasswordInput";
import changeUserService from "../../services/changeUser.service";
import { useDispatch, useSelector } from "react-redux";
import { setProfilePicture, setTokenData } from "../../utils/features/userDataSlice";
import { setLoginBox } from "../../utils/features/loginBoxSlice";

const LoginBox = () => {

  const loginBox = useSelector((state) => state.loginBox.loginBox);

  const [resetPass, setResetPass] = useState(false);
  const [error, setError] = useState(null);
  const [errors, setErrors] = useState([]);

  const dispatch = useDispatch();
  const navigate = useNavigate();

  const methods = useForm();
  const resetResponses = () => {
    error && setError(null);
    errors && setErrors([]);
  };
  const resetPassPopUp = () => {
    setResetPass((current) => !current);
  };

  const handleLogin = methods.handleSubmit(async (data) => {
    resetResponses();
    authService
      .login(data.email, data.password)
      .then(async (response) => {
        var picture = await changeUserService.returnProfilePicture();
        if (picture != undefined) {
          sessionStorage.setItem("profile_picture", picture);
        }
        dispatch(setProfilePicture(picture));
        dispatch(setTokenData(response.token));
        dispatch(setLoginBox(false));
        navigate("/main");
      })
      .catch((error) => {
        setError(error.response.data);
      })
  });

  return (
    <div className="signUp__background signUp__glass flex justify-center items-center">
      {resetPass && <ResetPass setResetPass={setResetPass} />}
      <div className="w-[550px] lg:h-[700px] bg-[#1A1A1AEF] rounded-[20px] flex flex-col items-center relative">
        <button
          className="mt-4 absolute right-[20px] text-primary text-[30px]"
          title={"Cancel"}
          onClick={() => {
            dispatch(setLoginBox(false))
            navigate("/")
          }}
        >
          <GiCancel />
        </button>
        <h2 className="text-[60px] text-white mt-10 mb-3">Login</h2>
        {error && (
          <div className="w-max text-center ">
            <pre id="error" className="font-poppins font-normal text-red-700 text-[18px]">{error}</pre>
          </div>

        )}
        {errors && (
          <div className="w-[500px] flex flex-col text-center">
            {Object.keys(errors).map((singleError) => (
              <HookErrorComponent
                key={JSON.stringify(singleError)}
                errorMessage={errors[singleError]["message"]}
                id={JSON.stringify(singleError)}
              />
            ))}
          </div>
        )}
        <FormProvider {...methods}>
          <div className="loginForm">
            <form
              onSubmit={(e) => e.preventDefault()}
              noValidate
              autoComplete="off"
              className=""
            >
              <div className="h-[200px] mt-[30px] flex flex-col items-center justify-evenly">
                <HookInput setErrors={setErrors} {...email_validation} />
                <NoValidationPasswordInput
                  setErrors={setErrors}
                  {...password_validation}
                />
              </div>

              <button
                type="button"
                className="w-[350px] flex justify-start"
                onClick={() => resetPassPopUp()}
                id="forgot_password_button"
              >
                <span className="lg:pl-4 md:pl-14 mb-10 text-primary hover:text-slate-300 cursor-pointer transition-all">
                  Forgot password?
                </span>
              </button>
              <div className="flex flex-col justify-between items-center h-[150px]">
                <Button
                  id={"LoginButton"}
                  title={"Login!"}
                  variation={1}
                  buttonSize={1}
                  type="submit"
                  handleClick={handleLogin}
                />
              </div>
            </form>
          </div>
        </FormProvider>
      </div>
    </div>
  );
};
LoginBox.propTypes = {
  navigate: PropTypes.func,
};

export default LoginBox;
