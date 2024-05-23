import { useEffect, useState } from 'react';
import Button from 'react-bootstrap/Button';
import Modal from 'react-bootstrap/Modal';
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import { putUpdateRequest, fetchAllUser } from '../../services/DeviceService';
import Table from 'react-bootstrap/Table';
const ModalEditRequest = (props) => {
    const { show, handleClose, dataRequest } = props;
    const [listUser, setListUser] = useState([]);
    const [requestId, setRequestId] = useState("");
    const [reason, setReason] = useState("");
    const [device, setDevice] = useState([]);
    const [start, setStart] = useState("");
    const [end, setEnd] = useState("");
    const [resolveDate, setResolveDate] = useState("");
    const [actualEndTime, setActualEndTime] = useState("");


    useEffect(() => {
        const fetchData = async () => {
            try {
                const user = await fetchAllUser("", 1, 1000);
                setListUser(user);
            } catch (error) {
                console.error("Error fetching categories:", error);
            }
        };

        fetchData();
    }, []); // Empty dependency array ensures this effect runs only once after initial render


    useEffect(() => {
        if (show) {
            setRequestId(dataRequest.requestId)
            setReason(dataRequest.reason)
            setResolveDate(dataRequest.resolveDate)
            if (dataRequest.requestDetails.length > 0) {
                setStart(dataRequest.requestDetails[0].startTime)
                setEnd(dataRequest.requestDetails[0].endTime);
            }
            setActualEndTime(dataRequest.actualEndTime);
            const devices = dataRequest.requestDetails.map(detail => detail.device);
            setDevice(devices);

        }

    }, [dataRequest])

    const handleDeleteDevice = async (deviceId) => {
        setDevice(prevDevices => prevDevices.filter(d => d.deviceId !== deviceId));
        toast.success("Device delete success")

    }

    const handleEditRequest = async (requestId) => {
        const deviceIds = device.map(d => d.deviceId);
        console.log("res", requestId)
        console.log("deviceIds", deviceIds) // Chuyển đổi danh sách thiết bị thành danh sách ID
        try {
            const res = await putUpdateRequest(requestId, reason, deviceIds, start, end);
            // Xử lý kết quả sau khi cập nhật thành công, ví dụ: hiển thị thông báo, làm mới dữ liệu, vv.
            toast.success("Request updated successfully");
            handleClose();
        } catch (error) {
            // Xử lý lỗi nếu có
            toast.error("Failed to update request");
        }
    }
    return (
        <>
            <Modal show={show} onHide={handleClose}>
                <Modal.Header closeButton>
                    <Modal.Title>Detail a request</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <div className='body-add-device'>
                        <div className="mb-3">
                            <label className="form-label">Reason</label>
                            <input type="text" className="form-control" value={reason} onChange={(event) => setReason(event.target.value)} />
                        </div>
                        <div className="mb-3">
                            <Table striped bordered hover>
                                <thead>
                                    <tr>
                                        <td>ID</td>
                                        <td>Device Name</td>
                                        {dataRequest.status === 'Pending' && (<td>Action</td>)}


                                    </tr>
                                </thead>

                                <tbody>
                                    {device.map(device => (
                                        <tr key={device.deviceId}>
                                            <td>{device.deviceId}</td>
                                            <td>{device.deviceName}</td>
                                            {dataRequest.status === 'Pending' && (<td><Button variant="danger" onClick={() => handleDeleteDevice(device.deviceId)}>
                                                Delete
                                            </Button></td>)}
                                        </tr>
                                    ))}
                                </tbody>
                            </Table>
                        </div>
                        <div className="mb-3">
                            <label className="form-label">Start Date</label>
                            <input type="date" className="form-control" value={start} onChange={(event) => setStart(event.target.value)} />
                        </div>
                        <div className="mb-3">
                            <label className="form-label">End Date</label>
                            <input type="date" className="form-control" value={end} onChange={(event) => setEnd(event.target.value)} />
                        </div>

                        {actualEndTime != null && (
                            <div className="mb-3">
                                <label className="form-label">Actual End Date</label>
                                <input type="date" className="form-control" defaultValue={actualEndTime} />
                            </div>
                        )}

                    </div>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={handleClose}>
                        Close
                    </Button>
                    {dataRequest.status === 'Pending' && (<Button variant="primary" onClick={() => handleEditRequest(requestId)}>
                        Save Changes
                    </Button>)}

                </Modal.Footer>
            </Modal>
        </>
    );
}

export default ModalEditRequest;