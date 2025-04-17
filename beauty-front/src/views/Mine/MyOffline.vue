<script setup>
import {ref, onMounted} from 'vue'
import {useRouter} from 'vue-router'
import {ElMessage, ElMessageBox} from 'element-plus'
import {myOfflineList, improveOffline, delOffline} from '@/api/offline'
import {uploadFile} from '@/api/fileUpload'
import RichTextEditor from '@/components/RichTextEditor.vue'

const router = useRouter()
const tableData = ref([])
const pageNum = ref(1)
const pageSize = ref(15)
const total = ref(0)

const decodeAndCleanHTML = (htmlStr) => {
  const textarea = document.createElement('textarea')
  textarea.innerHTML = htmlStr
  const decoded = textarea.value

  // 清理掉真正没内容的空段落，但保留 <p><br></p>
  return decoded.replace(/<p>(&nbsp;|\s|<span[^>]*><\/span>)*<\/p>/gi, '')
}


const fetchOfflineStores = async () => {
  try {
    const res = await myOfflineList({pageNum: pageNum.value, pageSize: pageSize.value})
    tableData.value = res.data.items || []
    total.value = res.data.total || 0
  } catch (err) {
    ElMessage.error(err.msg || '获取门店信息失败')
  }
}

const handlePageChange = (newPage) => {
  pageNum.value = newPage
  fetchOfflineStores()
}

const editDialogVisible = ref(false)
const editForm = ref({
  offline_id: '',
  offline_name: '',
  offline_phone: '',
  offline_position: '',
  offline_pic: '',
  offline_content: ''
})

const openEditDialog = (store) => {
  editForm.value = {
    ...store,
    offline_content: decodeAndCleanHTML(store.offline_content)
  }
  editDialogVisible.value = true
}

const handleCustomUpload = async ({file}) => {
  try {
    const res = await uploadFile(file)
    editForm.value.offline_pic = res.data
    ElMessage.success('上传成功')
  } catch (err) {
    ElMessage.error('上传失败')
  }
}

const saveStoreChanges = async () => {
  try {
    editForm.value.offline_content = decodeAndCleanHTML(editForm.value.offline_content) // 修复这里
    await improveOffline(editForm.value)
    ElMessage.success('更新成功')
    editDialogVisible.value = false
    fetchOfflineStores()
  } catch (err) {
    ElMessage.error(err.msg || '更新失败')
  }
}


const viewDetails = (store) => {
  router.push(`/content/offline/${store.offline_id}`)
}

const manageMembers = (store) => {
  router.push(`/offline/${store.offline_id}/members`)
}

const cancelStore = (store) => {
  ElMessageBox.confirm(
      `确认要注销门店「${store.offline_name}」吗？`,
      '提示',
      {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning'
      }
  ).then(async () => {
    try {
      await delOffline(store.offline_id)
      ElMessage.success('门店注销成功')
      fetchOfflineStores()
    } catch (err) {
      ElMessage.error(err.msg || '注销失败')
    }
  }).catch(() => {
  })
}

onMounted(() => {
  fetchOfflineStores()
})
</script>

<template>
  <div class="main-content">
    <el-button
        class="back-btn"
        type="primary"
        icon="el-icon-arrow-left"
        plain
        @click="() => router.push('/')"
    >
      返回首页
    </el-button>

    <div class="section-title">我的门店</div>

    <el-table :data="tableData" style="width: 100%; margin-top: 10px">
      <el-table-column prop="offline_id" label="门店ID" width="100"/>
      <el-table-column prop="offline_name" label="门店名称" width="180"/>
      <el-table-column prop="offline_position" label="门店地址" width="360"/>
      <el-table-column prop="offline_city" label="所在城市" width="160"/>
      <el-table-column prop="offline_phone" label="联系电话" width="240"/>
      <el-table-column label="操作" width="400">
        <template #default="{ row }">
          <el-button size="small" @click="viewDetails(row)">查看详情</el-button>
          <el-button size="small" type="primary" @click="openEditDialog(row)">编辑</el-button>
          <el-button size="small" type="warning" @click="manageMembers(row)">成员管理</el-button>
          <el-button size="small" type="danger" @click="cancelStore(row)">注销</el-button>
        </template>
      </el-table-column>
    </el-table>

    <div style="text-align: center; margin-top: 20px">
      <el-pagination
          background
          layout="prev, pager, next, ->, total"
          :total="total"
          :current-page="pageNum"
          :page-size="pageSize"
          @current-change="handlePageChange"
      />
    </div>

    <!-- 编辑门店弹窗 -->
    <el-dialog v-model="editDialogVisible" title="编辑门店" width="90%" top="5vh">
      <div class="edit-store-container">
        <div class="top-section">
          <div class="left">
            <img v-if="editForm.offline_pic" :src="editForm.offline_pic" class="preview-pic"/>
            <el-upload
                :show-file-list="false"
                :http-request="handleCustomUpload"
                accept="image/*"
            >
              <el-button type="primary">上传封面</el-button>
            </el-upload>
          </div>
          <div class="right">
            <label>门店名称</label>
            <el-input v-model="editForm.offline_name" placeholder="门店名称" style="margin-bottom: 12px"/>
            <label>门店地址</label>
            <el-input v-model="editForm.offline_position" placeholder="门店地址" style="margin-bottom: 12px"/>
            <label>联系电话</label>
            <el-input v-model="editForm.offline_phone" placeholder="联系电话" style="margin-bottom: 12px"/>
          </div>
        </div>
        <div class="bottom-section">
          <RichTextEditor v-model="editForm.offline_content" :visible="editDialogVisible"/>
        </div>
      </div>
      <template #footer>
        <el-button @click="editDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveStoreChanges">更新门店信息</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
.main-content {
  flex: 1;
  width: 1720px;
  margin: 0 auto;
  background: #f5f7fa;
  padding: 20px;
}

.section-title {
  font-size: 20px;
  font-weight: bold;
  background-color: #eef1f6;
  padding: 10px 15px;
  border-left: 5px solid #409EFF;
  margin-bottom: 10px;
}

.back-btn {
  margin-bottom: 20px;
}

.edit-store-container {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.top-section {
  display: flex;
  justify-content: space-between;
  gap: 30px;
}

.left {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 10px;
}

.right {
  flex: 2;
  display: flex;
  flex-direction: column;
}

.preview-pic {
  width: 240px;
  height: 140px;
  object-fit: cover;
  border-radius: 8px;
  border: 1px solid #ddd;
}

.bottom-section {
  border: 1px solid #dcdfe6;
  padding: 10px;
  border-radius: 6px;
}


</style>
