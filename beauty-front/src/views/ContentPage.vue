<script setup>
import {useRoute, useRouter} from 'vue-router'
import {
  creationInfo,
  likeInfo,
  likeCreation,
  collectInfo,
  collectCreation,
  listComment,
  editorial,
  delComment,
  delCreation,
  updateCreation,
  creationClassList
} from '@/api/creation'
import {hairstyleInfo} from '@/api/hairstyle'
import {offlineInfo, memberList} from '@/api/offline'
import {ref, onMounted, watch} from 'vue'
import {
  userCancelFollowService,
  userFollowInfoService,
  userFollowService,
  userOtherInfoService,
  userInfoService
} from '@/api/user'
import {offlineCommentList, addReservation} from '@/api/reservation'
import {ElMessage, ElMessageBox} from 'element-plus'
import {Icon} from '@iconify/vue'
import {enableCache} from '@iconify/vue'
import RichTextEditor from '@/components/part/RichTextEditor.vue'
import {ArrowLeft, Delete, Edit} from '@element-plus/icons-vue'
import {uploadFile} from '@/api/fileUpload'

enableCache('all')

const route = useRoute()
const router = useRouter()

const detail = ref({})
const content = ref('')
const userInfo = ref({})
const followState = ref()
const loading = ref(true)
const followLoading = ref(false)

const likeState = ref(1)
const collectState = ref(1)

const type = route.params.type
const id = route.params.id

const commentInput = ref('')
const comments = ref([])
const commentTotal = ref(0)
const commentPageNum = ref(1)
const commentPageSize = ref(10)
const loadingComments = ref(false)
const currentUserId = ref(null)
const commentEditorRef = ref(null)
const coverPreviewUrl = ref('')
const coverImageFile = ref(null)

const editDialogVisible = ref(false)
const form = ref({
  creation_id: null,
  title: '',
  abs_text: '',
  cover_pic: '',
  content: '',
  class_id: null
})
const classOptions = ref([])
const formLoading = ref(false)


/**
 * 用户部分
 * */
const follow = async () => {
  if (followLoading.value) return
  followLoading.value = true

  try {
    let res
    if (followState.value === 1) {
      res = await userFollowService(detail.value.user_id)
      if (res.code === 0) {
        ElMessage.success('关注成功')
        followState.value = 0
      }
    } else {
      res = await userCancelFollowService(detail.value.user_id)
      if (res.code === 0) {
        ElMessage.success('取消关注成功')
        followState.value = 1
      }
    }
  } catch (error) {
    ElMessage.error('操作失败')
  } finally {
    followLoading.value = false
  }
}


/**
 * 内容部分
 * */
//拉取分类列表
const fetchClassOptions = async () => {
  try {
    const res = await creationClassList({pageNum: 1, pageSize: 100})
    classOptions.value = res.data.items || []
  } catch (err) {
    ElMessage.error('获取分类失败')
  }
}

//打开编辑弹窗
const openEditDialog = async () => {

  const res = await creationInfo(detail.value.creation_id)
  form.value = {
    ...res.data,
    content: res.data.content
        .replace(/<article[^>]*>/gi, '<div>')
        .replace(/<\/article>/gi, '</div>')
  }
  //预加载分类
  await fetchClassOptions()

  editDialogVisible.value = true
}

//提交编辑内容
const handleEditSubmit = async () => {
  const { title, abs_text, cover_pic, content, class_id } = form.value
  if (!title || !abs_text || (!cover_pic && !coverPreviewUrl.value) || !content || !class_id) {
    return ElMessage.warning('请完整填写所有信息')
  }
  formLoading.value = true
  try {
    // 如果本地有新的图片，先上传
    if (coverImageFile.value) {
      const res = await uploadFile(coverImageFile.value)
      form.value.cover_pic = res.data  // 更新成服务器返回的正式图片地址
    }

    await updateCreation({ ...form.value })
    ElMessage.success('保存成功')
    editDialogVisible.value = false
    fetchData()
  } catch (err) {
    ElMessage.error('保存失败')
  } finally {
    formLoading.value = false
  }
}


