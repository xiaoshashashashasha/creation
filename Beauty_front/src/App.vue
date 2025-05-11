<template>
  <body>
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
  </body>
</template>

<script setup>
import Beauty_top from "@/components/beauty_top.vue";
import Mine_Side from "@/components/Mine_side.vue";
</script>

<style>
* {
  margin: 0;
  padding: 0;
}

html, body, #app {
  height: 100%;
}

body {
  background-image: url('/public/B0006674.png');
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  height: 100vh;
  margin: 0;
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

.body-container {
  display: flex;
  flex: 1;
  width: 100%;
  height: calc(100% - 60px);
}

.main {
  flex: 1;
  background-color: transparent;
  margin: 0;
  padding: 0;
  overflow-y: hidden;

  scrollbar-width: none;
  -ms-overflow-style: none;
}

.main::-webkit-scrollbar {
  display: none;
}

</style>
