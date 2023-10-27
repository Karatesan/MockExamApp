import { Alert } from "@mui/material";
import Button from "../atoms/Button";
import { FaExclamationTriangle } from "react-icons/fa";
import { useNavigate } from "react-router";
import { useState } from "react";
import { old_password_validation } from "../../utils/InputValidations";
import NoValidationPasswordInput from "../atoms/NoValidationPasswordInput";
import ChangeResponseBox from "../atoms/ChangeResponseBox";
import changeUserService from "../../services/changeUser.service";
import { FormProvider, useForm } from "react-hook-form";
import { GiCancel } from "react-icons/gi";
import authService from "../../services/auth.service";
import { useDispatch, useSelector } from "react-redux";
import { logout } from "../../utils/features/userDataSlice";
import { setLoginBox } from "../../utils/features/loginBoxSlice";


const DeleteAccountBox = () => {
    const navigate = useNavigate();
    const [errors, setErrors] = useState([]);
    const [error, setError] = useState(null);
    const [response, setResponse] = useState(null)
    const [passwordBox, setPasswordBox] = useState(false);
    const id = { id: "password" };
    function sleep(ms) {
        return new Promise(resolve => setTimeout(resolve, ms));
    }

    const methods = useForm();
    const dispatch = useDispatch();
    const loginBox = useSelector((state) => state.loginBox.loginBox);

    const deleteAccount = methods.handleSubmit(async (data) => {
        changeUserService.deleteUser(data)
            .then((response) => {
                if (response.status === 200) {
                    setResponse(response.data.confirmationMessage);
                    
                    sleep(4000).then(() => {  

                        authService.logout();      
                        navigate("/");
                        dispatch(logout()); 

                    })
                    
                    
                }
            })
            .catch((error) => {
                setError(error.response.data);
            });
    });

    const dontDeleteAccount = () => {
        navigate("/main");
    }

    return (
        <div className="signUp__background signUp__glass flex justify-center items-center">
            <div className="w-[500px] h-[400px] bg-[#1A1A1AEF] rounded-[20px] flex flex-col items-center relative">
                <button
                    className="mt-4 absolute right-[20px] text-primary text-[30px]"
                    title={"Cancel"}
                    onClick={() => navigate("/main")}
                >
                    <GiCancel />
                </button>
                {!passwordBox && !error &&
                    <div className="flex flex-col justify-between items-center">
                        <FaExclamationTriangle className="fill-[#7518ff] m-4" style={{ fontSize: "72px" }} icon={` fa-solid fa-triangle-exclamation`} />
                        <Alert variant="filled" className="bg-[#1A1A1AEF] mt-4 mb-3" severity="error">
                            <p className="text-[18px]">Do you really want to delete your Account?</p></Alert>

                        <div className="flex flex-col justify-between items-center">
                            <Button
                                id="yesButton"
                                buttonSize={1}
                                variation={1}
                                title="Yes"
                                handleClick={() => setPasswordBox(true)}

                            />
                            <Button
                                id="noButton"
                                buttonSize={1}
                                variation={1}
                                title="No"
                                handleClick={() => dontDeleteAccount()}

                            />
                        </div>
                    </div>
                }
                {passwordBox && !error &&

                    <div className="flex flex-col justify-between items-center">
                        <FaExclamationTriangle className="fill-[#7518ff] mt-16 m-4" style={{ fontSize: "72px" }} icon={` fa-solid fa-triangle-exclamation`} />
                        <FormProvider {...methods}>
                            <form
                                onSubmit={e => e.preventDefault()}
                                noValidate
                                autoComplete="off"
                                className=""
                            >
                                <div className="m- 4">
                                    <NoValidationPasswordInput setErrors={setErrors} {...{ ...old_password_validation, ...id }} />
                                </div>
                                <div className="flex flex-col justify-between items-center m-4">
                                    <Button
                                        id="DeleteButton"
                                        buttonSize={1}
                                        variation={1}
                                        title="Delete Account"
                                        handleClick={() => deleteAccount()}
                                    />
                                </div>
                            </form>
                        </FormProvider>
                    </div>
                }


            </div>
            {(error || response) &&
                <ChangeResponseBox
                    setResponse={setResponse}
                    response={response}
                    setError={setError}
                    error={error}
                />
            }


        </div>
    );
}
export default DeleteAccountBox;