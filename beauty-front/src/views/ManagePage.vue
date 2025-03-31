<script setup>
import {
  Document,
  EditPen,
  UserFilled,
  Shop
} from '@element-plus/icons-vue'

import { ref } from 'vue'

// 默认展开的菜单项 key（保持多个）
const openMenus = ref([])

// 展开时添加 key
const handleOpen = (key) => {
  if (!openMenus.value.includes(key)) {
    openMenus.value.push(key)
  }
}

// 折叠时移除 key
const handleClose = (key) => {
  openMenus.value = openMenus.value.filter(k => k !== key)
}
</script>

<template>
  <el-container class="view-box">
    <el-container>
      <!-- 侧边栏 -->
      <el-aside width="180px">
        <div style="height: 1000px; background: #fff;">
          <el-menu
              :default-openeds="openMenus"
              class="el-menu-vertical-demo"
              router
              unique-opened="false"
              @open="handleOpen"
              @close="handleClose"
          >
            <el-sub-menu index="user">
              <template #title>
                <el-icon><UserFilled/></el-icon>
                <span>用户组</span>
              </template>
              <el-menu-item index="/user/change-role">更改权限</el-menu-item>
            </el-sub-menu>

            <el-sub-menu index="creation">
              <template #title>
                <el-icon><EditPen/></el-icon>
                <span>内容组</span>
              </template>
              <el-menu-item index="/creation/tag-manage">标签管理</el-menu-item>
              <el-menu-item index="/creation/class-manage">分类管理</el-menu-item>
              <el-menu-item index="/creation/creation-manage">内容管理</el-menu-item>
              <el-menu-item index="/creation/creation-examine">内容审核</el-menu-item>
            </el-sub-menu>

            <el-sub-menu index="hairstyle">
              <template #title>
                <el-icon><Document/></el-icon>
                <span>发型组</span>
              </template>
              <el-menu-item index="/hairstyle/hairstyle-manage">发型管理</el-menu-item>
            </el-sub-menu>

            <el-sub-menu index="offline">
              <template #title>
                <el-icon><Shop/></el-icon>
                <span>线下门店组</span>
              </template>
              <el-menu-item index="/offline/request-manage">门店审核</el-menu-item>
            </el-sub-menu>
          </el-menu>
        </div>
      </el-aside>

      <!-- 主要内容 -->
      <el-main class="main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>


<style scoped>
* {
  margin: 0;
  padding: 0;
}

html, body {
  height: 100%;
}

.main {
  flex: 1;
  display: flex;
  flex-direction: column;
  background: #ededed;
}


.view-box {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  background: #2481a8;
  overflow-y: hidden;
}

.header {
  flex-shrink: 0;
  height: 60px;
}


</style>