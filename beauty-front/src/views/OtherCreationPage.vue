<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import CardCreation from '@/components/CardCreation.vue'
import { otherCreationList } from '@/api/creation'
import { ElMessage } from 'element-plus'

const route = useRoute()
const router = useRouter()

const userId = route.params.id

const creations = ref([])
const total = ref(0)
const pageNum = ref(1)
const pageSize = ref(15)
const loading = ref(false)

const fetchCreations = async () => {
  loading.value = true
  try {
    const res = await otherCreationList({
      pageNum: pageNum.value,
      pageSize: pageSize.value,
      user_id: userId
    })
    creations.value = res.data.items
    total.value = res.data.total
  } catch (err) {
    ElMessage.error('加载他人内容失败')
  } finally {
    loading.value = false
  }
}

const handleCurrentChange = (val) => {
  pageNum.value = val
  fetchCreations()
}

const goBack = () => {
  router.back()
}

onMounted(() => {
  fetchCreations()
})
</script>

<template>
  <el-container class="view-box">
    <el-main class="main">
      <div class="main-content">
        <el-card class="card">
          <template #header>
            <div class="card-header">
              <el-button @click="goBack" type="primary" icon="el-icon-arrow-left" plain>返回</el-button>
              <h2 style="margin: 0;">TA 的内容</h2>
            </div>
          </template>

          <div v-if="loading" class="card-grid">
            <el-skeleton v-for="n in pageSize" :key="n" style="width: 280px; height: 240px;" animated />
          </div>

          <div v-else>
            <div v-if="creations.length > 0" class="card-grid">
              <CardCreation v-for="item in creations" :key="item.id" :data="item" :loading="false" />
            </div>
            <el-empty v-else description="暂无内容" />
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
