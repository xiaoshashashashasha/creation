<template>
  <div class="article-card" @click="toContent">
    <el-skeleton :loading="loading" animated>
      <template #template>
        <el-skeleton-item variant="image" style="width: 280px; height: 140px;" />
        <el-skeleton-item variant="text" style="width: 130px; height: 35px; margin-top: 10px;" />
      </template>

      <template #default>
        <img :src="data.offline_pic" alt="文章图片" class="card-image" />
        <div class="card-header">
          <div class="card-title">{{ data.offline_name }}</div>
          <div class="card-city">{{ data.offline_city }}</div>
        </div>
      </template>
    </el-skeleton>
  </div>
</template>

<script setup>
import { defineProps } from 'vue'
import { useRouter } from 'vue-router'

const props = defineProps({
  data: {
    type: Object,
    required: true
  },
  loading: {
    type: Boolean,
    default: true
  }
})

const router = useRouter()
const toContent = () => {
  router.push(`/content/offline/${props.data.offline_id}`)
}
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
  justify-content: space-between;
  align-items: center;
  padding: 10px;
}

.card-title {
  font-size: 16px;
  font-weight: bold;
  color: #333;
  flex-grow: 1;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.card-city {
  font-size: 14px;
  color: #666;
  margin-left: 10px;
}
</style>
