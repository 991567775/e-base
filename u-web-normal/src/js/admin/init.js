/**
 * 日期格式化以及数组补充方法
 */
export const init=()=>{
	Date.prototype.format = function (fmt) {
	var o = {
	"M+": this.getMonth() + 1, //月份
	"d+": this.getDate(), //日
	"h+": this.getHours(), //小时
	"m+": this.getMinutes(), //分
	"s+": this.getSeconds(), //秒
	"q+": Math.floor((this.getMonth() + 3) / 3), //季度
	"S": this.getMilliseconds() //毫秒
	};
	if (/(y+)/.test(fmt)) {
	fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
	}
	for (var k in o) {
	if (new RegExp("(" + k + ")").test(fmt)) {
	fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
	}
	}
	return fmt;
	}
	//数组补充方法
	Array.prototype.remove = function (val) {
		for (let i = this.length-1; i >=0; i--) {
			if(this[i]==val){
				this.splice(i, 1);
			}
		}
	};
	Array.prototype.removes = function (vals) {
		for (let i = this.length-1; i >=0; i--) {
			for(let j=0;j<vals.length;j++){
				if(this[i]==vals[j]){
					this.splice(i, 1);
				}
			}
			
		}
	};
	Array.prototype.removeObj = function (val,field) {
		for (let i = this.length-1; i >=0; i--) {
			if(this[i]!=undefined&&this[i][field]!=undefined&&this[i][field]==val){
				this.splice(i, 1);
			}
		}
	};
	Array.prototype.removeObjs = function (vals,field) {
		for (let i = this.length-1; i >=0; i--) {
			for(let j=0;j<vals.length;j++){
				if(this[i]!=undefined&&this[i][field]!=undefined&&this[i][field]==vals[j]){
					this.splice(i, 1);
				}
			}
		}
	};
}

//自动更新
export	const autoUpdate=() =>{
	// 获取小程序更新机制兼容 
	if (uni.canIUse('getUpdateManager')) {
	    const updateManager = uni.getUpdateManager()
	    // 检查是否有新版本发布
	    updateManager.onCheckForUpdate(function(res) {
	        if (res.hasUpdate) {
	            //小程序有新版本，则静默下载新版本，做好更新准备
	            updateManager.onUpdateReady(function() {
	                uni.showModal({
	                    title: '更新提示',
	                    content: '新版本已经准备好，是否重启应用？',
	                    success: function(res) {
	                        if (res.confirm) {
	                            //新的版本已经下载好，调用 applyUpdate 应用新版本并重启
	                            updateManager.applyUpdate()
	                        } else if (res.cancel) {
	                            //如果需要强制更新，则给出二次弹窗，如果不需要，则这里的代码都可以删掉了
	                            uni.showModal({
	                                title: '温馨提示',
	                                content: '我们已经做了新的优化，请及时更新哦~',
	                                showCancel: false, //隐藏取消按钮，也可显示，取消会走res.cancel，然后从新开始提示
	                                success: function(res) {
	                                    //第二次提示后，强制更新           
	                                    if (res.confirm) {
	                                        // 新的版本已经下载好，调用 applyUpdate 应用新版本并重启
	                                        updateManager.applyUpdate()
	                                    } else if (res.cancel) {
	                                        //重新回到版本更新提示
	                                        autoUpdate()
	                                    }
	                                }
	                            })
	                        }
	                    }
	                })
	            })
	            // 新的版本下载失败
	            updateManager.onUpdateFailed(function() {
	                uni.showModal({
	                    title: '温馨提示',
	                    content: '新版本已经上线，请您删除当前小程序，重新搜索打开',
	                })
	            })
	        }
	    })
	} else {
	    // 提示用户在最新版本的客户端上体验
	    uni.showModal({
	        title: '温馨提示',
	        content: '当前微信版本过低，可能无法使用该功能，请升级到最新版本后重试。'
	    })
	}
}			


/**
* 微信登录
 */
export const loginWx=(store,back)=>{
	wx.login({
		success: function success(res) {
			if (res.code) {
				//发起网络请求
			 	uni.request({
			 		method:config.wxMini.method,
			 		url: config.wxMini.path,
			 		data: { code: res.code,type:'wechat' },
			 		success: function success(e) {
						if (e.code == 200) {
							store.dispatch("setUser", e.data.userInfo);
							store.dispatch("setToken", e.data.token);
			 			} else {
							uni.redirectTo({
								url:back
							})	
			 			}
			 		} 
				});
			} else {
			 	console.log('登录失败！' + res.errMsg);
			}
		} 
	});
}
/**
* 支付宝自动登录
 */
export const loginMy=(store,back)=>{
	my.getAuthCode({
		success: function success(res) {
			if (res.authCode) {
				//发起网络请求
				my.request({
					method:config.aliMini.method,
					url: config.url+config.aliMini.path,
					data: { thirdCode: res.authCode,type:'alipay' },
					success: function success(res1) {
						if (res1.data.code == 200) {
							store.dispatch("setUserInfo", res1.data.data);
						} else {
							uni.redirectTo({
								url:back
							})	
			 			}
					} 
				});
			} else {
				console.log('登录失败！' + res.errMsg);
			}
		} 
	});
}
/**
* pc模拟登录
 */
export const loginPc=(store)=>{
	//模拟登录
	const obj={
		access_token:'eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdGF0aW9uQ29kZSI6IltcInlmemdcIl0iLCJ1cGRhdGVEYXRlIjoiMjAyMS0wOS0xNFQwMTozMDowMyIsInR5cGUiOiIxIiwicmVtb3ZlIjoiZmFsc2UiLCJwYXNzd29yZCI6IiQyYSQxMCRXM29EeXhyYURxd0lWaGVXOEpkM2tPNnlGeUhXNDUuRHpsSHFaYlNDMm80OHhNSlVKWGpEYSIsInJvbGVDb2RlIjoiW1wiYWRtaW5cIl0iLCJpZCI6IjEiLCJleHAiOjE2Mzg1ODM2ODEsImVtYWlsIjoiOTkxNTY3Nzc1QHFxLmNvbSIsImdyb3VwQ29kZSI6IltcIm1yXCJdIiwiY3JlYXRlRGF0ZSI6IjIwMjEtMDktMTRUMDE6MzA6MDMiLCJ3eFVzZXJJZCI6IiIsImNvbXBhbnlDb2RlIjoiW1wiY3NcIl0iLCJJRENhcmQiOiIiLCJiaXJ0aCI6IiIsInVwZGF0ZVVzZXIiOiI3MDIzODEwNDAwMjg4NzY4IiwiaGVhZFBpYyI6IiIsInVzZXJCZWhpbmRJbWFnZSI6IiIsInJlYWxseSI6ImZhbHNlIiwidXNlckZyb250SW1hZ2UiOiIiLCJwaG9uZSI6IjE3Njk2ODQyMTA5IiwibmFtZSI6IueuoeeQhuWRmCIsImRlcGFydENvZGUiOiJbXCJ5ZlwiXSIsImNyZWF0ZVVzZXIiOiIxIiwidXNlcm5hbWUiOiJhZG1pbiIsImFsaXBheVVzZXJJZCI6IiIsInN0YXR1cyI6ImZhbHNlIn0.JqiIWP4teW8HGle6QsHDhq-DIR3uGpyxqYVnKyBQO7Y',
		userInfo:{id:7595489032667136,real:true,hasOrder:false,name:'一只麋鹿',phone:'151****6281',headPic:'/upload/2021/10/21/7868930510618624.jpg',fancy:['嘻哈','二傻子']},//登陆用户
	}
	store.dispatch("setUserInfo",obj);
}