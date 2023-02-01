import { defineStore } from 'pinia'

export const user = defineStore('user', {
    state: () => {
        return {
            auth: false,
            userInfo: {},
            permissions: [],
            roles: [],
            access_token: "",
            userList:[]
        }
    },
    getters:{},
    actions:{
        //设置用户相关信息到全局vuex
        setUserInfo(data){
            return new Promise((resolve) => {
                this.userInfo = data.userInfo;
                this.access_token = data.access_token;
                this.roles = data.userInfo&&data.userInfo.roleCode?data.userInfo.roleCode:[];
                this.permissions=data.userInfo&&data.userInfo.permissions?data.userInfo.permissions:[]
                resolve(data)
            })

        },
        //设置用户权限
        setPermission(data){
            this.roles = data.userInfo&&data.userInfo.roleCode?data.userInfo.roleCode:[]
            this.permissions=data.userInfo&&data.userInfo.permissions?data.userInfo.permissions:[]
        },
        //退出
        quit(){
            this.auth=false
            this.userInfo={}
            this.permissions=[]
            this.roles=[]
            this.access_token= null
            this.userList=[]
        },
        setUserList(list){
            this.userList=list;
        }
    },
})
