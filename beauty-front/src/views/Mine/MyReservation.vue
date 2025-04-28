<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { myReservationList, evaluate, deleteReservation } from '@/api/reservation'
import RichTextEditor from '@/components/part/RichTextEditor.vue'

const router = useRouter()
const tableData = ref([])
const pageNum = ref(1)
const pageSize = ref(15)
const total = ref(0)
const status = ref(0)

const statusMap = {
  0: '尚未履约',
  1: '未评价',
  2: '爽约',
  3: '已评价'
}

// 评价弹窗相关
const evaluateDialogVisible = ref(false)
const currentReservationId = ref(null)
const currentPoint = ref(5)
const currentComment = ref('')

const fetchReservations = async () => {
  try {
    const res = await myReservationList({ pageNum: pageNum.value, pageSize: pageSize.value, status: status.value })
    tableData.value = res.data.items || []
    total.value = res.data.total || 0
  } catch (err) {
    ElMessage.error(err.msg || '获取预约信息失败')
  }
}

const handlePageChange = (newPage) => {
  pageNum.value = newPage
  fetchReservations()
}

const handleStatusChange = (newStatus) => {
  status.value = newStatus
  pageNum.value = 1
  fetchReservations()
}

const openEvaluateDialog = (reservation) => {
  currentReservationId.value = reservation.reservation_id
  currentPoint.value = 5
  currentComment.value = ''
  evaluateDialogVisible.value = true
}

const submitEvaluation = async () => {
  if (!currentComment.value.trim()) {
    return ElMessage.warning('请填写评价内容')
  }
  try {
    await evaluate(currentReservationId.value, currentPoint.value, currentComment.value)
    ElMessage.success('评价成功！')
    evaluateDialogVisible.value = false
    fetchReservations()
  } catch (err) {
    ElMessage.error(err.msg || '评价失败')
  }
}

const cancelReservation = (reservation_id) => {
  ElMessageBox.confirm(
      '确定要取消此预约吗？取消后无法恢复哦~',
      '提示',
      {
        confirmButtonText: '确认取消',
        cancelButtonText: '我再想想',
        type: 'warning'
      }
  ).then(async () => {
    try {
      await deleteReservation(reservation_id)
      ElMessage.success('取消成功')
      fetchReservations()
    } catch (err) {
      ElMessage.error(err.msg || '取消失败')
    }
  }).catch(() => {})
}

onMounted(() => {
  fetchReservations()
})
</script>



<template>
  <div class="main-content">
    <el-button class="back-btn" type="primary" plain @click="() => router.push('/')">
      返回首页
    </el-button>

    <div class="section-title">我的预约</div>

    <div style="margin-bottom: 20px;">
      <el-select v-model="status" placeholder="请选择预约状态" @change="handleStatusChange" style="width: 200px;">
        <el-option :value="0" label="尚未履约" />
        <el-option :value="1" label="未评价" />
        <el-option :value="2" label="爽约" />
        <el-option :value="3" label="已评价" />
      </el-select>
    </div>

    <el-table :data="tableData" style="width: 100%;">
      <el-table-column prop="reservation_id" label="预约ID" width="100" />
      <el-table-column prop="offline_name" label="门店名称" width="160" />
      <el-table-column prop="member_name" label="受理托尼" width="140" />
      <el-table-column prop="start_at" label="预约开始时间" width="180" />
      <el-table-column prop="end_at" label="预约结束时间" width="180" />
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
      <el-table-column label="操作" width="200">
        <template #default="{ row }">
          <template v-if="row.status === 0">
            <el-button size="small" type="danger" @click="cancelReservation(row.reservation_id)">取消预约</el-button>
          </template>
          <template v-else-if="row.status === 1">
            <el-button size="small" type="primary" @click="openEvaluateDialog(row)">去评价</el-button>
          </template>
          <template v-else>
            <span style="color: #999;">无可操作项</span>
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

    <!-- 评价弹窗 -->
    <el-dialog
        v-model="evaluateDialogVisible"
        title="评价门店服务"
        width="1600px"
        destroy-on-close
    >
      <div style="margin-bottom: 20px;">
        <label>评分：</label>
        <el-rate v-model="currentPoint" :max="5" />
      </div>

      <div>
        <label>评论内容：</label>
        <RichTextEditor v-model="currentComment" :visible="evaluateDialogVisible" />
      </div>

      <template #footer>
        <el-button @click="evaluateDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitEvaluation">确认评价</el-button>
      </template>
    </el-dialog>
  </div>
</template>



<style scoped>
.main-content {
  flex: 1;
  width: 1720px;
  height: 765px;
  margin: 0 auto;
  margin-bottom: 10px;
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
