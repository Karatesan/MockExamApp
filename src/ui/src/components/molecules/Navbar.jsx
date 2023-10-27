import { React, useState, useEffect } from "react";
import styles from "../../../style";
import Button from "../atoms/Button";
import fdm_logo from "../../assets/logos/fdm_logo.png";
import SignUpBox from "../organisms/SignUpBox";
import LoginBox from "../organisms/LoginBox";
import authService from "../../services/auth.service";
import jwtDecode from "jwt-decode";
import { useNavigate } from "react-router";
import TestBox from "../organisms/TestBox";
import axios from "axios";
import authHeader from "../../services/auth-header";
import changeUserService from "../../services/changeUser.service";
import { RiSettings4Fill } from "react-icons/ri";
import { CgProfile } from "react-icons/cg";
import { useSessionStorage } from "../../utils/useSessionStorage";
import { useDispatch, useSelector } from "react-redux";
import { setProfilePicture } from "../../utils/features/userDataSlice";
import { setLoginBox } from "../../utils/features/loginBoxSlice";

const Navbar = () => {
  const [signUpBox, setSignUpBox] = useState(false);
  const [testBox, setTestBox] = useState(false);
  const dispatch= useDispatch();
  const [isImageLoading, setIsImageLoading] = useState(true);
  const navigate = useNavigate();
  const [pictureSettings, setPictureSettings] = useState(false);
  const loginBox = useSelector((state) => state.loginBox.loginBox);

  const firstname= useSelector((state) => state.userData.firstname);
  const profile_picture = useSelector((state) => state.userData.profilePicture);

  useEffect(() => {
    if (isLoggedin) {
      axios
        .get("http://localhost:8080/api/users/getImage", {
          headers: authHeader(),
        })
        .then((response) => {
          if (response.data.base64image) {
            dispatch(setProfilePicture("data:image/png;base64," + response.data.base64image));
          }
          setIsImageLoading(false);
        });
    }
  }, [loginBox]);

  const isLoggedin = sessionStorage.getItem("token") !== null;

  

  const signUpPop = () => {
    setSignUpBox((current) => !current);
  };
  const loginPopUp = () => {
    dispatch(setLoginBox(true));
  };

  const logout = () => {
    authService.logout();
    navigate("/");
  };


  const onPictureClick = () => {
    sessionStorage.setItem("settings", "true");
    //Reloades page if you are already on /main page
    if (window.location.pathname !== "/main") navigate("/main");
    else {
      console.log("reload");
      window.location.reload();
    }
  };

  //Moved to auth service so its set right after receiving token from api
  // useEffect(() => {
  //   const token = sessionStorage.getItem("token");
  //   if (token) {
  //     const decodedToken = jwtDecode(token);
  //     sessionStorage.setItem("username", decodedToken.firstname);
  //     sessionStorage.setItem("lastname", decodedToken.lastname);
  //     sessionStorage.setItem("role", decodedToken.role); // This will contain the decoded data from the token
  //   }
  // }, [loginBox]);

  return (
    <nav className="w-full mt-6">
      {signUpBox && <SignUpBox setSignUpBox={setSignUpBox} />}
      {loginBox && <LoginBox/>}

      <div className={`${styles.paddingX} ${styles.flexCenter} justify-center`}>
        <div className={`${styles.boxWidth}`}>
          <div className={`xl:max-w-[1280px] lg:max-w-[1000px] md:max-w-[750px] sm:max-w-[500px] w-full`}>
            <div className="flex justify-between items-center relative">
              <div
                id="logo_name_container"
                className="flex items-center w-[10%]"
              >
                <img
                  src={fdm_logo}
                  className="w-full cursor-pointer"
                  onClick={() => {
                    isLoggedin ? navigate("/main") : navigate("/");
                  }}
                />

                {!isLoggedin && (
                  <p className="text-[15px] text-white ml-4"></p>
                )}
              </div>
              <div
                id="name_picture_container"
                className="flex justify-between items-center absolute right-[20px]"
              >
                {isLoggedin && (
                  <h2 className="font-semibold  lg:text-[36px] md:text-[30px] text-[24px] text-white mr-4">
                    Hello, {firstname}
                  </h2>
                )}

                {isLoggedin && !isImageLoading && (
                  <div
                    id="profile_picture_container"
                    className={`relative flex justify-center items-center cursor-pointer lg:w-[56px] md:w-[36px] w-[30px] lg:h-[56px] md:h-[36px] h-[30px] object-cover rounded-xl ${
                      pictureSettings ? "border-[1px] border-white" : ""
                    }`}
                    onClick={() => onPictureClick()}
                    onMouseEnter={() => setPictureSettings(true)}
                    onMouseLeave={() => setPictureSettings(false)}
                  >
                    {pictureSettings && (
                      <RiSettings4Fill
                        size={15}
                        color="white"
                        className="absolute right-[5px] top-[5px]"
                      />
                    )}
                    {profile_picture && (
                      <img
                        className="rounded-full md:w-[34px] md:h-[34px]"
                        src={profile_picture}
                        alt="profile_picture"
                      ></img>
                    )}
                    {!profile_picture && <CgProfile size={50} color="white" />}
                  </div>
                )}
              </div>

              <div className="flex">
                {!isLoggedin && (
                  <Button
                    id="LoginButton"
                    title="Login"
                    buttonSize={0}
                    handleClick={loginPopUp}
                  />
                )}
                {!isLoggedin && (
                  <Button
                    id="SignUpButton"
                    title="Sign Up"
                    variation={1}
                    buttonSize={0}
                    handleClick={signUpPop}
                  />
                )}
              </div>
            </div>
          </div>
        </div>
      </div>
    </nav>
  );
};

export default Navbar;
