import { useEffect, useState } from 'react';
import Button from 'react-bootstrap/Button';
import Modal from 'react-bootstrap/Modal';
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import { putUser, getUser } from '../../services/DeviceService';

const ModalUpdateProfile = (props) => {
    const { show, handleClose } = props;
    const [user, setUser] = useState([]);
    const [userId, setUserId] = useState("");
    const [firstName, setFirstName] = useState("");
    const [lastName, setLastName] = useState("");
    const [phone, setPhone] = useState("");
    const [status, setStatus] = useState("");
    const [role, setRole] = useState("");
    const [password, setPassword] = useState("");
    const [departmentId, setDepartmentId] = useState("");
    const [isPasswordChanged, setIsPasswordChanged] = useState(false);

    useEffect(() => {
        if (show) {
            const fetchData = async () => {
                try {
                    const users = await getUser();
                    setUser(users);
                } catch (error) {
                    console.error("Error fetching user data:", error);
                }
            };
            fetchData();
        }
    }, [show]);

    console.log("User", departmentId);

    useEffect(() => {
        if (show) {
            setUserId(user.userId)
            setFirstName(user.firstName)
            setLastName(user.lastName)
            setPhone(user.phone)
            setRole(user.role)
            setStatus(user.status)
            setPassword(user.password)
            setDepartmentId(user.department.departmentId)
        }

    }, [user])

    const handlePasswordChange = (event) => {
        setPassword(event.target.value);
        setIsPasswordChanged(true);
    };
    const handleEditUser = async (userId) => {
        // Gọi hàm lưu hoặc gửi finalPassword đến API của bạn.
        const finalPassword = isPasswordChanged ? password : null;
        let res = await putUser(firstName, lastName, phone, status, role, finalPassword, departmentId);
        console.log(password)
        if (res) {
            handleClose();
            toast.success("User update success")
        } else {
            toast.error("User update failed")
        }
    }
    return (
        <>
            <Modal show={show} onHide={handleClose}>
                <Modal.Header closeButton>
                    <Modal.Title>Edit profile</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <div className='body-add-device'>
                        <div className="mb-3">
                            <label className="form-label">First Name</label>
                            <img src="https://tdevice-manager.s3.ap-southeast-2.amazonaws.com/Ava-CV.png" alt="Description of image" />
                        </div>

                        <div className="mb-3">
                            <label className="form-label">First Name</label>
                            <input type="text" className="form-control" value={firstName} onChange={(event) => setFirstName(event.target.value)} />
                        </div>
                        <div className="mb-3">
                            <label className="form-label">Last Name</label>
                            <input type="text" className="form-control" value={lastName} onChange={(event) => setLastName(event.target.value)} />
                        </div>

                        <div className="mb-3">
                            <label className="form-label">Phone</label>
                            <input type="number" className="form-control" value={phone} onChange={(event) => setPhone(event.target.value)} />
                        </div>

                        <div className="mb-3">
                            <label className="form-label">Password</label>
                            <input type="text" className="form-control" onChange={handlePasswordChange} />
                        </div>

                        <div className="mb-3">
                            <label className="form-label">Status</label>
                            <input type="text" readOnly className="form-control" defaultValue={status} />
                        </div>

                        <div className="mb-3">
                            <label className="form-label">Role</label>
                            <input type="text" readOnly className="form-control" defaultValue={role} />
                        </div>

                        <div className="mb-3">
                            <label className="form-label">Department</label>
                            <input type="text" readOnly className="form-control" defaultValue={departmentId} />
                        </div>

                    </div>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={handleClose}>
                        Close
                    </Button>
                    <Button variant="primary" onClick={() => handleEditUser(userId)}>
                        Save Changes
                    </Button>
                </Modal.Footer>
            </Modal>
        </>
    );
}

export default ModalUpdateProfile;