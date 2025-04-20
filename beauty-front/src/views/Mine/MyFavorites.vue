<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import CardCreation from '@/components/CardCreation.vue'
import { favoriteList } from '@/api/creation'
import { ElMessage } from 'element-plus'

const router = useRouter()

const creations = ref([])
const total = ref(0)
const pageNum = ref(1)
const pageSize = ref(15)
const loading = ref(false)

const getFavorites = async () => {
  loading.value = true
  try {
    const res = await favoriteList({
      pageNum: pageNum.value,
      pageSize: pageSize.value
    })
    creations.value = res.data.items
    total.value = res.data.total
  } catch (err) {
    ElMessage.error('收藏内容加载失败')
  }
  loading.value = false
}

const handleCurrentChange = (val) => {
  pageNum.value = val
  getFavorites()
}

const backToHome = () => {
  router.push('/')
}

onMounted(() => {
  getFavorites()
})
</script>

<template>
  <el-container class="view-box">
    <el-main class="main">
      <div class="main-content">
        <el-card class="card">
          <template #header>
            <div class="card-header">
              <div>
                <el-button @click="backToHome" type="primary" icon="el-icon-arrow-left" plain>
                  返回首页
                </el-button>
              </div>
              <h2 style="margin: 0;">我的收藏</h2>
            </div>
          </template>

          <div v-if="loading" class="card-grid">
            <el-skeleton v-for="n in pageSize" :key="n" style="width: 280px; height: 240px;" animated />
          </div>

          <div v-else>
            <div v-if="creations.length > 0" class="card-grid">
              <CardCreation v-for="item in creations" :key="item.id" :data="item" :loading="false" />
            </div>
            <el-empty v-else description="暂无收藏内容" />
          </div>

          <el-pagination
              layout="prev, pager, next"
              :total="total"
              :page-size="pageSize"
              :current-page="pageNum"
              @current-change="handleCurrentChange"
              background
              style="margin-top: 20px; text-align: center;"
          />
        </el-card>
      </div>
    </el-main>
  </el-container>
</template>

<style scoped>
.main-content {
  flex: 1;
  width: 1720px;
  margin: 0 auto;
  background: #fff;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-grid {
  display: flex;
  flex-wrap: wrap;
  justify-content: flex-start;
  gap: 58px;
  padding: 20px;
}
</style>
