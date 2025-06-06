<template>
  <div v-if="mine && !isManageRoute" class="sidebar-container">
    <!-- 我的按钮 -->
    <div :class="['my-btn', isOpen ? 'open' : '']" @click="toggleSidebar">
      我<br>的
    </div>

    <transition name="slide">
      <div v-if="isOpen" class="sidebar-content">
        <!-- 头像昵称区域 -->
        <div class="user-info">
          <img :src="userInfo.user_pic" alt="头像" class="avatar"/>
          <p class="nickname">{{ userInfo.nickname }}</p>
        </div>

        <!-- 菜单区域 -->
        <el-menu class="menu" unique-opened>
          <el-menu-item index="1" @click="goToMyInfo">账号详细</el-menu-item>
          <el-menu-item index="2" @click="goToMyWallet">我的钱包</el-menu-item>
          <el-menu-item index="3" @click="goToMyCreation">我的内容</el-menu-item>
          <el-menu-item index="4" @click="goToMyCollection">我的收藏</el-menu-item>
          <el-menu-item index="5" @click="goToMyMessage">
            <div class="menu-item-with-badge">
              <span>我的消息</span>
              <el-badge :value="unReadCount" :max="99" v-if="unReadCount > 0" class="unread-badge"/>
            </div>
          </el-menu-item>

          <el-menu-item index="6" @click="goToMyReservation">我的预约</el-menu-item>
          <el-sub-menu index="7">
            <template #title>我的门店</template>
            <el-menu-item index="7-1" @click="goToMyOffline">门店列表</el-menu-item>
            <el-menu-item index="7-2" @click="goToMyRequest">门店申请</el-menu-item>
          </el-sub-menu>


          <el-sub-menu index="8">
            <template #title>我的关注 ({{ follows.length }})</template>
            <el-menu-item v-if="follows.length === 0" disabled>
              暂无关注
            </el-menu-item>
            <el-menu-item
                v-for="item in follows"
                :key="item.id"
                @click="goToOtherInfo(item.user_id)"
            >
              <div class="follow-item">
                <img :src="item.user_pic" class="follow-avatar"/>
                <span>{{ item.nickname }}</span>
              </div>
            </el-menu-item>
          </el-sub-menu>

          <el-sub-menu index="9">
            <template #title>我的粉丝 ({{ fans.length }})</template>
            <el-menu-item v-if="fans.length === 0" disabled>
              暂无粉丝
            </el-menu-item>
            <el-menu-item
                v-for="item in fans"
                :key="item.id"
                @click="goToOtherInfo(item.user_id)"
            >
              <div class="follow-item">
                <img :src="item.user_pic" class="follow-avatar"/>
                <span>{{ item.nickname }}</span>
              </div>
            </el-menu-item>
          </el-sub-menu>


        </el-menu>
      </div>
    </transition>
  </div>
</template>


<script setup>
import {computed, ref, onMounted} from 'vue'
import {useStateStore} from '@/stores/state'
import {useTokenStore} from '@/stores/token'
import {useRouter} from 'vue-router'
import {userInfoService, listFollowedService, listFollowerService} from '@/api/user'
import {useRoute} from 'vue-router'
import {getUnReadCount} from '@/api/prMessage'
import {ElMessage} from "element-plus";


const route = useRoute()
const isManageRoute = computed(() => route.path.startsWith('/manage'))
const router = useRouter()
const tokenStore = useTokenStore()
const stateStore = useStateStore()
const mine = computed(() => [0, 1, 2].includes(stateStore.sta))


const isOpen = ref(false)

const userInfo = ref({})
const follows = ref([])
const fans = ref([])
const unReadCount = ref(0)

const goToMyInfo = () => {
  router.push('/myInfo')
  isOpen.value = false
}

const goToMyWallet = () => {
  router.push('/myWallet')
  isOpen.value = false
}

const goToMyCreation = () => {
  router.push('/myCreation')
  isOpen.value = false
}

const goToMyCollection = () => {
  router.push('/myFavorites')
  isOpen.value = false
}

const goToMyRequest = () => {
  router.push('/myRequest')
  isOpen.value = false
}
const goToMyOffline = () => {
  router.push('/myOffline')
  isOpen.value = false
}

const goToOtherInfo = (user_id) => {
  router.push(`/otherInfo/${user_id}`)
  isOpen.value = false
}

const goToMyMessage = () => {
  router.push('/myMessage')
  isOpen.value = false
}

const goToMyReservation = () => {
  router.push('/myReservation')
  isOpen.value = false
}


const toggleSidebar = async () => {
  isOpen.value = !isOpen.value
  if (isOpen.value) {
    try {

      await Promise.all([
        fetchUserData(),
        fetchUnReadCount(),
        fetchFollows(),
        fetchFans()
      ])

    } catch (err) {
      console.error('展开侧拉栏时加载数据失败', err)
    }
  }
}


const fetchUnReadCount = async () => {
  try {
    const res = await getUnReadCount()
    if (res.code === 0) {
      unReadCount.value = res.data || 0
    } else {
      console.error('获取未读消息数失败:', res.msg)
    }
  } catch (err) {
    console.error('拉取未读消息数出错:', err)
  }
}


const fetchUserData = async () => {
  try {
    const res = await userInfoService()
    userInfo.value = res.data
  } catch (err) {
    if (err?.response?.status === 401) {
      console.warn('[Sidebar] 未登录，跳过用户信息拉取')
    } else {
      console.error(err)
      ElMessage.error(err.msg || '用户信息加载失败')
    }
  }
}

const fetchFollows = async () => {
  try {
    const res = await listFollowedService()
    follows.value = res.data || []
  } catch (err) {
    console.error('获取关注列表失败:', err)
  }
}

const fetchFans = async () => {
  try {
    const res = await listFollowerService()
    fans.value = res.data || []
  } catch (err) {
    console.error('获取粉丝列表失败:', err)
  }
}


onMounted(async () => {
  if (!tokenStore.token) {
    return
  } else {
    mine.value = computed(() => [0, 1, 2].includes(stateStore.sta))
  }
  try {
    await Promise.allSettled([
      fetchUserData(),
      fetchFollows(),
      fetchFans(),
      fetchUnReadCount()
    ])
  } catch (err) {
    console.warn('初始化侧拉栏时出现问题', err)
  }
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

::v-deep(.el-menu-item:hover) {
  background-color: #f0f9ff;
  color: #409EFF;
  transition: all 0.3s ease;
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

.menu-item-with-badge {
  display: flex;
  align-items: center;
}

.unread-badge {
  margin-left: 8px;
  display: inline-flex;
  align-items: center;
}


</style>

