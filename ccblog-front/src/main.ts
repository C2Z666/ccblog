import './assets/main.css'

import { createApp, defineComponent, h } from 'vue'
import { createPinia } from 'pinia'
import ElementPlus  from 'element-plus'
import zhCn from 'element-plus/es/locale/lang/zh-cn'
// 导入css样式
import 'element-plus/dist/index.css'
import "./index.css"

import App from './App.vue'
import router from './router'


// markdown插件

// 创建一个 H2 组件映射到原生 h2 标签
const H2 = defineComponent({
  setup(_, { slots }) {
    return () => h('h2', slots.default ? slots.default() : [])
  }
})
const app = createApp(App)

// eslint-disable-next-line vue/no-reserved-component-names,vue/multi-word-component-names
app.component('H2', H2)

app.use(createPinia())
app.use(router)

// ....
// 添加KaTeX相关导入
import 'katex/dist/katex.min.css';
import katex from 'katex';
import { config } from 'md-editor-v3';

// 配置md-editor-v3的KaTeX支持
// 直接在KaTeX实例上配置选项，避免TypeScript类型错误
const katexInstance = katex
// 确保KaTeX实例存在
if (katexInstance && typeof katexInstance.render === 'function') {
  // 保存原始render方法
  const originalRender = katexInstance.render
  // 重写render方法，添加默认选项
  katexInstance.render = function(expression, element, options = {}) {
    const defaultOptions = {
      throwOnError: false,
      displayMode: true,
      trust: true,
      ...options
    }
    return originalRender.call(this, expression, element, defaultOptions)
  }
}

config({
  editorExtensions: {
    katex: {
      instance: katexInstance
    }
  }
});

app.use(ElementPlus, {
  locale: zhCn
}).mount('#app')


// 美容包(界面美化用的)
import { CaretTop, ChatDotRound, ChatSquare } from '@element-plus/icons-vue'
import { ArrowDown } from '@element-plus/icons-vue'
app.component('ArrowDown', ArrowDown)
app.component('CaretTop', CaretTop)
app.component('ChatDotRound', ChatDotRound)
app.component('ChatSquare', ChatSquare)

// 导入必要的store和常量
import { useGlobalStore } from './stores/global'
import { startCacheRefresh } from './stores/useFollowIdCache'
import { LOCALSTORAGE_AUTHORIZATION } from './constants/LocalStorageConstants'

// 应用初始化后检查用户登录状态并启动粉丝缓存刷新
const checkLoginStatus = () => {
  // 检查是否有保存的授权token
  const token = localStorage.getItem(LOCALSTORAGE_AUTHORIZATION)
  if (token) {
    // 由于可能存在异步状态更新，使用定时器延迟执行，确保能够获取到最新状态
    setTimeout(() => {
      const globalStore = useGlobalStore()
      // 如果用户已登录，启动缓存刷新
      if (globalStore.global?.isLogin && globalStore.global?.user?.userId) {
        startCacheRefresh(Number(globalStore.global.user.userId))
      }
    }, 500)
  }
}

// 执行登录状态检查
checkLoginStatus()