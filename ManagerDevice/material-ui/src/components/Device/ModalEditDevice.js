import { useEffect, useState } from 'react';
import Button from 'react-bootstrap/Button';
import Modal from 'react-bootstrap/Modal';
import { postSaveDevice } from '../../services/DeviceService';
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import { putUpdateDevice, getAllCategory } from '../../services/DeviceService';

const ModalEditDevice = (props) => {
    const { show, handleClose, dataDevice } = props;
    const [listCategory, setListCategory] = useState([]);
    const [deviceId, setDeviceId] = useState("");
    const [deviceName, setDeviceName] = useState("");
    const [price, setPrice] = useState("");
    const [description, setDescription] = useState("");
    const [status, setStatus] = useState("");
    const [category, setCategory] = useState("");
    useEffect(() => {
        const fetchData = async () => {
            try {
                const categories = await getAllCategory();
                setListCategory(categories);
            } catch (error) {
                console.error("Error fetching categories:", error);
            }
        };

        fetchData();
    }, []); // Empty dependency array ensures this effect runs only once after initial render


    useEffect(() => {
        if (show) {
            setDeviceId(dataDevice.deviceId)
            setDeviceName(dataDevice.deviceName)
            setPrice(dataDevice.price)
            setDescription(dataDevice.description)
            setStatus(dataDevice.status)
            setCategory(dataDevice.category.categoryId)
        }

    }, [dataDevice])
    const handleEditDevice = async (deviceId) => {
        let res = await putUpdateDevice(deviceId, deviceName, price, description, category, status);
        if (res) {
            handleClose();
            toast.success("Device update success")
        } else {
            toast.error("Device update failed")
        }
    }
    return (
        <>
            <Modal show={show} onHide={handleClose}>
                <Modal.Header closeButton>
                    <Modal.Title>Edit a device</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <div className='body-add-device'>
                        <div className="mb-3">
                            <label className="form-label">Device Name</label>
                            <input type="text" className="form-control" value={deviceName} onChange={(event) => setDeviceName(event.target.value)} />
                        </div>

                        <div className="mb-3">
                            <label className="form-label">Price</label>
                            <input type="number" className="form-control" value={price} onChange={(event) => setPrice(event.target.value)} />
                        </div>

                        <div className="mb-3">
                            <label className="form-label">Description</label>
                            <input type="text" className="form-control" value={description} onChange={(event) => setDescription(event.target.value)} />
                        </div>

                        <div className="mb-3">
                            <label className="form-label">Status</label>
                            <select className="form-select" aria-label="Default select example" onChange={(event) => setStatus(event.target.value)} defaultValue={dataDevice.status}>
                                <option value="Availability" >Availability</option>
                                <option value="Utilized" >Utilized</option>
                                <option value="Maintenance">Maintenance</option>
                            </select>
                        </div>
                    

                        <div className="mb-3">
                            <label className="form-label">Category</label>
                            <select className="form-select" aria-label="Default select example" onChange={(event) => setCategory(event.target.value)} defaultValue={category}>
                                {listCategory && listCategory.length > 0 &&
                                listCategory.map((item, index) => {
                                    return (
                                        <option key={index} value={item.categoryId}>{item.categoryName}</option>
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
                    <Button variant="primary" onClick={() => handleEditDevice(deviceId)}>
                        Save Changes
                    </Button>
                </Modal.Footer>
            </Modal>
        </>
    );
}

export default ModalEditDevice;