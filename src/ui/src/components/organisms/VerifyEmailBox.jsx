import React, { useState } from "react";
import { useNavigate, useParams } from "react-router";
import authService from "../../services/auth.service";
import { useEffect } from "react";
import Button from "../atoms/Button";
import { GiCancel } from "react-icons/gi";
import PacmanLoader from "react-spinners/ClipLoader";

const VerifyEmailBox = () => {
    const [response, setResponse] = useState(null);
    const [error, setError] = useState(null);
    const [expiredError, setExpiredError] = useState(false);
    const [isLoading, setIsLoading] = useState(false);
    const params = useParams();

    const navigate = useNavigate();

    const verifyEmail = async () => {

        return authService
            .verifyEmail(params.token)
            .then((response) => {
                if (response.status === 200) {
                    error && setError(null);
                    expiredError && setExpiredError(false);
                    setResponse(
                        "Your account has been verified!"
                    )
                }
            })
            .catch((error) => {
                setError(error.response.data);
                if (error.response.status === 409) {
                    setExpiredError(true);
                }
            });
    }
    const resendEmail = async () => {
        error && setError(null);
        setIsLoading(true);
        return authService
            .resendEmail(params.token)
            .then((response) => {
                if (response.status === 200) {
                    error && setError(null);
                    setIsLoading(false);
                    expiredError && setExpiredError(false);
                    setResponse(
                        "New Verification link has been send!"
                    )
                }
            })
            .catch((error) => {
                setIsLoading(false);
                setExpiredError(true);
                setError(error.response.data);
            });
    }

    return (
        <div className="signUp__background signUp__glass flex justify-center items-center">
            <div className="w-[550px] h-[325px] bg-[#1A1A1AEF] rounded-[20px] flex flex-col items-center relative">
                <button
                    className="mt-4 absolute right-[20px] text-primary text-[30px]"
                    title={"Cancel"}
                    onClick={() => navigate("/")}
                >
                    <GiCancel />
                </button>
                <h2 className="text-[60px] text-white mt-10 mb-3">Verification</h2>
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
                        <p id="success" className="text-green-700 text-[18px]">{response}</p>
                        <div className="flex flex-col justify-between items-center h-[150px] mt-[20px]">
                            <Button title={"Get started!"} variation={1} buttonSize={1} handleClick={() => navigate("/")} type="submit" />
                        </div>
                    </div>

                )}
                {!response && !error && (
                    <div className="flex flex-col justify-between items-center mt-[30px] h-[150px]">
                        <Button
                            id={"VerifyEmailButton"}
                            title={"Verify Email!"}
                            variation={1}
                            buttonSize={1}
                            handleClick={verifyEmail}
                            type="submit" />
                    </div>
                )}
                {error && (
                    <div id="errorDiv">
                        <div className="w-[500px] text-center ">
                            <p id="error" className="text-red-700 text-[18px]">{error}</p>
                        </div>
                        {expiredError && (
                            <div className="flex flex-col justify-between items-center h-[150px] mt-[20px]">
                                <Button
                                    id={"resendEmailButton"}
                                    title={"Resend Verification!"}
                                    variation={1}
                                    buttonSize={1}
                                    handleClick={resendEmail}
                                    type="submit" />
                            </div>
                        )}
                    </div>
                )}
            </div>
        </div>
    );
};


export default VerifyEmailBox;
