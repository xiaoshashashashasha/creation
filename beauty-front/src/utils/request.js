import axios from 'axios'
import {ElMessage} from "element-plus";
import {useTokenStore} from "@/stores/token";

const baseURL = '/api'

const instance = axios.create({baseURL})

// 请求拦截器
instance.interceptors.request.use(
    config => {
        const tokenStore = useTokenStore()


        if (tokenStore.token) {
            config.headers.Authorization = tokenStore.token
            console.log('✅ 请求头 token 设置成功:', tokenStore.token)
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
        ElMessage.error(result.msg?result.msg:'服务异常')
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
        } else {
            ElMessage.error(error.msg || '请求失败')
        }
        return Promise.reject(error)
    }
)

export default instance
