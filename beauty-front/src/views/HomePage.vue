<script setup>
import CardCreation from "@/components/CardCreation.vue";
import {onMounted, ref} from "vue";
import CardHairstyle from "@/components/CardHairstyle.vue";
import CardOffline from "@/components/CardOffline.vue";
import {ElMessage} from "element-plus";
import {creationInfo} from "@/api/creation";
import router from "@/router";


/**
 * 内容卡片
 * **/
const creationIds = ref([3, 4, 5, 6])

const loadingStates1  = ref({
  3: true,
  4: true,
  5: true,
  6: true
});
const handleLoadingUpdate1 = (id, newLoadingState) => {
  loadingStates1.value[id] = newLoadingState
}

/**
 * 发型卡片
 * **/
const hairstyleIds = ref([7, 8, 9, 10])

const loadingStates2  = ref({
  7: true,
  8: true,
  9: true,
  10: true
});
const handleLoadingUpdate2 = (id, newLoadingState) => {
  loadingStates2.value[id] = newLoadingState
}

/**
 * 门店卡片
 * **/
const offlineIds = ref([4, 5, 6, 7])

const loadingStates3  = ref({
  4: true,
  5: true,
  6: true,
  7: true
});
const handleLoadingUpdate3 = (id, newLoadingState) => {
  loadingStates3.value[id] = newLoadingState
}

/**
 * 轮播图
 * **/

const carouselData = ref([]); // 用于存储轮播图的数据
const loadingStates = ref({
  3: true,
  4: true,
  5: true,
  6: true
});

// 获取轮播图数据
const fetchCarouselData = async () => {
  try {
    const ids = [3, 4, 5, 6];
    const data = [];

    for (const id of ids) {
      loadingStates.value[id] = true; // 开始加载状态
      const response = await creationInfo(id);
      if (response && response.data) {
        data.push({
          id: id,
          image: response.data.cover_pic,
          title: response.data.title,
        });
        loadingStates.value[id] = false; // 数据加载完毕，更新加载状态
      } else {
        ElMessage.error("数据加载失败");
      }
    }

    // 将获取到的数据存入 carouselData
    carouselData.value = data;
  } catch (error) {
    ElMessage.error("加载轮播图数据失败");
    console.error("Error fetching carousel data:", error);
  }
};

const toContent = (id) => {
  router.push(`/content/creation/${id}`)
}


onMounted(() => {
  fetchCarouselData();
});
</script>

<template>
  <el-container class="view-box">
    <el-main class="main">
      <div class="main-content">
        <el-carousel :interval="4000" type="card" class="cardpic" card-scale="0.7">
          <el-carousel-item
              v-for="item in carouselData"
              :key="item.id"
              style="width: 600px"
              @click="toContent(item.id)">
            <img v-if="!loadingStates[item.id]" :src="item.image" alt="轮播图图片" class="carousel-item"/>
            <div v-if="!loadingStates[item.id]" class="carousel-title">{{ item.title }}</div>
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
            <CardCreation
                v-for="id in creationIds"
                :key="id"
                :creation_id="id"
                :loading="loadingStates1[id]"
                @update-loading1="handleLoadingUpdate1(id, $event)"
                class="card-creation"
            />
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
            <CardHairstyle
                v-for="id in hairstyleIds"
                :key="id"
                :hairstyle_id="id"
                :loading="loadingStates2[id]"
                @update-loading2="handleLoadingUpdate2(id, $event)"
                class="card-creation"
            />
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
            <CardOffline
                v-for="id in offlineIds"
                :key="id"
                :offline_id="id"
                :loading="loadingStates3[id]"
                @update-loading3="handleLoadingUpdate3(id, $event)"
                class="card-creation"
            />
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

.cardpic {
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
.el-carousel__item {
  background-color: #99a9bf;
}

.card {
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

.card-creation {
  margin: 0 50px;
}
.carousel-item{
  object-fit: cover;
  width: 100%;
}

.carousel-title {
  position: absolute;
  top: 0;
  background: rgba(0, 0, 0, 0.2); /* 半透明背景 */
  width: 100%;
  text-align: center;
}
</style>