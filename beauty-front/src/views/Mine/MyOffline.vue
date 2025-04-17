<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { myOfflineList } from '@/api/offline'

const router = useRouter()
const tableData = ref([])
const pageNum = ref(1)
const pageSize = ref(15)
const total = ref(0)

const fetchOfflineStores = async () => {
  try {
    const res = await myOfflineList({ pageNum: pageNum.value, pageSize: pageSize.value })
    tableData.value = res.data.items || []
    total.value = res.data.total || 0
  } catch (err) {
    ElMessage.error(err.msg || '获取门店信息失败')
  }
}

const handlePageChange = (newPage) => {
  pageNum.value = newPage
  fetchOfflineStores()
}

// 操作按钮
const viewDetails = (store) => {
  router.push(`/content/offline/${store.offline_id}`)
}

const editStore = (store) => {
  router.push(`/offline/edit/${store.offline_id}`)
}

const manageMembers = (store) => {
  router.push(`/offline/${store.offline_id}/members`)
}

const cancelStore = (store) => {
  ElMessage.success(`门店 ${store.offline_name} 注销成功（模拟）`)
  // 你可以改为调用注销接口
}

onMounted(() => {
  fetchOfflineStores()
})
</script>

<template>
  <div class="main-content">
    <el-button
        class="back-btn"
        type="primary"
        icon="el-icon-arrow-left"
        plain
        @click="() => router.push('/')"
    >
      返回首页
    </el-button>

    <div class="section-title">我的门店</div>

    <el-table :data="tableData" style="width: 100%; margin-top: 10px">
      <el-table-column prop="offline_id" label="门店ID" width="100" />
      <el-table-column prop="offline_name" label="门店名称" width="180" />
      <el-table-column prop="offline_position" label="门店地址" width="360" />
      <el-table-column prop="offline_city" label="所在城市" width="160" />
      <el-table-column prop="offline_phone" label="联系电话" width="240" />
      <el-table-column label="操作" width="400">
        <template #default="{ row }">
          <el-button size="small" @click="viewDetails(row)">查看详情</el-button>
          <el-button size="small" type="primary" @click="editStore(row)">编辑</el-button>
          <el-button size="small" type="warning" @click="manageMembers(row)">成员管理</el-button>
          <el-button size="small" type="danger" @click="cancelStore(row)">注销</el-button>
        </template>
      </el-table-column>
    </el-table>

    <div style="text-align: center; margin-top: 20px">
      <el-pagination
          background
          layout="prev, pager, next, ->, total"
          :total="total"
          :current-page="pageNum"
          :page-size="pageSize"
          @current-change="handlePageChange"
      />
    </div>
  </div>
</template>

<style scoped>
.main-content {
  flex: 1;
  width: 1720px;
  margin: 0 auto;
  background: #f5f7fa;
  padding: 20px;
}

.section-title {
  font-size: 20px;
  font-weight: bold;
  background-color: #eef1f6;
  padding: 10px 15px;
  border-left: 5px solid #409EFF;
  margin-bottom: 10px;
}

.back-btn {
  margin-bottom: 20px;
}
</style>
