<template>
  <div class="box-login-switch" @click="toggle">
    <div class="switch-slider" :class="{ 'left': localValue, 'right': !localValue }" />
    <p class="switch-text-login" v-if="textVisible && localValue">》管理视图</p>
    <p class="switch-text-register" v-if="textVisible && !localValue">一般视图《</p>
  </div>
</template>

<script setup>
import {ref, watch} from 'vue'

const props = defineProps({
  modelValue: {
    type: Boolean,
    default: false
  }
})

const emit = defineEmits(['update:modelValue', 'modeChange'])

const localValue = ref(props.modelValue) // 本地开关状态
const textVisible = ref(true)

// 监听外部 modelValue 的变化
watch(() => props.modelValue, (val) => {
  localValue.value = val
})

// 切换逻辑
const toggle = () => {
  localValue.value = !localValue.value
  textVisible.value = false
  setTimeout(() => {
    textVisible.value = true
    emit('update:modelValue', localValue.value)
    emit('modeChange', localValue.value)
  }, 400)
}
</script>

<style scoped>
.box-login-switch {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 40px;
  width: 180px;
  background-color: #eae9e9;
  position: relative;
  cursor: pointer;
  overflow: visible;
  box-shadow: inset 0 0 5px rgba(0, 0, 0, 0.1);
}

.switch-slider {
  position: absolute;
  height: 54px;
  width: 35px;
  background: linear-gradient(to bottom, #86e4d3, #60b8aa); /* 渐变绿色 */
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2), /* 外部阴影 */ inset 0 -2px 4px rgba(0, 0, 0, 0.2); /* 内部阴影 */
  border: 1px solid #5bb1a1;
  transition: all 0.4s ease;
  top: -8px;
}

.left {
  left: 0px;
}

.right {
  left: 145px;
}

.switch-text-login,
.switch-text-register {
  z-index: 1;
  font-family: 'Segoe UI', sans-serif;
  font-size: 24px;
  color: #555;
  user-select: none;
  transition: opacity 0.3s;
}

.switch-text-login {
  margin-left: 50px;
}

.switch-text-register {
  margin-left: 10px;
}
</style>
