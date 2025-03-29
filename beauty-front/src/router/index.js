import {createRouter,createWebHistory} from 'vue-router'


import Login_Register from "@/views/Login_Register.vue";
import HomePage from "@/views/HomePage.vue";
import ManagePage from "@/views/ManagePage.vue";

//定义路由关系
const routes = [
    {path: "/login",component: Login_Register},
    {path:"/",component:HomePage},
    {path:"/manage",component:ManagePage}
]

//创建路由器
const router = createRouter({
    history: createWebHistory(),
    routes: routes
})

//导出路由器
export default router;

