import { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { getNotification, setNotificationStatus } from "../../utils/features/notificationSlice";
import { useState } from "react";
import Button from "../atoms/Button";
import notificationsService from "../../services/notifications.service";
import { useNavigate } from "react-router";
import ChangeResponseBox from "../atoms/ChangeResponseBox";
import NotificationResponseBox from "../atoms/NotificationResponseBox";
import { setTokenData } from "../../utils/features/userDataSlice";

const NotificationDetailsBox = () => {

    const navigate = useNavigate();
    const dispatch = useDispatch();
    const message = useSelector((state) => state.notification.message);
    const category = useSelector((state) => state.notification.category);
    const action = useSelector((state) => state.notification.action);
    const user = useSelector((state) => state.notification.user);
    const question = useSelector((state) => state.notification.question);
    const id = useSelector((state) => state.notification.id);
    const comment = useSelector((state) => state.notification.comment);
    const notification = useSelector((state) => state.notification);
    const [error,setError] = useState(null);
    const [response, setResponse] = useState(null);


    const handleClick = (user_id, question_id, id) => {
        if (action === "Delete_account") {
            deleteAccount(user_id, id);
        }
        if (action === "Reporting_question") {
            handleQuestion(question_id, id);
        }
        if (action === "First_name") {
            changeFirstName(id);
        };
        if (action === "Last_name") {
            changeLastName(id);
        };
        if (action === "Exam_access") {
            acceptRequest(id);
        }
    }

    const handleQuestion = (question_id, id) => {
        try {
            notificationsService.handleNotification(id).then(
                navigate("/updateQuestion/" + question_id))
        }
        catch (error) {
            console.log(error);
        }
    }

    const changeFirstName = (id) => {
        notificationsService.changeFirstName(id).then((response) => {
            setResponse(response.data.confirmationMessage)
        }).catch((error)=> {
            setError(error.response.data);
        });
        
    }

    const changeLastName = (id) => {
        notificationsService.changeLastName(id).then((response) => {
            setResponse(response.data.confirmationMessage)
        }).catch((error)=> {
            setError(error.response.data);
        });
    }

    const deleteAccount = (user_id, id) => {
        notificationsService.deleteAccount(user_id, id).then((response) => {
            setResponse(response.data.confirmationMessage)
        }).catch((error)=> {
            setError(error.response.data);
        });
    }

    const acceptRequest = (id) => {
        notificationsService.acceptRequest(id).then((response) => {
            setResponse(response.data.confirmationMessage);
            console.log(response.data);
        }).catch((error)=> {
            setError(error.response.data);
        });
    }

    const deleteNotification = (id) => {
        notificationsService.deleteNotification(id).then((response) => {
            console.log("deleting");
            dispatch(setNotificationStatus(false))
        });
    }
    useEffect(() => {
        console.log(notification);
    }, [question, question.questionContent, category, action, user, id, comment, message])
    return (
        <div>
            <h2 className="lg:text-[30px] md:text-[38px] text-white mt-2 ml-2 text-center">{message}</h2>


            <div className="flex flex-col h-[60vh] items-center justify-start">
                {action === "Exam_access" && <div>
                    <div className="border-2  border-[#FFFFFF22] rounded-[20px] mt-8 mb-4 p-2">
                        <p className="text-white text-center text-[20px] mb-4">Do you want to grant</p>
                        <p className={` lg:w-[350px] md:w-[250px] h-[50px] m-4 border-2 border-primary bg-transparent rounded-[20px] text-white flex text-center outline-none focus:border-white align-center items-center justify-center`}>
                            {user.firstName} {user.lastName}
                        </p>
                        <p className="text-white text-center mb-2 text-[20px]">request for access to exams of type: </p>
                        <p className={` lg:w-[350px] md:w-[250px] h-[50px] m-4 border-2 border-primary bg-transparent rounded-[20px] text-white flex text-center outline-none focus:border-white align-center items-center justify-center`}>
                            {category.category}
                        </p>
                    </div>
                </div>}

                {action === "First_name" && <div>
                    <div className="border-2  border-[#FFFFFF22] rounded-[20px] mt-8 p-2">

                        <p className="lg:text-[30px] md:text-[20px] lg:font-bold text-center text-white" >User Details </p>
                        <p className="lg:text-[26px] md:text-[20px] lg:font-bold text-center text-white" >First name </p>
                        <p className={` lg:w-[350px] md:w-[250px] h-[50px] ml-8 border-2 border-primary bg-transparent rounded-[20px] text-white flex text-center outline-none focus:border-white align-center items-center justify-center`}>
                            {user.firstName}
                        </p>
                        <p className="lg:text-[26px] md:text-[20px] lg:font-bold text-center text-white mt-4" >Last name </p>
                        <p className={` lg:w-[350px] md:w-[250px] h-[50px] ml-8 border-2 border-primary bg-transparent rounded-[20px] text-white flex text-center outline-none focus:border-white align-center items-center justify-center`}>
                            {user.lastName}
                        </p>

                        <p className="md:text-[20px] mt-4 text-center text-white">Approve change of their first name to {comment}?</p>
                    </div>


                </div>}
                {action === "Last_name" && <div>
                    <div className="border-2  border-[#FFFFFF22] rounded-[20px] mt-8 p-2">

                        <p className="lg:text-[30px] md:text-[20px] lg:font-bold text-center text-white" >User Details </p>
                        <p className="lg:text-[26px] md:text-[20px] lg:font-bold text-center text-white" >First name </p>
                        <p className={` lg:w-[350px] md:w-[250px] h-[50px] ml-8 border-2 border-primary bg-transparent rounded-[20px] text-white flex text-center outline-none focus:border-white align-center items-center justify-center`}>
                            {user.firstName}
                        </p>
                        <p className="lg:text-[26px] md:text-[20px] lg:font-bold text-center text-white mt-4" >Last name </p>
                        <p className={` lg:w-[350px] md:w-[250px] h-[50px] ml-8 border-2 border-primary bg-transparent rounded-[20px] text-white flex text-center outline-none focus:border-white align-center items-center justify-center`}>
                            {user.lastName}
                        </p>

                        <p className="md:text-[20px] mt-4 text-center text-white">Approve change of their last name to {comment}?</p>
                    </div>


                </div>}

                {action === "Delete_account" && <div>
                    <div className="border-2  border-[#FFFFFF22] rounded-[20px] mt-8 p-2">

                        <p className="lg:text-[30px] md:text-[20px] lg:font-bold text-center text-white" >User Details </p>
                        <p className="lg:text-[26px] md:text-[20px] lg:font-bold text-center text-white" >First name </p>
                        <p className={` lg:w-[350px] md:w-[250px] h-[50px] ml-4 border-2 border-primary bg-transparent rounded-[20px] text-white flex text-center outline-none focus:border-white align-center items-center justify-center`}>
                            {user.firstName}
                        </p>
                        <p className="lg:text-[26px] md:text-[20px] lg:font-bold text-center text-white mt-4" >Last name </p>
                        <p className={` lg:w-[350px] md:w-[250px] h-[50px] ml-4 border-2 border-primary bg-transparent rounded-[20px] text-white flex text-center outline-none focus:border-white align-center items-center justify-center`}>
                            {user.lastName}
                        </p>
                    </div>
                </div>}
                {action === "Reporting_question" && <div className="flex text-center outline-none focus:border-white align-center items-center justify-center">
                    <div className="border-2  border-[#FFFFFF22] rounded-[20px] mt-8 p-2">
                        <p className="lg:text-[26px] md:text-[20px] lg:font-bold text-center text-white" >Question</p>
                        <textarea
                            readOnly
                            id="question"
                            rows={2}
                            cols={40}
                            className="mt-2 border-2 border-primary bg-transparent rounded-[20px] text-white p-2 resize-none"
                            value={question.questionContent}
                        >
                        </textarea>
                        <p className="lg:text-[26px] md:text-[20px] lg:font-bold text-center text-white" >Comment </p>
                        <textarea
                            readOnly
                            id="comment"
                            rows={3}
                            cols={40}
                            className="m-2 border-2 border-primary bg-transparent rounded-[20px] text-white p-2 resize-none"
                            value={comment}
                        >

                        </textarea>
                    </div>

                </div>


                }
                {(response || error) && (
                    <div>
                        <NotificationResponseBox
                            setResponse={setResponse}
                            response={response}
                            setError={setError}
                            error={error}
                        />
                    </div>
                )}

                <div className="flex flex-col">
                    <Button
                        title={"Accept"}
                        variation={1}
                        buttonSize={1}
                        id={"Accept_button"}
                        handleClick={() => handleClick(user.id, question.id, id)}
                    />
                    <Button
                        title={"Deny"}
                        variation={1}
                        buttonSize={1}
                        id={"Deny_button"}
                        handleClick={() => deleteNotification(id)}
                    />
                </div>
            </div>
        </div>


    )
}; export default NotificationDetailsBox;