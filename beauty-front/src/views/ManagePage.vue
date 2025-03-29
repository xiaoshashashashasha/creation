<script setup>
import Beauty_top from "@/components/beauty_top.vue";
import {
  Document,
  EditPen,
  UserFilled,
  Shop
} from '@element-plus/icons-vue'
import ChangeRole from "@/views/manage/user/ChangeRole.vue";


</script>

<template>
  <el-container class="view-box">
    <el-header class="header">
      <el-affix>
        <beauty_top/>
      </el-affix>
    </el-header>

    <el-container>
      <!--侧边栏-->
      <el-aside width="180px">
        <div style="height: 1000px; background: #fff;">
          <el-menu
              default-active="2"
              class="el-menu-vertical-demo"
              @open="handleOpen"
              @close="handleClose"
          >
            <el-sub-menu index="1">
              <template #title>
                <el-icon>
                  <UserFilled/>
                </el-icon>
                <span>用户组</span>
              </template>
              <el-menu-item index="1-1">更改权限</el-menu-item>
            </el-sub-menu>

            <el-sub-menu index="2">
              <template #title>
                <el-icon>
                  <EditPen/>
                </el-icon>
                <span>内容组</span>
              </template>
              <el-menu-item index="2-1">标签管理</el-menu-item>
              <el-menu-item index="2-2">分类管理</el-menu-item>
              <el-menu-item index="2-3">内容管理</el-menu-item>
              <el-menu-item index="2-4">内容审核</el-menu-item>
            </el-sub-menu>

            <el-sub-menu index="3">
              <template #title>
                <el-icon>
                  <document/>
                </el-icon>
                <span>发型组</span>
              </template>
              <el-menu-item index="3-1">发型管理</el-menu-item>
            </el-sub-menu>

            <el-sub-menu index="4">
              <template #title>
                <el-icon>
                  <Shop/>
                </el-icon>
                <span>线下门店组</span>
              </template>
              <el-menu-item index="4-1">门店审核</el-menu-item>
            </el-sub-menu>
          </el-menu>
        </div>
      </el-aside>
      <!--主要内容-->
      <el-main class="main">

        <ChangeRole/>

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