import { createSSRApp } from 'vue'
import App from './App.vue'

//pina全局状态
import { createPinia } from 'pinia'
import piniaStoragePlugin from './js/plugin/piniaStoragePlugin'
const pinia = createPinia()
pinia.use(piniaStoragePlugin({
    keepId: ["tags","menu","user","direct","conf"],
}))
import  store from '@/pina'


//router路由
import pages from './pages.json'
import error404 from '@/pages/admin/error/error404.vue'
import { createRouter } from '@wzabcd/u-ui'
const routes = {
    error: {
        path: '/pages/admin/error/error404',
        name: 'error404',
        component: error404,
    },
    first: (localStorage.getItem("userTagActive") == '0' || localStorage.getItem("adminLoginToOneUser") == "1") ? config.userTag.path : config.tag.path,
    //前置守卫
    before: (to, from, next) => {
        const {tag} =store();
        console.log("前置守卫", to, from);
        //添加tab标签
        tag.addTag(to)
        //继续跳转
        next()
    },
    //后置守卫
    after: (to, from, next) => {

    }
}



//其他
//配置信息
import { config } from './config'
//拦截器
import { uniInterceptor } from './js/plugin/plugin'
//初始化
import { init } from "./js/admin/init";
init()

//监测浏览器刷新
if (document) {
    window.onload = function () {
        //刷新跳转首页
        const {tag} =store();
        tag.delAll()
        if (localStorage.getItem("userTagActive") == '0' || localStorage.getItem("adminLoginToOneUser") == "1") {
            // 取水户登录或者admin登录切换了一户一档 或者取水户
            // store.commit("set_user_tag")
            tag.addTag(config.userTag.path)
        } else {
           tag.addTag(config.tag.path)
        }
    }
}




//start启动
export function createApp() {
    const app = createSSRApp(App);
    app.use(pinia)
    const router = createRouter(routes)
    app.use(router)
    uniInterceptor(config, pages);
    //全局路由
    uni.$router = router
    return {
        app
    };
}
