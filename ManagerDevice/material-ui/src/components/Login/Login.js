import { useState, useEffect  } from "react";
import { loginAPI,getUser } from "../../services/DeviceService";
import {  toast } from 'react-toastify';
import {useNavigate, Navigate } from "react-router-dom";
import { useContext } from "react";
import { UserContext } from "../../context/UserContext";
function Login({ setTokenExists }) {
    const {loginContext} = useContext(UserContext);
    const navigate = useNavigate();
    const [email, setEmail] = useState("");
    const [User, setUser] = useState("");
    const [password, setPassword] = useState("");
    const [isShowPassword, setIsShowPassword] = useState(false)
    useEffect(() => {
        // Kiểm tra nếu có token trong localStorage
        const token = localStorage.getItem('token');
        if (token) {
            navigate("/device");
        }
    }, []);
    const handleLogin = async () => {
        
        if(!email || !password ){
            toast.error("Email/ Password is required");
            return;
        }
        let res = await loginAPI(email, password);
        
        if(res && res.token){
            loginContext(email, res.token);
            navigate("/device");
            setTokenExists(true);
            
        }
        console.log(res);
    }


    return (
        <div className="login-container col-4 col-sm-4">
            <div className="title">Login</div>
            <div className="text" >Email</div>
            <input type="text" placeholder="Email....." value={email} onChange={(event) => setEmail(event.target.value)} />

            <div className="input-2">
                <input type={isShowPassword? "text": "password"} placeholder="Password....." value={password} onChange={(event) => setPassword(event.target.value)} />
                <i className={isShowPassword === true ? "fa-solid fa-eye":  "fa-solid fa-eye-slash"  } onClick={() => setIsShowPassword(!isShowPassword)}></i>
            </div>

            <a className="forgot" >Forgot Password?</a>
            <button className={email && password ? "active": ""} 
            disabled={email && password ? false : true}
            onClick={() => handleLogin()}>Login</button>
            <div className="back">
                <i className="fa-solid fa-angles-left"></i>Go back
            </div>
        </div>
    );
}

export default Login;