import { createApp } from 'vue'
import { createPinia } from 'pinia'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'


//https://element-plus.org/en-US/guide/quickstart.html  참고
//https://axios-http.com/kr/docs/intro 참고
//https://getbootstrap.com/ 참고
import App from './App.vue'
import router from './router'
import "bootstrap/dist/css/bootstrap-utilities.css"




// import './assets/main.css'

const app = createApp(App)

app.use(createPinia())
app.use(router)
app.use(ElementPlus)

app.mount('#app')
