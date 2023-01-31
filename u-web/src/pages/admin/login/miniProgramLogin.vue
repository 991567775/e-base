<template>
	<view class="page">
		<view style="width: 100%;">
			<view :style="1===1?'height: 10rem;background-repeat: no-repeat;'+
			'background-position: center;border-bottom: #dddddd solid 0.05rem;display:flex;justify-content: space-evenly;align-items: center;':''">
			<image  src="/static/foodOrder/logo.png" style="width: 4rem;height: 4rem;"></image>
			</view>
			//#ifdef MP-ALIPAY
			<view style="margin: 30px 30rpx;">
				<view>登录后该应用将获得以下权限</view>
				<view style="font-size: 20rpx;color: #969696;margin-top: 20rpx;display: flex;align-items: center;">
					<label class="icon icon-xuanze" style="font-size: 32rpx;"></label>
					获得你的信息（用户昵称、用户头像和绑定手机号等）
				</view>
			</view>
			<button  style="margin: 30rpx;font-size: 36rpx !important;"
				type="primary" open-type="getPhoneNumber" @getphonenumber="decryptPhoneNumber">确认支付宝登录</button>			
			//#endif
			
			//#ifdef MP-WEIXIN
			<view style="margin: 30px 30rpx;">
				<view>登录后该应用将获得以下权限</view>
				<view style="font-size: 20rpx;color: #969696;margin-top: 20rpx;display: flex;align-items: center;">
					<checkbox :checked="getUserInfo" @click="chenckBtn('getUserInfo',getUserInfo)"></checkbox>
					获得你的公开信息（手机号等）
				</view>
			</view>
			<button  :disabled="logBtn" style="margin: 30rpx;font-size: 36rpx !important;"
				type="primary" open-type="getPhoneNumber" @getphonenumber="decryptPhoneNumber">确认微信登录</button>
			<view style="margin: 30px 30rpx;">
				<view style="font-size: 20rpx;color: #969696;display: flex;align-items: center;">
					<checkbox :checked="readBook" @click="chenckBtn('readBook',readBook)"></checkbox>
					我已阅读并同意
					<label style="color: blue;">《用户协议》</label>
				</view>
			</view>
			//#endif
		</view>
	</view>
</template>

