import './assets/main.css'
import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import zhCn from 'element-plus/dist/locale/zh-cn.mjs' // 导入中文语言包

const app = createApp(App)

app.use(router)
// app.use(ElementPlus)
app.use(ElementPlus, {
  locale: zhCn, // 设置为中文
})
app.mount('#app')
