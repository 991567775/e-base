<template>
	<view class="content">
		<view class="text-area">
			<view>SSO应用端（前后端分离版） demo</view>
			<view>当前是否登录：{{state}}</view>
		</view>
		<view>
			<button @click="login">登录</button>
			<button @click="out">退出</button>
			<button @click="test">查询</button>
		</view>
		<view>{{user}}</view>
		<view>{{ds}}</view>
	</view>
</template>

<script>
	import {ref} from 'vue'
	export default {
		setup() {
			const state=ref(null);
			uni.request({
				service:"system",
				url:"/sso/isLogin",
				header: {
					"token": localStorage.getItem("token")
				},
				success: (res) => {
					state.value=res.data
				},
				fail: () => {
					
				}
				
			})
			const user=ref(null);
			
			const login=()=>{
				uni.navigateTo({
					url:'/pages/admin/sso/login?back=' + encodeURIComponent('/pages/admin/sso/index')
				})
			}
			const out=()=>{
				uni.request({
					service:"system",
					url:'/sso/logout',
					header: {
						"token": localStorage.getItem("token")
					},
					success: (res) => {
						alert(res.data.msg);
					}
				})
			}
			const ds=ref([])
			const test=()=>{
				uni.request({
					service:"system",
					url:"/sysDemo/list",
					header: {
						"token": localStorage.getItem("token")
					},
					success: (res) => {
						if(res.code==200){
							ds.value=res.data
						}else{
							// window.location.href='http://127.0.0.1:199/sso/sso/login?back=' + encodeURIComponent(location.href);
						}
					},
					fail: () => {
						
					}
					
				})
				uni.request({
					service:"system",
					url:"/sso/myInfo",
					header: {
						"token": localStorage.getItem("token")
					},
					success: (res) => {
						user.value=res.data
					},
					fail: () => {
						
					}
					
				})
			}
			return {state,login,out,user,test,ds}
		}
	}
</script>

<style>
	.content {
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
	}

	.logo {
		height: 200rpx;
		width: 200rpx;
		margin-top: 200rpx;
		margin-left: auto;
		margin-right: auto;
		margin-bottom: 50rpx;
	}

	.text-area {
		display: flex;
		justify-content: center;
	}

	.title {
		font-size: 36rpx;
		color: #8f8f94;
	}
</style>
