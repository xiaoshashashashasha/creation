<template>
  <div class="box-login-switch" @click="toggle">
    <div class="switch-slider" :class="{ 'left': isManage, 'right': !isManage }" />
    <p class="switch-text-login" v-if="textVisible && isManage">》管理视图</p>
    <p class="switch-text-register" v-if="textVisible && !isManage">一般视图《</p>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const isManage = ref(true)
const textVisible = ref(true)

// eslint-disable-next-line no-undef
const emit = defineEmits(['update:modelValue', 'modeChange'])

function toggle() {
  isManage.value = !isManage.value
  textVisible.value = false
  setTimeout(() => {
    textVisible.value = true
    emit('update:modelValue', isManage.value)
    emit('modeChange', isManage.value)
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
  box-shadow: inset 0 0 5px rgba(0,0,0,0.1);
}

.switch-slider {
  position: absolute;
  height: 54px;
  width: 35px;
  background: linear-gradient(to bottom, #86e4d3, #60b8aa); /* 渐变绿色 */
  box-shadow:
      0 4px 8px rgba(0, 0, 0, 0.2),      /* 外部阴影 */
      inset 0 -2px 4px rgba(0, 0, 0, 0.2); /* 内部阴影 */
  border: 1px solid #5bb1a1; /* 更深绿色边框 */
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
