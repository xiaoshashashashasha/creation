<script setup>
import { ref, onMounted, nextTick, onUnmounted } from 'vue'
import { throttle } from 'lodash-es'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getChatList, getHistory, sendMessage, setMessageRead } from '@/api/prMessage'
import { userOtherInfoService, userInfoService } from '@/api/user'
import RichTextEditor from "@/components/RichTextEditor.vue";

const chatList = ref([])
const activeChat = ref(null)
const messages = ref([])
const inputMessage = ref('')
const pageNum = ref(1)
const pageSize = ref(20)
const loadingMessages = ref(false)
const noMoreMessages = ref(false)
const currentUser = ref({})
const chatBodyRef = ref(null)
const router = useRouter()

const throttledHandleScroll = throttle(async () => {
  if (!chatBodyRef.value) return;
  const { scrollTop } = chatBodyRef.value;

  console.log('检测滚动 scrollTop:', scrollTop)

  if (scrollTop <= 10 && !loadingMessages.value && !noMoreMessages.value) {
    console.log('到顶了，加载更多');
    pageNum.value += 1
    await fetchMessages(true)
  }
}, 300)

const goBack = () => router.back()

const fetchCurrentUser = async () => {
  try {
    const res = await userInfoService()
    if (res.data) {
      currentUser.value = res.data
    }
  } catch (err) {
    if (err?.response?.status !== 401) {
      console.error('获取当前用户信息失败:', err)
      ElMessage.error('无法获取当前登录信息')
    }
  }
}

const fetchChatList = async () => {
  try {
    const res = await getChatList()
    if (res.code === 0) {
      const list = res.data || []
      for (const chat of list) {
        try {
          const userRes = await userOtherInfoService(chat.target_id)
          if (userRes.data && userRes.code === 0) {
            chat.nickname = userRes.data.nickname
            chat.user_pic = userRes.data.user_pic
          } else {
            chat.nickname = '未知用户'
            chat.user_pic = ''
          }
        } catch {
          chat.nickname = '未知用户'
          chat.user_pic = ''
        }
      }
      chatList.value = list
    } else {
      ElMessage.error(res.msg || '获取聊天列表失败')
    }
  } catch (err) {
    console.error('请求异常:', err)
    ElMessage.error('网络错误')
  }
}

const fetchMessages = async (append = false) => {
  if (!activeChat.value) return
  loadingMessages.value = true
  try {
    const res = await getHistory({
      pageNum: pageNum.value,
      pageSize: pageSize.value,
      target_id: activeChat.value.target_id
    })
    if (res.code === 0) {
      const list = res.data || []

      if (list.length === 0 && append) {
        noMoreMessages.value = true
        return
      }

      if (append) {
        const oldHeight = chatBodyRef.value.scrollHeight
        messages.value = [...list.reverse(), ...messages.value]
        await nextTick()
        const newHeight = chatBodyRef.value.scrollHeight
        chatBodyRef.value.scrollTop = newHeight - oldHeight // 保持滚动位置
      } else {
        messages.value = list.reverse()
        await nextTick()
        scrollToBottom()
      }
    } else {
      ElMessage.error(res.msg || '拉取聊天记录失败')
    }
  } catch (err) {
    console.error('获取聊天记录异常:', err)
    ElMessage.error('网络错误')
  } finally {
    loadingMessages.value = false
  }
}

const selectChat = async (chat) => {
  activeChat.value = chat
  pageNum.value = 1
  messages.value = []
  await setMessageRead(chat.target_id)
  await fetchMessages()
  await fetchChatList()

  nextTick(() => {
    if (chatBodyRef.value) {
      // 先解绑（防止重复绑定）
      chatBodyRef.value.removeEventListener('scroll', throttledHandleScroll)
      // 再绑定新的
      chatBodyRef.value.addEventListener('scroll', throttledHandleScroll)
    }
  })
}



const sendMsg = async () => {
  if (!inputMessage.value.trim() || !activeChat.value) return
  try {
    const content = inputMessage.value.trim()
    await sendMessage({
      to_id: activeChat.value.target_id,
      content
    })
    inputMessage.value = ''

    // 不要去fetchMessages重拉！！！
    // 直接在messages后面追加一条新的
    const newMessage = {
      message_id: Date.now(), // 随便造一个临时id
      from_id: currentUser.value.user_id,
      to_id: activeChat.value.target_id,
      content: content,
      created_at: new Date().toISOString()
    }
    messages.value.push(newMessage)

    // 手动滚到底部
    await nextTick()
    scrollToBottom()

    // 更新聊天列表里的最后一条消息
    const now = new Date().toISOString().slice(0, 19).replace('T', ' ')
    activeChat.value.lastMessage = content
    activeChat.value.lastTime = now

    const chat = chatList.value.find(c => c.target_id === activeChat.value.target_id)
    if (chat) {
      chat.lastMessage = content
      chat.lastTime = now
      chatList.value = [chat, ...chatList.value.filter(c => c.target_id !== chat.target_id)]
    }

  } catch (err) {
    ElMessage.error('发送失败')
  }
}


