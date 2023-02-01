import { defineStore } from 'pinia'
import { config } from "../../config";
const tagObj = {
	label: '', // 标题名称
	path: '', // 标题的路径
	query: '', // 标题的参数
	param: '',//参数
	close: true
}
export const tag = defineStore('tag', {
	state: () => {
		return {
			tagList: [config.tag],
			currentTag: tagObj,
		}
	},
	getters:{
		findTag: (state) => {
			return (path) => {
				let tag, key;
				state.tagList.map((item, index) => {
					if (item.path === path) {
						tag = item;
						key = index;
					}
				});
				return { tag: tag, key: key };
			}
		}
	},
	actions:{
		//设置当前(点击)
		setCurrentTag (cTag) {
			//判断类型是对象还是字符串地址
			if (Object.prototype.toString.call(cTag).indexOf("String") != -1) {
				for (let x = 0; x < this.tagList.length; x++) {
					if (this.tagList[x].path === cTag) {
						this.currentTag = this.tagList[x];
						break;
					}
				}
			} else {
				state.currentTag = cTag
			}
		},
		addTag (currentTag)  {
			if (Object.prototype.toString.call(currentTag).indexOf("String") != -1) {
				for (let x = 0; x < this.tagList.length; x++) {
					if (this.tagList[x].path === currentTag) {
						this.currentTag = this.tagList[x];
						break;
					}
				}
			} else {
				this.currentTag = currentTag
				if (currentTag.label == null || currentTag.label == "") return;
				if (this.tagList.some(item => {
					if (item.path == currentTag.path) {
						//修改当前tag
						item.label = currentTag.label
						return true
					}
				})) return
				this.tagList.push(currentTag)
			}

		},
		delTag ( currentTag) {
			if (Object.prototype.toString.call(currentTag).indexOf("String") != -1) {
				this.tagList.some((item, i) => {
					if (item.path == currentTag) {
						this.tagList.splice(i, 1);
						this.currentTag = this.tagList[i === 0 ? i : i - 1];
						return true
					}
				})
			} else {
				this.tagList.some((item, i) => {
					if (item.path == currentTag.path) {
						this.tagList.splice(i, 1);
						this.currentTag = this.tagList[i === 0 ? i : i - 1];
						return true
					}
				})
			}
		},
		delAll()  {
			let flag = localStorage.getItem("userTagActive") == '0' || localStorage.getItem("adminLoginToOneUser") == "1"
			this.tagList = flag ? [config.userTag] : [config.tag]
			this.currentTag = flag ? config.userTag : config.tag

		},
		delOther ()  {
			this.tagList = this.tagList.filter(item => {
				if (config.tag.path == item.path) {
					return true
				}
				if (item.path == this.currentTag.path) {
					return true;
				}
			})
		}
	},
})




