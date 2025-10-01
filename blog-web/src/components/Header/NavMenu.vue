<template>
  <div class="menu-container">
    <div v-for="item in filteredMenuItems" :key="item.path" class="nav-item" @mouseleave="handleMouseLeave">
      <div @click="handleDropdownItemClick(item)" class="nav-link pointer" :class="{
        'has-dropdown': item.children,
        'active': isActive(item),
        [item.colorClass]: true
      }" @mouseenter="handleMouseEnter(item)" v-pio="{ text: `${item.name}`, type: 'link' }">
        <SvgIcon :name="item.icon" class="svg-icon" />
        {{ item.name }}
        <SvgIcon name="icon-xiala" v-if="item.children" class="dropdown-icon svg-icon" />
      </div>

      <div v-if="item.children" class="dropdown-menu" :class="{ active: activeDropdown === item.name }">
        <a href="javascript:void(0)" v-for="child in item.children" :key="child.path" class="dropdown-item"
          :class="{ 'active': isChildActive(child) }" @click="handleDropdownItemClick(child)"
          v-pio="{ text: `${child.name}`, type: 'link' }">
          <SvgIcon :name="child.icon" class="svg-icon" />
          {{ child.name }}
        </a>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { getMenuItems, type MenuItem } from '@/types/menu';
import { t } from '@/utils/i18n';
import { computed, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';


const route = useRoute()
const router = useRouter()
const activeDropdown = ref<string | null>(null)
const menuItems = computed(() =>
  getMenuItems().map(item => ({
    ...item,
    name: t(item.name),
    children: item.children?.map(child => ({
      ...child,
      name: t(child.name)
    }))
  }))
)
// 计算属性
const filteredMenuItems = computed(() =>
  menuItems.value.map(item => ({
    ...item,
    path:
      item.children && item.children.length && item.path === ''
        ? item.children[0].path
        : item.path
  }))
)


const handleMouseLeave = () => {
  activeDropdown.value = null
}
const handleMouseEnter = (item: MenuItem) => {
  if (item.children) {
    activeDropdown.value = item.name
  }
}
const handleDropdownItemClick = (item: MenuItem) => {
  activeDropdown.value = null
  if (item.external) {
    const newWindow = window.open(item.path, '_blank')
    newWindow!.opener = null;
    return
  }
  router.push(item.path)
}

const isActive = (item: MenuItem): boolean => {
  if (route.path === item.path) {
    return true
  }
  if (item.children) {
    return item.children.some(child => isChildActive(child))
  }
  return false
}

const isChildActive = (child: MenuItem): boolean => {
  return route.path === child.path
}


</script>

<style lang="scss" scoped>
@use '/src/assets/styles/variables.scss' as va;

.menu-container {
  display: flex;
}

.nav-item {
  position: relative;
  white-space: nowrap;


  &:hover {
    .dropdown-menu {
      opacity: 1;
      visibility: visible;
      transform: translateX(-50%) translateY(0);
      pointer-events: auto;
    }

  }
}

.nav-link {
  color: var(--color-black);
  text-decoration: none;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  gap: va.$spacing-sm;
  padding: va.$spacing-sm;
  border-radius: va.$border-radius-md;
  font-size: 16px;
  position: relative;
  height: 40px;
  line-height: normal;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;

  .svg-icon {
    flex: 0 0 16px;
    width: 16px;
    height: 16px;
    display: inline-flex;
    align-items: center;
    justify-content: center;
    position: relative;
    top: 0.5px;
  }

  &:hover,
  &.active {
    color: va.$primary;
    background: var(--hover-bg);
  }

  &.has-dropdown {
    .dropdown-icon {
      transition: transform 0.3s ease;
    }

    &:hover .dropdown-icon {
      transform: rotate(180deg);
    }
  }

  .menu-item {
    padding: 12px 20px;
    display: flex;
    align-items: center;
    line-height: normal;
    gap: 12px;
    color: var(--grey-1);
    text-decoration: none;
    transition: all 0.2s;

    &:hover {
      background: var(--hover-bg);
      color: va.$primary;
    }
  }

}

.dropdown-menu {
  position: absolute;
  top: calc(100% + 8px);
  left: 50%;
  transform: translateX(-50%) translateY(15px);
  background: var(--color-white);
  border-radius: va.$border-radius-md;
  box-shadow: va.$shadow-lg;
  width: max-content;
  min-width: 120px;
  opacity: 0;
  visibility: hidden;
  transition: all 0.25s cubic-bezier(0.4, 0, 0.2, 1);
  pointer-events: none;
  padding: va.$spacing-xs 0;

  &::before {
    content: '';
    position: absolute;
    top: -8px;
    left: 0;
    width: 100%;
    height: 8px;
    background: transparent;
  }

  .dropdown-item {
    display: flex;
    align-items: center;
    gap: va.$spacing-sm;
    padding: 8px va.$spacing-md;
    height: 36px;
    color: var(--color-black);
    text-decoration: none;
    transition: all 0.3s ease;
    white-space: nowrap;
    font-size: 0.9em;

    &:hover {
      color: va.$primary;
      background: var(--hover-bg);
    }
  }

  &.active {
    opacity: 1;
    visibility: visible;
    transform: translateX(-50%) translateY(0);
    pointer-events: auto;
  }

  @keyframes dropdownEnter {
    from {
      opacity: 0;
      transform: translateX(-50%) translateY(10px);
    }

    to {
      opacity: 1;
      transform: translateX(-50%) translateY(0);
    }
  }

  &.active {
    animation: dropdownEnter 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  }
}
</style>
