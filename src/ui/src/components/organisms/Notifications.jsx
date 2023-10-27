import { useEffect, useState } from "react";
import notificationsService from "../../services/notifications.service";
import { useDispatch, useSelector } from "react-redux";
import { setNotificationDetails, setNotificationMessage, setNotificationStatus } from "../../utils/features/notificationSlice";
import { Button } from "@mui/material";
import NotificationDetailsBox from "../molecules/NotificationDetailsBox";
const testData = [{
    id: "1",
    headline: "This is just a test"
},{
    id: "2",
    headline: "This is just a test"
}]

const Notifications = () => {

    const dispatch = useDispatch();

    const readTheNotification = useSelector((state) => state.notification.readNotification);

    const [empty,setEmpty] = useState(false);
    const [notifications,setNotifications] = useState([]);

    const readNotification = (id) => {
        notificationsService.readNotification(id).then((response)=>{
            console.log(response.data)
            dispatch(setNotificationDetails(response.data));
            dispatch(setNotificationStatus(true));
        })
    }

    const loadNotifications = async () => { 
        notificationsService.getNotifications({"action":"All","page":1}).then((response) => {
            setNotifications(response.data.list);
            setEmpty(false);
        }
        ).catch((error)=> {
            setEmpty(true);
        })
    }

    useEffect(() =>{
        loadNotifications();
    },[readTheNotification]);


    return(
        <div >
            <h2 className="lg:text-[40px] md:text-[20px] lg:font-bold text-center text-white mt-2 ml-2">Notifications</h2>
            <div className="flex flex-row">
            <div className="xl:w-1/2 m-2 rounded-[20px] border-[1px] border-[#FFFFFF22]" style={{ minHeight: "68.5vh" }}>
            {empty && <div className="flex justify-center items-center">
                <p className="text-center text-primary mt-12 text-[22px]">There are no notifications for you!</p>
            </div>
            }
            { !empty &&
            <table className="mt-2 ml-12 mb-2 w-5/6 absolut">
                <thead className="border-none w-full h-[0px]">
                    <tr className="border-spacing-0">
                        <th className="w-full border-none">
                        </th>
                    </tr>
                </thead>
                <tbody>
                {notifications.map((notification) =>  
                (<tr>
                <td className="text-white border-x-0 border-primary focus:border-[#7d35ea]" key={notification.id}>
                    <button onClick={() =>
                        { dispatch(setNotificationMessage(notification.message))
                            readNotification(notification.id)
                        
                        }}
                        className="text-[20px]"
                        >
                            {notification.message}
                            </button>
                            </td>
                </tr>
                ))}
                </tbody>
            </table>
            }
        </div>
        <div className="xl:w-1/2 m-2 rounded-[20px] border-[1px] border-[#FFFFFF22]" style={{ minHeight: "68.5vh" }}>
        {readTheNotification && <NotificationDetailsBox />
        }
        </div>
        </div>
        </div>
    );
}; export default Notifications;