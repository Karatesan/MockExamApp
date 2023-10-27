import { useDispatch } from "react-redux";
import Button from "./Button";
import { setNotificationStatus } from "../../utils/features/notificationSlice";

const NotificationResponseBox = ({ setResponse, response, setError, error }) => {
    const dispatch = useDispatch();
    return (
        
        <div className="signUp__background signUp__glass flex justify-center items-center">
            <div className="w-[550px] h-[300px] bg-[#1A1A1AEF] rounded-[20px] flex flex-col items-center text-center relative">
                {response ?
                    <p className="text-green-700 w-[400px] text-center  break-words font-poppins font-normal my-[50px] text-[24px]">{response}</p> :
                    <p className="text-red-700 w-[400px] text-center font-poppins font-normal my-[50px] text-[24px]">{error}</p>}
                <div className="">
                    <Button id={"Return"} title={"Back"} variation={1} buttonSize={1} handleClick={() => { setResponse(null), setError(null), dispatch(setNotificationStatus(false));  }} type="submit" />
                </div>
            </div>
        </div>
    )
};
export default NotificationResponseBox;
