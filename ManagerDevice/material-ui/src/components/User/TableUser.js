import { useEffect, useState } from 'react';
import Table from 'react-bootstrap/Table';
import { fetchAllUser} from '../../services/DeviceService';
import ReactPaginate from 'react-paginate';
import { Button } from 'react-bootstrap';
import ModalAddUser from './ModalAddUser';
import ModalEditUser from './ModalEditUser';
import _ from "lodash";
import { debounce } from "lodash";
import { CSVLink, CSVDownload } from "react-csv";
const TableUser = () => {
    const [listUser, setListUser] = useState([]);
    const [totalUsers, setTotalUsers] = useState(0);
    const [totalPages, setTotalPages] = useState(0);
    const [dataUser, setDataUser] = useState([]);
    const [isShowModalEditUser, setIsShowModalEditUser] = useState(false);
    const [isShowModalAddUser, setIsShowModalAddUser] = useState(false);
    const [keyword, setKeyword] = useState("")
    const [dataExport, setDataExport] = useState([]);

    const handleClose = () => {
        setIsShowModalAddUser(false);
        setIsShowModalEditUser(false);
    }

    useEffect(() => {

        getUser(keyword, 1, 3);
    }, [])

    const getUser = async (keyword, pageNo, pageSize) => {
        let res = await fetchAllUser(keyword, pageNo, pageSize);
        console.log("Check user",res)
        
        if (res && res.content) {
            setListUser(res.content)
            setTotalUsers(res.totalElements)
            setTotalPages(res.totalPages);
        }
    }

    const handlePageClick = (event) => {
        getUser(keyword, +event.selected + 1, 3);
    }

    const handleSearch = debounce((event) => {
        const searchValue = event.target.value;
        setKeyword(searchValue);

        if (searchValue) {
            // Nếu có giá trị tìm kiếm, gọi getDevice với trang 1
            getUser(searchValue, 1, 3);
        } else {

            // Nếu giá trị tìm kiếm rỗng, gọi getDevice với keyword hiện tại và trang 1
            getUser("", 1, 3);
        }
    }, 2000)

    const handleEditUser = (item) => {
        console.log("event lib: ", item)
        setDataUser(item);
        setIsShowModalEditUser(true);
    
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
            <b>List User</b>
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
                {/* <button style={{ marginLeft: '10px' }} className="btn btn-success" onClick={() => setIsShowModalAddUser(true)}>
                    <i className="fa-solid fa-circle-plus"></i> Add User
                </button> */}
            </div>

        </div>
        <div className='col-3 my-3'>
            <input className='form-control' placeholder='Search user by name.....'
                onChange={(event) => handleSearch(event)} />
        </div>
        <Table striped bordered hover>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Email</th>
                    <th>Phone</th>
                    <th>Status</th>
                    <th>Role</th>
                    <th>Department</th>
                    <th>Action</th>
                </tr>
            </thead>
            <tbody>
                {listUser && listUser.length > 0 &&

                    listUser.map((item, index) => {
                        return (
                            <tr key={`user-${index}`}>
                                <td>{item.userId}</td>
                                <td>{item.firstName + item.lastName}</td>
                                <td>{item.email}</td>
                                <td>{item.phone}</td>
                                <td>{item.status}</td>
                                <td>{item.role}</td>
                                <td>{item.department.departmentName}</td>
                             
                                <td>
                                    <button className="btn btn-warning mx-3" onClick={() => handleEditUser(item)}>Edit</button>
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

        {/* <ModalAddUser
            show={isShowModalAddUser}
            handleClose={handleClose}
        /> */}
        <ModalEditUser
            show={isShowModalEditUser}
            dataUser={dataUser}
            handleClose={handleClose}
        /> 
    </>)
}
export default TableUser;