import { createSlice } from "@reduxjs/toolkit"

const initialState = {
    confirmationWindow: false,
    question_id : ""
}

export const deleteQuestionSlice = createSlice({
    name: "deleteQuestion",
    initialState,
    reducers: {
        setConfirmationWindow: (state, action) =>{
            state.confirmationWindow = action.payload;
        },
        setQuestionToDelete: ( state, action) =>{
            state.question_id = action.payload; 
        }
    }
});
export const {setConfirmationWindow, setQuestionToDelete} = deleteQuestionSlice.actions;

export default deleteQuestionSlice.reducer;


