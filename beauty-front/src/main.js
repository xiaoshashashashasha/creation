import {createApp} from 'vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import App from './App.vue'
import router from './router'
import {createPinia} from 'pinia'
import {createPersistedState} from 'pinia-persistedstate-plugin'


const pinia = createPinia();
const app = createApp(App);
const persistedState = createPersistedState();
pinia.use(persistedState)
app.use(pinia);
app.use(router);
app.use(ElementPlus);
app.mount('#app');
