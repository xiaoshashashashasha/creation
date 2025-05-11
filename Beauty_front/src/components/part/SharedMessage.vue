<template>
  <div  class="shared-message" >
    <div class="shared-part" @click="navigateToDetail">
      <img :src="cover_pic" alt="封面" class="cover-image"/>
      <h3 class="message-title">{{ title }}</h3>
    </div>
    <p class="message-content">{{ content }}</p>
  </div>
</template>

<script setup>
import {defineProps} from 'vue';
import {useRouter} from 'vue-router';

const props = defineProps({
  cover_pic: String,
  title: String,
  content: String,
  type: Number,
  content_id: Number
});

const router = useRouter();

const navigateToDetail = () => {
  // 根据 type 路由到不同的页面
  if (props.type === 1) {
    router.push(`/content/creation/${props.content_id}`)

  } else if (props.type === 2) {
    router.push(`/content/hairstyle/${props.content_id}`)
  }
};
</script>

<style scoped>
.shared-message {
  background-color: #ccd1d1;
  flex-direction: column;
  max-width: 500px;
  align-items: center;
}

.shared-part {
  padding: 5px;
  margin: 5px auto;
  display: flex;
  flex-direction: column;
  align-items: center;
  cursor: pointer;
  width: 300px;
}

.cover-image {
  width: 360px;
  height: 240px;
  margin-bottom: 5px;
}

.message-title {
  font-size: 16px;
  font-weight: bold;
  margin: 0;
}

.message-content {
  margin: 2px 0 0;
  font-size: 14px;
  color: #666;
  text-align: left;
}
</style>
