import axios from"axios";
import { toast } from 'react-toastify';

const instance = axios.create({
    baseURL: 'http://localhost:8080'
});

instance.interceptors.response.use(function(response){
    return response.data;
}, function (erro) {
    return Promise.reject(erro);
});

instance.interceptors.response.use(
    (response) => {
        return response;
    },
    (error) => {
        if (error.response && error.response.status === 401) {
            toast.error("Session expired. Please log in again.");
            localStorage.removeItem('token');
            window.location.href = '/login'; // Điều hướng đến trang đăng nhập
        }
        return Promise.reject(error);
    }
);

export default instance;