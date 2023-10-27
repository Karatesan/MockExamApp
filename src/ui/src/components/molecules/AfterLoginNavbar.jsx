import React, { useEffect, useState } from "react";
import { afterLoginNavbarConsts } from "../../assets/constants";
import { RiSettings4Fill } from "react-icons/ri";
import { RiLogoutCircleFill } from "react-icons/ri";
import { useNavigate } from "react-router";
import authService from "../../services/auth.service";
import { useDispatch, useSelector } from "react-redux";
import { logout } from "../../utils/features/userDataSlice";

const AfterLoginNavbar = ({
  setTestBox,
  testBoxOpen,
  setSettingsWindow,
  setAddQuesionWindow,
  setHistoryWindow,
  clearAllWindows,
  setStatsWindow,
  setSearchWindow,
  setRequestExamWindow,
}) => {
  const [iconHighlighted, setIconHighlighted] = useState("");
  const navigate = useNavigate();
  const role = useSelector((state) => state.userData.role);
  const dispatch = useDispatch();

  const handleClick = (e) => {
    //so when user cancels testBox goes back to window he had oppened not main page

    if (e !== "test" && e !== "examRequest") clearAllWindows();
    if (e === "test") {
      setRequestExamWindow(false);
      setTestBox(true);
    }
    if (e === "question" && role === "ADMIN") setAddQuesionWindow(true);
    if (e === "settings") setSettingsWindow(true);
    if (e === "completed") setHistoryWindow(true);
    if (e == "main") clearAllWindows();
    if (e === "stats") setStatsWindow(true);
    if (e === "search" && role === "ADMIN") setSearchWindow(true);
    if (e === "examRequest") {
      setTestBox(false);
      setRequestExamWindow(true);
    }
    setIconHighlighted(e);
  };

  const handleLogout = () => {
    authService.logout();
    dispatch(logout());
    navigate("/");
  };
  return (
    <div className={`absolute left-0 z-[200] `}>
      <div className="w-[82px] overflow-x-hidden hover:w-[270px] transition-all duration-500 h-[85vh] rounded-r-[20px] lg:bg-[#FFFFFF22] md:bg-[#343434] cursor-pointer flex flex-col justify-between">
        <div className="flex flex-col w-[250px] h-[350px] justify-evenly overflow-x-hidden">
          {afterLoginNavbarConsts.map(
            (elem, index) =>
              elem.access.includes(role) && (
                <div
                  key={index}
                  className="flex items-center"
                  onClick={() => handleClick(elem.id)}
                >
                  <a id={elem.id}>
                    <elem.img
                      className={`text-[40px] ml-[20px] mr-[25px] ${
                        iconHighlighted === elem.id
                          ? "text-primary"
                          : "text-[#FFFFFF88]"
                      } hover:text-primary transition-all`}
                    />
                  </a>
                  <p className="text-[17px] text-[#FFFFFF88] font-semibold">
                    {elem.title}
                  </p>
                </div>
              )
          )}
        </div>
        <div className="mb-10 flex flex-col w-[250px] h-[120px] justify-evenly overflow-x-hidden">
          <div
            className="flex items-center"
            onClick={() => {
              setIconHighlighted("settings"), handleClick("settings");
            }}
          >
            <RiSettings4Fill
              className={`text-[40px] ml-[20px] mr-[25px] text-[#FFFFFF88] ${
                iconHighlighted === "settings"
                  ? "text-primary"
                  : "text-[#FFFFFF88]"
              }
            hover:text-primary
             focus:text-primary transition-all`}
            />
            <p className="text-[17px] text-[#FFFFFF88] font-semibold">
              SETTINGS
            </p>
          </div>
          <div className="flex items-center" id="logout" onClick={handleLogout}>
            <RiLogoutCircleFill
              className={`text-[40px] ml-[20px] mr-[25px] text-[#FFFFFF88] hover:text-primary 
            focus:text-primary transition-all`}
            />
            <p className="text-[17px] text-[#FFFFFF88] font-semibold">
              LOG OUT
            </p>
          </div>
        </div>
      </div>
    </div>
  );
};

export default AfterLoginNavbar;
