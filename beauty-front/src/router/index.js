import {createRouter,createWebHistory} from 'vue-router'


import Login_Register from "@/views/Login_Register.vue";
import HomePage from "@/views/HomePage.vue";
import ManagePage from "@/views/ManagePage.vue";
import ChangeRole from "@/views/manage/user/ChangeRole.vue";
import ClassManage from "@/views/manage/creation/ClassManage.vue";
import TagManage from "@/views/manage/creation/TagManage.vue";
import CreationManage from "@/views/manage/creation/CreationManage.vue";
import CreationExamine from "@/views/manage/creation/CreationExamine.vue";
import HairstyleManage from "@/views/manage/hairstyle/HairstyleManage.vue";
import RequestManage from "@/views/manage/offline/RequestManage.vue";


//定义路由关系
const routes = [
    {path: "/login",component: Login_Register},
    {path:"/",component:HomePage},
    {path:"/manage",component:ManagePage,children:[
            {path:"/user/change-role",component:ChangeRole},
            {path: "/creation/class-manage",component:ClassManage},
            {path: "/creation/tag-manage",component:TagManage},
            {path:"/creation/creation-manage",component:CreationManage},
            {path:"/creation/creation-examine",component:CreationExamine},
            {path: "/hairstyle/hairstyle-manage",component:HairstyleManage},
            {path: "/offline/request-manage",component:RequestManage},
        ]}
]

//创建路由器
const router = createRouter({
    history: createWebHistory(),
    routes: routes
})

//导出路由器
export default router;

