import { Routes, Route } from "react-router-dom";
import TableDevices from '../components//Device/TableDevice';
import TableUser from '../components/User/TableUser';
import Login from '../components/Login/Login';
import PrivateRoute from "./PrivateRoutes";
const AppRoutes = () => {
    return (<>
        <Routes>
            <Route path="/device"  element={<TableDevices />}></Route>
            
            
            <Route path="/login" element={<Login />}>  </Route>
         </Routes>
         <PrivateRoute path="/user">
                <TableUser/>
            </PrivateRoute>
    </>)
}

export default AppRoutes;