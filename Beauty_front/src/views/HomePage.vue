<script setup>
import CardCreation from "@/components/part/CardCreation.vue";
import {onMounted, ref} from "vue";
import CardHairstyle from "@/components/part/CardHairstyle.vue";
import CardOffline from "@/components/part/CardOffline.vue";
import {ElMessage} from "element-plus";
import {creationInfo} from "@/api/creation";
import router from "@/router";
import {hairstyleInfo} from "@/api/hairstyle";
import {offlineInfo} from "@/api/offline";
import { ArrowRight } from '@element-plus/icons-vue'

// 内容数据
const creationIds = ref([3, 4, 5, 6])
const creationData = ref([])
const loadingStates1 = ref({})

// 发型数据
const hairstyleIds = ref([7, 8, 9, 10])
const hairstyleData = ref([])
const loadingStates2 = ref({})

// 门店数据
const offlineIds = ref([4, 5, 6, 7])
const offlineData = ref([])
const loadingStates3 = ref({})

// 轮播图数据
const carouselData = ref([])
const carouselLoading = ref({})

const toCreations = ()=>{
  router.push(`/creations`)
}

const toHairstyles = ()=>{
  router.push(`/hairstyles`)
}

const toOfflines = () => {
  router.push(`/offlines`)
}

const fetchCreationData = async (ids, dataList, loadingStates) => {
  for (const id of ids) {
    loadingStates.value[id] = true
    try {
      const res = await creationInfo(id)
      if (res && res.data) {
        dataList.value.push(res.data)
      } else {
        ElMessage.error('内容数据加载失败')
      }
    } catch (err) {
      console.warn('加载内容失败', err)
      ElMessage.error('内容数据加载失败')
    } finally {
      loadingStates.value[id] = false
    }
  }
}

const fetchHairstyleData = async (ids, dataList, loadingStates) => {
  for (const id of ids) {
    loadingStates.value[id] = true
    try {
      const res = await hairstyleInfo(id)
      if (res && res.data) {
        dataList.value.push(res.data)
      } else {
        ElMessage.error('发型数据加载失败')
      }
    } catch (err) {
      console.warn('加载发型失败', err)
      ElMessage.error('发型数据加载失败')
    } finally {
      loadingStates.value[id] = false
    }
  }
}

const fetchOfflineData = async (ids, dataList, loadingStates) => {
  for (const id of ids) {
    loadingStates.value[id] = true
    try {
      const res = await offlineInfo(id)
      if (res && res.data) {
        dataList.value.push(res.data)
      } else {
        ElMessage.error('门店数据加载失败')
      }
    } catch (err) {
      console.warn('加载门店失败', err)
      ElMessage.error('门店数据加载失败')
    } finally {
      loadingStates.value[id] = false
    }
  }
}



// 跳转
const toContent = (type, id) => {
  router.push(`/content/${type}/${id}`)
}



onMounted(() => {
  creationIds.value.forEach(id => {
    loadingStates1.value[id] = true
  })
  hairstyleIds.value.forEach(id => {
    loadingStates2.value[id] = true
  })
  offlineIds.value.forEach(id => {
    loadingStates3.value[id] = true
  })

  // 加载数据
  fetchCreationData(creationIds.value, creationData, loadingStates1)
  fetchHairstyleData(hairstyleIds.value, hairstyleData, loadingStates2)
  fetchOfflineData(offlineIds.value, offlineData, loadingStates3)
  fetchCreationData([3, 4, 5, 6], carouselData, carouselLoading)
})

</script>


<template>
  <el-container class="view-box">
    <el-main class="main">
      <div class="main-content">
        <!-- 轮播图 -->
        <el-carousel :interval="4000" type="card" class="cardpic" card-scale="0.7">
          <el-carousel-item v-for="item in carouselData" :key="item.id" @click="toContent('creation', item.creation_id)">
            <img v-if="!carouselLoading[item.creation_id]" :src="item.cover_pic" alt="轮播图" class="carousel-item" />
            <div v-if="!carouselLoading[item.creation_id]" class="carousel-title">{{ item.title }}</div>
          </el-carousel-item>
        </el-carousel>

        <el-card class="card">
          <template #header>
            <div class="card-header">
              <span>内容</span>
              <el-button type="primary" link size="large" @click="toCreations" class="more-btn">
                更多
                <el-icon style="margin-left: 4px;">
                  <ArrowRight />
                </el-icon>
              </el-button>
            </div>
          </template>

          <div style="margin-top: 20px; display: flex; justify-content: center;">
            <CardCreation style="margin-left: 40px;margin-right: 40px" v-for="item in creationData" :key="item.creation_id" :data="item" :loading="loadingStates1[item.creation_id]" />
          </div>
        </el-card>

        <el-card class="card">
          <template #header>
            <div class="card-header">
              <span>发型</span>
              <el-button type="primary" link size="large" @click="toHairstyles" class="more-btn">
                更多
                <el-icon style="margin-left: 4px;">
                  <ArrowRight />
                </el-icon>
              </el-button>
            </div>
          </template>
          <div style="margin-top: 20px; display: flex; justify-content: center;">
            <CardHairstyle style="margin-left: 40px;margin-right: 40px" v-for="item in hairstyleData" :key="item.hairstyle_id" :data="item" :loading="loadingStates2[item.hairstyle_id]" />
          </div>
        </el-card>

        <el-card class="card">
          <template #header>
            <div class="card-header">
              <span>线下门店</span>
              <el-button type="primary" link size="large" @click="toOfflines" class="more-btn">
                更多
                <el-icon style="margin-left: 4px;">
                  <ArrowRight />
                </el-icon>
              </el-button>
            </div>
          </template>
          <div style="margin-top: 20px; display: flex; justify-content: center;">
            <CardOffline style="margin-left: 40px;margin-right: 40px" v-for="item in offlineData" :key="item.offline_id" :data="item" :loading="loadingStates3[item.offline_id]" />
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

.more-btn {
  color: #409EFF;
  cursor: pointer;
  font-size: 18px;
  transition: all 0.3s;
}

.more-btn:hover {
  color: #66b1ff;
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
  background: rgba(0, 0, 0, 0.2);
  width: 100%;
  text-align: center;
}
</style>