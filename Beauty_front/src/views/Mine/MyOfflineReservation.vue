<script setup>
import {ref, onMounted} from 'vue'
import {useRoute, useRouter} from 'vue-router'
import {ElMessage, ElMessageBox} from 'element-plus'
import {offlineReservationList, updateReservation} from '@/api/reservation'
import {ArrowLeft} from "@element-plus/icons-vue";

const route = useRoute()
const router = useRouter()

const pageNum = ref(1)
const pageSize = ref(15)
const total = ref(0)
const tableData = ref([])
const status = ref(0)  // 新增：默认0
const offlineId = route.params.offline_id
const loadingMap = ref({})

const statusOptions = [
  { label: '尚未履约', value: 0 },
  { label: '未评价', value: 1 },
  { label: '爽约', value: 2 },
  { label: '已评价', value: 3 }
]

const statusMap = {
  0: '尚未履约',
  1: '未评价',
  2: '爽约',
  3: '已评价'
}

const fetchReservations = async () => {
  try {
    const res = await offlineReservationList({
      pageNum: pageNum.value,
      pageSize: pageSize.value,
      offline_id: offlineId,
      status: status.value
    })
    tableData.value = res.data.items || []
    total.value = res.data.total || 0
  } catch (err) {
    ElMessage.error(err.msg || '获取预约列表失败')
  }
}

const handlePageChange = (newPage) => {
  pageNum.value = newPage
  fetchReservations()
}

// 新增：切换 status 时
const handleStatusChange = (val) => {
  pageNum.value = 1  // 每次切换回第一页
  fetchReservations()
}

const verifyReservation = (reservation) => {
  ElMessageBox.confirm(
      `确定核销预约ID「${reservation.reservation_id}」吗？`,
      '提示',
      {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'success'
      }
  ).then(async () => {
    loadingMap.value[reservation.reservation_id] = true
    try {
      await updateReservation(reservation.reservation_id, 1)
      ElMessage.success('核销成功')
      fetchReservations()
    } catch (err) {
      ElMessage.error(err.msg || '核销失败')
    } finally {
      loadingMap.value[reservation.reservation_id] = false
    }
  }).catch(() => {})
}

const timeoutReservation = (reservation) => {
  ElMessageBox.confirm(
      `确定将预约ID「${reservation.reservation_id}」标记为超时吗？`,
      '提示',
      {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning'
      }
  ).then(async () => {
    loadingMap.value[reservation.reservation_id] = true
    try {
      await updateReservation(reservation.reservation_id, 2)
      ElMessage.success('标记超时成功')
      fetchReservations()
    } catch (err) {
      ElMessage.error(err.msg || '操作失败')
    } finally {
      loadingMap.value[reservation.reservation_id] = false
    }
  }).catch(() => {})
}

onMounted(() => {
  fetchReservations()
})
</script>

<template>
  <div class="main-content">
    <el-button
        class="back-btn"
        type="primary"
        :icon="ArrowLeft"
        plain
        @click="() => router.back()"
    >
      返回
    </el-button>

    <div class="section-title">门店预约管理</div>

    <div style="margin-bottom: 20px; ">
      <el-select v-model="status" placeholder="请选择预约状态" @change="handleStatusChange" style="width: 200px;">
        <el-option
            v-for="item in statusOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
        />
      </el-select>
    </div>

    <el-table :data="tableData" style="width: 100%;">
      <el-table-column prop="reservation_id" label="预约ID" width="100"/>
      <el-table-column prop="user_id" label="顾客ID" width="100"/>
      <el-table-column prop="start_at" label="预约开始时间" width="200"/>
      <el-table-column prop="end_at" label="预约结束时间" width="200"/>
      <el-table-column prop="status" label="状态" width="120">
        <template #default="{ row }">
          <el-tag>{{ statusMap[row.status] }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="point" label="评分" width="100">
        <template #default="{ row }">
          <span v-if="row.comment">{{ row.point }}</span>
          <span v-else style="color: #999;">暂无评分</span>
        </template>
      </el-table-column>
      <el-table-column prop="comment" label="评论" width="520">
        <template #default="{ row }">
          <div v-if="row.comment" v-html="row.comment"></div>
          <span v-else style="color: #999;">暂无评论</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="220">
        <template #default="{ row }">
          <template v-if="row.status === 0">
            <el-button
                size="small"
                type="success"
                :loading="loadingMap[row.reservation_id]"
                @click="verifyReservation(row)"
            >核销</el-button>
            <el-button
                size="small"
                type="danger"
                :loading="loadingMap[row.reservation_id]"
                @click="timeoutReservation(row)"
            >超时</el-button>
          </template>
          <template v-else>
            <span style="color: #999;">无需操作</span>
          </template>
        </template>
      </el-table-column>
    </el-table>

    <div style="text-align: center; margin-top: 20px;">
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
  padding: 20px;
  background: #f5f7fa;
  min-height: 800px;
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
