<script setup>
import {ref, onMounted} from "vue"
import {ElMessage} from "element-plus"
import {
  creationTagList,
  creationCancelConnectTag,
  creationListExamine,
  creationGetTags,
  creationConnectTag,
  creationExamine,
  creationClassList,
  creationChangeClass,
  creationInfo
} from "@/api/creation"

const tableData = ref([])
const pageNum = ref(1)
const pageSize = ref(5)
const total = ref(0)
const tagOptions = ref([])
const tagDialogVisible = ref(false)
const classDialogVisible = ref(false)
const contentDialogVisible = ref(false)
const contentDetail = ref(null)
const currentCreationId = ref(null)
const selectedTagId = ref(null)
const selectedClassId = ref(null)
const commentMap = ref({})
const selectedSearchClassId = ref(null)
const selectedSearchTagId = ref(null)
const SearchClassOptions = ref([])
const SearchTagOptions = ref([])
const titleKeyword = ref('')

const handleSearch = () => {
  pageNum.value = 1
  fetchData()
}

const fetchData = async () => {
  try {
    const res = await creationListExamine({
      pageNum: pageNum.value,
      pageSize: pageSize.value,
      examine: 0,
      title: titleKeyword.value,
      class_id: selectedSearchClassId.value,
      tag_id: selectedSearchTagId.value
    })


    const creations = res.data.items

    const tagRequests = creations.map(item =>
        creationGetTags(item.creation_id).then(tagRes => {
          item.tags = tagRes.data || []
        }).catch(() => {
          item.tags = []
        })
    )

    await Promise.all(tagRequests)

    tableData.value = creations
    total.value = res.data.total

  } catch (err) {
    ElMessage.error(err.msg || "获取内容失败")
  }
}

const handleReset = () => {
  titleKeyword.value = ''
  selectedSearchClassId.value = null
  selectedSearchTagId.value = null
  pageNum.value = 1
  fetchData()
}


const fetchSearchOptions = async () => {
  try {
    const [classRes, tagRes] = await Promise.all([
      creationClassList({pageNum: 1, pageSize: 100}),
      creationTagList({pageNum: 1, pageSize: 100})
    ])

    SearchClassOptions.value = classRes.data.items || []
    SearchTagOptions.value = tagRes.data.items || []
  } catch (err) {
    ElMessage.error("分类或标签加载失败")
  }
}

const openTagDialog = async (creation_id) => {
  try {
    const res = await creationTagList({pageNum: 1, pageSize: 100})
    const allTags = res.data.items || []

    const tagRes = await creationGetTags(creation_id)
    const existingTags = tagRes.data || []

    const existingTagIds = new Set(existingTags.map(tag => tag.tag_id))

    tagOptions.value = allTags.filter(tag => !existingTagIds.has(tag.tag_id))

    currentCreationId.value = creation_id
    tagDialogVisible.value = true
  } catch (err) {
    ElMessage.error("标签加载失败")
  }
}

const openClassDialog = (creation_id) => {
  currentCreationId.value = creation_id
  classDialogVisible.value = true
}

const openContentDialog = async (creation_id) => {
  try {
    const res = await creationInfo(creation_id)
    contentDetail.value = res.data
    contentDialogVisible.value = true
  } catch (err) {
    ElMessage.error(err.msg || "获取内容详情失败")
  }
}

const connectTag = async () => {
  if (!selectedTagId.value) return ElMessage.warning("请选择标签")
  try {
    await creationConnectTag(currentCreationId.value, selectedTagId.value)
    ElMessage.success("关联成功")
    tagDialogVisible.value = false
    selectedTagId.value = null
    fetchData()
  } catch (err) {
    ElMessage.error(err.msg || "关联失败")
  }
}

const confirmClassChange = async () => {
  if (!selectedClassId.value) return ElMessage.warning("请选择分类")
  try {
    await creationChangeClass(currentCreationId.value, selectedClassId.value)
    ElMessage.success("分类更改成功")
    classDialogVisible.value = false
    selectedClassId.value = null
    fetchData()
  } catch (err) {
    ElMessage.error(err.msg || "分类更改失败")
  }
}

const handleRemoveTag = async (creation_id, tag_id) => {
  try {
    await creationCancelConnectTag(creation_id, tag_id)
    ElMessage.success("标签已取消关联")
    fetchData()
  } catch (err) {
    ElMessage.error(err.msg || "取消关联失败")
  }
}

const handleReject = async (row) => {
  const comment = commentMap.value[row.creation_id]
  if (!comment || comment.trim() === '') {
    ElMessage.warning("请输入打回原因")
    return
  }
  try {
    const params = new URLSearchParams()
    params.append('creation_id', row.creation_id)
    params.append('examine', 1)
    params.append('review_comments', comment)

    await creationExamine(params)
    ElMessage.success("已打回")
    fetchData()
  } catch (err) {
    ElMessage.error(err.msg || "操作失败")
  }
}

