<script setup>
import { ref } from "vue"
import { userListService, userChangeRoleService } from "@/api/user"
import {ElMessage} from "element-plus";

// 表格数据
const tableData = ref([])

// 分页数据
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)

// 搜索条件
const searchValue = ref('')
const searchType = ref('user_id')

const changeRole = async (user_id,role) => {
  try {
    await userChangeRoleService(user_id,role)
    ElMessage.success('权限更改成功')

    getUserList()
  } catch (error) {
    console.log(error)
    ElMessage.error(error.message)
  }
}

// 操作按钮
function setRoleban(row) {
  changeRole(row.user_id,2)
}
function setRoleuser(row) {
  changeRole(row.user_id,0)
}
function setRolemanager(row) {
  changeRole(row.user_id,1)
}



// 获取用户列表
const getUserList = async () => {
  const params = {
    pageNum: pageNum.value,
    pageSize: pageSize.value,
  }

  if (searchType.value === 'user_id' && searchValue.value) {
    params.user_id = parseInt(searchValue.value)
  } else if (searchType.value === 'username' && searchValue.value) {
    params.keyWord = searchValue.value
  }

  try {
    const result = await userListService(params)
    tableData.value = result.data.items
    total.value = result.data.total
  } catch (err) {
    console.error('获取用户列表失败:', err)
  }
}

// 搜索按钮
const handleSearch = () => {
  pageNum.value = 1
  getUserList()
}

// 重置按钮
const handleReset = () => {
  searchValue.value = ''
  searchType.value = 'user_id'
  pageNum.value = 1
  getUserList()
}

// 分页变化
const handlePageChange = (newPage) => {
  pageNum.value = newPage
  getUserList()
}

// 初始化加载
getUserList()
</script>

<template>
  <el-row>
    <!--路径部分-->
    <div class="path">
      <p style="font-size: 20px; margin-top: 5px">更改权限</p>
    </div>

    <!-- 搜索栏 -->
    <div class="search">
      <el-row :gutter="10" align="middle" style="padding: 10px;">
        <el-col :span="4">
          <el-select v-model="searchType" placeholder="搜索类型">
            <el-option label="用户ID" value="user_id"/>
            <el-option label="用户名" value="username"/>
          </el-select>
        </el-col>
        <el-col :span="6">
          <el-input v-model="searchValue" placeholder="请输入搜索内容"/>
        </el-col>
        <el-col :span="4">
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-col>
      </el-row>
    </div>

    <!-- 表格部分 -->
    <el-table :data="tableData" style="width: 100%">
      <el-table-column prop="user_id" label="用户ID" width="120px"/>
      <el-table-column prop="username" label="用户名" width="240px"/>
      <el-table-column prop="nickname" label="昵称" width="160px"/>
      <el-table-column prop="email" label="邮箱" width="200px"/>
      <el-table-column prop="created_at" label="创建时间" width="240px"/>
      <el-table-column prop="updated_at" label="更新时间" width="240px"/>
      <el-table-column label="角色" width="120px">
        <template #default="{ row }">
          <el-tag
              :type="row.role === 1 ? 'success' : row.role === 2 ? 'danger' : 'info'"
              disable-transitions
          >
            {{ row.role === 0 ? '正常用户' : row.role === 1 ? '管理员' : '封禁中' }}
          </el-tag>
        </template>
      </el-table-column>

      <el-table-column label="操作">
        <template #default="scope">
          <el-button @click="setRoleban(scope.row)">封禁</el-button>
          <el-button @click="setRoleuser(scope.row)">设为用户</el-button>
          <el-button @click="setRolemanager(scope.row)">设为管理员</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <div style="text-align: center; margin-top: 20px; width: 100%">
      <el-pagination
          background
          layout="prev, pager, next, jumper, ->, total"
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
.search {
  width: 100%;
  background-color: #bbd2e3;
  height: 52px;
}
</style>
