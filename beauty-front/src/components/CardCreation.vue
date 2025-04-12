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
  router.push(`/content/creation/${props.data.creation_id}`)
}
</script>

<template>
  <div class="article-card" @click="toContent">
    <el-skeleton :loading="props.loading" animated>
      <template #template>
        <el-skeleton-item variant="image" style="width: 280px; height: 140px;" />
        <el-skeleton-item variant="text" style="width: 130px; height: 35px; margin-top: 10px;" />
        <el-skeleton-item variant="text" style="width: 260px; height: 35px; margin-top: 10px;" />
      </template>

      <template #default>
        <img :src="props.data.cover_pic" class="card-image" />
        <div class="card-title">{{ props.data.title }}</div>
        <div class="card-summary">{{ props.data.abs_text }}</div>
      </template>
    </el-skeleton>
  </div>
</template>


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
