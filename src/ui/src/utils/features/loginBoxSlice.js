import { createSlice } from "@reduxjs/toolkit";

const initialState = {
    loginBox: false
}

export const loginBoxSlice = createSlice({
    name: 'loginBox',
    initialState,
    reducers: {
        setLoginBox: (state, action) =>{
            state.loginBox = action.payload;
        }
    }
});

export const {setLoginBox} = loginBoxSlice.actions;

export default loginBoxSlice.reducer;