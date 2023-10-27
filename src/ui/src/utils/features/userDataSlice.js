import { createSlice } from "@reduxjs/toolkit";
import jwtDecode from "jwt-decode";

const initialState = {
    firstname: sessionStorage.getItem("username"),
    lastname: sessionStorage.getItem("lastname"),
    role: sessionStorage.getItem("role"),
    profilePicture: sessionStorage.getItem("profile_picture"),
    loggedIn: false,
}

export const userDataSlice = createSlice({
    name:'userData',
    initialState,
    reducers: {
        setTokenData: (state,action) =>{
            let decodedToken = jwtDecode(action.payload);
            state.firstname = decodedToken.firstname;
            state.lastname = decodedToken.lastname;
            state.role = decodedToken.role;
            state.loggedIn = true;
        },
        setProfilePicture: (state, action) =>{
            state.profilePicture = action.payload;
        },
        logout: (state) => {
            state.firstname = null;
            state.lastname = null;
            state.role = null;
            state.loggedIn = false;
            state.profilePicture = null;
        }
    }
});

export const {setTokenData, setProfilePicture, logout} = userDataSlice.actions;

export default userDataSlice.reducer;
