
import './App.scss';
import Header from './components/Header';

import Container from 'react-bootstrap/Container';
import TableDevices from './components/Device/TableDevice';
import TableUser from './components/User/TableUser';
import Login from './components/Login/Login';
import TableRequests from './components/Request/TableRequest';
import TableCheckRequests from './components/Request/TableCheckRequest';
import ForgotPassword from './components/Login/ForgotPassword';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import { useContext, useEffect, useState } from "react";
import { UserContext } from './context/UserContext';
import { useNavigate, Navigate } from "react-router-dom";
import AppRoutes from "./routes/AppRoutes";
function App() {
  const { user, loginContext } = useContext(UserContext);
  console.log(user);
  console.log(user.role);
  useEffect(() => {
    if (localStorage.getItem("token")) {
      loginContext(localStorage.getItem("email"), localStorage.getItem("token"))
    }
  }, [])
  // Middleware để kiểm tra trạng thái đăng nhập và vai trò trước khi truy cập vào route
  const [tokenExists, setTokenExists] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    const token = localStorage.getItem('token');
    if (token) {
      setTokenExists(true);
      // Assuming you have a way to fetch user details using the token
      // setUser(fetchUserDetails(token));
    } else {
      setTokenExists(false);
      navigate('/login'); // Redirect to login if no token
    }
  }, [navigate]);

  console.log(tokenExists);

  return (
    <>
      <div className='app-container'>

        <Header />
        <Container>

          <Routes>
            <Route path="/request" element={tokenExists ? <TableRequests /> : <Login />} />
            <Route path="/checkRequest" element={tokenExists && user.role == 'ADMIN' ? <TableCheckRequests /> : <Login />} />
            <Route path="/device" element={tokenExists ? <TableDevices /> : <Login />} />
            {/* <Route path="/device" element={tokenExists?  <Login setTokenExists={setTokenExists} />:<TableUser />} /> */}
            <Route path="/user" element={tokenExists && user.role == 'ADMIN' ? <TableUser /> : <Login />}></Route>

            <Route path="/login" element={<Login setTokenExists={setTokenExists} />} />
            <Route path="/forgotPassword" element={<ForgotPassword setTokenExists={setTokenExists} />}>  </Route>

          </Routes>
        </Container>
      </div>

      <ToastContainer
        position="top-right"
        autoClose={5000}
        hideProgressBar={false}
        newestOnTop={false}
        closeOnClick
        rtl={false}
        pauseOnFocusLoss
        draggable
        pauseOnHover

      />
    </>
  );
}


export default App;
