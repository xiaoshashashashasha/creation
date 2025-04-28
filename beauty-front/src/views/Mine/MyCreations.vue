<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import CardCreation from '@/components/part/CardCreation.vue'
import { myCreationList } from '@/api/creation'
import { ElMessage } from 'element-plus'

const router = useRouter()

const creations = ref([])
const total = ref(0)
const pageNum = ref(1)
const pageSize = ref(15)
const examine = ref(0)
const loading = ref(false)

const getMyCreations = async () => {
  loading.value = true
  try {
    const res = await myCreationList({
      pageNum: pageNum.value,
      pageSize: pageSize.value,
      examine: examine.value
    })
    creations.value = res.data.items
    total.value = res.data.total
  } catch (err) {
    ElMessage.error('我的内容加载失败')
  }
  loading.value = false
}

const switchExamine = (val) => {
  if (examine.value !== val) {
    examine.value = val
    pageNum.value = 1
    getMyCreations()
  }
}


const handleCurrentChange = (val) => {
  pageNum.value = val
  getMyCreations()
}

const backToHome = () => {
  router.push('/')
}

onMounted(() => {
  getMyCreations()
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
                <el-button @click="backToHome" type="primary" icon="el-icon-arrow-left" plain>返回首页</el-button>

                <!-- 状态切换按钮组 -->
                <el-button-group style="margin-left: 20px;">
                  <el-button
                      :type="examine === 0 ? 'primary' : 'default'"
                      @click="switchExamine(0)"
                  >已通过</el-button>
                  <el-button
                      :type="examine === 1 ? 'primary' : 'default'"
                      @click="switchExamine(1)"
                  >被打回</el-button>
                  <el-button
                      :type="examine === 2 ? 'primary' : 'default'"
                      @click="switchExamine(2)"
                  >待审核</el-button>
                </el-button-group>
              </div>
              <h2 style="margin: 0;">我的内容</h2>
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
