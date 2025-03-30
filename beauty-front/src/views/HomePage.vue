<script setup>
import CardCreation from "@/components/CardCreation.vue";
import CardOther from "@/components/CardOther.vue";

console.log(window.innerWidth)
</script>

<template>
  <el-container class="view-box">
    <el-main class="main">
        <div class="main-content">
          <el-carousel :interval="4000" type="card" class="cardpic" card-scale="0.7">
            <el-carousel-item v-for="item in 6" :key="item" style="width: 600px">
              <h3 text="2xl" justify="center">{{ item }}</h3>
            </el-carousel-item>
          </el-carousel>

          <el-card class="card">
            <template #header>
              <div class="card-header">
                <span>内容</span>
                <span>更多</span>
              </div>
            </template>
            <div style="margin-top: 20px; display: flex; justify-content: center;">
              <CardCreation class="card-creation"/>
              <CardCreation class="card-creation"/>
              <CardCreation class="card-creation"/>
              <CardCreation class="card-creation"/>
            </div>
          </el-card>

          <el-card class="card">
            <template #header>
              <div class="card-header">
                <span>发型</span>
                <span>更多</span>
              </div>
            </template>
            <div style="margin-top: 20px; display: flex; justify-content: center;">
              <CardOther class="card-creation"/>
              <CardOther class="card-creation"/>
              <CardOther class="card-creation"/>
              <CardOther class="card-creation"/>
            </div>
          </el-card>

          <el-card class="card">
            <template #header>
              <div class="card-header">
                <span>线下门店</span>
                <span>更多</span>
              </div>
            </template>
            <div style="margin-top: 20px; display: flex; justify-content: center;">
              <CardOther class="card-creation"/>
              <CardOther class="card-creation"/>
              <CardOther class="card-creation"/>
              <CardOther class="card-creation"/>
            </div>
          </el-card>

          <el-card class="card">
            <div style="margin-top: 20px; display: flex; height: 120px">
              <p style="font-size: 20px; font-family: 'Segoe Script'; margin-top: 100px;">Beayty</p>
              <p style="font-size: 20px; margin-left: 5px; margin-top: 104px;">Created by Xiaoshashashashasha</p>
            </div>
          </el-card>
        </div>


    </el-main>
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

.main-content {
  flex: 1;
  display: block;
  width: 1720px;
  margin: 0 auto;
  padding-top: 20px;
  background: #fff;
}

.view-box {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
}

.main {
  flex: 1;
  background: #ededed;
  overflow-y: visible;
}
.cardpic{
  max-width: 1200px;
  margin: 0 auto;
  margin-bottom: 20px;
  height: 300px;
}
.el-carousel__item h3 {
  color: #475669;
  opacity: 0.75;
  line-height: 300px;
  margin: 0;
  text-align: center;
}

.el-carousel__item{
  background-color: #99a9bf;
}

.card{
  margin-top: 5px;
  margin-bottom: 5px;
}
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 18px;
  font-weight: bold;
}

.card-creation{
  margin: 0 50px;
}
</style>