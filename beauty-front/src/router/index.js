import {createRouter, createWebHistory} from 'vue-router'
import {useTokenStore} from "@/stores/token";

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
import ContentPage from "@/views/ContentPage.vue";
import CreationsPage from "@/views/CreationsPage.vue";
import HairstylesPage from "@/views/HairstylesPage.vue";
import OfflinesPage from "@/views/OfflinesPage.vue";
import MyInfoPage from "@/views/Mine/MyInfoPage.vue";
import MyWallet from "@/views/Mine/MyWallet.vue";
import MyCreations from "@/views/Mine/MyCreations.vue";
import MyRequest from "@/views/Mine/MyRequest.vue";
import MyOffline from "@/views/Mine/MyOffline.vue";
import MyMember from "@/views/Mine/myMember.vue";
import OtherInfoPage from "@/views/OtherInfoPage.vue";
import OtherCreationPage from "@/views/OtherCreationPage.vue";
import MyFavorites from "@/views/Mine/MyFavorites.vue";
import CreateCreationPage from "@/views/CreateCreationPage.vue";
import MyReservation from "@/views/Mine/MyReservation.vue";
import MyOfflineReservation from "@/views/Mine/MyOfflineReservation.vue";


//定义路由关系
const routes = [
    {path: "/login", component: Login_Register},
    {
        path: "/",
        component: HomePage,
        meta: {requiresAuth: true}
    },
    {
        path: "/content/:type/:id",
        component: ContentPage,
        meta: {requiresAuth: true}
    },
    {
        path: "/creations",
        component: CreationsPage,
        meta: {requiresAuth: true}
    },
    {
        path: "/hairstyles",
        component: HairstylesPage,
        meta: {requiresAuth: true}
    },
    {
        path: "/offlines",
        component: OfflinesPage,
        meta: {requiresAuth: true}
    },
    {
        path: "/create",
        component: CreateCreationPage,
        meta: {requiresAuth: true}
    },
    {
        path: "/myInfo",
        component: MyInfoPage,
        meta: {requiresAuth: true}
    },
    {
        path: "/myWallet",
        component: MyWallet,
        meta: {requiresAuth: true}
    },
    {
        path: "/myFavorites",
        component: MyFavorites,
        meta: {requiresAuth: true}
    },
    {
        path: "/myReservation",
        component: MyReservation,
        meta: {requiresAuth: true}
    },
    {
        path: "/myCreation",
        component: MyCreations,
        meta: {requiresAuth: true}
    },
    {
        path: "/myRequest",
        component: MyRequest,
        meta: {requiresAuth: true}
    },
    {
        path: "/myOffline",
        component: MyOffline,
        meta: {requiresAuth: true}
    },
    {
        path: "/myOffline/:id/members",
        component: MyMember,
        meta: {requiresAuth: true}
    },
    {
        path: "/myOffline/:offline_id/reservation",
        component: MyOfflineReservation,
        meta: {requiresAuth: true}
    },
    {
        path: "/otherInfo/:id",
        component: OtherInfoPage,
        meta: {requiresAuth: true}
    },
    {
        path: "/otherCreation/:id",
        component: OtherCreationPage,
        meta: {requiresAuth: true}
    },
    {
        path: "/manage",
        component: ManagePage,
        children: [
            {path: "/manage/user/change-role", component: ChangeRole},
            {path: "/manage/creation/class-manage", component: ClassManage},
            {path: "/manage/creation/tag-manage", component: TagManage},
            {path: "/manage/creation/creation-manage", component: CreationManage},
            {path: "/manage/creation/creation-examine", component: CreationExamine},
            {path: "/manage/hairstyle/hairstyle-manage", component: HairstyleManage},
            {path: "/manage/offline/request-manage", component: RequestManage},
        ],
        meta: {requiresAuth: true}
    }
]

//创建路由器
const router = createRouter({
    history: createWebHistory(),
    routes: routes
})

router.beforeEach((to, from, next) => {
    const tokenStore = useTokenStore()
    const token = tokenStore.token

    if (to.meta.requiresAuth && !token) {
        next('/login')
    } else {
        next()
    }
})

//导出路由器
export default router;