const scrollToBottom = () => {
  if (chatBodyRef.value) {
    chatBodyRef.value.scrollTop = chatBodyRef.value.scrollHeight
  }
}



onMounted(async () => {
  await fetchCurrentUser()
  await fetchChatList()
})

onUnmounted(() => {
  if (chatBodyRef.value) {
    chatBodyRef.value.removeEventListener('scroll', throttledHandleScroll)
  }
})


</script>


<template>
  <div class="main-content">
    <el-button type="primary" plain class="back-btn" @click="goBack">
      返回
    </el-button>
    <div class="message-page">
      <div class="chat-list">
        <div
            v-for="chat in chatList"
            :key="chat.target_id"
            class="chat-item"
            :class="{ active: activeChat && activeChat.target_id === chat.target_id }"
            @click="selectChat(chat)"
        >
          <el-avatar :src="chat.user_pic" size="large">{{ chat.nickname ? chat.nickname[0] : '?' }}</el-avatar>
          <div class="chat-info">
            <div class="chat-name">{{ chat.nickname }}</div>
            <div class="chat-last" v-html="chat.lastMessage"></div>
          </div>
          <el-badge :value="chat.unReadCount" v-if="chat.unReadCount > 0"/>
        </div>
      </div>

      <div class="chat-window" v-if="activeChat">
        <div class="chat-header">
          与 {{ activeChat.nickname }} 的聊天
        </div>

        <div class="chat-body" ref="chatBodyRef" v-loading="loadingMessages">
          <div v-for="msg in messages" :key="msg.message_id" class="chat-message"
               :class="{ 'me': msg.from_id === currentUser.user_id }">
            <div class="chat-content" v-html="msg.content"></div>
            <div class="chat-time">{{ new Date(msg.created_at).toLocaleString() }}</div>
          </div>
        </div>

        <div class="chat-input">
          <RichTextEditor
              v-model="inputMessage"
              style="height: 200px"
              :visible="true"
          />
          <el-button type="primary" @click="sendMsg" style="height: 60px ;margin-top: 10px;">发送</el-button>
        </div>
      </div>

      <div class="chat-window" v-else>
        <div class="chat-placeholder">请选择一个聊天</div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.main-content {
  flex: 1;
  width: 1720px;
  margin: 0 auto;
  background: #f5f7fa;
}
.message-page {
  display: flex;
  width: 1720px;
  margin: 0 auto;
  margin-bottom: 10px;
  height: 86vh;
  background: #f5f7fa;
}

.chat-list {
  width: 300px;
  background: white;
  border-right: 1px solid #ddd;
  overflow-y: auto;
}

.chat-item {
  display: flex;
  align-items: center;
  padding: 15px;
  cursor: pointer;
  border-bottom: 1px solid #eee;
  transition: background-color 0.3s, box-shadow 0.3s;
}

.chat-item:hover {
  background: #f0faff;
}

.chat-item.active {
  background: #e6f7ff;
  box-shadow: inset 4px 0 0 #409eff; /* 左边一条蓝色高亮条 */
}

.chat-info {
  margin-left: 10px;
  flex: 1;
  overflow: hidden;
}

.chat-name {
  font-weight: bold;
  margin-bottom: 5px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.chat-last {
  color: #999;
  font-size: 12px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}


.chat-window {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.chat-header {
  background: white;
  padding: 15px;
  border-bottom: 1px solid #ddd;
  font-weight: bold;
}

.chat-body {
  flex: 1;
  height: 0;
  min-height: 0;
  max-height: calc(86vh - 160px);
  overflow-y: auto;
  padding: 15px;
  background: #f9f9f9;
}


.chat-message {
  margin-bottom: 10px;
  max-width: 60%;
  word-break: break-word;
}

.chat-message.me {
  margin-left: auto;
  text-align: right;
}

.chat-content {
  display: inline-block;
  background: #409eff;
  color: white;
  padding: 10px;
  border-radius: 8px;
}

.chat-message.me .chat-content {
  background: #67c23a;
}

.chat-time {
  font-size: 10px;
  color: #999;
  margin-top: 5px;
}

.chat-input {
  display: flex;
  padding: 15px;
  background: white;
  border-top: 1px solid #ddd;
}

.chat-placeholder {
  margin: auto;
  font-size: 20px;
  color: #ccc;
}

.back-btn {
  margin: 5px;
}
</style>
