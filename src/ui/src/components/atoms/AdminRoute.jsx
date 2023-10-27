import { Navigate } from "react-router";

function AdminRoute( {role, children}) {
    if(role!=='ADMIN'){
        return <Navigate to="/main" />
    }
    return children;
} export default AdminRoute;