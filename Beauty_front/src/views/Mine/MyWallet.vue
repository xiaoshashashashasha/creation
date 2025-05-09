<script setup>
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { walletInfo } from '@/api/wallet'
import { ElMessage } from 'element-plus'
import { Wallet } from '@element-plus/icons-vue'

const router = useRouter()

const Info = ref({
  coins: 0,
  membership: 0
})

const membershipMap = {
  0: '无',
  1: '普通会员',
  2: '高级会员'
}

const fetchWalletInfo = async () => {
  try {
    const res = await walletInfo()
    Info.value = res.data
  } catch (err) {
    ElMessage.error('钱包信息加载失败')
    console.log(err)
  }
}

const backToHome = () => {
  router.push('/')
}

onMounted(() => {
  fetchWalletInfo()
})
</script>

<template>
  <el-container class="view-box">
    <el-main class="main">
      <div class="main-content">

        <!-- 返回首页按钮 -->
        <el-button
            class="back-btn"
            type="primary"
            icon="el-icon-arrow-left"
            @click="backToHome"
            plain
        >
          返回首页
        </el-button>

        <div class="wallet-box">
          <div class="wallet-icon">
            <el-icon size="50"><Wallet /></el-icon>
          </div>
          <div class="info">
            <el-descriptions title="我的钱包" column="1" border>
              <el-descriptions-item label="金币余额">
                <span style="font-weight: bold; font-size: 22px;">{{ Info.coins }} 枚</span>
              </el-descriptions-item>
              <el-descriptions-item label="会员状态">
                <el-tag :type="Info.membership === '2' ? 'success' : Info.membership === '1' ? 'info' : 'default'">
                  {{ membershipMap[Info.membership] || '无' }}
                </el-tag>
              </el-descriptions-item>
            </el-descriptions>
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
  max-width: 800px;
  margin: 40px auto;
  background: #fff;
  padding: 30px;
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
}

.wallet-box {
  display: flex;
  gap: 40px;
  align-items: flex-start;
}

.wallet-icon img {
  width: 140px;
  height: 140px;
  object-fit: contain;
}

.info {
  flex: 1;
}

::v-deep(.el-descriptions__title) {
  font-size: 26px;
}
::v-deep(.el-descriptions__body td) {
  font-size: 18px !important;
  padding: 12px 16px;
}

.back-btn {
  margin-bottom: 20px;
}
</style>