const handleCoverUpload = ({ file }) => {
  coverImageFile.value = file
  coverPreviewUrl.value = URL.createObjectURL(file)
}

watch(editDialogVisible, (val) => {
  if (!val) {
    coverPreviewUrl.value = ''
    coverImageFile.value = null
  }
})


const decodeHTML = (htmlStr) => {
  const textarea = document.createElement('textarea')
  textarea.innerHTML = htmlStr
  return textarea.value
}



const fetchLikeCollect = async () => {
  try {
    const [likeRes, collectRes] = await Promise.all([
      likeInfo(id),
      collectInfo(id)
    ])
    likeState.value = likeRes.data
    collectState.value = collectRes.data
    console.log(likeState.value + ";" + collectState.value)
  } catch (err) {
    console.error('获取点赞/收藏失败', err)
  }
}

const toggleLike = async () => {
  try {
    await likeCreation({creation_id: Number(id)})
    likeState.value = likeState.value === 0 ? 1 : 0
    ElMessage.success(likeState.value === 0 ? '点赞成功' : '取消点赞成功')
  } catch (err) {
    ElMessage.error('操作失败')
  }
}

const toggleCollect = async () => {
  try {
    await collectCreation({creation_id: Number(id)})
    collectState.value = collectState.value === 0 ? 1 : 0
    ElMessage.success(collectState.value === 0 ? '收藏成功' : '取消收藏成功')
  } catch (err) {
    ElMessage.error('操作失败')
  }
}


