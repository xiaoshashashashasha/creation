<script setup>

import {ref} from "vue";


const tableData = ref([
  {
    user_id: 1,
    username: 'admin1',
    nickname: 'A',
    email: 'admin1@gmail.com',
    created_at: new Date(),
    updated_at: new Date(),
    role: 0
  },
  {
    user_id: 2,
    username: 'admin2',
    nickname: 'B',
    created_at: new Date(),
    updated_at: new Date(),
    role: 1
  },
  {
    user_id: 3,
    username: 'admin4',
    nickname: 'D',
    created_at: new Date(),
    updated_at: new Date(),
    role: 2
  }
])


function setRoleban(row) {
  row.role = 2
  row.updated_at = new Date()

}

function setRoleuser(row) {
  row.role = 0
  row.updated_at = new Date()

}

function setRolemanager(row) {
  row.role = 1
  row.updated_at = new Date()

}

import {userListService} from "@/api/user";

const userList = async ()=>{
  const result = await userListService({
    pageNum: 1,
    pageSize: 10
  });
  tableData.value = result.data.items;
}
userList();
</script>

<template>
  <el-row>
    <!--路径部分-->
    <div class="path">
      <p style="font-size: 20px; margin-top: 5px">更改权限</p>
    </div>
    <!--搜索栏部分-->
    <div class="search">

    </div>
    <!--表格部分-->
    <el-table :data="tableData" style="width: 100%">
      <el-table-column prop="user_id" label="用户id" width="120px"/>
      <el-table-column prop="username" label="用户名" width="240px"/>
      <el-table-column prop="nickname" label="昵称" width="160px"/>
      <el-table-column prop="email" label="邮箱" width="200px"/>
      <el-table-column prop="created_at" label="created_at" width="240px"/>
      <el-table-column prop="updated_at" label="updated_at" width="240px"/>
      <el-table-column prop="role" label="role" width="80px"/>
      <el-table-column label="操作">
      <template #default="scope">
        <el-button @click="setRoleban(scope.row)">
          封禁
        </el-button>
        <el-button @click="setRoleuser(scope.row)">
          设为用户
        </el-button>
        <el-button @click="setRolemanager(scope.row)">
          设为管理员
        </el-button>
      </template>
      </el-table-column>
    </el-table>
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
  height: 60px;
  background-color: #bbd2e3;
}
</style>