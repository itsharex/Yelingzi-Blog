<template>
  <div class="side-card">
    <Author></Author>
    <VisitInfo />
    <particleClock></particleClock>
    <TagCloud></TagCloud>
  </div>
</template>

<script setup lang="ts">
import Author from './Author.vue';
import VisitInfo from './VisitInfo.vue';
import particleClock from '../Clock/particleClock.vue';
import TagCloud from './SideBarTagCloud.vue';

</script>

<style lang="scss" scoped>
.side-card {
  display: flex;
  flex-direction: column;
  gap: 20px;
}
</style>
