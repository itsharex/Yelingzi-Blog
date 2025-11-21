<template>
  <CommonLayout :title="photoInfo.albumName" :bg-img="photoInfo.albumCover" />
  <div class="bg">
    <div class="page-container" :loading="albumloading">
      <div v-if="!albumloading">
        <div class="image-layout-tool" v-if="!isMobi" style="margin-right: 10px">
          <el-segmented v-model="imageLayout" :options="options">
            <template #default="{ item }">
              <div class="img-icon-warp pointer">
                <SvgIcon :name="item.icon" :size="32" />
              </div>
            </template>
          </el-segmented>
        </div>
        <ImageList v-if="imageList.length > 0" :key="imageLayout" :image-layout="imageLayout" :image-list="imageList">
        </ImageList>
        <div class="empty" v-else>
          <Empty :loading="false" text="当前相册为空">
          </Empty>
        </div>
      </div>
      <div class="empty" v-else>
        <Empty :loading="loading">
          <div class="empty-action pointer">
            <proButton v-if="!loading" info="重新加载" before="#ed6ea0" after="#9cd0ed" width="120px" @click="getImageList">
            </proButton>
          </div>
        </Empty>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import CommonLayout from '@/components/Layout/CommonLayout.vue';
import ImageList from '@/components/Image/ImageList.vue';
import type { Photo, PhotoInfo } from '@/types/album';
import { ref, onMounted, defineAsyncComponent } from 'vue';
import { useResize } from '@/utils/common';
import { getAlbumDataService } from '@/api/album'
import Empty from '@/components/Empty/Empty.vue';
import proButton from '@/components/Button/proButton.vue';

interface Props {
  id: number
}

const props = withDefaults(defineProps<Props>(), {
  id: 0
})
const isMobi = useResize()
const loading = ref(true);
const albumloading = ref(true);
const photoInfo = ref<PhotoInfo>({
  id: 0,
  albumCover: "",
  albumDesc: "",
  createTime: "",
  albumName: "",
  photoList: [] as Photo[],
});

const imageList = ref<string[]>([])

type ImageLayout = 24 | 16 | 8
const imageLayout = ref<ImageLayout>(24)
const options = [
  { value: 24 as ImageLayout, icon: 'icon-shop_c_grid' },
  { value: 16 as ImageLayout, icon: 'icon-layout-grid2-alt' },
  { value: 8 as ImageLayout, icon: 'icon-layout-grid3-alt' },
]

const getImageList = async () => {
  loading.value = true;
  albumloading.value = true;
  try {
    const res = await getAlbumDataService(props.id)
    photoInfo.value = res.data.data
    imageList.value = photoInfo.value.photoList.map(item => item.photoUrl);
    albumloading.value = false;
  } catch {
    loading.value = false;
  }

}
onMounted(() => {
  getImageList()
})

</script>

<style lang="scss" scoped>
.image-layout-tool {
  margin-bottom: 1.2rem;
  display: flex;
  justify-content: flex-end;

  .img-icon-warp {
    border-radius: var(--border-radius-1);
    border: 1px solid var(--border-color);
    display: flex;
    justify-content: center;
    align-items: center;
    padding: 12px 18px;
  }

}

:deep(.el-segmented__item) {
  padding: 0;
}

.empty {
  width: 100%;
  display: flex;
  justify-content: center;
  margin-top: 50px;
}

.empty-action {
  display: flex;
  justify-content: center;
  margin-bottom: 24px;
}
</style>
