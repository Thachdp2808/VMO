import { useContext } from "react";
import { UserContext } from "../context/UserContext";
import { Routes,Route, Navigate, useNavigate } from "react-router-dom";

const PrivateRoute = (props) => {
    const {user, loginContext} = useContext(UserContext);
    const navigate = useNavigate();
    const token = localStorage.getItem("token");
    // if(token){
    //     if(user && !user.auth){
    //         return <>
    //             You don't have permission;
    //         </>
    //     }
    //     return (<>
    //         <Routes>
    //             <Route path = {props.path} element={props.children}/>
    //         </Routes>
        
    //     </>)
    // }else{
    //     return <Navigate to="/login" />
    // }
   if(user.auth===true){
    navigate("/login");
   }
    if(user  && user.role == 'USER'){
        return <>
            You don't have permission;
        </>
    }
    return (<>
        <Routes>
            
            <Route path = {props.path} element={props.children}/>
        </Routes>
    
    </>)
   
}

export default PrivateRoute