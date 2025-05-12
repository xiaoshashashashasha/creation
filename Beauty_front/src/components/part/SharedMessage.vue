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
  background-color: #d4d8d8;
  flex-direction: column;
  max-width: 500px;
  align-items: center;
  padding: 5px;
  border-radius: 8px;
}

.shared-part {
  background-color: #989a9a;
  border-radius: 8px;
  padding: 5px;
  margin: 5px auto;
  display: flex;
  flex-direction: column;
  align-items: center;
  cursor: pointer;
  width: auto;
}

.cover-image {
  width: 460px;
  height: auto;
  margin-bottom: 5px;
  object-fit: cover;
}

.message-title {
  font-size: 16px;
  font-weight: bold;
  margin: 0;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 100%;
}

.message-title::before {
  content: "点击查看对方的分享:";
  display: inline;
  color: #333;
  margin-right: 10px;
}

.message-content {
  margin: 2px;
  font-size: 15px;
  color: #666;
  text-align: left;
}
</style>
