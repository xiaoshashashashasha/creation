//定义store
import {defineStore} from 'pinia'
import {ref} from "vue";

export const useTokenStore = defineStore('token', () => {
    const token = ref('')

    const setToken = (newtoken) => {
        token.value = newtoken
        console.log(token.value+"token设置成功")
    }

    const removeToken = () => {
        token.value = ''
    }

    return {
        token, setToken, removeToken
    }
},{
    persist: true//持久化存储
})