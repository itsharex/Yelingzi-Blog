import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vueDevTools from 'vite-plugin-vue-devtools'
import svgLoader from 'vite-svg-loader'
import compression from 'vite-plugin-compression'
import dayjs from 'dayjs'
import uni from "@dcloudio/vite-plugin-uni";

// https://vitejs.dev/config/
export default defineConfig(({}) => {


	const appFullVersion = `YelingziBlog_1.0.0_${dayjs().format(
		'YYYYMMDDHHmmss'
	)}`

	return {
		define: {
			__APP_FULL_VERSION__: JSON.stringify(appFullVersion),
		},

		plugins: [
			uni(),
			vueDevTools(),
			svgLoader(),
			compression({
				algorithm: 'gzip',
				threshold: 12800,
				deleteOriginFile: false,
			}),
		],
		resolve: {
			alias: {
				'@': fileURLToPath(new URL('./', import.meta.url)),

			},
		},
		css: {
			preprocessorOptions: {
				scss: {
					additionalData: `
            @use "@/static/styles/main.scss";
            @use "@/static/styles/mixin.scss";
          `,
				},
			},
		},
		build: {
			target: 'es2022',
			commonjsOptions: { transformMixedEsModules: true },
			assetsInlineLimit: 0,
		},
		optimizeDeps: { include: ['lodash-es', 'md-editor-v3'] },
	}
})