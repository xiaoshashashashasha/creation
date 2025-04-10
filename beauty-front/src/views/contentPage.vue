<script setup>
import {useRoute} from 'vue-router'
import {creationInfo} from '@/api/creation'
import {hairstyleInfo} from '@/api/hairstyle'
import {offlineInfo} from '@/api/offline'
import {ref, onMounted} from 'vue'
import {userCancelFollowService, userFollowInfoService, userFollowService, userOtherInfoService} from "@/api/user";
import {ElMessage} from "element-plus";

const route = useRoute()
const detail = ref({})
const content = ref('')
const userInfo = ref({})
const followState = ref()

const type = route.params.type


const fetchData = async () => {
  const {type, id} = route.params
  let res
  let userRes
  let followRes

  if (type === 'creation') {
    res = await creationInfo(id)
    userRes = await userOtherInfoService(res.data.user_id)
    userInfo.value = userRes.data
    followRes = await userFollowInfoService(res.data.user_id)
    followState.value = followRes.data
  } else if (type === 'hairstyle') {
    res = await hairstyleInfo(id)
  } else if (type === 'offline') {
    res = await offlineInfo(id)
  }

  detail.value = res.data
  content.value = res.data.content || res.data.offline_content
}

const follow = async () => {
  try {
    let res
    if (followState.value === 1) {
      res = await userFollowService(detail.value.user_id)
      if (res.code === 0) {
        ElMessage.success("关注成功！")
        followState.value = 0
      }
    } else {
      res = await userCancelFollowService(detail.value.user_id)
      if (res.code === 0) {
        ElMessage.success("取消关注成功！")
        followState.value = 1
      }
    }
  } catch (error) {
    console.error('关注或取消关注失败:', error)
    ElMessage.error('操作失败')
  }
}


onMounted(() => {
  fetchData()
})
</script>

<template>
  <el-main class="main">

    <div class="main-content">
      <div v-if="type === 'creation'" class="creater">
        <div class="avatar-box">
          <img :src="userInfo.user_pic"  alt="cover" />
        </div>
        <div>
          <p class="user-name">{{userInfo.nickname}}</p>
          <el-button @click="follow" style="width: 100px; height: 40px; margin-left: 10px; margin-top: 20px; font-size: 18px">
            {{ followState === 0 ? '取消关注' : '关注' }}
          </el-button>
        </div>
      </div>

      <div style="padding: 20px;">
        <h1 style="text-align: center">{{ detail.title || detail.offline_name || detail.hairstyle_name }}</h1>
        <img :src="detail.cover_pic || detail.hairstyle_pic || detail.offline_pic"
             style="width: 100%; max-width: 400px; display: block; margin: 0 auto;" alt="cover"/>
        <div class="content" v-html="content"></div>
      </div>

    </div>
  </el-main>
</template>

<style scoped>
.main {
  flex: 1;
  background: #ededed;
  overflow-y: visible;
}

.creater {
  display: flex;
  width: 300px;
  height: 100px;
  position: absolute;
  margin-left: 10px;
  background-color: #cbdfe8;
}
.avatar-box {
  width: 100px;
  height: 100px;
  background-color: #5bb1a1;
  overflow: hidden;
}
.avatar-box img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}
.user-name {
  margin-left: 20px;
  margin-top: 5px;
  font-size: 20px;
  font-weight: bold;
}

.main-content {
  flex: 1;
  display: block;
  width: 1720px;
  margin: 0 auto;
  padding-top: 20px;
  background: #fff;
}

.content {
  width: 1080px;
  margin: 0 auto;
}

</style>