const handlePageChange = (newPage) => {
  pageNum.value = newPage
  fetchData()
}

onMounted(() => {
  fetchData()
  fetchSearchOptions()
})
</script>

<template>
  <el-row :gutter="0">
    <div class="path">
      <p style="font-size: 20px; margin-top: 5px">内容管理</p>
    </div>

    <div class="search">
      <el-row :gutter="10" style="padding: 10px">
        <el-col :span="6">
          <el-input
              v-model="titleKeyword"
              placeholder="请输入标题关键词"
              clearable
              style="width: 100%"
          />
        </el-col>

        <el-col :span="6">
          <el-select v-model="selectedSearchClassId" placeholder="请选择分类" clearable style="width: 100%">
            <el-option
                v-for="cls in SearchClassOptions"
                :key="cls.class_id"
                :label="cls.class_name"
                :value="cls.class_id"
            />
          </el-select>
        </el-col>

        <el-col :span="6">
          <el-select v-model="selectedSearchTagId" placeholder="请选择标签" clearable style="width: 100%">
            <el-option
                v-for="tag in SearchTagOptions"
                :key="tag.tag_id"
                :label="tag.tag_name"
                :value="tag.tag_id"
            />
          </el-select>
        </el-col>

        <el-col :span="4">
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="handleReset">
            重置
          </el-button>
        </el-col>
      </el-row>

    </div>

    <el-table :data="tableData" style="width: 100%">
      <el-table-column prop="creation_id" label="内容ID" width="120px"/>
      <el-table-column prop="user_id" label="用户ID" width="120px"/>
      <el-table-column label="封面" width="200px">
        <template #default="{ row }">
          <img :src="row.cover_pic" alt="封面" style="width: 200px; height: 100px; object-fit: cover;"/>
        </template>
      </el-table-column>
      <el-table-column prop="title" label="标题" width="160px"/>
      <el-table-column prop="class_name" label="分类名" width="120px"/>
      <el-table-column label="更改分类" width="120px">
        <template #default="{ row }">
          <el-button size="small" @click="openClassDialog(row.creation_id)">更改</el-button>
        </template>
      </el-table-column>
      <el-table-column label="关联的标签" width="300px">
        <template #default="{ row }">
          <el-tag
              v-for="tag in row.tags"
              :key="tag.tag_id"
              closable
              @close="handleRemoveTag(row.creation_id, tag.tag_id)"
              style="margin-right: 5px; cursor: pointer;"
          >
            {{ tag.tag_name }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="关联标签" width="120px">
        <template #default="{ row }">
          <el-button size="small" @click="openTagDialog(row.creation_id)">添加标签</el-button>
        </template>
      </el-table-column>
      <el-table-column label="查看内容" width="120px">
        <template #default="{ row }">
          <el-button size="small" @click="openContentDialog(row.creation_id)">查看</el-button>
        </template>
      </el-table-column>
      <el-table-column label="打回原因" width="240px">
        <template #default="{ row }">
          <el-input v-model="commentMap[row.creation_id]" placeholder="请输入原因" size="small"/>
        </template>
      </el-table-column>
      <el-table-column label="打回稿件" width="120px">
        <template #default="{ row }">
          <el-button size="small" type="danger" @click="handleReject(row)">打回</el-button>
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

    <!-- 标签选择弹窗 -->
    <el-dialog v-model="tagDialogVisible" title="选择标签" width="30%">
      <el-select v-model="selectedTagId" placeholder="请选择标签" style="width: 100%">
        <el-option
            v-for="tag in tagOptions"
            :key="tag.tag_id"
            :label="tag.tag_name"
            :value="tag.tag_id"
        />
      </el-select>
      <template #footer>
        <el-button @click="tagDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="connectTag">确认</el-button>
      </template>
    </el-dialog>

    <!-- 分类修改弹窗 -->
    <el-dialog v-model="classDialogVisible" title="更改分类" width="30%">
      <el-select v-model="selectedClassId" placeholder="请选择新分类" style="width: 100%">
        <el-option
            v-for="cls in SearchClassOptions"
            :key="cls.class_id"
            :label="cls.class_name"
            :value="cls.class_id"
        />
      </el-select>
      <template #footer>
        <el-button @click="classDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmClassChange">确认</el-button>
      </template>
    </el-dialog>

    <!-- 查看内容弹窗 -->
    <el-dialog v-model="contentDialogVisible" title="内容详情" width="50%">
      <div v-if="contentDetail">
        <h3>{{ contentDetail.title }}</h3>
        <img :src="contentDetail.cover_pic" alt="封面"
             style="width: 100%; max-height: 300px; object-fit: contain; margin-bottom: 20px;"/>
        <div v-html="contentDetail.content"></div>
      </div>
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
  background-color: #bbd2e3;
}

::v-deep(.el-table__row) {
  height: 100px;
}
</style>
