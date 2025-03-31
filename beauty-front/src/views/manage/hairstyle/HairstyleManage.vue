<script setup>
import {ref, onMounted} from "vue"
import {ElMessage} from "element-plus"
import {
  hairstyleList,
  hairstyleInfo,
  hairstyleDel,
  hairstyleUpdate,
  hairstyleAdd
} from "@/api/hairstyle"
import {uploadFile} from "@/api/fileUpload"
import RichTextEditor from "@/components/RichTextEditor.vue";
const tableData = ref([])
const pageNum = ref(1)
const pageSize = ref(6)
const total = ref(0)
const dialogVisible = ref(false)
const editDialogVisible = ref(false)
const detail = ref(null)
const editForm = ref({})
const keyWord = ref("")


const createDialogVisible = ref(false)
const createForm = ref({
  hairstyle_name: "",
  hairstyle_pic: "",
  content: ""
})

const fetchData = async () => {
  try {
    const res = await hairstyleList({
      pageNum: pageNum.value,
      pageSize: pageSize.value,
      keyWord: keyWord.value
    })
    tableData.value = res.data.items
    total.value = res.data.total
  } catch (err) {
    ElMessage.error(err.msg || "获取发型数据失败")
  }
}

const handleSearch = () => {
  pageNum.value = 1
  fetchData()
}

const handleReset = () => {
  keyWord.value = ""
  fetchData()
}

const handlePageChange = (newPage) => {
  pageNum.value = newPage
  fetchData()
}

const openDetailDialog = async (id) => {
  try {
    const res = await hairstyleInfo(id)
    detail.value = res.data
    dialogVisible.value = true
  } catch (err) {
    ElMessage.error(err.msg || "获取发型详情失败")
  }
}

const openEditDialog = async (id) => {
  try {
    const res = await hairstyleInfo(id)
    editForm.value = { ...res.data }
    editDialogVisible.value = true

  } catch (err) {
    ElMessage.error(err.msg || "获取发型详情失败")
  }
}

const handleDelete = async (id) => {
  try {
    await hairstyleDel(id)
    ElMessage.success("删除成功")
    fetchData()
  } catch (err) {
    ElMessage.error(err.msg || "删除失败")
  }
}

const handleCustomUpload = async ({file}) => {
  try {
    const res = await uploadFile(file)
    editForm.value.hairstyle_pic = res.data
    ElMessage.success("上传成功")
  } catch (err) {
    ElMessage.error("上传失败")
  }
}

const handleCreateUpload = async ({file}) => {
  try {
    const res = await uploadFile(file)
    createForm.value.hairstyle_pic = res.data
    ElMessage.success("上传成功")
  } catch (err) {
    ElMessage.error("上传失败")
  }
}

const handleSaveEdit = async () => {
  try {
    await hairstyleUpdate(editForm.value)
    ElMessage.success("保存成功")
    editDialogVisible.value = false
    fetchData()
  } catch (err) {
    ElMessage.error(err.msg || "保存失败")
  }
}

const openCreateDialog = () => {
  createForm.value = {
    hairstyle_name: "",
    hairstyle_pic: "",
    content: ""
  }
  createDialogVisible.value = true

}
const handleCreateSubmit = async () => {
  if (!createForm.value.hairstyle_name) {
    return ElMessage.warning("请输入发型名称")
  }
  try {
    await hairstyleAdd(createForm.value)
    ElMessage.success("新建成功")
    createDialogVisible.value = false
    fetchData()
  } catch (err) {
    ElMessage.error(err.msg || "新建失败")
  }
}

onMounted(() => {
  fetchData()
})
</script>

