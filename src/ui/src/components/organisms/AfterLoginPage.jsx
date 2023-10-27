import React, { useEffect, useRef, useState } from "react";
import styles from "../../../style";
import AfterLoginNavbar from "../molecules/AfterLoginNavbar";
import TestBox from "./TestBox";
import { set } from "react-hook-form";
import UserSettings from "./UserSettings";
import AddQuestion from "./AddQuestion";
import ExamHistory from "./ExamHistory";
import Stats from "./Stats";
import SearchWindow from "./SearchWindow";
import MainPageComponent from "../atoms/MainPageComponent";
import { afterLoginNavbarConsts } from "../../assets/constants";
import { FaChartLine } from "react-icons/fa";
import { useSelector } from "react-redux";
import Notifications from "./Notifications";
import RequestExamAccess from "../molecules/RequestExamAccess";

const AfterLoginPage = () => {
  const [testBox, setTestBox] = useState(false);
  const [settingsWindow, setSettingsWindow] = useState(false);
  const [addQuestionWindow, setAddQuesionWindow] = useState(false);
  const [historyWindow, setHistoryWindow] = useState(false);
  const [statsWindow, setStatsWindow] = useState(false);
  const [searchWindow, setSearchWindow] = useState(false);
  const [requestExamWindow, setRequestExamWindow] = useState(false);
  const role = useSelector((state) => state.userData.role);

  const clearAllWindows = () => {
    setTestBox(false);
    setAddQuesionWindow(false);
    setSettingsWindow(false);
    setHistoryWindow(false);
    setStatsWindow(false);
    setSearchWindow(false);
    setRequestExamWindow(false);
  };

  useEffect(() => {
    navigateWithSession("history", setHistoryWindow);
    navigateWithSession("settings", setSettingsWindow);
  }, []);

  const navigateWithSession = (name, setter) => {
    if (sessionStorage.getItem(name)) {
      sessionStorage.removeItem(name);
      setter(true);
    }
  };

  return (
    <>
      {/* <div className="w-full h-[1px] bg-[#FFFFFF22]" /> */}

      {testBox && <TestBox setTestBox={setTestBox} />}
      <AfterLoginNavbar
        testBoxOpen={testBox}
        setTestBox={setTestBox}
        setAddQuesionWindow={setAddQuesionWindow}
        setSettingsWindow={setSettingsWindow}
        setHistoryWindow={setHistoryWindow}
        clearAllWindows={clearAllWindows}
        setStatsWindow={setStatsWindow}
        setSearchWindow={setSearchWindow}
        setRequestExamWindow={setRequestExamWindow}
      />
      <div className={`${styles.paddingX} ${styles.flexCenter} justify-center`}>
        <div className={`${styles.boxWidth}`}>
          {/* <h2 className="font-semibold ml-20 text-[36px] text-white">
            Hello, {sessionStorage.getItem("username")}
          </h2> */}
          <div
            style={{ minHeight: "78.5vh" }}
            className="xl:w-full rounded-[20px] border-[1px] border-[#FFFFFF22]"
          >
            {!addQuestionWindow &&
              !historyWindow &&
              !settingsWindow &&
              !searchWindow &&
              !statsWindow &&
              role === "TRAINEE" && (
                <div>
                  <div className="flex flex-row m-6 mt-28 justify-center">
                    <MainPageComponent
                      text={
                        "Here, you can see your learning path and take a look on your past achievements."
                      }
                      elem="stats"
                    />
                    <MainPageComponent
                      text={
                        "Here, you can take mock exams where you can choose category, difficulty level and exam style."
                      }
                      elem="test"
                    />
                  </div>
                  <div className="flex flex-row m-6 mt-12 justify-center">
                    <MainPageComponent
                      text={
                        "Here, you can look through your past exams and extend your learning growth."
                      }
                      elem="completed"
                    />
                    <MainPageComponent
                      text={
                        "Here, you can change your first and last name, your password and upload a profile picture."
                      }
                      elem="settings"
                    />
                  </div>
                </div>
              )}
            {!addQuestionWindow &&
              !historyWindow &&
              !settingsWindow &&
              !searchWindow &&
              !statsWindow &&
              role === "ADMIN" && <Notifications />}
            {addQuestionWindow && (
              <AddQuestion setAddQuesionWindow={setAddQuesionWindow} />
            )}
            {historyWindow && (
              <ExamHistory setHistoryWindow={setHistoryWindow} />
            )}
            {settingsWindow && <UserSettings />}
            {statsWindow && <Stats />}
            {searchWindow && <SearchWindow setSearchWindow={setSearchWindow} />}
            {requestExamWindow && (
              <RequestExamAccess setRequestExamWindow={setRequestExamWindow} />
            )}
          </div>
        </div>
      </div>
    </>
  );
};

export default AfterLoginPage;
