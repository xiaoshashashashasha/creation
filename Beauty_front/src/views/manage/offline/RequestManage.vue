<script setup>
import { ref, reactive, onMounted } from "vue"
import { ElMessage } from "element-plus"
import { offlineRequestListExa, offlineExamine} from "@/api/offline"
import { userListService} from "@/api/user"

const tableData = ref([])
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)
const reasonMap = reactive({})
const userInfoMap = reactive({})

// 获取店长的昵称与邮箱
const fetchManagerInfo = async (managerId) => {
  if (userInfoMap[managerId]) return

  try {
    const res = await userListService({ pageSize:1,pageNum:1, user_id: managerId })
    const user = res.data.items?.[0] || {}
    userInfoMap[managerId] = {
      nickname: user.nickname || "-",
      email: user.email || "-"
    }
  } catch (err) {
    userInfoMap[managerId] = {
      nickname: "-",
      email: "-"
    }
    console.warn("获取用户失败", err)
  }
}

// 加载审核列表
const fetchData = async () => {
  try {
    const res = await offlineRequestListExa({
      pageNum: pageNum.value,
      pageSize: pageSize.value
    })
    tableData.value = res.data.items || []
    total.value = res.data.total || 0

    // 加载对应的用户信息
    for (const item of tableData.value) {
      await fetchManagerInfo(item.manager_id)
    }
  } catch (err) {
    ElMessage.error(err.msg || "获取审核列表失败")
  }
}

const handlePageChange = (newPage) => {
  pageNum.value = newPage
  fetchData()
}

// 审核操作
const handleExamine = async (row, pass) => {
  const reason = reasonMap[row.request_id] || ""
  if (!pass && !reason.trim()) {
    return ElMessage.warning("请填写否决原因")
  }
  try {
    await offlineExamine(row.request_id, pass ? 0 : 1, reason)
    ElMessage.success(pass ? "已通过" : "已否决")
    fetchData()
  } catch (err) {
    ElMessage.error(err.msg || "操作失败")
  }
}

onMounted(() => {
  fetchData()
})
</script>


<template>
  <el-row :gutter="0">
    <div class="path">
      <p style="font-size: 20px; margin-top: 5px">门店审核</p>
    </div>

    <div class="search">

    </div>

    <el-table :data="tableData" style="width: 100%; margin-top: 10px">
      <el-table-column prop="request_id" label="申请ID" width="120" />
      <el-table-column prop="manager_id" label="店长ID" width="120" />
      <el-table-column label="店长昵称" width="160">
        <template #default="{ row }">
          {{ userInfoMap[row.manager_id]?.nickname || '-' }}
        </template>
      </el-table-column>
      <el-table-column label="店长邮箱" width="240">
        <template #default="{ row }">
          {{ userInfoMap[row.manager_id]?.email || '-' }}
        </template>
      </el-table-column>
      <el-table-column prop="target_city" label="目标城市" width="160" />
      <el-table-column label="否决原因" width="260">
        <template #default="{ row }">
          <el-input
              v-model="reasonMap[row.request_id]"
              placeholder="填写否决原因"
              size="small"
          />
        </template>
      </el-table-column>
      <el-table-column label="操作" width="220">
        <template #default="{ row }">
          <el-button type="success" size="small" @click="handleExamine(row, true)">通过</el-button>
          <el-button type="danger" size="small" @click="handleExamine(row, false)">否决</el-button>
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
  </el-row>
</template>


<style scoped>
.path {
  height: 40px;
  width: 100%;
  background-color: #d8a6a6;
}

::v-deep(.el-table__row) {
  height: 60px;
}

.search {
  width: 100%;
  height: 52px;
  background-color: #bbd2e3;
}
</style>
