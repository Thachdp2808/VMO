import { useEffect, useState } from 'react';
import Table from 'react-bootstrap/Table';
import { getRequestByCreated, postSendRequest } from '../../services/DeviceService';
import ReactPaginate from 'react-paginate';
import { Button } from 'react-bootstrap';
import { ToastContainer, toast } from 'react-toastify';
import _ from "lodash";
import { debounce } from "lodash";
import { CSVLink, CSVDownload } from "react-csv";
import ModalAddRequest from './ModalAddRequest';
import ModalEditRequest from './ModalEditRequest';
const TableRequests = () => {
    const [listRequest, setListRequest] = useState([]);
    const [totalRequests, setTotalRequests] = useState(0);
    const [totalPages, setTotalPages] = useState(0);
    const [dataRequest, setDataRequest] = useState([]);
    const [isShowModalEditRequest, setIsShowModalEditRequest] = useState(false);
    const [isShowModalAddRequest, setIsShowModalAddRequest] = useState(false);
    const [dataExport, setDataExport] = useState([]);
    const [status, setStatus] = useState([]);

    const handleClose = () => {
        setIsShowModalAddRequest(false);
        setIsShowModalEditRequest(false);
    }

    useEffect(() => {
        getRequest("", 1, 4);
    }, [])
    const getRequest = async (status, pageNo, pageSize) => {
        let res = await getRequestByCreated("", pageNo, pageSize);
        console.log("Check request", res)
        if(res === 'No requests found'){
            setListRequest([]);
        }

        if (res && res.content) {
            setListRequest(res.content)
            setTotalRequests(res.totalElements)
            setTotalPages(res.totalPages);
        }
        
    }

    const handlePageClick = (event) => {
        getRequest("", +event.selected + 1, 4);
    }

    const handleSearch = debounce((event) => {
        const status = event.target.value;
        setStatus(status);

        if (status) {
            // Nếu có giá trị tìm kiếm, gọi getDevice với trang 1
            getRequest(status, 1, 4);
        } else {

            // Nếu giá trị tìm kiếm rỗng, gọi getDevice với keyword hiện tại và trang 1
            getRequest("", 1, 4);
        }
    }, 2000)

    const handleEditRequest = (item) => {
        console.log("event lib: ", item)
        setDataRequest(item);
        setIsShowModalEditRequest(true);

      }
    const handleSend = async (item) => {
        let res = await postSendRequest(item.requestId);
        if (res) {
            toast.success("Send request success")
        } else {
            toast.error("Send request failed")
        }


    }

    //   const getUserExport = (event, done) => {
    //     let result = [];
    //     if (listUser && listUser.length > 0) {
    //       result.push(["ID", "Device Name", "Price", "Status", "Category Name", "Category Type"])
    //       listUser.map((item, index) => {
    //         let arr = [];
    //         arr[0] = item.deviceId
    //         arr[1] = item.deviceName
    //         arr[2] = item.price
    //         arr[3] = item.status
    //         arr[4] = item.category.categoryName
    //         arr[5] = item.category.type
    //         result.push(arr);
    //       })

    //       setDataExport(result);
    //       done();
    //     }
    //   }


    return (<>
        <div className="my-3 add-device">
            <b>List Request</b>
            <div>
                {/* <CSVLink
                    data={dataExport}
                    filename={"users.csv"}
                    className="btn btn-primary"
                    asyncOnClick={true}
                    onClick={(event, done) => getUserExport(event, done)}
                >
                    <i className="fa-solid fa-file-arrow-down"></i> Export
                </CSVLink> */}
            </div>

        </div>
        <Table striped bordered hover>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Created Date</th>
                    <th>Resolve Date</th>
                    <th>User Created</th>
                    <th>User Resolve</th>
                    <th>Reason</th>
                    <th>Status</th>
                    <th>Action</th>
                </tr>
            </thead>
            <tbody>
            {listRequest && listRequest.length > 0 ? (
                    listRequest.map((item, index) => (
                        <tr key={`request-${index}`}>
                            <td>{item.requestId}</td>
                            <td>{item.createdDate}</td>
                            <td>{item.resolveDate}</td>
                            <td>{item.userCreated.firstName + " " + item.userCreated.lastName}</td>
                            <td>{item.userResolve}</td>
                            <td>{item.reason}</td>
                            <td>{item.status}</td>
                            <td>
                                <button className="btn btn-warning mx-3" onClick={() => handleEditRequest(item)}>Detail</button>
                                {item.status === "Pending" && (
                                    <button className="btn btn-primary mx-3" onClick={() => handleSend(item)}>Send</button>
                                )}
                            </td>
                        </tr>
                    ))
                ) : (
                    <tr>
                        <td colSpan="8" className="text-center">Empty</td>
                    </tr>
                )}
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
        <ModalEditRequest
            show={isShowModalEditRequest}
            dataRequest={dataRequest}
            handleClose={handleClose}
        />
    </>)
}
export default TableRequests;