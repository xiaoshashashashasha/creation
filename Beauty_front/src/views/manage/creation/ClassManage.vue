<script setup>
import { ref, onMounted } from "vue"
import { ElMessage } from "element-plus"
import { creationClassList, creationClassAdd, creationClassDel } from "@/api/creation"

// 表格数据
const tableData = ref([])
const classNameInput = ref('')
const pageNum = ref(1)
const pageSize = ref(5)
const total = ref(0)

// 获取标签列表
const fetchClassList = async () => {
  try {
    const result = await creationClassList({
      pageNum: pageNum.value,
      pageSize: pageSize.value
    })
    tableData.value = result.data.items
    total.value = result.data.total
  } catch (err) {
    ElMessage.error(err.msg || "分类获取失败")
  }
}

// 添加标签
const handleAddClass = async () => {
  if (!classNameInput.value.trim()) {
    ElMessage.warning("分类名不能为空")
    return
  }

  try {
    await creationClassAdd({ class_name: classNameInput.value.trim() })
    ElMessage.success("分类添加成功")
    classNameInput.value = ''
    pageNum.value = 1 // 回到第一页
    fetchClassList()
  } catch (err) {
    ElMessage.error(err.msg || "添加失败")
  }
}

// 删除标签
const handleDelete = async (row) => {
  try {
    await creationClassDel(row.class_id)
    ElMessage.success("删除成功")
    fetchClassList()
  } catch (err) {
    ElMessage.error(err.msg || "删除失败")
  }
}

// 页码变更
const handlePageChange = (newPage) => {
  pageNum.value = newPage
  fetchClassList()
}

// 初始化
onMounted(() => {
  fetchClassList()
})
</script>

<template>
  <el-row :gutter="0">
    <!--路径部分-->
    <div class="path">
      <p style="font-size: 20px; margin-top: 5px">分类管理</p>
    </div>

    <!-- 搜索栏 -->
    <div class="search">

    </div>
    <!-- 表格区域 -->
    <el-col :span="14">
      <el-table :data="tableData" style="width: 100%">
        <el-table-column prop="class_id" label="标签ID" width="180px"/>
        <el-table-column prop="class_name" label="标签名" width="280px"/>
        <el-table-column prop="created_at" label="创建时间" width="320px"/>
        <el-table-column label="操作" width="180px">
          <template #default="{ row }">
            <el-button type="danger" size="small" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
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
    </el-col>

    <!-- 添加分类区域 -->
    <el-col :span="10">
      <div class="add-box">
        <h3>添加分类</h3>
        <el-form @submit.prevent>
          <el-form-item>
            <el-input v-model="classNameInput" placeholder="请输入分类名"/>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" class="btn" @click="handleAddClass">添加</el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-col>
  </el-row>
</template>

<style scoped>

.path {
  height: 40px;
  width: 100%;
  background-color: #d8a6a6;
}

.search {
  width: 100%;
  height: 52px;
  background-color: #bbd2e3;
}

.add-box {
  background-color: #f9f9f9;
  padding: 30px 20px;
  border: 1px solid #ddd;
  border-radius: 6px;
  height: 900px;
  position: relative;
}

h3 {
  text-align: center;
  margin-bottom: 20px;
  font-weight: normal;
}

.btn{
  position: absolute;
  left: 46%;
  margin-top: 40px;
}
</style>
