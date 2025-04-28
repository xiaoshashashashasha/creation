<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import CardCreation from '@/components/part/CardCreation.vue'
import { creationList, creationClassList, creationTagList } from '@/api/creation'
import { ElMessage } from 'element-plus'

const router = useRouter()

const creations = ref([])
const total = ref(0)
const pageNum = ref(1)
const pageSize = ref(15)

const searchTitle = ref('')
const selectedSearchClassId = ref(null)
const selectedSearchTagId = ref(null)

const SearchClassOptions = ref([])
const SearchTagOptions = ref([])

const loading = ref(false)

// 获取数据
const getCreations = async () => {
  loading.value = true
  try {
    const res = await creationList({
      pageNum: pageNum.value,
      pageSize: pageSize.value,
      title: searchTitle.value,
      class_id: selectedSearchClassId.value,
      tag_id: selectedSearchTagId.value
    })
    creations.value = res.data.items
    total.value = res.data.total
  } catch (err) {
    ElMessage.error('数据加载失败')
  }
  loading.value = false
}

// 获取分类和标签
const fetchSearchOptions = async () => {
  try {
    const [classRes, tagRes] = await Promise.all([
      creationClassList({pageNum: 1, pageSize: 100}),
      creationTagList({pageNum: 1, pageSize: 100})
    ])
    SearchClassOptions.value = classRes.data.items || []
    SearchTagOptions.value = tagRes.data.items || []
  } catch (err) {
    ElMessage.error('分类或标签加载失败')
  }
}

onMounted(() => {
  fetchSearchOptions()
  getCreations()
})

const handleSearch = () => {
  pageNum.value = 1
  getCreations()
}

const handleReset = () => {
  searchTitle.value = ''
  selectedSearchClassId.value = null
  selectedSearchTagId.value = null
  pageNum.value = 1
  getCreations()
}

const handleCurrentChange = (val) => {
  pageNum.value = val
  getCreations()
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
              <el-button @click="backToHome" type="primary" icon="el-icon-arrow-left" plain>返回首页</el-button>
              <div class="search-box">
                <el-input v-model="searchTitle" placeholder="请输入标题" style="width: 200px; margin-right: 10px" clearable/>
                <el-select v-model="selectedSearchClassId" placeholder="请选择分类" style="width: 150px; margin-right: 10px" clearable>
                  <el-option v-for="cls in SearchClassOptions" :key="cls.class_id" :label="cls.class_name" :value="cls.class_id"/>
                </el-select>
                <el-select v-model="selectedSearchTagId" placeholder="请选择标签" style="width: 150px; margin-right: 10px" clearable>
                  <el-option v-for="tag in SearchTagOptions" :key="tag.tag_id" :label="tag.tag_name" :value="tag.tag_id"/>
                </el-select>
                <el-button type="primary" @click="handleSearch">搜索</el-button>
                <el-button @click="handleReset">重置</el-button>
              </div>
            </div>
          </template>

          <!-- skeleton -->
          <div v-if="loading" class="card-grid">
            <el-skeleton v-for="n in pageSize" :key="n" style="width: 280px; height: 240px;" animated/>
          </div>

          <!-- 正常数据 -->
          <div v-else>
            <div v-if="creations.length > 0" class="card-grid">
              <CardCreation v-for="item in creations" :key="item.id" :data="item" :loading="false"/>
            </div>
            <el-empty v-else description="暂无数据" />
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
