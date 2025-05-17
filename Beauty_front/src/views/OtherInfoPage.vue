<script setup>
import { onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import {userInfoService, userOtherInfoService} from '@/api/user'
import { ElMessage } from 'element-plus'
import {ArrowLeft} from "@element-plus/icons-vue";

const route = useRoute()
const router = useRouter()
const userId = route.params.id // 从路由中获取 user_id
const myId = ref()

const userInfo = ref({})
const roleMap = {
  0: '普通用户',
  1: '管理员',
  2: '封禁用户'
}

const fetchUserInfo = async () => {
  try {
    const res = await userInfoService()
    myId.value = res.data.user_id
  } catch (err) {
    ElMessage.error('用户信息加载失败')
  }
}

const fetchOtherUserInfo = async () => {
  try {
    const res = await userOtherInfoService(userId)
    userInfo.value = res.data
  } catch (err) {
    ElMessage.error('用户信息加载失败')
  }
}

const goBack = () => {
  router.back()
}

const goToUserCreation = (user_id) => {
  router.push(`/otherCreation/${user_id}`)
}

const goForPrChat = (user_id) =>{
  router.push(`/myMessage/${user_id}`)
}

onMounted(async () => {
  await Promise.all([
      fetchOtherUserInfo(),
      fetchUserInfo()
  ])
})
</script>

<template>
  <el-container class="view-box">
    <el-main class="main">
      <div class="main-content">
        <el-button
            class="back-btn"
            type="primary"
            :icon="ArrowLeft"
            @click="goBack"
            plain
        >
          返回上一页
        </el-button>

        <div class="profile-box">
          <div class="avatar">
            <img :src="userInfo.user_pic" alt="头像" />
          </div>
          <div class="info">
            <el-descriptions title="用户信息" column="1" border>
              <el-descriptions-item label="用户名">{{ userInfo.username }}</el-descriptions-item>
              <el-descriptions-item label="昵称">{{ userInfo.nickname }}</el-descriptions-item>
              <el-descriptions-item label="性别">{{ userInfo.gender }}</el-descriptions-item>
              <el-descriptions-item label="邮箱">{{ userInfo.email }}</el-descriptions-item>
              <el-descriptions-item label="注册时间">{{ userInfo.created_at }}</el-descriptions-item>
              <el-descriptions-item label="粉丝数">{{ userInfo.followers_count }}</el-descriptions-item>
              <el-descriptions-item label="关注数">{{ userInfo.following_count }}</el-descriptions-item>
              <el-descriptions-item label="权限状态">
                <el-tag :type="userInfo.role === 0 ? 'info' : userInfo.role === 1 ? 'success' : 'danger'">
                  {{ roleMap[userInfo.role] }}
                </el-tag>
              </el-descriptions-item>
            </el-descriptions>

            <div class="actions">
              <el-button type="primary" plain @click="goToUserCreation(userInfo.user_id)">TA 的内容</el-button>
              <el-button v-if="userInfo.user_id !== myId" type="primary" plain @click="goForPrChat(userInfo.user_id)">私信 TA</el-button>
            </div>
          </div>
        </div>
      </div>
    </el-main>
  </el-container>
</template>

<style scoped>
.main-content {
  flex: 1;
  width: 100%;
  max-width: 1000px;
  height: 685px;
  margin: 40px auto;
  background: #fff;
  padding: 30px;
  padding-top: 20px;
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
}

.profile-box {
  display: flex;
  gap: 40px;
  align-items: flex-start;
}

.avatar img {
  width: 140px;
  height: 140px;
  object-fit: cover;
  border-radius: 50%;
  border: 3px solid #dcdfe6;
}

.actions {
  margin-top: 20px;
}

.info {
  flex: 1;
}

::v-deep(.el-descriptions__title) {
  font-size: 28px;
}
::v-deep(.el-descriptions__body td) {
  font-size: 18px !important;
  padding: 12px 16px;
}

.back-btn {
  z-index: 10;
  margin-bottom: 20px;
}
</style>
