<script setup>
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { userInfoService } from '@/api/user'
import { ElMessage } from 'element-plus'

const router = useRouter()

const userInfo = ref({})
const roleMap = {
  0: '普通用户',
  1: '管理员',
  2: '封禁用户'
}

const fetchUserInfo = async () => {
  try {
    const res = await userInfoService()
    userInfo.value = res.data
  } catch (err) {
    ElMessage.error('用户信息加载失败')
  }
}

const backToHome = () => {
  router.push('/')
}

onMounted(() => {
  fetchUserInfo()
})
</script>

<template>
  <el-container class="view-box">
    <el-main class="main">
      <div class="main-content">
        <!-- 返回按钮 -->
        <el-button
            class="back-btn"
            type="primary"
            icon="el-icon-arrow-left"
            @click="backToHome"
            plain
        >
          返回首页
        </el-button>

        <div class="profile-box">
          <div class="avatar">
            <img :src="userInfo.user_pic" alt="头像" />
          </div>
          <div class="info">
            <el-descriptions title="账号信息" column="1" border>
              <el-descriptions-item label="用户名">{{ userInfo.username }}</el-descriptions-item>
              <el-descriptions-item label="昵称">{{ userInfo.nickname }}</el-descriptions-item>
              <el-descriptions-item label="性别">{{ userInfo.gender }}</el-descriptions-item>
              <el-descriptions-item label="邮箱">{{ userInfo.email }}</el-descriptions-item>
              <el-descriptions-item label="入站时间">{{ userInfo.created_at }}</el-descriptions-item>
              <el-descriptions-item label="粉丝数">{{ userInfo.followers_count }}</el-descriptions-item>
              <el-descriptions-item label="关注数">{{ userInfo.following_count }}</el-descriptions-item>
              <el-descriptions-item label="权限状态">
                <el-tag :type="userInfo.role === 0 ? 'info' : userInfo.role === 1 ? 'success' : 'danger'">
                  {{ roleMap[userInfo.role] }}
                </el-tag>
              </el-descriptions-item>
            </el-descriptions>

            <!-- 👇 添加操作按钮 -->
            <div class="actions">
              <el-button type="warning" plain style="margin-right: 12px">修改密码</el-button>
              <el-button type="primary" plain>更换头像</el-button>
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
