<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import CardHairstyle from '@/components/part/CardHairstyle.vue'
import { hairstyleList } from '@/api/hairstyle'
import { ElMessage } from 'element-plus'
import {ArrowLeft} from "@element-plus/icons-vue";

const router = useRouter()

const hairstyles = ref([])
const total = ref(0)
const pageNum = ref(1)
const pageSize = ref(15)

const keyWord = ref('')
const loading = ref(false)

const getHairstyles = async () => {
  loading.value = true
  try {
    const res = await hairstyleList({
      pageNum: pageNum.value,
      pageSize: pageSize.value,
      keyWord: keyWord.value
    })
    hairstyles.value = res.data.items
    total.value = res.data.total
  } catch (err) {
    ElMessage.error('数据加载失败')
  }
  loading.value = false
}

onMounted(() => {
  getHairstyles()
})

const handleSearch = () => {
  pageNum.value = 1
  getHairstyles()
}

const handleReset = () => {
  keyWord.value = ''
  pageNum.value = 1
  getHairstyles()
}

const handleCurrentChange = (val) => {
  pageNum.value = val
  getHairstyles()
}

const backToHome = () => {
  router.push('/')
}
</script>

<template>
  <el-container class="view-box">
    <el-main class="main">
      <div class="main-content">
        <el-card class="card">
          <template #header>
            <div class="card-header">
              <el-button @click="backToHome" type="primary" :icon="ArrowLeft" plain>返回首页</el-button>
              <div class="search-box">
                <el-input v-model="keyWord" placeholder="请输入发型名称" style="width: 200px; margin-right: 10px"
                          clearable @keyup.enter="handleSearch"/>
                <el-button type="primary" @click="handleSearch">搜索</el-button>
                <el-button @click="handleReset">重置</el-button>
              </div>
            </div>
          </template>

          <div v-if="loading" class="card-grid">
            <el-skeleton v-for="n in pageSize" :key="n" style="width: 280px; height: 190px" animated/>
          </div>

          <div v-else>
            <div v-if="hairstyles.length > 0" class="card-grid">
              <CardHairstyle
                  v-for="item in hairstyles"
                  :key="item.hairstyle_id"
                  :data="item"
                  :loading="false"
              />
            </div>
            <el-empty v-else description="暂无数据"/>
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
.view-box {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
}

.main{
  flex: 1;
  overflow-y: visible;
}


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

.search-box {
  display: flex;
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
