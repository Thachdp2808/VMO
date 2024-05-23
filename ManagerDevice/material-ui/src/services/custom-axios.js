import axios from"axios";

const instance = axios.create({
    baseURL: 'http://localhost:8080'
});

instance.interceptors.response.use(function(response){
    return response.data;
}, function (erro) {
    return Promise.reject(erro);
});

export default instance;