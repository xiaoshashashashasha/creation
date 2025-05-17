<script setup>
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { userInfoService,updateUserPicService,updatePasswordService } from '@/api/user'
import { uploadFile } from '@/api/fileUpload'
import { ElMessage,ElDialog } from 'element-plus'
import {ArrowLeft} from "@element-plus/icons-vue";

const router = useRouter()

const showAvatarDialog = ref(false)
const selectedFile = ref(null)
const uploading = ref(false)
const avatarPreviewUrl = ref('')
const showPasswordDialog = ref(false)
const oldPassword = ref('')
const newPassword = ref('')
const confirmPassword = ref('')
const updatingPassword = ref(false)
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

const openPasswordDialog = () => {
  showPasswordDialog.value = true
  oldPassword.value = ''
  newPassword.value = ''
  confirmPassword.value = ''
}

const closePasswordDialog = () => {
  showPasswordDialog.value = false
}

const handleUpdatePassword = async () => {
  if (!oldPassword.value || !newPassword.value || !confirmPassword.value) {
    ElMessage.warning('请填写所有密码字段')
    return
  }

  if (newPassword.value !== confirmPassword.value) {
    ElMessage.warning('两次密码输入不一致')
    return
  }

  try {
    updatingPassword.value = true
    await updatePasswordService(oldPassword.value, newPassword.value, confirmPassword.value)

    ElMessage.success('密码更新成功')
    closePasswordDialog() // 关闭弹窗

    router.push('/login')
  } catch (err) {
    console.error('更新密码失败', err)
    ElMessage.error('更新密码失败，请重试')
  } finally {
    updatingPassword.value = false
  }
}


// 打开弹窗
const openAvatarDialog = () => {
  showAvatarDialog.value = true
  selectedFile.value = null
}

const closeAvatarDialog = () => {
  showAvatarDialog.value = false
  selectedFile.value = null
  avatarPreviewUrl.value = ''
}


// 选择文件
const handleFileChange = (file) => {
  selectedFile.value = file.raw
  avatarPreviewUrl.value = URL.createObjectURL(file.raw)
}


// 上传并更新头像
const uploadAndUpdateAvatar = async () => {
  if (!selectedFile.value) {
    ElMessage.warning('请先选择文件')
    return
  }
  try {
    uploading.value = true
    const res = await uploadFile(selectedFile.value)
    const avatarUrl = res.data  // 后端返回的图片地址

    await updateUserPicService(avatarUrl)

    ElMessage.success('头像更新成功！')
    showAvatarDialog.value = false
    await fetchUserInfo()  // 重新拉取用户信息，更新头像
  } catch (err) {
    console.error('上传或更新失败', err)
    ElMessage.error('上传失败')
  } finally {
    uploading.value = false
  }
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
            :icon="ArrowLeft"
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

            <!-- 操作按钮 -->
            <div class="actions">
              <el-button type="warning" plain style="margin-right: 12px" @click="openPasswordDialog">修改密码</el-button>
              <el-button type="primary" plain @click="openAvatarDialog">更换头像</el-button>
            </div>
          </div>
        </div>
      </div>
    </el-main>
  </el-container>

  <!-- 更改密码 -->
  <el-dialog v-model="showPasswordDialog" title="修改密码" width="400px" @close="closePasswordDialog">
    <el-form>
      <el-form-item label="旧密码" prop="oldPassword">
        <el-input v-model="oldPassword" type="password" placeholder="请输入旧密码"></el-input>
      </el-form-item>
      <el-form-item label="新密码" prop="newPassword">
        <el-input v-model="newPassword" type="password" placeholder="请输入新密码"></el-input>
      </el-form-item>
      <el-form-item label="确认密码" prop="confirmPassword">
        <el-input v-model="confirmPassword" type="password" placeholder="请输入确认密码"></el-input>
      </el-form-item>
    </el-form>

    <div style="text-align: right;">
      <el-button @click="closePasswordDialog">取消</el-button>
      <el-button type="success" @click="handleUpdatePassword" :loading="updatingPassword">确认修改</el-button>
    </div>
  </el-dialog>


  <!-- 更新头像 -->
  <el-dialog v-model="showAvatarDialog" title="上传新头像" width="400px" @close="closeAvatarDialog">
    <!-- 本地预览 -->
    <div v-if="avatarPreviewUrl" style="text-align: center; margin-bottom: 20px;">
      <img :src="avatarPreviewUrl" alt="预览头像" style="width: 120px; height: 120px; border-radius: 50%; object-fit: cover; border: 2px solid #ccc;" />
    </div>

    <el-upload
        class="upload-demo"
        :show-file-list="false"
        :before-upload="() => false"
    @change="handleFileChange"
    >
    <el-button type="primary">选择文件</el-button>
    </el-upload>

    <div style="margin-top: 20px; text-align: right;">
      <el-button @click="closeAvatarDialog">取消</el-button>
      <el-button
          type="success"
          @click="uploadAndUpdateAvatar"
          :loading="uploading"
      >上传并保存</el-button>
    </div>
  </el-dialog>


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
