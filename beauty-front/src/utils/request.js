import axios from 'axios'
import {ElMessage} from "element-plus";

const baseURL = '/api'

const instance = axios.create({
    baseURL,
    timeout: 5000
})

// 请求拦截器
instance.interceptors.request.use(
    config => {
        // 可在此添加 token 或统一 header
        return config
    },
    error => {
        return Promise.reject(error)
    }
)

// 响应拦截器
instance.interceptors.response.use(
    result => {
        if (result.data.code === 0) {
            return result.data
        }
        ElMessage.error(result.data.msg?result.data.msg:'服务异常')
        return Promise.reject(result.data)
    },
    error => {
        alert('服务异常')
        return Promise.reject(error)
    }
)

export default instance
