<template>
  <div class="editor-wrapper" v-if="visible">
    <Toolbar v-if="editorRef" :editor="editorRef" mode="default" />
    <Editor
        v-model="localValue"
        :defaultConfig="editorConfig"
        mode="default"
        style="height: 300px; width: 100%; overflow-y: auto"
        @onCreated="handleEditorCreated"
    />
  </div>
</template>

<script setup>
import {ref, watch, onBeforeUnmount, shallowRef} from 'vue'
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'
import '@wangeditor/editor/dist/css/style.css'
import { uploadFile } from '@/api/fileUpload'
import { ElMessage } from 'element-plus'

const props = defineProps({
  modelValue: String,
  visible: Boolean
})
const emit = defineEmits(['update:modelValue'])

const editorRef = shallowRef()
const localValue = ref(props.modelValue)

watch(() => props.modelValue, (val) => {
  localValue.value = val
})

watch(localValue, (val) => {
  emit('update:modelValue', val)
})

const editorConfig = {
  placeholder: '请输入内容...',
  MENU_CONF: {
    uploadImage: {
      async customUpload(file, insertFn) {
        try {
          const res = await uploadFile(file)
          insertFn(String(res.data), '', '')
          ElMessage.success('图片上传成功')
        } catch {
          ElMessage.error('图片上传失败')
        }
      }
    }
  }
}

watch(() => props.visible, (val) => {
  if (!val && editorRef.value) {
    editorRef.value.destroy()
    editorRef.value = null
  }
})

const handleEditorCreated = (editor) => {
  editorRef.value = editor
}

onBeforeUnmount(() => {
  if (editorRef.value) {
    editorRef.value.destroy()
    editorRef.value = null
  }
})
</script>
