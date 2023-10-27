import { configureStore } from "@reduxjs/toolkit";
import userDataReducer from '../utils/features/userDataSlice';
import loginBoxReducer from "./features/loginBoxSlice";
import deleteQuestionReducer from "./features/deleteQuestionSlice";
import notificationReducer from "./features/notificationSlice";

export const store = configureStore({
    reducer: {
        notification: notificationReducer,
        userData: userDataReducer,
        loginBox: loginBoxReducer,
        deleteQuestion: deleteQuestionReducer,       
    }
})