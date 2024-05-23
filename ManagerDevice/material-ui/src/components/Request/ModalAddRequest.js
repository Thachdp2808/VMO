import { useState, useEffect } from 'react';
import Button from 'react-bootstrap/Button';
import Modal from 'react-bootstrap/Modal';
import { postAddRequest, fetchAllDevice } from '../../services/DeviceService';
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import Table from 'react-bootstrap/Table';

const ModalAddRequest = (props) => {
    const { show, handleClose, deviceSave } = props;
    const [listDevice, setListDevice] = useState([]);
    const [reason, setReason] = useState("");
    const [startDate, setStartDate] = useState("");
    const [endDate, setEndDate] = useState("");
    const [errors, setErrors] = useState({});

    console.log("data", deviceSave);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const device = await fetchAllDevice("", 1, 1000);
                if (device && device.content) {
                    setListDevice(device.content);
                }
            } catch (error) {
                console.error("Error fetching categories:", error);
            }
        };

        fetchData();
    }, []); // Empty dependency array ensures this effect runs only once after initial render

    const filteredDevices = listDevice.filter(device => deviceSave.includes(device.deviceId));

    const handleSaveRequest = async () => {
        const newErrors = validateFields();
        if (Object.keys(newErrors).length === 0) {
            let res = await postAddRequest(reason, deviceSave, startDate, endDate);
            handleClose();
            setReason('');
            setStartDate('');
            setEndDate('');
            toast.success("Request save success")
        } else {
            setErrors(newErrors);
            toast.error("Request save failed")
        }
    }

    const validateFields = () => {
        const newErrors = {};
        if (filteredDevices.length === 0 ) newErrors.device = 'Device is required';
        if (!reason) newErrors.reason = 'Reason is required';
        return newErrors;
    };

    return (
        <>
            <Modal show={show} onHide={handleClose}>
                <Modal.Header closeButton>
                    <Modal.Title>Add New Request</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <div className='body-add-device'>
                        <div className="mb-3">
                            <label className="form-label">Reason</label>
                            <input type="text" className={`form-control ${errors.reason ? 'is-invalid' : ''}`} value={reason} onChange={(event) => setReason(event.target.value)} />
                            {errors.reason && <div className="invalid-feedback">{errors.reason}</div>}
                        </div>

                        <div className={`mb-3  ${errors.device ? 'is-invalid form-control' : ''}`}>
                            <Table striped bordered hover >
                                <thead>
                                    <tr>
                                        <td>ID</td>
                                        <td>Device Name</td>
                                    </tr>
                                </thead>

                                <tbody>
                                    {filteredDevices.map(device => (
                                        <tr key={device.deviceId}>
                                            <td>{device.deviceId}</td>
                                            <td>{device.deviceName}</td>
                                        </tr>
                                    ))}
                                </tbody>
                               
                            </Table>
                        </div>
                        {errors.device && <div className="invalid-feedback">{errors.device}</div>}

                        <div className="mb-3">
                            <label className="form-label">Start Date</label>
                            <input type="date" className="form-control" value={startDate} onChange={(event) => setStartDate(event.target.value)} />
                        </div>

                        <div className="mb-3">
                            <label className="form-label">End Date</label>
                            <input type="date" className="form-control" value={endDate} onChange={(event) => setEndDate(event.target.value)} />
                        </div>



                    </div>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={handleClose}>
                        Close
                    </Button>
                    <Button variant="primary" onClick={() => handleSaveRequest()}>
                        Save Request
                    </Button>
                </Modal.Footer>
            </Modal>
        </>
    );
}

export default ModalAddRequest;