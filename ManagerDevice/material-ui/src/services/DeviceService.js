import axios from './custom-axios';

const fetchAllDevice = (keyword, pageNo, pageSize, type, category, status) => {
    const params = new URLSearchParams({ keyword, pageNo, pageSize });
    if (type) type.forEach(t => params.append('type', t));
    if (category) category.forEach(c => params.append('category', c));
    if (status) status.forEach(s => params.append('status', s));

    return axios.get(`/api/v1/devices?${params.toString()}`, {
        headers: {
            'Authorization': `Bearer ${localStorage.getItem('token')}`,
        }
    });
}

const fetchAllUser = (keyword, pageNo, pageSize) => {
    return axios.get(`/api/v1/admin/search-user?keyword=${keyword}&pageNo=${pageNo}&pageSize=${pageSize}`, {
        headers: {
            'Authorization': `Bearer ${localStorage.getItem('token')}`,
        }
    });
}

const getAllCategory = () => {
    return axios.get(`/api/v1/categories`, {
        headers: {
            'Authorization': `Bearer ${localStorage.getItem('token')}`,
        }
    });
}


const getUser = () => {
    return axios.get(`/api/v1/profile/users`, {
        headers: {
            'Authorization': `Bearer ${localStorage.getItem('token')}`,
        }
    });
}

const postUser = async (email, firstName, lastName, departmentId) => {
    try {
        return await axios.post(`/api/v1/admin/users`, { email, firstName, lastName, departmentId}, {
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('token')}`,
            }
        })
    } catch (error) {
        console.error('Error fetching requests:', error.response.data.error);
        return error.response.data.error;
    }
   
}

const putUser = ( firstName, lastName, phone, status, role, password, departmentId) => {
    return axios.put(`/api/v1/profile/users`, {firstName, lastName, phone, status, role, password, departmentId}, {
        headers: {
            'Authorization': `Bearer ${localStorage.getItem('token')}`,
        }
    });
}

const logoutAccount = () => {
    return axios.get(`/api/v1/logout-account`, {
        headers: {
            'Authorization': `Bearer ${localStorage.getItem('token')}`,
        }
    });
}

const postSaveDevice = (deviceName, price, description, category) => {
    return axios.post(`/api/v1/admin/devices`, { deviceName, price, description, category }, {
        headers: {
            'Authorization': `Bearer ${localStorage.getItem('token')}`,
        }
    })
}


const getAllDepartment = () => {
    return axios.get(`/api/v1/admin/departments`, {
        headers: {
            'Authorization': `Bearer ${localStorage.getItem('token')}`,
        }
    })
}

const getDashboard = () => {
    return axios.get(`/api/v1/admin/upload-file-dashboard`, {
        headers: {
            'Authorization': `Bearer ${localStorage.getItem('token')}`,
        }
    });
}

const postUploadImage = (id, type, file) => {
    const formData = new FormData();
    formData.append('file', file);
    return axios.post(`/api/v1/file/upload/${id}?type=${type}`,formData,{
        headers: {
            'Content-Type': 'multipart/form-data',
            'Authorization': `Bearer ${localStorage.getItem('token')}`,
        }
    });
}

const getRequestByCreated = async (status, pageNo, pageSize) => {
    try {
        return await axios.get(`/api/v1/requests?status=${status}&pageNo=${pageNo}&pageSize=${pageSize}`, {
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('token')}`,
            }
        })
    } catch (error) {
        console.error('Error fetching requests:', error.response.data.error);
        return error.response.data.error;

    }
}

const postSendRequest = (requestId) => {
    console.log(localStorage.getItem('token'))
    return axios.post(`/api/v1/send-request/${requestId}`, {
        headers: {
            'Authorization': `Bearer ${localStorage.getItem('token')}`,
        }
    })
}

const postAddRequest = (reason, deviceIds, startDate, endDate) => {
    console.log("device", deviceIds);
    return axios.post(`/api/v1/requests`, { reason, deviceIds, startDate, endDate }, {
        headers: {
            'Authorization': `Bearer ${localStorage.getItem('token')}`,
        }
    })
}

const getRequestAdmin = async (status, pageNo, pageSize) => {
    try {
        return await axios.get(`/api/v1/admin/requests?status=${status}&pageNo=${pageNo}&pageSize=${pageSize}`, {
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('token')}`,
            }
        })
    } catch (error) {
        console.error('Error fetching requests:', error.response.data.error);
        return error.response.data.error;
    }

}

const postApprove = (requestId) => {
    return axios.post(`/api/v1/admin/approve/${requestId}`, {}, {
        headers: {
            'Authorization': `Bearer ${localStorage.getItem('token')}`,
        }
    })
}

const postReject = (requestId) => {
    return axios.post(`/api/v1/admin/reject/${requestId}`, {}, {
        headers: {
            'Authorization': `Bearer ${localStorage.getItem('token')}`,
        }
    })
}
const postReturnDevice = (requestId) => {
    return axios.post(`/api/v1/admin/return-device/${requestId}`, {}, {
        headers: {
            'Authorization': `Bearer ${localStorage.getItem('token')}`,
        }
    })
}


const putSaveUser = (userId, firstName, lastName, phone, status, role, password, departmentId) => {
    return axios.put(`/api/v1/admin/update-users/${userId}`, { firstName, lastName, phone, status, role, password, departmentId }, {
        headers: {
            'Authorization': `Bearer ${localStorage.getItem('token')}`,
        }
    });
}

const putUpdateDevice = (deviceId, deviceName, price, description, category, status) => {
    return axios.put(`/api/v1/admin/devices/${deviceId}`, { deviceName, price, description, category, status }, {
        headers: {
            'Authorization': `Bearer ${localStorage.getItem('token')}`,
        }
    })
}

const putUpdateRequest = (requestId, reason, deviceIds, start, end) => {
    return axios.put(`/api/v1/requests/${requestId}`, { reason, deviceIds, start, end }, {
        headers: {
            'Authorization': `Bearer ${localStorage.getItem('token')}`,
        }
    })
}

const loginAPI = async (email, password) => {
    try {
        return await axios.post(`/api/v1/authentication/signin`, { email, password })
    } catch (error) {
        console.error('Error fetching requests:', error.response.data.error);
        return error.response.data.error;
    }
    
}

const putVerify = async (email) => {
    try {
        return await axios.put(`/api/v1/authentication/regenerate-otp?email=${email}`)
    } catch (error) {
        console.error('Error fetching requests:', error.response.data.error);
        return error.response.data.error;
    }  
}

const putResetPassword = async (email, otp) => {
    try {
        return await axios.put(`/api/v1/authentication/reset-password?email=${email}&otp=${otp}`)
    } catch (error) {
        console.error('Error fetching requests:', error.response.data.error);
        return error.response.data.error;
    }  
}

export {
    fetchAllDevice, getAllCategory, postSaveDevice, putUpdateDevice, loginAPI, getUser, fetchAllUser,
    putSaveUser, logoutAccount, getAllDepartment, getRequestByCreated, postSendRequest, postAddRequest,
    getRequestAdmin, postApprove, postReject, putUpdateRequest, postReturnDevice, putUser, getDashboard,
    postUploadImage, postUser, putVerify, putResetPassword
};