const handleDeleteCreation = async () => {
  try {
    await ElMessageBox.confirm(
        '确定要删除该内容吗？删除后无法恢复！',
        '提示',
        {
          confirmButtonText: '删除',
          cancelButtonText: '取消',
          type: 'warning'
        }
    )

    await delCreation(detail.value.creation_id)
    ElMessage.success('删除成功')
    router.push('/')
  } catch (err) {
    if (err !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

const fetchComments = async () => {
  try {
    loadingComments.value = true
    const res = await listComment({
      pageNum: commentPageNum.value,
      pageSize: commentPageSize.value,
      creation_id: Number(id)
    })
    comments.value = res.data.items
    commentTotal.value = res.data.total
  } catch (e) {
    ElMessage.error('评论加载失败')
  } finally {
    loadingComments.value = false
  }
}


const submitComment = async () => {
  if (!commentInput.value.trim()) {
    return ElMessage.warning('评论内容不能为空')
  }
  try {
    await editorial({
      creation_id: Number(id),
      content: commentInput.value.trim()
    })
    commentInput.value = ''
    commentEditorRef.value?.editorRef?.clear()
    ElMessage.success('评论成功')
    fetchComments()
  } catch (e) {
    ElMessage.error('评论失败')
  }
}


const handleCommentPageChange = (val) => {
  commentPageNum.value = val
  fetchComments()
}


const handleDelete = async (cid) => {
  try {
    await delComment(cid)
    ElMessage.success('已删除')
    fetchComments()
  } catch (e) {
    ElMessage.error('删除失败')
  }
}


const fechCurrentUserId = async () => {
  try {
    const res = await userInfoService()
    currentUserId.value = res.data.user_id
  } catch (err) {
    console.error('获取当前用户失败')
  }
}

/**
 * 门店部分
 * */
const offlineComments = ref([])
const offlineCommentTotal = ref(0)
const offlineCommentPageNum = ref(1)
const offlineCommentPageSize = ref(10)
const loadingOfflineComments = ref(false)
const userInfoMap = ref({})
const reserveDialogVisible = ref(false)
const reserveForm = ref({
  member_id: null,
  date: '',
  start_time: '',
  end_time: ''
})


const memberOptions = ref([])
const reserveLoading = ref(false)


const openReserveDialog = async () => {
  try {
    const res = await memberList(id)
    memberOptions.value = res.data || []

    const now = new Date()
    const currentHour = now.getHours()
    const currentMinute = now.getMinutes()

    // 默认最近的半小时起步
    let startHour = currentHour
    let startMinute = currentMinute < 30 ? 30 : 0
    if (currentMinute >= 30) startHour++

    const start_time = `${String(startHour).padStart(2, '0')}:${String(startMinute).padStart(2, '0')}`
    const endHour = startMinute === 0 ? startHour : startHour + 1
    const end_time = `${String(endHour).padStart(2, '0')}:${startMinute === 0 ? '30' : '00'}`

    reserveForm.value = {
      member_id: null,
      date: formatDate(now),
      start_time,
      end_time
    }

    generateTimeOptions()
    generateDateOptions()
    reserveDialogVisible.value = true
  } catch (err) {
    ElMessage.error(err.msg || '获取门店成员失败')
  }
}


const formatDate = (date) => {
  const yyyy = date.getFullYear()
  const MM = String(date.getMonth() + 1).padStart(2, '0')
  const dd = String(date.getDate()).padStart(2, '0')
  return `${yyyy}-${MM}-${dd}`
}


const timeOptions = ref([])


const generateTimeOptions = () => {
  const times = []
  for (let h = 0; h < 24; h++) {
    times.push(`${h.toString().padStart(2, '0')}:00`)
    times.push(`${h.toString().padStart(2, '0')}:30`)
  }
  timeOptions.value = times
}


const dateOptions = ref([])


const generateDateOptions = () => {
  const today = new Date()
  const dates = []
  for (let i = 0; i < 7; i++) {
    const d = new Date(today)
    d.setDate(today.getDate() + i)
    const yyyy = d.getFullYear()
    const MM = String(d.getMonth() + 1).padStart(2, '0')
    const dd = String(d.getDate()).padStart(2, '0')
    dates.push(`${yyyy}-${MM}-${dd}`)
  }
  dateOptions.value = dates
}


const submitReservation = async () => {
  const { member_id, date, start_time, end_time } = reserveForm.value

  if (!member_id || !date || !start_time || !end_time) {
    ElMessage.warning('请完整填写预约信息')
    return
  }

  const start_at = `${date} ${start_time}:00`
  const end_at = `${date} ${end_time}:00`

  if (start_at >= end_at) {
    ElMessage.warning('结束时间必须晚于开始时间')
    return
  }

  try {
    reserveLoading.value = true
    await addReservation({
      offline_id: Number(id),
      member_id,
      start_at,
      end_at
    })
    ElMessage.success('预约成功！')
    reserveDialogVisible.value = false
    reserveForm.value = {
      member_id: null,
      date: '',
      start_time: '',
      end_time: ''
    }
  } catch (err) {
    ElMessage.error(err.msg || '预约失败')
  } finally {
    reserveLoading.value = false
  }
}


const fetchOfflineComments = async () => {
  try {
    loadingOfflineComments.value = true

    const res = await offlineCommentList({
      pageNum: offlineCommentPageNum.value,
      pageSize: offlineCommentPageSize.value,
      offline_id: id
    })

    offlineComments.value = res.data.items || []
    offlineCommentTotal.value = res.data.total || 0

    for (const item of offlineComments.value) {
      if (item.user_id && !userInfoMap.value[item.user_id]) {
        try {
          const userRes = await userOtherInfoService(item.user_id)
          userInfoMap.value[item.user_id] = userRes.data
        } catch (e) {
          console.error('获取用户信息失败', e)
        }
      }
    }
  } catch (err) {
    ElMessage.error(err.msg || '门店评价加载失败')
  } finally {
    loadingOfflineComments.value = false
  }
}


const handleOfflineCommentPageChange = (newPage) => {
  offlineCommentPageNum.value = newPage
  fetchOfflineComments()
}


/**
 * 数据准备
 * */

const fetchData = async () => {
  try {
    loading.value = true
    let res

    if (type === 'creation') {
      res = await creationInfo(id)
      detail.value = res.data
      content.value = decodeHTML(res.data.content)

      const userRes = await userOtherInfoService(res.data.user_id)
      userInfo.value = userRes.data

      const followRes = await userFollowInfoService(res.data.user_id)
      followState.value = followRes.data

      await fetchLikeCollect()
    } else if (type === 'hairstyle') {
      res = await hairstyleInfo(id)
      detail.value = res.data
      content.value = decodeHTML(res.data.content)
    } else if (type === 'offline') {
      res = await offlineInfo(id)
      detail.value = res.data
      content.value = decodeHTML(res.data.offline_content)
    }
  } catch (error) {
    console.error('数据加载失败', error)
    ElMessage.error('数据加载失败')
  } finally {
    loading.value = false
  }
}



onMounted(() => {
  fetchData()
  fechCurrentUserId()
  if (type === 'creation') {
    fetchComments()
  }
  if (type === 'offline') {
    fetchOfflineComments()
  }
})

</script>


<template>
  <el-main class="main">
    <div class="main-content">
      <el-skeleton :loading="loading" animated>
        <template #template>
          <el-skeleton-item variant="image" style="width: 100%; height: 300px"/>
          <el-skeleton-item variant="text" style="width: 60%; height: 30px; margin-top: 20px;"/>
          <el-skeleton-item variant="text" style="width: 80%; height: 300px; margin-top: 20px;"/>
        </template>

        <template #default>
          <div class="top-bar">
            <div class="left-bar">
              <el-button
                  type="primary"
                  :icon="ArrowLeft"
                  plain
                  @click="router.back()"
              >
                返回
              </el-button>

              <el-button
                  v-if="currentUserId === detail.user_id && type === 'creation'"
                  type="warning"
                  :icon="Edit"
                  plain
                  @click="openEditDialog"
              >
                编辑
              </el-button>

              <el-button
                  v-if="currentUserId === detail.user_id && type === 'creation'"
                  type="danger"
                  :icon="Delete"
                  plain
                  @click="handleDeleteCreation"
              >
                删除
              </el-button>
            </div>

            <div v-if="type === 'creation'" class="action-bar-top" :class="{ disabled: detail.examine !== 0 }">
              <!-- 互动按钮集 -->
              <el-tooltip content="点赞" placement="bottom">
                <el-button circle @click="toggleLike" :disabled="detail.examine !== 0">
                  <Icon :icon="likeState === 0 ? 'mdi:thumb-up' : 'mdi:thumb-up-outline'" width="36" height="36"/>
                </el-button>
              </el-tooltip>

              <el-tooltip content="收藏" placement="bottom">
                <el-button circle @click="toggleCollect" :disabled="detail.examine !== 0">
                  <Icon :icon="collectState === 0 ? 'mdi:star' : 'mdi:star-outline'" width="36" height="36"/>
                </el-button>
              </el-tooltip>

              <el-tooltip content="转发" placement="bottom">
                <el-button circle @click="() => ElMessage.info('暂未实现')" :disabled="detail.examine !== 0">
                  <Icon icon="mdi:share-variant" width="36" height="36"/>
                </el-button>
              </el-tooltip>
            </div>

            <div v-else-if="type === 'offline'" class="action-bar-top">
              <el-button type="primary" @click="openReserveDialog">立刻预约</el-button>
            </div>

          </div>


          <div v-if="type === 'creation'" class="creater">
            <div class="avatar-box">
              <img :src="userInfo.user_pic" alt="cover"/>
            </div>
            <div>
              <p class="user-name">{{ userInfo.nickname }}</p>
              <p style="font-size: 16px;margin-left: 20px;"> 粉丝数:{{ userInfo.followers_count }}</p>
              <el-button
                  :loading="followLoading"
                  @click="follow"
                  :disabled="followState === 2"
                  style="width: 140px; height: 40px; margin-left: 10px; margin-top: 10px; font-size: 18px"
              >
                {{ followState === 2 ? '不可关注自己' : (followState === 0 ? '取消关注' : '关注') }}
              </el-button>
            </div>
          </div>


          <div style="padding: 20px;">
            <h1 style="text-align: center">
              {{ detail.title || detail.offline_name || detail.hairstyle_name }}
            </h1>
            <img
                :src="detail.cover_pic || detail.hairstyle_pic || detail.offline_pic"
                style="width: 100%; max-width: 400px; display: block; margin: 0 auto;"
                alt="cover"
            />

            <div class="content" v-html="content"/>

            <!-- 内容评论区 -->
            <div v-if="type === 'creation' && detail.examine === 0" class="comment-section">
              <h2 style="margin-bottom: 20px">评论区</h2>

              <RichTextEditor
                  ref="commentEditorRef"
                  v-model="commentInput"
                  :visible="true"
                  style="height: 280px"
              />

              <div style="text-align: right; margin-top: 10px">
                <el-button type="primary" @click="submitComment">发表评论</el-button>
              </div>

              <el-divider/>

              <el-empty description="暂无评论" v-if="comments.length === 0 && !loadingComments"/>

              <el-skeleton v-else-if="loadingComments" rows="4" animated/>

              <div v-else class="comment-list">
                <div v-for="item in comments" :key="item.comment_id" class="comment-item">
                  <div class="comment-meta">
                    <span class="comment-user">用户 {{ item.user_id }}</span>
                    <span class="comment-time">{{ item.created_at }}</span>
                    <el-button
                        v-if="item.user_id === currentUserId"
                        type="danger"
                        text
                        @click="handleDelete(item.comment_id)"
                        style="float: right"
                    >删除
                    </el-button>
                  </div>
                  <div class="comment-content" v-html="item.content"></div>

                  <el-divider/>
                </div>

                <el-pagination
                    :current-page="commentPageNum"
                    :page-size="commentPageSize"
                    :total="commentTotal"
                    layout="prev, pager, next"
                    @current-change="handleCommentPageChange"
                    background
                    style="text-align: center; margin-top: 20px"
                />
              </div>
            </div>
            <!-- 门店服务评价区 -->
            <div v-if="type === 'offline'" class="comment-section">
              <h2 style="margin-bottom: 20px">门店服务评价</h2>

              <el-empty description="暂无评价" v-if="offlineComments.length === 0 && !loadingOfflineComments"/>

              <el-skeleton v-else-if="loadingOfflineComments" rows="4" animated/>

              <div v-else class="comment-list">
                <div v-for="item in offlineComments" :key="item.reservation_id" class="comment-item">
                  <div class="comment-meta">
                    <div class="left-info">
                      <img
                          v-if="userInfoMap[item.user_id]"
                          :src="userInfoMap[item.user_id].user_pic"
                          class="comment-avatar"
                      />
                      <span class="nickname">{{ userInfoMap[item.user_id]?.nickname }}</span>
                    </div>
                    <div class="time">{{ item.evaluate_at }}</div>
                  </div>


                  <div class="comment-rate">
                    <el-rate
                        :model-value="item.point"
                        disabled
                        show-score
                        score-template="{value} 分"
                        style="margin-bottom: 8px;"
                    />
                  </div>

                  <div class="comment-content" v-html="item.comment">
                  </div>
                  <el-divider/>
                </div>

                <el-pagination
                    :current-page="offlineCommentPageNum"
                    :page-size="offlineCommentPageSize"
                    :total="offlineCommentTotal"
                    layout="prev, pager, next"
                    @current-change="handleOfflineCommentPageChange"
                    background
                    style="text-align: center; margin-top: 20px"
                />
              </div>
            </div>

          </div>
        </template>
      </el-skeleton>

      <el-dialog
          v-model="editDialogVisible"
          title="编辑创作内容"
          width="800px"
          :destroy-on-close="true"
      >
        <el-form :model="form" label-width="80px">
          <el-form-item label="标题">
            <el-input v-model="form.title" maxlength="16" show-word-limit/>
          </el-form-item>

          <el-form-item label="摘要">
            <el-input v-model="form.abs_text" maxlength="30" show-word-limit/>
          </el-form-item>

          <el-form-item label="封面">
            <div style="display: flex; align-items: center; gap: 16px">
              <img
                  v-if="coverPreviewUrl"
                  :src="coverPreviewUrl"
                  style="width: 160px; height: 100px; object-fit: cover"
              />
              <img
                  v-else-if="form.cover_pic"
                  :src="form.cover_pic"
                  style="width: 160px; height: 100px; object-fit: cover"
              />
              <el-upload
                  :show-file-list="false"
                  :http-request="handleCoverUpload"
                  accept="image/*"
              >
                <el-button type="primary">上传</el-button>
              </el-upload>
            </div>
          </el-form-item>


          <el-form-item label="分类">
            <el-select v-model="form.class_id" placeholder="请选择分类">
              <el-option v-for="item in classOptions" :key="item.class_id" :label="item.class_name"
                         :value="item.class_id"/>
            </el-select>
          </el-form-item>

          <el-form-item label="内容">
            <RichTextEditor
                v-model="form.content"
                :visible="editDialogVisible"
            />
          </el-form-item>

        </el-form>

        <template #footer>
          <el-button @click="editDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleEditSubmit" :loading="formLoading">保存</el-button>
        </template>
      </el-dialog>

      <el-dialog v-model="reserveDialogVisible" title="立刻预约" width="600px">
        <el-form :model="reserveForm" label-width="100px">

          <el-form-item label="选择成员">
            <el-select v-model="reserveForm.member_id" placeholder="请选择服务人员">
              <el-option v-for="member in memberOptions" :key="member.member_id" :label="member.member_name" :value="member.member_id" />
            </el-select>
          </el-form-item>

          <el-form-item label="预约日期">
            <el-select v-model="reserveForm.date" placeholder="请选择日期" style="width: 100%">
              <el-option
                  v-for="date in dateOptions"
                  :key="date"
                  :label="date"
                  :value="date"
              />
            </el-select>
          </el-form-item>


          <el-form-item label="开始时间">
            <el-select v-model="reserveForm.start_time" placeholder="请选择开始时间" style="width: 100%">
              <el-option v-for="time in timeOptions" :key="time" :label="time" :value="time" />
            </el-select>
          </el-form-item>

          <el-form-item label="结束时间">
            <el-select v-model="reserveForm.end_time" placeholder="请选择结束时间" style="width: 100%">
              <el-option v-for="time in timeOptions" :key="time" :label="time" :value="time" />
            </el-select>
          </el-form-item>

        </el-form>

        <template #footer>
          <el-button @click="reserveDialogVisible = false">取消</el-button>
          <el-button type="primary" :loading="reserveLoading" @click="submitReservation">预约</el-button>
        </template>
      </el-dialog>




    </div>
  </el-main>
</template>

<style scoped>
.main {
  flex: 1;
  background: #ededed;
  overflow: visible;
}

.creater {
  margin-top: 20px;
  margin-left: 20px;
  width: 300px;
  height: 100px;
  background-color: #cbdfe8;
  display: flex;
  align-items: center;
  padding: 10px;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}


.avatar-box {
  width: 100px;
  height: 100px;
  background-color: #5bb1a1;
  overflow: hidden;
}

.avatar-box img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.user-name {
  margin-left: 20px;
  margin-top: 5px;
  font-size: 20px;
  font-weight: bold;
}

.main-content {
  flex: 1;
  display: block;
  width: 1720px;
  margin: 0 auto;
  padding-top: 0;
  background: #fff;
}

.content {
  width: 1080px;
  margin: 0 auto;
}

.top-bar {
  position: sticky;
  top: 0;
  z-index: 10;
  width: 96%;
  background-color: #f6f3f3;
  padding: 10px 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.left-bar {
  display: flex;
  gap: 12px;
}

.action-bar-top {
  display: flex;
  gap: 16px;
  min-width: 200px;
  justify-content: flex-end;
}

.comment-section {
  max-width: 1080px;
  margin: 40px auto 0;
  background-color: #fafafa;
  padding: 20px;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.comment-input {
  width: 100%;
}

.comment-list {
  margin-top: 20px;
}

.comment-item {
  margin-bottom: 10px;
}

.comment-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.left-info {
  display: flex;
  align-items: center;
}

.comment-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  object-fit: cover;
}

.nickname {
  margin-left: 10px;
  font-size: 18px;
}

.time {
  font-size: 16px;
  color: #999;
}

.comment-content {
  font-size: 16px;
  line-height: 1.6;
}

.comment-rate {
  margin: 5px 0;
}


</style>
