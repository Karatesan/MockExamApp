import { Main, TestPage } from "./components";
import styles from "../style";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Navbar from "./components/molecules/Navbar";
import LoginBox from "./components/organisms/LoginBox";
import ProtectedRoute from "./components/atoms/ProtectedRoute";
import ResettingPasswordBox from "./components/organisms/ResettingPasswordBox";
import VerifyEmailBox from "./components/organisms/VerifyEmailBox";
import AfterLoginPage from "./components/organisms/AfterLoginPage";
import Stats from "./components/organisms/Stats";
import AddQuestion from "./components/organisms/AddQuestion";
import TakingExamInfo from "./components/organisms/TakingExamInfo";
import ChangePasswordPage from "./components/organisms/ChangePasswordPage";
import DeleteAccountPage from "./components/organisms/DeleteAccountPage";
import AdminMainPage from "./components/organisms/AdminMainPage";
import AdminRoute from "./components/atoms/AdminRoute";
import { useSelector } from "react-redux";
import UnprotectedRoute from "./components/atoms/UnprotectedRoute";
import { useEffect } from "react";
import UpdateQuestion from "./components/organisms/UpdateQuestion";

function App() {
  const isSignedIn = sessionStorage.getItem("token") !== null;
  const role = useSelector((state)=> state.userData.role);

  return (
    <>
      <div className="w-full m-0">
        <div
          className={`${styles.paddingX} ${styles.flexCenter} justify-center`}
        >
          <div className={`${styles.boxWidth}`}>
            <Navbar />
            <Routes>
              <Route
                path="/"
                element={
                  <UnprotectedRoute isSignedIn={isSignedIn}>
                    <Main />
                  </UnprotectedRoute>
                }
              />
              <Route path="/login" element={<LoginBox />} />
              <Route
                path="/resettingPassword/:token"
                element={<ResettingPasswordBox />}
              />

              <Route
                path="/deleteAccount"
                element={
                  <ProtectedRoute isSignedIn={isSignedIn}>
                    <DeleteAccountPage />
                  </ProtectedRoute>
                }
              />
              <Route path="/confirm/:token" element={<VerifyEmailBox />} />

              <Route
                path="/addQuestion"
                element={
                  <ProtectedRoute isSignedIn={isSignedIn}>
                    <AdminRoute role={role}>
                      <AddQuestion />
                    </AdminRoute>
                  </ProtectedRoute>
                }
              />
              <Route
                path="/updateQuestion/:id"
                element={
                  <ProtectedRoute isSignedIn={isSignedIn}>
                    <AdminRoute role={role}>
                      <UpdateQuestion />
                    </AdminRoute>
                  </ProtectedRoute>
                }
              />
              {/*<Route path="/taking" element={<TakingExamInfo />} />*/}
              <Route
                path="/changePassword"
                element={
                  <ProtectedRoute isSignedIn={isSignedIn}>
                    <ChangePasswordPage />
                  </ProtectedRoute>
                }
              />
              <Route
                path="/main"
                element={
                  <ProtectedRoute isSignedIn={isSignedIn}>
                    <AfterLoginPage />
                  </ProtectedRoute>
                }
              />
              <Route
                path="/test"
                element={
                  <ProtectedRoute isSignedIn={isSignedIn}>
                    <TestPage />
                  </ProtectedRoute>
                }
              />
            </Routes>
          </div>
        </div>
      </div>
    </>
  );
}

export default App;
