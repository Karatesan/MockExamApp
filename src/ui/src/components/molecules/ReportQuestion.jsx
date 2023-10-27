import { GiCancel } from "react-icons/gi";
import Button from "../atoms/Button";
import { FormProvider, useForm } from "react-hook-form";
import { comment_validation } from "../../utils/InputValidations";
import TextArea from "../atoms/TextArea";
import { useState } from "react";
import ErrorComponent from "../atoms/ErrorComponent";
import authHeader from "../../services/auth-header";
import axios from "axios";


const ReportQuestion = ({ setReportQuestionWindow, question_id }) => {

    const [errors, setErrors] = useState([]);
    const methods = useForm({
        defaultValues: {
            questionId: question_id,
            comment: ""
        }
    });
    const submitRegister = methods.handleSubmit(async (data) => {
        const body = JSON.stringify(data);
        const headers = {"Content-type": "application/json"};
        headers["Authorization"] =  authHeader().Authorization;
        axios.post(
            "http://localhost:8080/api/users/reportQuestion", body, {headers}
        ).then((response) => setReportQuestionWindow(false))

    })
    return (
        <div className="signUp__background signUp__glass flex justify-center items-center">
            <div className="w-[500px] h-[350px] bg-[#1A1A1AEF] rounded-[20px] flex flex-col items-center relative">
                <button
                    className=" absolute right-[20px] top-[20px] text-primary text-[30px]"
                    title={"Cancel"}
                    onClick={() => setReportQuestionWindow(false)}
                >
                    <GiCancel />
                </button>
                <h2 className="lg:text-[50px] md:text-[38px] text-white mt-2 ml-2"> Report Question</h2>

                <FormProvider {...methods}>
                    <form
                        onSubmit={e => e.preventDefault()}
                        noValidate
                        autoComplete="off"
                        className="flex flex-col items-center relative m-4"
                    >
                        {errors["comment"]!=undefined &&
                        <div>
                        <p
                        className="text-red-700 mb-2"
                        >{errors["comment"].message}</p>  
                        </div>}

                        <TextArea
                            {...comment_validation}
                            question_id={question_id}
                            setErrors={setErrors}
                        />
                        <Button
                            title="Submit Report"
                            buttonSize={1}
                            variation={1}
                            handleClick={submitRegister}
                        />
                    </form>
                </FormProvider>
            </div>
        </div>)
};
export default ReportQuestion;