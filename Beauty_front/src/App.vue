<template>
  <div class="app">
    <el-container class="view-box">

      <!-- 顶部导航栏 -->
      <el-header class="header">
        <el-affix>
          <beauty_top/>
        </el-affix>
      </el-header>

      <el-container class="body-container">
        <!-- 主体内容 -->
        <el-main class="main">
          <router-view/>
        </el-main>

      </el-container>

      <!-- 右侧侧边栏 -->
      <Mine_Side/>
    </el-container>
  </div>
</template>

<script setup>
import Beauty_top from "@/components/beauty_top.vue";
import Mine_Side from "@/components/Mine_side.vue";  // 刚刚生成的侧边栏组件
</script>

<style>
* {
  margin: 0;
  padding: 0;
}

html, body, #app {
  height: 100%;
}

.app {
  height: 100%;
}

.view-box {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
}

.header {
  flex-shrink: 0;
  height: 60px;
  width: 100%;
  padding: 0;
}

/* 主体内容 + 右侧侧边栏 */
.body-container {
  display: flex;
  flex: 1;
  width: 100%;
  height: calc(100% - 60px); /* 减去 header 高度 */
}

.main {
  flex: 1;
  background: #ededed;
  margin: 0;
  padding: 0;
  overflow-y: auto;

  scrollbar-width: none; /* Firefox */
  -ms-overflow-style: none; /* IE 和 Edge */
}

.main::-webkit-scrollbar {
  display: none; /* Chrome Safari */
}

</style>
