import { defineStore } from 'pinia'

export const conf = defineStore('conf', {
    state: () => {
        return {
            isCollapse:false,
            lockPwd:null,
        }
    },
    getters:{},
    actions:{
        set_collapse:()=>{
            this.isCollapse=!this.isCollapse
        },
        set_lock:(pwd)=>{//设置锁屏
            this.lockPwd=pwd;
        }
    },
})
