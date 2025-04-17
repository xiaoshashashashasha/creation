<template>
  <div class="editor-wrapper" v-if="visible">
    <Toolbar v-if="editorRef" :editor="editorRef" mode="default" />
    <Editor
        :defaultConfig="editorConfig"
        mode="default"
        style="height: 300px; width: 100%; overflow-y: auto"
        @onCreated="handleEditorCreated"
        @onChange="handleEditorChange"
    />
  </div>
</template>

<script setup>
import { shallowRef, watch, onBeforeUnmount, nextTick } from 'vue'
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'
import '@wangeditor/editor/dist/css/style.css'
import { uploadFile } from '@/api/fileUpload'
import { ElMessage } from 'element-plus'
import { decode } from 'html-entities'

const props = defineProps({
  modelValue: String,
  visible: Boolean
})
const emit = defineEmits(['update:modelValue'])

const editorRef = shallowRef()

const editorConfig = {
  placeholder: '请输入内容...',
  MENU_CONF: {
    uploadImage: {
      async customUpload(file, insertFn) {
        try {
          const res = await uploadFile(file)
          insertFn(res.data, '', '')
          ElMessage.success('图片上传成功')
        } catch {
          ElMessage.error('图片上传失败')
        }
      }
    }
  }
}

const handleEditorChange = (editor) => {
  const html = decode(editor.getHtml())
  emit('update:modelValue', html)
}

const handleEditorCreated = (editor) => {
  editorRef.value = editor
  nextTick(() => {
    if (props.modelValue) {
      editor.clear()
      editor.setHtml(props.modelValue)
    }
  })
}

watch(() => props.visible, (val) => {
  if (!val && editorRef.value) {
    editorRef.value.destroy()
    editorRef.value = null
  }
})

onBeforeUnmount(() => {
  if (editorRef.value) {
    editorRef.value.destroy()
    editorRef.value = null
  }
})
</script>

<style scoped>
.editor-wrapper {
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  overflow: hidden;
}
</style>
