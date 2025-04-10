<template>
  <div class="article-card" @click="toContent">
    <el-skeleton :loading="loading" animated>
      <template #template>
        <!-- 图片骨架 -->
        <el-skeleton-item variant="image" style="width: 280px; height: 140px;"/>
        <!-- 标题骨架 -->
        <el-skeleton-item variant="text" style="width: 130px; height: 35px; margin-top: 10px;"/>
        <!-- 摘要骨架 -->
        <el-skeleton-item variant="text" style="width: 260px; height: 35px;  margin-top: 10px;"/>
      </template>

      <template #default>
        <img :src="image" alt="文章图片" class="card-image"/>
        <div class="card-title">{{ title }}</div>
        <div class="card-summary">{{ abs_text }}</div>
      </template>
    </el-skeleton>
  </div>
</template>

<script setup>
import {ref, onMounted} from 'vue'
import {creationInfo} from '@/api/creation'
import {nextTick} from "vue-demi";
import {useRouter} from "vue-router";

// 组件的props
const props = defineProps({
  creation_id: {
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
const abs_text = ref('')
const loading = ref(true)
const router = useRouter()

const toContent = ()=>{
  router.push(`/content/creation/${props.creation_id}`)
}

const fetchData = async () => {
  try {
    const res = await creationInfo(props.creation_id)
    if (res && res.data) {
      image.value = res.data.cover_pic
      title.value = res.data.title
      abs_text.value = res.data.abs_text
      nextTick(() => {
        loading.value = false // 更新 loading 状态
        emit('update-loading1', false)
      })
    } else {
      throw new Error("数据未能正确返回")
    }
  } catch (error) {
    console.error("Error fetching data:", error)
    loading.value = false // 即使出错也会关闭骨架屏
    emit('update-loading1', false)
  }
}

// 在组件挂载时请求数据
onMounted(() => {
  fetchData()
})

// 触发事件来通知父组件
const emit = defineEmits(['update-loading1'])
</script>

<style scoped>
.article-card {
  width: 280px;
  height: 240px;
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

.card-title,
.card-summary {
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

.card-summary {
  color: #666;
}
</style>
