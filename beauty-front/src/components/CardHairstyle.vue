<template>
  <div class="article-card">
    <el-skeleton :loading="loading" animated>
      <template #template>
        <!-- 图片骨架 -->
        <el-skeleton-item variant="image" style="width: 280px; height: 140px;" />
        <!-- 标题骨架 -->
        <el-skeleton-item variant="text" style="width: 130px; height: 35px; margin-top: 10px;" />
      </template>

      <template #default>
        <img :src="image" alt="文章图片" class="card-image" />
        <div class="card-title">{{ title }}</div>
      </template>
    </el-skeleton>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import {hairstyleInfo} from "@/api/hairstyle";
import {nextTick} from "vue-demi";


const props = defineProps({
  hairstyle_id: {
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
const loading = ref(true)

const fetchData = async () => {
  try {
    const res = await hairstyleInfo(props.hairstyle_id)
    if (res && res.data) {
      image.value = res.data.hairstyle_pic
      title.value = res.data.hairstyle_name
      nextTick(() => {
        loading.value = false // 更新 loading 状态
        emit('update-loading2', false)
      })
    } else {
      throw new Error("数据未能正确返回")
    }
  } catch (error) {
    console.error("Error fetching data:", error)
    loading.value = false // 即使出错也会关闭骨架屏
    emit('update-loading2', false)
  }
}

onMounted(() => {
  fetchData()
})

// 触发事件来通知父组件
const emit = defineEmits(['update-loading2'])
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

.card-title{
  width: 100%;
  height: 50px;
  line-height: 50px;
  padding: 0 10px;
  font-size: 16px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.card-title {
  font-weight: bold;
  color: #333;
  border-bottom: 1px solid #f0f0f0;
}

</style>
