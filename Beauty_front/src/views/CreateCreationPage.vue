<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { uploadFile } from '@/api/fileUpload'
import { addCreation, creationClassList } from '@/api/creation'
import RichTextEditor from '@/components/part/RichTextEditor.vue'
import {ArrowLeft} from "@element-plus/icons-vue";

const router = useRouter()

const coverPreviewUrl = ref('')
const coverImageFile = ref(null)

const form = ref({
  title: '',
  abs_text: '',
  cover_pic: '',
  content: '<p><br></p>',
  class_id: null
})

const classOptions = ref([])
const loading = ref(false)

const handleUpload = ({ file }) => {
  coverImageFile.value = file
  coverPreviewUrl.value = URL.createObjectURL(file)
}


const handleSubmit = async () => {
  const { title, abs_text, content, class_id } = form.value
  const isEmptyContent = !content || content.trim() === '' || content === '<p><br></p>'

  if (!title || !abs_text || (!form.value.cover_pic && !coverPreviewUrl.value) || isEmptyContent || !class_id) {
    return ElMessage.warning('请完整填写所有信息')
  }

  loading.value = true
  try {
    // 统一处理图片上传
    if (coverImageFile.value) {
      const res = await uploadFile(coverImageFile.value)
      form.value.cover_pic = res.data
    }

    await addCreation({ ...form.value })
    ElMessage.success('发布成功')
    router.back()
  } catch (err) {
    ElMessage.error('发布失败')
  } finally {
    loading.value = false
  }
}

const fetchClassOptions = async () => {
  try {
    const res = await creationClassList({ pageNum: 1, pageSize: 100 })
    classOptions.value = res.data.items || []

    // 默认选择第一个分类
    if (!form.value.class_id && classOptions.value.length > 0) {
      form.value.class_id = classOptions.value[0].class_id
    }
  } catch (err) {
    ElMessage.error('获取分类失败')
  }
}

onMounted(() => {
  fetchClassOptions()
})
</script>


<template>
  <div class="creation-page">
    <div class="header">
      <el-button :icon="ArrowLeft"  @click="router.back()">返回</el-button>
    </div>

    <div class="form-section">
      <el-form :model="form" label-width="80px">
        <el-form-item label="封面">
          <div class="upload-box">
            <img v-if="coverPreviewUrl" :src="coverPreviewUrl" class="cover-img" />
            <img v-else-if="form.cover_pic" :src="form.cover_pic" class="cover-img" />
            <el-upload :http-request="handleUpload" :show-file-list="false" accept="image/*">
              <el-button type="primary">上传封面</el-button>
            </el-upload>
          </div>
        </el-form-item>

        <el-form-item label="分类">
          <el-select v-model="form.class_id" placeholder="请选择分类" style="width: 160px">
            <el-option
                v-for="cls in classOptions"
                :key="cls.class_id"
                :label="cls.class_name"
                :value="cls.class_id"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="标题">
          <el-input v-model="form.title" placeholder="1~16字" maxlength="16" style="width: 200px" show-word-limit />
        </el-form-item>

        <el-form-item label="摘要">
          <el-input v-model="form.abs_text" placeholder="1~30字" maxlength="30" style="width: 400px;" show-word-limit />
        </el-form-item>
      </el-form>
    </div>

    <div class="editor-section">
      <RichTextEditor v-model="form.content" :visible="true" />
    </div>

    <div class="submit-btn">
      <el-button type="success" @click="handleSubmit" :loading="loading">发布</el-button>
    </div>
  </div>
</template>

<style scoped>
.creation-page {
  width: 1720px;
  margin: 0 auto;
  padding: 20px;
  background-color: #fff;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.header {
  margin-bottom: 20px;
}

.upload-box {
  display: flex;
  align-items: center;
  gap: 20px;
}

.cover-img {
  width: 200px;
  height: 120px;
  object-fit: cover;
  border-radius: 4px;
  border: 1px solid #eee;
}

.form-section {
  margin-bottom: 30px;
}

.editor-section {
  margin-bottom: 30px;
}

.submit-btn {
  text-align: center;
  margin-top: 30px;
}
</style>
