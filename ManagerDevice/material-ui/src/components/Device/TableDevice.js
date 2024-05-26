
import { useEffect, useState } from 'react';
import Table from 'react-bootstrap/Table';
import { fetchAllDevice, getAllCategory, getDashboard, getUser } from '../../services/DeviceService';
import ReactPaginate from 'react-paginate';
import { Button } from 'react-bootstrap';
import ModalAddDevice from './ModalAddDevice';
import ModalEditDevice from './ModalEditDevice';
import _ from "lodash";
import { debounce } from "lodash";
import { CSVLink, CSVDownload } from "react-csv";
import axios from '../../services/custom-axios';
import ModalAddRequest from '../Request/ModalAddRequest';

const TableDevices = (props) => {
  const [listDevice, setListDevice] = useState([]);
  const [totalDevices, setTotalDevices] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [dataDevice, setDataDevice] = useState([]);
  const [dataDeviceToRequest, setDataDeviceToRequest] = useState([]);
  const [type, setType] = useState([]);
  const [category, setCategory] = useState([]);
  const [status, setStatus] = useState([]);
  const [listCategory, setListCategory] = useState([]);
  const [deviceSave, setDeviceSave] = useState([]);
  const [user, setUser] = useState([]);

  const [isShowModalEditDevice, setIsShowModalEditDevice] = useState(false);
  const [isShowModalAddDevice, setIsShowModalAddDevice] = useState(false);
  const [isShowModalAddRequest, setIsShowModalAddRequest] = useState(false);

  const [keyword, setKeyword] = useState("")
  const [dataExport, setDataExport] = useState([]);


  const handleClose = () => {
    setIsShowModalAddRequest(false)
    setIsShowModalAddDevice(false);
    setIsShowModalEditDevice(false);
  }

  useEffect(() => {
    const fetchData = async () => {
      try {
        const users = await getUser();
        setUser(users);
        const categories = await getAllCategory();
        setListCategory(categories);
      } catch (error) {
        console.error("Error fetching categories:", error);
      }
    };

    fetchData();
  }, []); // Empty dependency array ensures this effect runs only once after initial render

  useEffect(() => {
    getDevice(keyword, 1, 4, type, category, status);
  }, [])

  const getDevice = async (keyword, pageNo, pageSize, type, category, status) => {
    let res = await fetchAllDevice(keyword, pageNo, pageSize, type, category, status);
    console.log(res);
    if (res && res.content) {
      setListDevice(res.content)
      setTotalDevices(res.totalElements)
      setTotalPages(res.totalPages);
    }
  }

  const handlePageClick = (event) => {
    getDevice(keyword, +event.selected + 1, 4, type, category, status);
  }

  const handleSearchButtonClick = () => {
    getDevice(keyword, 1, 4, type, category, status);
  }

  const handleSearchInputChange = (event) => {
    const searchValue = event.target.value;
    setKeyword(searchValue);
  }

  const handleTypeChange = (event) => {
    const selectedOptions = Array.from(event.target.selectedOptions, option => option.value);
    setType(selectedOptions);
  }

  const handleCategoryChange = (event) => {
    const selectedOptions = Array.from(event.target.selectedOptions, option => option.value);
    setCategory(selectedOptions);
  }

  const handleStatusChange = (event) => {
    const selectedOptions = Array.from(event.target.selectedOptions, option => option.value);
    setStatus(selectedOptions);
  }

  const handleEditDevice = (item) => {
    console.log("event lib: ", item)
    setDataDevice(item);
    setIsShowModalEditDevice(true);

  }



  useEffect(() => {
    const deviceSave = dataDeviceToRequest.map(device => device.deviceId);
    setDeviceSave(deviceSave);
  }, [dataDeviceToRequest]);

  const handleSaveRequest = (device) => {
    setDataDeviceToRequest(prevDevices => {
      return [...prevDevices, device];
    });

  }

  const uniqueTypes = Array.isArray(listCategory)
  ? [...new Set(listCategory.map(item => item.type))]
  : [];
  const uniqueCategories = Array.isArray(listCategory)
        ? [...new Set(listCategory.map(item => item.categoryName))]
        : [];
  function formatPrice(price) {
    // Chuyển đổi giá thành chuỗi và thêm dấu phẩy phân cách hàng nghìn
    const formattedPrice = price.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' });
    return formattedPrice;
  }

  const handleRedirect = async () => {
    try {
      const res = await getDashboard();
      console.log(res);
      if (res) {
        // Assuming the URL is in res.data.url
        window.location.href = res;
      }
    } catch (error) {
      console.error("Error fetching dashboard link:", error);
    }
  };

  return (<>
    <div className="my-3 add-device">
      <b>List Device</b>
      <div>
        <button className="btn btn-primary" onClick={() => handleRedirect()} >
          <i className="fa-solid fa-file-arrow-down"></i> Export
        </button>
        <button style={{ marginLeft: '10px' }} className="btn btn-success" onClick={() => setIsShowModalAddDevice(true)}>
          <i className="fa-solid fa-circle-plus"></i> Add Device
        </button>
        <button style={{ marginLeft: '10px' }} className="btn btn-secondary" onClick={() => setIsShowModalAddRequest(true)}>
          <i className="fa-solid fa-circle-plus"></i> Add Request
        </button>
      </div>

    </div>
    <div className='col-6 my-6 search-container'>
      <input className='form-control' placeholder='Search by name.....'
        onChange={handleSearchInputChange} />

      <select onChange={handleTypeChange} className='select-category'>
        <option value="">TYPE</option>
        {uniqueTypes.map((type, index) => (
          <option key={index} value={type}>{type}</option>
        ))}
      </select>

      <select onChange={handleCategoryChange} className='select-category'>
        <option value="">CATEGORY</option>
        {uniqueCategories.map((category, index) => (
          <option key={index} value={category}>{category}</option>
        ))}
      </select>

      <select onChange={handleStatusChange} className='select-category'>
        <option value="">STATUS</option>
        <option value="Availability">Availability</option>
        <option value="Utilized">Utilized</option>
        <option value="Maintenance">Maintenance</option>
      </select>

      <button style={{ marginLeft: '10px' }} className="btn btn-success" onClick={handleSearchButtonClick}> Search
      </button>
    </div>

    <Table striped bordered hover>
      <thead>
        <tr>
          <th>ID</th>
          <th>Device Name</th>
          <th>Price</th>
          <th>Status</th>
          <th>Category</th>
          <th>Type</th>
          <th>Action</th>
        </tr>
      </thead>
      <tbody>
        {listDevice && listDevice.length > 0 &&

          listDevice.map((item, index) => {
            return (
              <tr key={`devices-${index}`}>
                <td>{item.deviceId}</td>
                <td>{item.deviceName}</td>
                <td>{formatPrice(item.price)}</td>
                <td>{item.status}</td>
                <td>{item.category.categoryName}</td>
                <td>{item.category.type}</td>
                <td>
                  <button className="btn btn-warning mx-3" onClick={() => handleEditDevice(item)}>Edit</button>
                  {item.status === "Availability" && !deviceSave.includes(item.deviceId) && (
                    <button className="btn btn-outline-primary mx-1 btn-fit-content" onClick={() => handleSaveRequest(item)}>
                      <i className="fa-solid fa-circle-plus"></i>
                    </button>
                  )}
                  {deviceSave.includes(item.deviceId) && (
                    <button className="btn btn-primary mx-1 btn-fit-content">
                      <i className="fa-solid fa-check"></i>
                    </button>
                  )}

                </td>
              </tr>
            )
          })}
      </tbody>
    </Table>
    <ReactPaginate
      breakLabel="..."
      nextLabel="next >"
      onPageChange={handlePageClick}
      pageRangeDisplayed={5}
      pageCount={totalPages}
      previousLabel="< previous"

      pageClassName="page-item"
      pageLinkClassName="page-link"
      previousClassName="page-item"
      previousLinkClassName="page-link"
      nextClassName="page-item"
      nextLinkClassName="page-link"
      breakClassName="page-item"
      breakLinkClassName="page-link"
      containerClassName="pagination"
      activeClassName="active"
    />

    <ModalAddDevice
      show={isShowModalAddDevice}
      handleClose={handleClose}
    />
    <ModalEditDevice
      show={isShowModalEditDevice}
      dataDevice={dataDevice}
      handleClose={handleClose}
    />
    <ModalAddRequest
      show={isShowModalAddRequest}
      deviceSave={deviceSave}
      handleClose={handleClose}
    />
  </>)
}

export default TableDevices;