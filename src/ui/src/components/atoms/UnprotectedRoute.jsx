import { Navigate } from "react-router-dom";

function UnprotectedRoute({ isSignedIn,children }) {
  if(isSignedIn) return <Navigate to="/main" replace />
  return children;
}

export default UnprotectedRoute;
