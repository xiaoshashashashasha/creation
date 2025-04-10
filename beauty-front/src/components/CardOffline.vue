<template>
  <div class="article-card" @click="toContent">
    <el-skeleton :loading="loading" animated>
      <template #template>
        <!-- 图片骨架 -->
        <el-skeleton-item variant="image" style="width: 280px; height: 140px;" />
        <!-- 标题骨架 -->
        <el-skeleton-item variant="text" style="width: 130px; height: 35px; margin-top: 10px;" />
      </template>

      <template #default>
        <img :src="image" alt="文章图片" class="card-image" />
        <div class="card-header">
          <div class="card-title">{{ title }}</div>
          <div class="card-city">{{ city }}</div>
        </div>
      </template>
    </el-skeleton>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { nextTick } from "vue-demi";
import { offlineInfo } from "@/api/offline";
import {useRouter} from "vue-router";

const props = defineProps({
  offline_id: {
    type: Number, // 使用数值类型
    required: true
  },
  loading: {
    type: Boolean,
    default: true
  }
})

const image = ref('')
const title = ref('')
const city = ref('')
const loading = ref(true)
const router = useRouter()

const toContent = ()=>{
  router.push(`/content/offline/${props.offline_id}`)
}

const fetchData = async () => {
  try {
    const res = await offlineInfo(props.offline_id)
    if (res && res.data) {
      image.value = res.data.offline_pic
      title.value = res.data.offline_name
      city.value = res.data.offline_city
      nextTick(() => {
        loading.value = false // 更新 loading 状态
        emit('update-loading3', false)
      })
    } else {
      throw new Error("数据未能正确返回")
    }
  } catch (error) {
    console.error("Error fetching data:", error)
    loading.value = false // 即使出错也会关闭骨架屏
    emit('update-loading3', false)
  }
}

onMounted(() => {
  fetchData()
})

// 触发事件来通知父组件
const emit = defineEmits(['update-loading3'])
</script>

<style scoped>
.article-card {
  width: 280px;
  height: 190px;
  border-radius: 10px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  background-color: #fff;
}

.card-image {
  width: 100%;
  height: 140px;
  object-fit: cover;
}

.card-header {
  display: flex;
  justify-content: space-between; /* Make title and city appear on the same row with space between them */
  align-items: center;
  padding: 10px;
}

.card-title {
  font-size: 16px;
  font-weight: bold;
  color: #333;
  flex-grow: 1; /* Ensures the title takes all available space */
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.card-city {
  font-size: 14px;
  font-weight: normal;
  color: #666;
  text-align: right; /* Align city to the right */
  margin-left: 10px; /* Optional margin between title and city */
}

</style>
