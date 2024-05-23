import Container from 'react-bootstrap/Container';
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';
import NavDropdown from 'react-bootstrap/NavDropdown';
import { toast } from 'react-toastify';
import { useLocation, NavLink, useNavigate } from 'react-router-dom';
import { useContext, useEffect, useState } from "react";
import { UserContext } from '../context/UserContext';
import ModalUpdateProfile from './User/ModalUpdateProfile';

const Header = (props) => {
  const { logout, user } = useContext(UserContext);
  const [hideHeader, setHideHeader] = useState(false);
  const [isShowModalProfile, setIsShowModaProfile] = useState(false);

  const handleClose = () => {
    setIsShowModaProfile(false);
}

  const navigate = useNavigate();
  const handleLogout = () => {
    logout();
    navigate("/login");
    toast.success("Logout success!");
  }

  const handleProfile = () => {
    setIsShowModaProfile(true);
  }

  useEffect(() => {
    if (window.location.pathname === '/login') {
      setHideHeader(true);
    }
  }, [])
  return (<>
    <Navbar expand="lg" className="bg-body-tertiary" style={{ backgroundColor: 'beige' }}>
      <Container>
        <Navbar.Brand href="/">Device Manager</Navbar.Brand>
        <Navbar.Toggle aria-controls="basic-navbar-nav" />
        <Navbar.Collapse id="basic-navbar-nav" >
          {user && user.auth &&
            <>
              <Nav className="me-auto">
                {user && user.email && user.role === "ADMIN" && <NavLink to="/user" className='nav-link' >User</NavLink>}

                <NavLink to="/device" className='nav-link' >Device</NavLink>
                <NavLink to="/request" className='nav-link' >Request</NavLink>

                {user && user.email && user.role === "ADMIN" && <NavLink to="/checkRequest" className='nav-link' >Check Request</NavLink>}
              </Nav>
              <Nav>
                {user && user.email && <span className='nav-link'>Welcome {user.email} </span>}
                
                  {user && user.auth ? (
                    <NavDropdown title="Setting" id="basic-nav-dropdown">
                      <NavDropdown.Item onClick={() => handleProfile()}>Profile</NavDropdown.Item>
                      <NavDropdown.Item onClick={() => handleLogout()}>Logout</NavDropdown.Item>
                    </NavDropdown>
                  ) : (
                    <NavLink to="/login" className='dropdown-item' >Login</NavLink>
                  )}



              </Nav>
            </>}

        </Navbar.Collapse>
      </Container>
    </Navbar>
    <ModalUpdateProfile
            show={isShowModalProfile}
            handleClose={handleClose}
        /> 
  </>)
}

export default Header;