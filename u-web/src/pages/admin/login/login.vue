<template>
	<view class="login" >
		<view class="login-main">
			<view class="login-logo">
				<image src="/static/admin/login/ll.png"></image>
			</view>
			<view class="login-form">
<!--				<u-tabs class="login-tabs" v-model="type" :list="tabList">-->
<!--				</u-tabs>-->
				<u-form   ref="baseform" v-model="form"  @save="submit" :submitIcon="false"  :resetShow="false" :onePencent="true"  submitTitle="登录" >
					<u-text    required v-if="type==1" leftIcon="icon-yonghu" clearable placeholder="请输入用户名" prop="username" v-model="form.username"  />
					<u-text   rule="phone" v-else-if="type==2" leftIcon="icon-shouji" placeholder="请输入手机号" prop="phone" password showPwd v-model="form.phone"/>
					<u-text   rule="email" v-else leftIcon="icon-youxiang" placeholder="请输入邮箱" prop="mail"   v-model="form.mail"/>

					<u-text   required  v-if="type==1" leftIcon="icon-mima" placeholder="请输入密码" prop="password" password showPwd v-model="form.password"/>
					<u-text   required v-else="type==2" leftIcon="icon-yanzhengyanzhengma" placeholder="请输入验证码" prop="phone"   v-model="form.yzm"/>

					<u-select   required prop="companyCode" v-model="form.companyCode" :list="organize.organizeList.value.companyList" placeholder="请选择机构"></u-select>
					<div style="display: flex">
            <u-text required rightSeparate placeholder="请输入验证码" prop="picCode" v-model="form.picCode">
            </u-text>
            <image mode="scaleToFill" :src="picImage" style="width: 160rpx;height:60rpx" @click="getPicCode"></image>
          </div>
          <template v-slot:btn>
						<u-btn width="100%"   size="middle" formType="submit" type="primary"  >登录</u-btn>
					</template>
				</u-form>
				<view class="login-other">
<!--					<u-link url="pwd" >忘记密码</u-link>-->
<!--					<u-link url="register" style="color: #409EFF;">点此注册</u-link>-->
				</view>
				<u-divider width="40%"  style="" class="login-divider">其他方式登录</u-divider>
				<view class="login-other-item">
					<view >QQ</view>
					<u-divider vertical></u-divider>
					<view>微信</view>
					<u-divider vertical></u-divider>
					<view>支付宝</view>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	import {ref,reactive } from 'vue';
  import store from "@/pina";
	import {organize} from '@/js/organize.js'
	import {config} from '@/config.js'
	export default {
		setup(){
      //全局状态
      const {user,menu,direct}=store()

			const tabList=reactive([{path:1,label:'账号登录'},{path:2,label:'手机号登录'},{path:3,label:'邮箱登录'}])
			const type=ref(1);
			//已经登录直接跳转后台
			if(user.access_token!=undefined&&user.access_token!=""){
				uni.navigateTo({url:config.bgPath})
			}
			const form=ref({
				username: "",
				password: "",
				companyCode:[],
				picCode:"",
				picKey:""
				
			})
      /**
       *登录
       */
			const submit=()=>{
				uni.request({
					service:'system',
					url:"/login/login",
					data:form.value,
					success: (e) => {
						if(e.code==200){
              user.setUserInfo(e.data).then(d=>{
                menu.getTopMenu({perm:d.userInfo.permissions,userId:d.userInfo.id}).then(e1=>{
                  if(e1.code==200){
                    direct.directList({}).then(e2=>{
                      //获取菜单后跳转路由
                      if(e2.code==200){
                        uni.navigateTo({url:config.bgPath})
                      }else{
                        uni.$msg({message:e2.message,type:"error"})
                      }
                    })
                  }else{
                    uni.$msg({message:e1.message,type:"error"})
                  }
                })
              })
						}else{
							uni.$msg({message:e.message,type:"error"})
						}
					}
				})
			}
			

			//获取验证码
			const  picImage=ref('');
			const getPicCode=()=>{
				uni.request({
					service:'system',
					url:"/login/image",
					success: (e) => {
						picImage.value=e.data.image;
						form.value.picKey=e.data.key;
					}
				})
			}
			getPicCode();
			//查询公司
			organize.init(form)
			 
			return{
				type,
				tabList,
				form,
				submit,
				organize,
				getPicCode,
				picImage
			}
			
		}
			
	}
</script>

<style scoped lang="scss">
	@import "./login.scss";
	
</style>
