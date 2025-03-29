<script setup>

import ManageSwitch from "@/components/ManageSwitch.vue";
</script>

<template>

  <div class="top-box">
    <p class="logo">Beauty</p>
    <el-button class="plus_btn">+</el-button>
    <manage-switch class="manage-switch"/>
    <el-button class="creation_btn">内 容</el-button>
    <el-button class="haristyle_btn">发 型</el-button>
    <el-button class="offline_btn">线下门店</el-button>
  </div>

</template>

<style scoped>
.top-box {
  position: relative;
  width: 100%;
  height: 60px;
  margin: 0;
  padding: 0;
  background: #beccd3;
}

.logo {
  font-size: 38px;
  font-style: normal;
  margin: 0;
  padding: 0;
  font-family: 'Segoe Script';
}

.plus_btn {
  position: absolute;
  left: 50%;
  transform: translateX(-65%);
  font-size: 60px;
  top: 5px;
  height: 50px;
  width: 50px;
  border-radius: 10px;
}

.creation_btn {
  position: absolute;
  font-size: 26px;
  top: 5px;
  right: 17%;
  width: 100px;
  height: 50px;
  border-radius: 10px;
}

.haristyle_btn {
  position: absolute;
  font-size: 26px;
  top: 5px;
  right: 10%;
  width: 100px;
  height: 50px;
  border-radius: 10px;
}

.offline_btn {
  position: absolute;
  font-size: 26px;
  top: 5px;
  right: 2%;
  width: 120px;
  height: 50px;
  border-radius: 10px;
}

.manage-switch {
  position: absolute;
  top: 10px;
  left: 20%;
}
</style>
