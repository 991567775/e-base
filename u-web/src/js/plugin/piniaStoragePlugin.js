import {crypto} from "./crypto";
import { toRaw } from 'vue'
import {util} from './edp-util'
const piniaKey= 'pinia'

// 来个持久化存储的函数
const setStorage = (type,enable,key, value) => {
	if(type==undefined){
		type=sessionStorage;
	}
	try {
		if(util.platform()=="pc"){
			type.setItem(key, JSON.stringify(value))
		}else{
			uni.setStorageSync(key, JSON.stringify(value));
		}
	}catch (e) {
		type.setItem(key, JSON.stringify(value))
	}


}
// 来个取的函数
const getStorage = (type,enable,key) => {
	if(type==undefined){
		type=sessionStorage;
	}
	try {
		if(util.platform()=="pc"){
			return type.getItem(key)? JSON.parse(type.getItem(key)): {}
		}else{
			return uni.getStorageSync((key))? JSON.parse(uni.getStorageSync((key))):{}
		}
	}catch (e) {
		return type.getItem(key)? JSON.parse(type.getItem(key)): {}
	}

}



const piniaStoragePlugin = (options) => {
	const { key,storage, keepId = [],enable } = options
	//使用函数柯里化
	return (context) => {
		const { store } = context
		if (keepId.length === 0) {
			//没有指定存全部
			// 有个监听是$subscribe  1. 先监听是否改变了store的内容 改变了就存
			store.$subscribe(() => {
				//无论哪个 state  所有的改变都走这个函数  所以我们可以在这里搞一些动作
				setStorage(storage,enable,`${key ?? piniaKey}-${store.$id}`, toRaw(store.$state))
			})
		} else {
			//有指定要存哪些
			//使用短路运算
			keepId.includes(store.$id) &&store.$subscribe(() => {
				setStorage(storage,enable,`${ piniaKey}-${store.$id}`, toRaw(store.$state))
			})
		}
		//2. 获取本地存储的localStorage 有就放在页面上
		const data = getStorage(storage,enable,`${key ?? piniaKey}-${store.$id}`)
		return {
			...data,
		}
	}
}

export default piniaStoragePlugin

