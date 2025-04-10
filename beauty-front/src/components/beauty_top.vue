<script setup>
import ManageSwitch from "@/components/ManageSwitch.vue";
import { computed } from 'vue'
import router from "@/router";
import {useStateStore} from "@/stores/state";
import { userLogoutService} from "@/api/user";
import {useTokenStore} from "@/stores/token";


const stateStore = useStateStore()

// 基于 role 值派生三个控制变量
const content = computed(() => [0, 1, 2].includes(stateStore.sta))
const create = computed(() => [0, 1].includes(stateStore.sta))
const Mswitch = computed(() => stateStore.sta === 1)
// 切换视图时跳转路由
const handleModeChange = (isManage) => {
  if (isManage) {
    router.push('/')
  } else {
    router.push('/manage')
  }
}

const logout = async () => {
  try {
    await userLogoutService()

    const tokenStore = useTokenStore()
    const stateStore = useStateStore()

    tokenStore.removeToken()
    stateStore.setState(3)
    router.push('/login')
  }catch (error) {
    console.log(error)
  }
}
</script>

<template>
  <div class="top-box">
    <p class="logo">Beauty</p>

    <!-- 显示按钮部分 -->
    <template v-if="create">
      <el-button class="plus_btn">+</el-button>
    </template>

    <template v-if="content">
      <el-button class="creation_btn">内 容</el-button>
      <el-button class="hairstyle_btn">发 型</el-button>
      <el-button class="offline_btn">线下门店</el-button>
    </template>

    <!-- 显示切换开关 -->
    <ManageSwitch
        v-if="Mswitch"
        class="manage-switch"
        @modeChange="handleModeChange"
    />
    <el-button v-if="content" class="logout_btn" type="danger" @click="logout">退<br/>出</el-button>
  </div>
</template>

<style scoped>
.top-box {
  position: relative;
  width: 100%;
  height: 60px;
  background: #beccd3;
}

.logo {
  font-size: 38px;
  font-family: 'Segoe Script';
}

.plus_btn {
  position: absolute;
  left: 50%;
  transform: translateX(-65%);
  font-size: 60px;
  top: 5px;
  height: 50px;
  width: 50px;
  border-radius: 10px;
}

.creation_btn {
  position: absolute;
  font-size: 26px;
  top: 5px;
  right: 17%;
  width: 100px;
  height: 50px;
  border-radius: 10px;
}

.hairstyle_btn {
  position: absolute;
  font-size: 26px;
  top: 5px;
  right: 10%;
  width: 100px;
  height: 50px;
  border-radius: 10px;
}

.offline_btn {
  position: absolute;
  font-size: 26px;
  top: 5px;
  right: 2%;
  width: 120px;
  height: 50px;
  border-radius: 10px;
}

.manage-switch {
  position: absolute;
  top: 10px;
  left: 20%;
}

.logout_btn {
  position: absolute;
  top: 5px;
  right: 0;
  height: 50px;
  width: 30px;
  border-radius: 10px;
}

</style>
