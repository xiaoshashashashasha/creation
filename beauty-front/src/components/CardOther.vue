<!-- components/ArticleCard.vue -->
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
        <div class="card-summary">{{ summary }}</div>
      </template>
    </el-skeleton>
  </div>
</template>

<script setup>
defineProps({
  image: String,
  title: String,
  summary: String,
  loading: {
    type: Boolean,
    default: true
  }
})
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
