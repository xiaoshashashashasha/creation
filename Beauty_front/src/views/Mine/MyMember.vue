<script setup>
import {ref, onMounted} from 'vue'
import {useRoute,useRouter} from 'vue-router'
import {ElMessage, ElMessageBox} from 'element-plus'
import {memberList, addMember, delMember} from '@/api/offline'

// 获取 offline_id 参数
const route = useRoute()
const offlineId = route.params.id

const members = ref([])
const loading = ref(false)

const router = useRouter()


const form = ref({
  offline_id: offlineId,
  username: '',
  member_name: ''
})

const fetchMembers = async () => {
  loading.value = true
  try {
    const res = await memberList(offlineId)
    members.value = res.data || []
  } catch (err) {
    ElMessage.error(err.msg || '获取成员列表失败')
  } finally {
    loading.value = false
  }
}

const handleAddMember = async () => {
  if (!form.value.username || !form.value.member_name) {
    ElMessage.warning('请填写完整信息')
    return
  }

  try {
    await addMember({
      offline_id: offlineId,
      username: form.value.username,
      member_name: form.value.member_name
    })
    ElMessage.success('成员添加成功')
    form.value.username = ''
    form.value.member_name = ''
    fetchMembers()
  } catch (err) {
    ElMessage.error(err.msg || '添加成员失败')
  }
}

const handleDelete = (member_id) => {
  ElMessageBox.confirm(
      '确认删除该成员吗？',
      '提示',
      {
        confirmButtonText: '删除',
        cancelButtonText: '取消',
        type: 'warning'
      }
  ).then(async () => {
    try {
      await delMember(member_id)
      ElMessage.success('删除成功')
      fetchMembers()
    } catch (err) {
      ElMessage.error(err.msg || '删除失败')
    }
  }).catch(() => {
  })
}

onMounted(() => {
  fetchMembers()
})
</script>

<template>
  <div class="main-content">
    <!-- 返回按钮 -->
    <el-button
        type="primary"
        icon="el-icon-arrow-left"
        plain
        @click="router.back()"
        style="margin-bottom: 20px"
    >
      返回
    </el-button>
    <div class="member-page">
      <div class="left-table">
        <el-table :data="members" v-loading="loading" stripe border style="width: 100%">
          <el-table-column prop="member_id" label="成员ID" width="100"/>
          <el-table-column prop="username" label="用户名"/>
          <el-table-column prop="nickname" label="昵称"/>
          <el-table-column prop="member_name" label="成员名"/>
          <el-table-column label="操作" width="120">
            <template #default="{ row }">
              <el-button type="danger" size="small" @click="handleDelete(row.member_id)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <div class="right-form">
        <h3>添加新成员</h3>
        <el-input
            v-model="form.username"
            placeholder="请输入用户名"
            style="margin-bottom: 12px"
        />
        <el-input
            v-model="form.member_name"
            placeholder="请输入成员名"
            style="margin-bottom: 12px"
        />
        <el-button type="primary" @click="handleAddMember">添加成员</el-button>
      </div>
    </div>
  </div>
</template>

<style scoped>
.main-content {
  flex: 1;
  width: 1720px;
  height: 765px;
  margin: 0 auto;
  margin-bottom: 10px;
  background: #f5f7fa;
  padding: 20px;
}
.member-page {
  display: flex;
  padding: 20px;
  gap: 30px;
}

.left-table {
  flex: 3;
}

.right-form {
  flex: 1;
  background: #f9f9f9;
  padding: 20px;
  border-radius: 6px;
  border: 1px solid #e4e4e4;
}
</style>
