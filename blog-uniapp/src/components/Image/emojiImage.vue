<template>
  <ImageWithFallback
    :src="imgSrc"
    class="image-mapper"
	:lazy="false"
	:preview="false"
  />
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { textToImageMap } from '@/utils/mapping'
import { APP_BASE_URL } from '@/utils/config'
import ImageWithFallback from './ImageWithFallback.vue'

interface Props {
  text: string
  basePath?: string
}
const props = withDefaults(defineProps<Props>(), {
  basePath: `${APP_BASE_URL}/images/emoji/`
})

const imgSrc = computed(() => {
  const fileName = textToImageMap[props.text]
  if (!fileName) return ''
  
  return `${props.basePath}${fileName}.jpg`
})
</script>

<style scoped>
.image-mapper {
  width: 100%;
  height: 100%;
  display: inline-block;
}
</style>