<script setup>
import {onMounted, ref} from "vue"
import {ElMessage} from "element-plus"
import {offlineRequestList, addRequest} from "@/api/offline"
import { useRouter } from 'vue-router'
import {ArrowLeft} from "@element-plus/icons-vue";




const tableData = ref([])
const pageNum = ref(1)
const pageSize = ref(15)
const total = ref(0)

const router = useRouter()

const backToHome = () => {
  router.push('/')
}
// 新申请表单
const newRequest = ref({
  target_city: ""
})

const fetchMyRequests = async () => {
  try {
    const res = await offlineRequestList({
      pageNum: pageNum.value,
      pageSize: pageSize.value
    })
    tableData.value = res.data.items || []
    total.value = res.data.total || 0
  } catch (err) {
    ElMessage.error(err.msg || "获取申请列表失败")
  }
}

const handlePageChange = (newPage) => {
  pageNum.value = newPage
  fetchMyRequests()
}

const submitRequest = async () => {
  if (!newRequest.value.target_city.trim()) {
    return ElMessage.warning("请填写目标城市")
  }
  try {
    console.log(newRequest.value.target_city)
    await addRequest(newRequest.value)
    ElMessage.success("申请已提交")
    newRequest.value.target_city = ""
    fetchMyRequests()
  } catch (err) {
    ElMessage.error(err.msg || "提交失败")
  }
}

onMounted(() => {
  fetchMyRequests()
})
</script>

<template>
  <div class="main-content">
    <!-- 返回首页按钮 -->
    <el-button
        class="back-btn"
        type="primary"
        :icon="ArrowLeft"
        plain
        @click="backToHome"
    >
      返回首页
    </el-button>
    <el-row :gutter="20" style="padding: 20px">
      <!-- 左侧申请列表 -->
      <el-col :span="15">
        <div class="section-title">我的门店申请记录</div>
        <el-table :data="tableData" style="width: 100%; margin-top: 10px">
          <el-table-column prop="request_id" label="申请ID" width="100"/>
          <el-table-column prop="target_city" label="目标城市" width="180"/>
          <el-table-column prop="comment" label="否决原因"/>
          <el-table-column prop="examine" label="状态" width="120">
            <template #default="{ row }">
              <el-tag
                  :type="row.examine === 0 ? 'success' : row.examine === 1 ? 'danger' : 'warning'"
              >
                {{
                  row.examine === 0
                      ? '已通过'
                      : row.examine === 1
                          ? '被打回'
                          : '审核中'
                }}
              </el-tag>
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

      </el-col>

      <!-- 右侧提交表单 -->
      <el-col :span="9">
        <div class="section-title">发起新申请</div>
        <el-form :model="newRequest" label-width="80px" style="margin-top: 20px">
          <el-form-item label="目标城市">
            <el-input v-model="newRequest.target_city" placeholder="请输入城市名称"/>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="submitRequest">提交申请</el-button>
          </el-form-item>
        </el-form>
      </el-col>
    </el-row>
  </div>
</template>

<style scoped>
.main-content {
  flex: 1;
  width: 1720px;
  margin: 0 auto;
  background: #e8e4e4;
}

.back-btn {
  margin: 10px 0 0 20px;
}

.section-title {
  font-size: 20px;
  font-weight: bold;
  background-color: #eef1f6;
  padding: 10px 15px;
  border-left: 5px solid #409EFF;
  margin-bottom: 10px;
}
</style>
