import axios from 'axios'
import {ElMessage} from "element-plus";
import {useTokenStore} from "@/stores/token";

import router from "@/router";

const baseURL = '/api'

const instance = axios.create({baseURL})

// 请求拦截器
instance.interceptors.request.use(
    config => {
        const tokenStore = useTokenStore()


        if (tokenStore.token) {
            config.headers.Authorization = tokenStore.token
        } else {
            console.warn('⚠️ 请求时 token 为空')
        }

        return config
    },
    error => Promise.reject(error)
)

// 响应拦截器
instance.interceptors.response.use(
    result => {
        if (result.data.code === 0) {
            return result.data
        }
        console.log(result.data.msg)
        ElMessage.error(result.data.msg?result.data.msg:'服务异常')
        return Promise.reject(result.data)
    },
    error => {
        const status = error.response?.status

        if (status === 500) {
            ElMessage.error('服务器内部错误，请稍后重试')
        } else if (status === 404) {
            ElMessage.error('请求接口不存在')
        } else if (status === 401) {
            ElMessage.error('登录状态已过期，请重新登录')
            router.push('/login')
        } else {
            ElMessage.error(error.message || '请求失败')
        }
        return Promise.reject(error)
    }
)

export default instance
