<template>
	<uni-section title="左侧滑出" type="line">
		<view class="example-body">
			<button type="primary" @tap="showDrawer('showLeft')"><text class="word-btn-white">显示Drawer</text>
			</button>
			<uni-drawer ref="showLeft" mode="left" :width="320" @change="change($event,'showLeft')">
				<view class="close">
					<button @tap="closeDrawer('showLeft')"><text class="word-btn-white">关闭Drawer</text></button>
				</view>
			</uni-drawer>
		</view>
	</uni-section>
</template>

<script setup>
	import {
		ref
	} from 'vue'
	const status = ref('loading')
	const onLoad = () => {
		status.value = 'load 成功'
	}
	const onError = (e) => {
		status.value = 'error ' + JSON.stringify(e)
	}
	const showLeft = ref()

	function showDrawer(e) {
		showLeft.value.open()
	}
	// 关闭窗口
	function closeDrawer(e) {
		showLeft.value.close()
	}
</script>