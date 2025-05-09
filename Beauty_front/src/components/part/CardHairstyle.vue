<template>
  <div class="article-card" @click="toContent">
    <el-skeleton :loading="loading" animated>
      <template #template>
        <el-skeleton-item variant="image" style="width: 280px; height: 140px;" />
        <el-skeleton-item variant="text" style="width: 130px; height: 35px; margin-top: 10px;" />
      </template>

      <template #default>
        <img :src="data.hairstyle_pic" alt="文章图片" class="card-image" />
        <div class="card-title">{{ data.hairstyle_name }}</div>
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
  router.push(`/content/hairstyle/${props.data.hairstyle_id}`)
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

.card-title {
  width: 100%;
  height: 50px;
  line-height: 50px;
  padding: 0 10px;
  font-size: 16px;
  font-weight: bold;
  color: #333;
  border-bottom: 1px solid #f0f0f0;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
</style>
