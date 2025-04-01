import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useStateStore = defineStore('state', () => {
    const sta = ref(3) // 默认 3（无权限）

    const setState = (newVal) => {
        sta.value = newVal
    }

    const clearState = () => {
        sta.value = 3
    }

    return {
        sta,
        setState,
        clearState
    }
}, {
    persist: true // ✅ 持久化存储
})
