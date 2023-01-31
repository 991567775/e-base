//true 微服务; false单独服务;
let microService = true;
//后端访问地址
//开发
let url = process.env.NODE_ENV == 'development' ? 'http://127.0.0.1:1993' : 'https://test.tonhai.cn/api'
//
// let url=process.env.NODE_ENV == 'development'?'https://dc.tonhai.cn':'https://www.ezeyc.net'
//默认服务名【如需请求其他模块在请求中添加service值】
let service = microService ? "/water" : "";
//基础配置
let base = {
    port: 1990,
    loginPath: '/pages/admin/login/login',
    bgPath: '/pages/admin/base/base',
    tag: {
        label: "控制台",
        path: "/pages/admin/base/kzt",
        close: false
    },
    appBgName: '建湖县水资源管理平台',
    appBgBottom: '底部',
    wxMini: {
        use: true,
        method: "GET",
        path: "/user/loginByWx"
    },
    aliMini: {
        use: true,
        method: "POST",
        path: "/system/login/loginByThirdDefault"
    },
}
// 常见文件预览支持的后缀
const fileSuffix = ['doc', 'docx', 'xls', 'xlsx', 'pdf', 'txt'];
//下面非必要不需要修改
//后端请求前缀
//解决跨域问题:走vite.config.js代理
let serverPrefix = "/api";
//#ifdef MP
//小程序后端访问地址(直接访问。不走vite.config.js代理)
serverPrefix = url;
// #endif
export const config = {
    //登录页面
    loginPath: base.loginPath,
    bgPath: base.bgPath,
    //后台默认tab页面
    tag: base.tag,
    logo: base.logo,
    appBgName: base.appBgName,
    appBgBottom: base.appBgBottom,
    wxMini: base.wxMini,
    aliMini: base.aliMini,
    //控制台菜单显示类型
    menuType: base.menuType,
    //加密密钥
    securityKey: "991567775",
    //端口
    port: base.port,
    //后端请求地址
    url: url,
    //默认服务
    service: service,
    //接口前缀
    serverPrefix: serverPrefix,
    //文件预览支持图片 word excel
    uploadUrl: serverPrefix + "/system/file/view?path=",
    //cdn加速预览
    // uploadUrl: 'https://static.tonhai.cn/file/',
    //附件上传地址
    uploadPath: serverPrefix + "/system/file/upload",
    //附件下载地址
    downloadPath: serverPrefix + "/system/file/down?path=",
    // 大图预览
    bigImgPath: 'https://test.tonhai.cn/file/',
    //常见文件后缀
    fileSuffix: fileSuffix
}