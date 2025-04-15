<template>
  <div v-if="mine" class="sidebar-container">
    <!-- 我的按钮 -->
    <div :class="['my-btn', isOpen ? 'open' : '']" @click="toggleSidebar">
      我<br>的
    </div>

    <transition name="slide">
      <div v-if="isOpen" class="sidebar-content">
        <!-- 头像昵称区域 -->
        <div class="user-info">
          <img :src="userInfo.user_pic" alt="头像" class="avatar" />
          <p class="nickname">{{ userInfo.nickname }}</p>
        </div>

        <!-- 菜单区域 -->
        <el-menu class="menu" :default-active="activeMenu" unique-opened>
          <el-menu-item index="1">账号详细</el-menu-item>
          <el-menu-item index="2">我的钱包</el-menu-item>

          <el-menu-item index="3">我的内容</el-menu-item>

          <el-sub-menu index="4">
            <template #title>我的门店</template>
            <el-menu-item index="4-1">门店列表</el-menu-item>
            <el-menu-item index="4-2">门店申请</el-menu-item>
          </el-sub-menu>

          <el-sub-menu index="5">
            <template #title>我的收藏</template>
<!--            <el-menu-item v-for="item in collections" :key="item.id">-->
<!--              {{ item.title }}-->
<!--            </el-menu-item>-->
          </el-sub-menu>

          <el-sub-menu index="6">
            <template #title>我的关注</template>
<!--            <el-menu-item v-for="item in follows" :key="item.id">-->
<!--              <div class="follow-item">-->
<!--                <img :src="item.user_pic" class="follow-avatar" />-->
<!--                <span>{{ item.nickname }}</span>-->
<!--              </div>-->
<!--            </el-menu-item>-->
          </el-sub-menu>

          <el-sub-menu index="7">
            <template #title>我的粉丝</template>
<!--            <el-menu-item v-for="item in fans" :key="item.id">-->
<!--              <div class="follow-item">-->
<!--                <img :src="item.user_pic" class="follow-avatar" />-->
<!--                <span>{{ item.nickname }}</span>-->
<!--              </div>-->
<!--            </el-menu-item>-->
          </el-sub-menu>

        </el-menu>
      </div>
    </transition>
  </div>
</template>


<script setup>
import { computed, ref, onMounted } from 'vue'
import { useStateStore } from '@/stores/state'
import { userInfoService, userCollectionService, userFollowService, userFansService } from '@/api/user'

const state = useStateStore()
const mine = computed(() => [0, 1, 2].includes(state.sta))

const isOpen = ref(false)
const activeMenu = ref('1')

const userInfo = ref({})
const collections = ref([])
const follows = ref([])
const fans = ref([])

const toggleSidebar = async () => {
  isOpen.value = !isOpen.value
  if (isOpen.value) {
    await fetchUserData()  // 侧边栏打开时更新用户信息
  }
}


const fetchUserData = async () => {
  const res = await userInfoService()
  userInfo.value = res.data
  console.log(res.data)
}

// const fetchCollections = async () => {
//   const res = await userCollectionService()
//   collections.value = res.data || []
// }
//
// const fetchFollows = async () => {
//   const res = await userFollowService()
//   follows.value = res.data || []
// }
//
// const fetchFans = async () => {
//   const res = await userFansService()
//   fans.value = res.data || []
// }

onMounted(() => {
  fetchUserData()
  // fetchCollections()
  // fetchFollows()
  // fetchFans()
})
</script>


<style scoped>
.sidebar-container {
  position: relative;
  height: 100vh;
}

.my-btn {
  position: fixed;
  top: 60px;
  right: 0;
  background-color: #409EFF;
  color: white;
  width: 40px;
  height: 80px;
  font-size: 18px;
  line-height: 30px;
  text-align: center;
  padding-top: 20px;
  border-radius: 10px 0 0 10px;
  cursor: pointer;
  transition: all 0.3s;
  z-index: 999;
}

.my-btn.open {
  right: 200px;
}

.sidebar-content {
  position: fixed;
  right: 0;
  top: 60px;
  width: 180px;
  height: 100vh;
  background-color: #fff;
  box-shadow: -2px 0 8px rgba(0, 0, 0, 0.1);
  padding: 20px 10px;
  overflow-y: auto;
  z-index: 998;
}

.slide-enter-active, .slide-leave-active {
  transition: all 0.3s;
}
.slide-enter-from, .slide-leave-to {
  transform: translateX(100%);
}

.user-info {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 20px;
}

.avatar {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  object-fit: cover;
}

.nickname {
  margin-top: 10px;
  font-weight: bold;
  font-size: 18px;
}

.follow-item {
  display: flex;
  align-items: center;
}

.follow-avatar {
  width: 24px;
  height: 24px;
  border-radius: 50%;
  margin-right: 8px;
}
</style>

