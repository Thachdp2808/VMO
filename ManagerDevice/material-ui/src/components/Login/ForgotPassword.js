import { useState, useEffect  } from "react";
import { loginAPI,putResetPassword,putVerify } from "../../services/DeviceService";
import {  toast } from 'react-toastify';
import {useNavigate, Navigate } from "react-router-dom";
import { useContext } from "react";
import { UserContext } from "../../context/UserContext";
function ForgotPassword({ }) {
    const navigate = useNavigate();
    const [otp, setOtp] = useState("");
    const [email, setEmail] = useState("");
    const [isShowOtp, setIsShowOtp ] = useState(false);
    const [isShowButtonOtp, setIsShowButtonOtp] = useState(false);
    const [isButtonDisabled, setIsButtonDisabled] = useState(false);

    useEffect(() => {
        // Kiểm tra nếu có token trong localStorage
        const token = localStorage.getItem('token');
        if (token) {
            navigate("/device");
        }
    }, []);

    const handleSignupClick = () => {
        navigate("/login");
    };

    const handleSendOTP = async () => {
        let res = await putVerify(email);
        
        if(res === 'Email sent... please check account within 1 minute'){
            setIsShowOtp(true);
            toast.success("Email sent... please check account within 1 minute")
        }else{
            toast.error("Not found with this email");
        }
    }

    const handleRegenerateOtp = async () => {
        let res = await putVerify(email);
        if(res === 'Email sent... please check account within 1 minute'){
            toast.success("OTP sent... please check account within 1 minute")
            setIsButtonDisabled(true);
            // Disable the button for 30 seconds
            setTimeout(() => {
                setIsButtonDisabled(false);
            }, 10000);
        }else{
            toast.error("Not found with this email");
        }
    }

    const handleResetPassword = async (email, otp) => {
        let res = await putResetPassword(email, otp);
        if(res === 'OTP verified password can reset'){
            toast.success("New password send your email please check account")
            navigate("/login");
        }
        if(res === 'Please regenerate otp and try again'){
            setIsShowButtonOtp(true);
            
            toast.error("Please regenerate otp and try again")
        }
        if(res === 'Unable to send password please try again'){
            toast.error("Unable to send password please try again")
        }
    }

    return (
        <div className="login-container col-4 col-sm-4">
            <div className="title">Forgot Password</div>
            <div className="text" >Email</div>
            <input type="text" placeholder="Email....." value={email} onChange={(event) => setEmail(event.target.value)} />
            {isShowOtp && (
                <div className="otp-container">
                    <input 
                        type="text" 
                        placeholder="OTP..." 
                        value={otp} 
                        onChange={(event) => setOtp(event.target.value)} 
                    />
                    {isShowButtonOtp && (
                        <button style={{ width: '32%',marginBottom: '8px' }} onClick={handleRegenerateOtp} disabled={isButtonDisabled}>
                            Resend OTP
                        </button>
                    )}
                </div>
            )}
            {!isShowOtp ? (
                <button
                    className={email ? "active" : ""}
                    disabled={!email}
                    onClick={handleSendOTP}
                >
                    Send Email
                </button>
            ) : (
                <button
                    className={email && otp ? "active" : ""}
                    disabled={!(email && otp)}
                    onClick={() => handleResetPassword(email,otp)}
                >
                    Reset Password
                </button>
            )}
            <div className="back"  onClick={handleSignupClick}>
            <i className="fa-solid fa-angles-left"></i> Login 
            </div>
        </div>
    );
}

export default ForgotPassword;