//定义store
import {defineStore} from 'pinia'
import {ref} from "vue";

export const useTokenStore = defineStore('token', () => {
    const token = ref('')

    const setToken = (newtoken) => {
        token.value = newtoken
    }

    const removeToken = () => {
        token.value = ''
    }

    return {
        token, setToken, removeToken
    }
},{
    persist: true
})