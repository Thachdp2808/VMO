import { useState, useEffect  } from 'react';
import Button from 'react-bootstrap/Button';
import Modal from 'react-bootstrap/Modal';
import { postSaveDevice,getAllCategory } from '../../services/DeviceService';
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

const ModalAddDevice = (props) => {
    const { show, handleClose } = props;
    const [listCategory, setListCategory] = useState([]);
    const [deviceName, setDeviceName] = useState("");
    const [price, setPrice] = useState("");
    const [description, setDescription] = useState("");
    const [category, setCategory] = useState("1");
    const [errors, setErrors] = useState({});
    
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

    const validateFields = () => {
        const newErrors = {};
        if (!deviceName) newErrors.deviceName = 'Device Name is required';
        if (!price) newErrors.price = 'Price is required';
        return newErrors;
    };

    const handleSaveDevice = async () => {
        const newErrors = validateFields();
        if (Object.keys(newErrors).length === 0) {
            let res = await postSaveDevice(deviceName, price, description, category);
            console.log(">>> category ", category)
            if(res){
                handleClose();
                setDeviceName('');
                setPrice('');
                setDescription('');
                toast.success("Device save success")
            }
        }else {
            toast.error("Device save failed")
            setErrors(newErrors);
        }
    }
    return (
        <>
            <Modal show={show} onHide={handleClose}>
                <Modal.Header closeButton>
                    <Modal.Title>Add new device</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <div className='body-add-device'>
                        <div className="mb-3">
                            <label className="form-label">Device Name</label>
                            <input required type="text" className={`form-control ${errors.deviceName ? 'is-invalid' : ''}`} value={deviceName} onChange={(event) => setDeviceName(event.target.value)}/>
                            {errors.deviceName && <div className="invalid-feedback">{errors.deviceName}</div>}
                        </div>

                        <div className="mb-3">
                            <label className="form-label">Price</label>
                            <input required type="number" className={`form-control ${errors.price ? 'is-invalid' : ''}`} value={price} onChange={(event) => setPrice(event.target.value)}/>
                            {errors.price && <div className="invalid-feedback">{errors.price}</div>}
                        </div>

                        <div className="mb-3">
                            <label className="form-label">Description</label>
                            <input type="text" className="form-control" value={description} onChange={(event) => setDescription(event.target.value)}/>
                        </div>

                        <div className="mb-3">
                            <label className="form-label">Status</label>
                            <input className="form-control" disabled defaultValue={'Availability'}/>
                        </div>

                        <div className="mb-3">
                            <label className="form-label">Category</label>
                            <select className="form-select" aria-label="Default select example" onChange={(event) => setCategory(event.target.value)}>
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
                    <Button variant="primary" onClick={() => handleSaveDevice()}>
                        Save Changes
                    </Button>
                </Modal.Footer>
            </Modal>
        </>
    );
}

export default ModalAddDevice;