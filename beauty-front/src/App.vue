<template>
  <div class="app">
    <el-container class="view-box">

      <el-header class="header">
        <el-affix>
          <beauty_top/>
        </el-affix>
      </el-header>

      <el-main class="main">
        <router-view/>
      </el-main>
    </el-container>
  </div>


</template>

<script setup>
import Beauty_top from "@/components/beauty_top.vue";


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

.main {
  flex: 1;
  background: #ededed;
  margin: 0;
  padding: 0;
}
</style>

