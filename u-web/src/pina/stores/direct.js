import { defineStore } from 'pinia'
import {util} from "@wzabcd/u-plugin";
export const direct = defineStore('direct', {
    state: () => {
        return {
            allDirect:[]
        }
    },
    getters:{

    },
    actions:{
        getDirect (code,type)  {
            //返回一个函数用于接收
            let list=util.deepCopy(this.allDirect.filter(item => {
                    return item.code == code
                }
            ))[0]
            if(list!=null&&list!=undefined){
                list=list.list;
                if(type!=undefined){
                    if(type.name ==  'Boolean'){
                        for(let x=0;x<list.length;x++){
                            list[x].val=eval(list[x].val)
                        }
                    }else if(type.name ==  'Number'){
                        for(let x=0;x<list.length;x++){
                            list[x].val=Number(list[x].val)
                        }
                    }
                }else{
                    for(let x=0;x<list.length;x++){
                        list[x].val=''+list[x].val
                    }
                }
                return list
            }
            return []
        },
        delDirect(){
            return new Promise((resolve, reject) => {
                this.allDirect=null
                resolve();
            })

        },
        directList(param){
            return new Promise((resolve, reject) => {
                uni.request({
                    service:'system',
                    method:"GET",
                    url:'/sysDirect/listAllWithChild',
                    data:param,
                    success: (e) => {
                        if(e.code==200){
                            this.allDirect=e.data;
                            resolve(e)
                        }else{
                            uni.$msg({message:e.message,type:"error"})
                            reject(e)
                        }
                    }
                })
            })
        }
    },
})

