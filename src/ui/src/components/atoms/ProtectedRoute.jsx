import { Route, Navigate } from "react-router-dom";
import LoginBox from "../organisms/LoginBox";

function ProtectedRoute({ isSignedIn,children }) {
  if(!isSignedIn) return <Navigate to="/login" replace />
  return children;
}

export default ProtectedRoute;
