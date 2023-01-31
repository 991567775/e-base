import { defineStore } from 'pinia'

export const menu = defineStore('menu', {
	state: () => {
		return {
			menu: [],
			szyMenu: [],
			fileMenu: [],
			saveWaterMenu: [],
			businessMenu: [],
			systemMenu: [],
			topMenu: [],
			currentTopMenu:null
		}
	},
	getters:{},
	actions:{
		//删除全部
		deleteAllMenu() {
			return new Promise((resolve, reject) => {
				this.menu = null
				this.topMenu = null
				resolve()
			})

		},
		//获取右侧菜单
		getMenu (param){
			return new Promise((resolve, reject) => {
				if (typeof uni != 'undefined') {
					uni.request({
						service: 'system',
						url: '/sysMenu/menuTree',
						data: param,
						success: (e) => {
							if (e.code == 200) {
								this.menu = e.data
								resolve(e)
							} else {
								uni.$msg({ message: e.message, type: "error" })
								reject(e)
							}
						}
					})
				} else {

				}
			})
		},
		//顶部菜单模式
		getTopMenu(param) {
			return new Promise((resolve, reject) => {
				if (typeof uni != 'undefined') {
					uni.request({
						service: 'system',
						url: '/sysMenu/listAll',
						data: Object.assign(param, { pid: -1 }),
						success: (e) => {
							if (e.code == 200) {
								this.topMenu = e.data
								this.currentTopMenu=e.data[0]
								this.getMenu(Object.assign(param, {pid: e.data[0].id})).then(r =>{
									resolve(e)
								})
							} else {
								uni.$msg({ message: e.message, type: "error" })
								reject(e)
							}

						}
					})

				} else {

				}
			})
		}
	},
})
