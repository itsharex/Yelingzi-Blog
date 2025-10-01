<template>
  <div class="weather-info slider-item-warp" :class="{ 'intersection-observer': isMobi }"
    v-if="weather && weather.uptime">
    <div class="slider-title">
      <SvgIcon name="icon-diqiu" color="#222" />{{ t('accessInfo') }}
    </div>
    <el-divider style="margin: 12px 0 0 0;" />

    <component :is="weatherIcon" class="tianqi-icon-bg" />

    <div class="weather-content-warp">
      <div class="weather-item-warp" v-for="item in weatherData" :key="item.title">
        <div class="weather-item">
          <div class="weather-label">
            <SvgIcon :name="item.icon" />{{ t(item.title) }}：
          </div>
          <div class="weather-value">{{ item.value }}</div>
        </div>
      </div>
    </div>
  </div>
</template>


<script lang="ts" setup>
import { removeTFrontDateString, useResize } from '@/utils/common';
import { useUserStore } from '@/stores';
import { computed, defineAsyncComponent, h, onMounted, reactive, ref } from 'vue';
import { t } from '@/utils/i18n';
import { type Weather, type WeatherView } from '@/types/home';
import { getWeatherService } from '@/api/common';

const userStore = useUserStore();
const isMobi = useResize();

const weatherData = ref<WeatherView[]>([]);
const weather = reactive<Weather>({
  sheng: '',
  shi: '',
  weather1: '',
  weather2: '',
  wd1: '',
  wd2: '',
  winddirection1: '',
  winddirection2: '',
  windleve1: '',
  windleve2: '',
  lon: '',
  lat: '',
  uptime: '',
  nowinfo: {
    precipitation: '',
    temperature: '',
    pressure: '',
    humidity: '',
    windDirection: '',
    windDirectiondegree: '',
    windSpeed: '',
    windScale: '',
    feelst: '',
    uptime: ''
  }
})

// 获取当前小时数
const currentHour = computed(() => {
  return new Date().getHours();
});

// 判断是上午还是下午
const isMorning = computed(() => {
  return currentHour.value < 12;
});

// 动态图标计算属性（自动响应currentWeather变化）
const weatherIcon = computed(() =>
  defineAsyncComponent({
    loader: () => {
      const weatherDesc = isMorning.value ? weather?.weather1 || '' : weather?.weather2 || ''

      const matched = ['晴', '多云', '阴', '小雨', '中雨', '大雨', '阵雨', '暴雨', '小雪', '中雪', '大雪', '雨夹雪', '雾']
        .find(item => weatherDesc.includes(item)) || '阴'

      return import(`@/assets/icons/${matched}.svg`)
    },
    loadingComponent: () => h('div', '加载天气图标...'),
    errorComponent: () => h('div', '图标加载失败'),
    delay: 100,
    timeout: 2000
  })
);

// 设置天气数据
const setViewDate = () => {
  // 清空旧数据
  weatherData.value = [];

  // 基础信息
  weatherData.value.push({ icon: 'icon-chengshi', title: 'location', value: `${weather.sheng} - ${weather.shi}` });
  weatherData.value.push({ icon: 'icon-rili1', title: 'date', value: removeTFrontDateString(weather.uptime) });

  // 天气信息
  const currentWeather = isMorning.value ? weather.weather1 : weather.weather2;
  weatherData.value.push({ icon: 'icon-tianqi', title: 'weather', value: currentWeather });

  weatherData.value.push({ icon: 'icon-kongqiwendu', title: 'temperatureRange', value: `${weather.wd2} ~ ${weather.wd1} ℃` });

  const currentWindLevel = isMorning.value ? weather.windleve1 : weather.windleve2;
  weatherData.value.push({ icon: 'icon-fengli', title: 'wind', value: `${weather.nowinfo.windDirection} ${currentWindLevel}` });

  // 环境参数
  weatherData.value.push({ icon: 'icon-shidu', title: 'humidity', value: `${weather.nowinfo.humidity} %` });
  weatherData.value.push({ icon: 'icon-qiya', title: 'pressure', value: `${weather.nowinfo.pressure} hPa` });
  weatherData.value.push({ icon: 'icon-yuliang', title: 'precipitation', value: `${weather.nowinfo.precipitation} mm` });

  // 风速与体感
  weatherData.value.push({ icon: 'icon-fengsu', title: 'windSpeed', value: `${weather.nowinfo.windSpeed} km/h` });
  weatherData.value.push({ icon: 'icon-tiwenji', title: 'feelsLike', value: `${weather.nowinfo.feelst} ℃` });
};

/* ------------ 获取天气 ------------ */
const fetchWeather = async () => {

  const { data } = await getWeatherService(userStore.deviceId)

  Object.assign(weather, data.data)
  setViewDate();

}

onMounted(() => {
  fetchWeather();
});
</script>



<style lang="scss" scoped>
.weather-info {
  position: relative; // 必须，作为绝对定位图标的父容器
  display: flex;
  flex-direction: column;
  background-image: linear-gradient(#ffffff00, #caf3f4 50%, #b4f6e2 100%);
  border-radius: 10px;
  overflow: hidden; // 防止图标超出圆角

  .tianqi-icon-bg {
    position: absolute;
    top: 50px;
    right: 50px;
    width: 30%;
    height: auto;
    z-index: 1;
    animation: cloudAnimation 2s ease-in-out infinite;
  }

  .weather-content-warp {
    padding: 20px;
    position: relative;
    z-index: 2;
  }

  .weather-item-warp {
    display: flex;
    flex-direction: column;
    gap: 12px;
  }

  .weather-item {
    color: #222;
    display: flex;
    align-items: center;
    flex-wrap: wrap;
    padding-top: 8px;
    gap: 5px;

    .weather-label {
      display: flex;
      align-items: center;
      text-align: left;
    }

    .weather-label svg {
      margin-right: 8px;
    }

    .weather-value {
      flex: 1;
      word-break: break-all;
      white-space: pre-wrap;
    }
  }
}

.slider-title {
  display: flex;
  align-items: center;
  gap: 5px;
  margin-left: 10px;
}

.slider-title svg {
  vertical-align: middle;
  line-height: 1;
}

@keyframes cloudAnimation {
  0% {
    transform: translateX(0);
  }

  50% {
    transform: translateX(30px);
  }

  100% {
    transform: translateX(0);
  }
}
</style>
