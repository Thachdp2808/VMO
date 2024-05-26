import { useEffect, useState } from 'react';
import Button from 'react-bootstrap/Button';
import Modal from 'react-bootstrap/Modal';
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import { postUser, getAllDepartment } from '../../services/DeviceService';

const ModalAddUser = (props) => {
    const { show, handleClose } = props;
    const [listDepartment, setListDepartment] = useState([]);
    const [email, setEmail] = useState("");
    const [firstName, setFirstName] = useState("");
    const [lastName, setLastName] = useState("");
    const [phone, setPhone] = useState("");
    const [status, setStatus] = useState("");
    const [role, setRole] = useState("");
    const [password, setPassword] = useState("");
    const [departmentId, setDepartmentId] = useState("1");
    const [isPasswordChanged, setIsPasswordChanged] = useState(false);
    const [errors, setErrors] = useState({});

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

    const validateFields = () => {
        const newErrors = {};
        if (!email) newErrors.email = 'Email is required';
        if (!firstName) newErrors.firstName = 'FirstName is required';
        if (!lastName) newErrors.lastName = 'LastName is required';
        return newErrors;
    };

    const handleAddUser = async () => {
        const newErrors = validateFields();
        if (Object.keys(newErrors).length === 0) {
            try {
                const res = await postUser(email, firstName, lastName, departmentId);
                if (res === 'Email already exists') {
                    newErrors.email = 'Email already exists';
                    setErrors(newErrors);
                    toast.error("Save user failed: Email already exists");
                } else {
                    handleClose();
                    toast.success("Save user success");
                }
            } catch (error) {
                toast.error("Save user failed: " + error.message);
            }
        } else {
            setErrors(newErrors);
            toast.error("Save user failed: Please fill in all required fields");
        }
    }
    console.log(errors.email);
    return (
        <>
            <Modal show={show} onHide={handleClose}>
                <Modal.Header closeButton>
                    <Modal.Title>Add new User</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <div className='body-add-device'>
                    <div className="mb-3">
                            <label className="form-label">Email</label>
                            <input type="text" className={`form-control ${errors.email ? 'is-invalid' : ''}`} value={email}  onChange={(event) => setEmail(event.target.value)} />
                            {errors.email && <div className="invalid-feedback">{errors.email}</div>}
                        </div>
                        <div className="mb-3">
                            <label className="form-label">First Name</label>
                            <input type="text" className={`form-control ${errors.firstName ? 'is-invalid' : ''}`} value={firstName} onChange={(event) => setFirstName(event.target.value)} />
                            {errors.firstName && <div className="invalid-feedback">{errors.firstName}</div>}
                        </div>
                        <div className="mb-3">
                            <label className="form-label">Last Name</label>
                            <input type="text"className={`form-control ${errors.lastName ? 'is-invalid' : ''}`} value={lastName} onChange={(event) => setLastName(event.target.value)} />
                            {errors.lastName && <div className="invalid-feedback">{errors.lastName}</div>}
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
                    <Button variant="primary" onClick={() => handleAddUser()}>
                        Save User
                    </Button>
                </Modal.Footer>
            </Modal>
        </>
    );
}

export default ModalAddUser;