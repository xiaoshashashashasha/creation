<template>
  <div class="box-login-switch" @click="toggle">
    <div class="switch-slider" :class="{ 'left': isLogin, 'right': !isLogin }" />
    <p class="switch-text-login" v-if="textVisible && isLogin">》注册</p>
    <p class="switch-text-register" v-if="textVisible && !isLogin">登录《</p>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const isLogin = ref(true)
const textVisible = ref(true)

// eslint-disable-next-line no-undef
const emit = defineEmits(['update:modelValue', 'modeChange'])

function toggle() {
  isLogin.value = !isLogin.value
  textVisible.value = false
  setTimeout(() => {
    textVisible.value = true
    emit('update:modelValue', isLogin.value)
    emit('modeChange', isLogin.value)
  }, 400)
}
</script>

<style scoped>
.box-login-switch {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 60px;
  width: 160px;
  background-color: #eae9e9;
  position: relative;
  cursor: pointer;
  overflow: visible;
  box-shadow: inset 0 0 5px rgba(0,0,0,0.1);
}

.switch-slider {
  position: absolute;
  height: 70px;
  width: 35px;
  background: linear-gradient(to bottom, #86e4d3, #60b8aa); /* 渐变绿色 */
  box-shadow:
      0 4px 8px rgba(0, 0, 0, 0.2),      /* 外部阴影 */
      inset 0 -2px 4px rgba(0, 0, 0, 0.2); /* 内部阴影 */
  border: 1px solid #5bb1a1; /* 更深绿色边框 */
  transition: all 0.4s ease;
  top: -5px;
}


.left {
  left: 4px;
}

.right {
  left: 120px;
}

.switch-text-login,
.switch-text-register {
  z-index: 1;
  font-family: 'Segoe UI', sans-serif;
  font-size: 30px;
  color: #555;
  user-select: none;
  transition: opacity 0.3s;
}

.switch-text-login {
  margin-left: 60px;
}

.switch-text-register {
  margin-left: 10px;
}
</style>
