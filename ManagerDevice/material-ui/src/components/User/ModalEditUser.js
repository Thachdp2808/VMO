import { useEffect, useState } from 'react';
import Button from 'react-bootstrap/Button';
import Modal from 'react-bootstrap/Modal';
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import { putSaveUser, getAllDepartment } from '../../services/DeviceService';

const ModalEditUser = (props) => {
    const { show, handleClose, dataUser } = props;
    const [listDepartment, setListDepartment] = useState([]);
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
        const fetchData = async () => {
            try {
                const departments = await getAllDepartment();
                setListDepartment(departments);
            } catch (error) {
                console.error("Error fetching categories:", error);
            }
        };

        fetchData();
    }, []); // Empty dependency array ensures this effect runs only once after initial render


    useEffect(() => {
        if (show) {
            setUserId(dataUser.userId)
            setFirstName(dataUser.firstName)
            setLastName(dataUser.lastName)
            setPhone(dataUser.phone)
            setRole(dataUser.role)
            setStatus(dataUser.status)
            setPassword(dataUser.password)
            setDepartmentId(dataUser.department.departmentId)
        }

    }, [dataUser])

    const handlePasswordChange = (event) => {
        setPassword(event.target.value);
        setIsPasswordChanged(true);
    };
    const handleEditUser = async (userId) => {
        // Gọi hàm lưu hoặc gửi finalPassword đến API của bạn.
        const finalPassword = isPasswordChanged ? password : null;
        let res = await putSaveUser(userId, firstName, lastName, phone, status, role, finalPassword, departmentId);
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
                    <Modal.Title>Edit a User</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <div className='body-add-device'>
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
                            <select className="form-select" aria-label="Default select example" onChange={(event) => setStatus(event.target.value)} defaultValue={dataUser.status}>
                                <option value="Active" >Active</option>
                                <option value="Deactive" >Deactive</option>
                            </select>
                        </div>

                        <div className="mb-3">
                            <label className="form-label">Role</label>
                            <select className="form-select" aria-label="Default select example" onChange={(event) => setRole(event.target.value)} defaultValue={dataUser.role}>
                                <option value="ADMIN" >ADMIN</option>
                                <option value="USER" >USER</option>
                            </select>
                        </div>

                        <div className="mb-3">
                            <label className="form-label">Department</label>
                            <select className="form-select" aria-label="Default select example" onChange={(event) => setDepartmentId(event.target.value)} value={departmentId}>
                                {listDepartment && listDepartment.length > 0 &&
                                    listDepartment.map((item, index) => {
                                        return (
                                            <option key={index} value={item.departmentId}>{item.departmentName}</option>
                                        )
                                    })}
                            </select>
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

export default ModalEditUser;