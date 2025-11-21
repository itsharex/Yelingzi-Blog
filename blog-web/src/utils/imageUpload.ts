import { ref } from 'vue'
import type { UploadFile, UploadRawFile, UploadProps } from 'element-plus'
import { ElMessage } from 'element-plus'
import { uploadImageService } from '@/api/common'

export const MAX = 5

export function useImageUpload() {
  const fileList = ref<UploadFile[]>([])
  const imageList = ref<string[]>([])   // 仅保存可访问的 url，用于预览

  /* 上传前校验：类型 + 数量 */
  const beforeUpload: UploadProps['beforeUpload'] = (rawFile: UploadRawFile) => {
    const allow = ['image/jpeg', 'image/jpg', 'image/png']
    if (!allow.includes(rawFile.type)) {
      ElMessage.error('仅支持 jpg / png 格式')
      return false
    }
    if (rawFile.size / 1024 / 1024 > 2) {
      ElMessage.error('单张图片不能超过 2MB')
      return false
    }
    if (fileList.value.length >= MAX) {
      ElMessage.warning(`最多上传 ${MAX} 张图片`)
      return false
    }
    return true
  }

  /* 真正上传 */
  const httpRequest: UploadProps['httpRequest'] = async ({ file }) => {
    const fd = new FormData()
    fd.append('image', file)
    try {
      const { data } = await uploadImageService(fd)
      const url = data.data as string
      imageList.value.push(url)
      ElMessage.success('上传成功')
    } catch {
      ElMessage.error('上传失败，请重试')
      // 失败时把占位文件踢掉
      fileList.value = fileList.value.filter((f) => f.uid !== (file as any).uid)
    }
  }

  /* 删除 */
  const onRemove: UploadProps['onRemove'] = (uploadFile) => {
    const idx = imageList.value.findIndex((u) => u === uploadFile.url)
    if (idx > -1) imageList.value.splice(idx, 1)
    fileList.value = fileList.value.filter((f) => f.uid !== uploadFile.uid)
  }

  return {
    fileList,
    imageList,
    beforeUpload,
    httpRequest,
    onRemove,
  }
}
