<template>
	<view>
		<text class="title">SSO端-登录页 demo</text>
	</view>
</template>

<script>
	export default {
		setup(){
			// 重定向至认证中心
			const goSsoAuthUrl=()=> {
				uni.request({
					service:"system",
					url:'/sso/getSsoAuthUrl',
					data:{clientLoginUrl: location.href},
					success: (res) => {

						window.location.href=res.data
						
					}
				})
				 
			}
					
			// 根据ticket值登录 
			const  doLoginByTicket=(ticket)=> {
				uni.request({
					service:"system",
					url:'/sso/doLoginByTicket',
					data:{ticket: ticket},
					success: (res) => {
						if(res.code == 200) {
							localStorage.setItem('token', res.data);
							var ss=decodeURIComponent('back');
							uni.navigateTo({
								url:'/pages/admin/sso/index'
							})
						} else {
							alert(res.message);
						}
					}
				})
			}
			
			// 从url中查询到指定名称的参数值 
			const  getParam=(name, defaultValue)=>{
				var query = window.location.search.substring(1);
				var vars = query.split("&");
				for (var i=0;i<vars.length;i++) {
					var pair = vars[i].split("=");
					if(pair[0] == name){return pair[1];}
				}
				return(defaultValue == undefined ? null : defaultValue);
			}
			
			var back = getParam('back', '/');
			var ticket = getParam('ticket');
			if(ticket) {
				doLoginByTicket(ticket);
			} else {
				goSsoAuthUrl();
			}
		}
	}
</script>

<style>

</style>
