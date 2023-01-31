
import {crypto} from "./crypto";
import {util} from './edp-util'

const storePlugin = function ({key="edp", watch= '*',autoInit=true ,safe=false}) {
    return store => {
		let watchedDatas = {}
		store.subscribe((mutation, state) => {
            if (watch === '*') {
                watchedDatas = state
            } else {
                watch.forEach(key => {
                    if (key.split('.').length > 1) {
                        watchedDatas[`deep_${key}`] = getObjDeepValue(state, key.split('.'))
                    } else {
                        watchedDatas[key] = state[key]
                    }
                })
            }
            if(safe){
				try {
					if(util.platform()=="pc"){
						sessionStorage.setItem(key,crypto.encode(JSON.stringify(watchedDatas)))
					}else{
						uni.setStorageSync(key, crypto.encode(JSON.stringify(watchedDatas)));
					}
				} catch(e) {
					sessionStorage.setItem(key,crypto.encode(JSON.stringify(watchedDatas)))
				}
            }else{
				try {
					if(util.platform()=="pc"){
						sessionStorage.setItem(key,JSON.stringify(watchedDatas))
					}else{
						uni.setStorageSync(key, JSON.stringify(watchedDatas));
					}
				} catch(e) {
					sessionStorage.setItem(key,JSON.stringify(watchedDatas))
				}
            }
            
        })
		 //自动更新
		if (autoInit) {
		    let localState ={};
		    if(safe){
				try {
					let temp=null;
					if(util.platform()=='pc'){
						 temp=util.isJSON(crypto.decode(sessionStorage.getItem(key)))
					}else{
						 temp=util.isJSON(crypto.decode(uni.getStorageSync(key)));
						
					}
					if(temp!=null&&temp!=false){
						localState=temp;
					}
				} catch(e) {
				   let temp=util.isJSON(crypto.decode(sessionStorage.getItem(key)))
				   if(temp!=false){
				   	localState=temp;
				   }
				}
			}else{
				try {
					let temp=null;
					if(util.platform()=='pc'){
						 temp=util.isJSON(sessionStorage.getItem(key))
					}else{
						 temp=util.isJSON(uni.getStorageSync(key));
					}
					if(temp!=false){
						localState=temp;
					}
				} catch(e) {
					let temp=util.isJSON(sessionStorage.getItem(key))
					if(temp!=false){
						localState=temp;
					}
				}
		    }
		    const storeState = store.state
		    if (localState) {
		        Object.keys(localState).forEach(key => {
		            if (key.includes('deep_')) {
		                let keysArr = key.replace('deep_', '').split('.')
		                setObjDeepValue(storeState, keysArr, localState[key])
		                delete localState[key]
		            }
		        })
		    }
		    store.replaceState({ ...storeState, ...localState })
		}
			   
    }
}
//监听获取深层数据
function getObjDeepValue (obj, keysArr) {
    let val = obj
    keysArr.forEach(key => {
        val = val[key]
    })
    return val
}
//设置深层数据
function setObjDeepValue (obj, keysArr, value) {
    let key = keysArr.shift()
    if (keysArr.length) {
        setObjDeepValue(obj[key], keysArr, value)
    } else {
        obj[key] = value
    }
}

export default storePlugin