<script>
	import {
		config
	} from '../../../const/config.js'

	import {
		ref,
		reactive,

	} from 'vue';
	import {useStore} from 'vuex'
	export default {
		setup() {
			const store=useStore();
			const canIUseAuthButton = true;//使用该按钮
			const userInfo = ref({}); //用户对象
			const readBook = ref(true); //是否阅读协议
			const getUserInfo = ref(true); //是否同意获取用户信息
			const logBtn = ref(false); //登录按钮是否可以使用（true不可以，false可以）
			const chenckBtn = (type, val) => {
				if (type == 'readBook') {
					readBook.value = !readBook.value;
				} else {
					getUserInfo.value = !getUserInfo.value;
				}
				if (readBook.value && getUserInfo.value) {
					logBtn.value = false;
				} else {
					logBtn.value = true;
				}
			}
			/**
			 * 更新用户数据
			 */
			const updateUser=()=>{
				//数据更新
				//#ifdef MP-ALIPAY
				my.request({
					method:'POST',
					url: config.url+'/system/login/updateUser',
					data: JSON.stringify(userInfo.value),
					success:function(res){
						var data = {userInfo:res.data.data};
						store.dispatch("setUserInfo", data);
						uni.navigateBack({})
					}
				})
				//#endif
				//#ifdef MP-WEIXIN
				wx.request({
					method:'POST',
					url: config.url+'/system/login/updateUser',
					data: JSON.stringify(userInfo.value),
					success:function(res){
						var data = {userInfo:res.data.data};
						store.dispatch("setUserInfo", data);
						uni.navigateBack({})
					}
				})
				//#endif
			}
			/**
			 * 执行登录
			 */
			const login = () => {
				//#ifdef MP-ALIPAY
				my.getAuthCode({
					success(e){
						if (e.authCode) {
							//发起网络请求
							my.request({
								method: 'POST',
								url: config.url + '/system/login/loginByThird',
								data: {
									thirdCode: e.authCode,type:'alipay'
								},
								success: function success(res) {
									if (res.data.code == 200) {
										res.data.data.userInfo.iv = userInfo.value.iv;
										res.data.data.userInfo.encryptedData = userInfo.value.encryptedData;
										userInfo.value = res.data.data.userInfo;
										store.dispatch("setUserInfo", res.data.data);
										//判断用户是否以及获取过支付宝数据
										if (res.data.data.userInfo && res.data.data.userInfo.name.indexOf('游客') != -1) {
											//如果没有获取过直接进行获取并更新
											uni.showModal({
												title: '温馨提示',
												content: '亲，授权支付宝登录后才能正常使用小程序功能',
												success(e) {
													my.getUserInfo({
													    success: (infoRes) => {

															res.data.data.userInfo.name = infoRes.nickName;
															res.data.data.userInfo.nickName = infoRes.nickName;
															res.data.data.userInfo.headPic = infoRes.avatar;
															userInfo.value = res.data.data.userInfo;
															store.dispatch("setUserInfo",res.data.data);
															updateUser()
													    },
													  });
												}
											})
										} else {
											//返回原来的页面
											uni.navigateBack({})
										}
										
									} else {
										uni.$msg({message: res.errMsg,type:'error'})
									}
								}
							});
						} else {
							uni.$msg({message:'登录失败！' + res.errMsg,type:'error'})
						}
					}
				})
				//#endif
				//#ifdef MP-WEIXIN
				wx.login({
					success: function success(res) {
						if (res.code) {
							//发起网络请求
							wx.request({
								method: 'POST',
								url: config.url + '/system/login/loginByThird',
								data: {
									thirdCode: res.code,type:'wechat'
								},
								success: function success(res) {
									if (res.data.code == 200) {
										res.data.data.userInfo.iv = userInfo.value.iv;
										res.data.data.userInfo.encryptedData = userInfo.value.encryptedData;
										userInfo.value = res.data.data.userInfo;
										store.dispatch("setUserInfo", res.data.data);
										//判断用户是否以及获取过微信数据
										if (res.data.data.userInfo && res.data.data.userInfo.name.indexOf('游客') != -1) {
											//如果没有获取过直接进行获取并更新
											uni.showModal({
												title: '温馨提示',
												content: '亲，授权微信登录后才能正常使用小程序功能',
												success(e) {
													// wxUtil.getUserInfo(function(res1) {
													// 	res.data.data.userInfo.name = res1.userInfo.nickName;
													// 	res.data.data.userInfo.nickName = res1.userInfo.nickName;
													// 	res.data.data.userInfo.headPic = res1.userInfo.avatarUrl;
													// 	userInfo.value = res.data.data.userInfo;
													// 	store.dispatch("setUserInfo",res.data.data);
													// 	updateUser()
													// })
												}
											})
										} else {
											//返回原来的页面
											uni.navigateBack({})
										}
										
									} else {
										
									}
								}
							});
						} else {
							uni.$msg({message:'登录失败！' + res.errMsg,type:'error'})
						}
					}
				});
				//#endif
			}
			
			
			//获取手机号
			const decryptPhoneNumber = (e) => {
				if(e.detail.errMsg=='getPhoneNumber:ok'){
					userInfo.value.iv = e.detail.iv;
					userInfo.value.sign = e.detail.sign;
					userInfo.value.encryptedData = e.detail.encryptedData;
					login()
				}else{
					if(e.detail.errorMessage){
						uni.showModal({
							content:e.detail.errorMessage,
							showCancel:false
						})
					}else{
						uni.showModal({
							content:"用户拒绝授权",
							showCancel:false
						})
						
					}
				}
			}
			
			return {
				userInfo,
				readBook,
				getUserInfo,
				logBtn,
				chenckBtn,
				config,
				login,decryptPhoneNumber
			}
		},

	}
</script>

<style scoped>
	.page {
		width: 100%;
		height: 100vh;
		overflow-y: auto;

	}
</style>