<template>
  <el-row :gutter="0">
    <div class="path">
      <p style="font-size: 20px; margin-top: 5px">发型管理</p>
    </div>

    <div class="search">
      <el-row justify="space-between" align="middle" style="padding: 10px">
        <el-col :span="20">
          <el-row :gutter="10">
            <el-col :span="6">
              <el-input v-model="keyWord" placeholder="请输入发型名称" clearable @keyup.enter="handleSearch"/>
            </el-col>
            <el-col :span="6">
              <el-button type="primary" @click="handleSearch">搜索</el-button>
              <el-button @click="handleReset">重置</el-button>
            </el-col>
          </el-row>
        </el-col>
        <el-col :span="4" style="text-align: right">
          <el-button type="primary" @click="openCreateDialog">新建发型</el-button>
        </el-col>
      </el-row>
    </div>

    <el-table :data="tableData" style="width: 100%">
      <el-table-column prop="hairstyle_id" label="发型ID" width="120px"/>
      <el-table-column label="封面" width="200px">
        <template #default="{ row }">
          <img :src="row.hairstyle_pic" style="width: 200px; height: 100px; object-fit: cover"/>
        </template>
      </el-table-column>
      <el-table-column prop="hairstyle_name" label="发型名" width="160px"/>
      <el-table-column prop="created_at" label="创建时间" width="240px"/>
      <el-table-column prop="updated_at" label="更新时间" width="240px"/>
      <el-table-column label="操作" width="360px">
        <template #default="{ row }">
          <el-button size="small" @click="openDetailDialog(row.hairstyle_id)">查看</el-button>
          <el-button size="small" @click="openEditDialog(row.hairstyle_id)">编辑</el-button>
          <el-button size="small" type="danger" @click="handleDelete(row.hairstyle_id)">删除</el-button>
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

    <!-- 查看弹窗 -->
    <el-dialog v-model="dialogVisible" title="发型详情" width="40%">
      <div v-if="detail">
        <h3>{{ detail.hairstyle_name }}</h3>
        <img :src="detail.hairstyle_pic" style="width: 100%; max-height: 300px; object-fit: contain"/>
        <p>创建时间：{{ detail.created_at }}</p>
        <p>更新时间：{{ detail.updated_at }}</p>
        <div v-html="detail.content" style="margin-top: 10px"/>
      </div>
    </el-dialog>

    <!-- 编辑弹窗 -->
    <el-dialog v-model="editDialogVisible" title="编辑发型">
      <el-form :model="editForm" label-width="80px">
        <el-form-item label="名称">
          <el-input v-model="editForm.hairstyle_name"/>
        </el-form-item>
        <el-form-item label="封面">
          <div style="display: flex; align-items: center; gap: 20px">
            <img v-if="editForm.hairstyle_pic" :src="editForm.hairstyle_pic" style="width: 200px; height: 120px"/>
            <el-upload :show-file-list="false" :http-request="handleCustomUpload" accept="image/*">
              <el-button type="primary">上传封面</el-button>
            </el-upload>
          </div>
        </el-form-item>
        <el-form-item label="内容">
          <RichTextEditor
              v-model="editForm.content"
              :visible="editDialogVisible"
          />
        </el-form-item>




      </el-form>
      <template #footer>
        <el-button @click="editDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSaveEdit">保存</el-button>
      </template>
    </el-dialog>

    <!-- 新建弹窗 -->
    <el-dialog v-model="createDialogVisible" title="新建发型">
    <el-form :model="createForm" label-width="80px">
        <el-form-item label="名称">
          <el-input v-model="createForm.hairstyle_name"/>
        </el-form-item>
        <el-form-item label="封面">
          <div style="display: flex; align-items: center; gap: 20px">
            <img v-if="createForm.hairstyle_pic" :src="createForm.hairstyle_pic" style="width: 200px; height: 120px"/>
            <el-upload :show-file-list="false" :http-request="handleCreateUpload" accept="image/*">
              <el-button type="primary">上传封面</el-button>
            </el-upload>
          </div>
        </el-form-item>
      <el-form-item label="内容">
        <RichTextEditor
            v-model="createForm.content"
            :visible="createDialogVisible"
        />
      </el-form-item>





    </el-form>
      <template #footer>
        <el-button @click="createDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleCreateSubmit">创建</el-button>
      </template>
    </el-dialog>
  </el-row>
</template>

<style scoped>
.path {
  height: 40px;
  width: 100%;
  background-color: #d8a6a6;
}

.search {
  width: 100%;
  height: 52px;
  background-color: #bbd2e3;
}

::v-deep(.el-table__row) {
  height: 100px;
}
.editor-wrapper {
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  overflow: hidden;
}

.w-e-toolbar {
  border-bottom: 1px solid #f0f0f0;
}

.w-e-text-container {
  min-height: 200px;
  padding: 10px;
}

.w-e-text img {
  max-width: 100%;
  height: auto;
  display: block;
  margin: 10px 0;
  cursor: nwse-resize; /* 显示拖拽角标 */
}

</style>
