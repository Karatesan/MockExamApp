import { createSlice } from "@reduxjs/toolkit"
import { comment } from "postcss";

const initialState = {
    readNotification: false,
    id: "",
    message: "",
    action: "",
    category: "",
    user: [],
    question: [],
    comment: ""
}

export const notificationSlice = createSlice({
    name: 'notification',
    initialState,
    reducers: {
        setNotificationStatus: (state, action) => {
            state.readNotification = action.payload;
        },
        setNotificationDetails: (state, action) => {
            let notificationDetails = action.payload;
            switch (action.payload.action) {
                case ("Exam_access"):
                    return Object.assign({}, state, {
                        id: notificationDetails.id,
                        action: notificationDetails.action,
                        category: notificationDetails.category,
                        user: notificationDetails.user
                    })
                case ("Delete_account"):
                    return Object.assign({}, state, {
                        id: notificationDetails.id,
                        action: notificationDetails.action,
                        user: notificationDetails.user
                    })
                        ;
                case ("Reporting_question"):
                    return Object.assign({}, state, {
                        id: notificationDetails.id,
                        action: notificationDetails.action,
                        question: notificationDetails.question,
                        comment: notificationDetails.comment
                    });
                case ("First_name"):
                    return Object.assign({}, state, {
                        id: notificationDetails.id,
                        action: notificationDetails.action,
                        user: notificationDetails.user,
                        comment: notificationDetails.comment
                    })
                case ("Last_name"):
                    return Object.assign({}, state, {
                        id: notificationDetails.id,
                        action: notificationDetails.action,
                        user: notificationDetails.user,
                        comment: notificationDetails.comment
                    })
                default:
                    return state;
            }
        },
        setNotificationMessage: (state, action) => {
            return Object.assign({}, state, { message: action.payload })
        },
        getNotification: (state) => {
            return { state };
        }
    }
});

export const { setNotificationDetails, setNotificationMessage, setNotificationStatus, getNotification } = notificationSlice.actions;

export default notificationSlice.reducer;