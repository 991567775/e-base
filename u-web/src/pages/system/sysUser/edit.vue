<template>
	<div class="data-edit">
		<u-form  @save="save" v-model="obj"  size="small">
			<u-hidden   v-model="obj.id"></u-hidden>
			<u-hidden   v-model="obj.password"></u-hidden>
			<u-text  required label="登录名" prop="username"  :disabled="route.read" v-model="obj.username" > </u-text>
			 <u-text  required  label="昵称" prop="name"  :disabled="route.read" v-model="obj.name" > </u-text>
			<u-text  required label="手机号"  prop="phone"  :disabled="route.read" v-model="obj.phone" > </u-text>
			<u-text  required label="邮箱"  prop="email" :disabled="route.read" v-model="obj.email" > </u-text>
			<u-text  label="出生日期"  prop="birth" :disabled="route.read" v-model="obj.birth" > </u-text>
			<u-text  required label="类型"  prop="type"  :disabled="route.read" v-model="obj.type" > </u-text>
			<u-number label="年龄"  prop="age" v-model="obj.age" :disabled="route.read"></u-number>
			 <u-select required label="所属公司" @select="changeCompany" prop="companyCode" v-model="obj.companyCode" :disabled="route.read" :list="organize.organizeList.value.companyList"></u-select>
			<u-select required label="所属部门" @select="changeDepart"  prop="departCode" v-model="obj.departCode" :disabled="route.read" :list="organize.organizeList.value.departList"></u-select>
			<u-select required label="所属岗位"  prop="stationCode" v-model="obj.stationCode" :disabled="route.read" :list="organize.organizeList.value.stationList"></u-select>
			<u-select required label="所属用户组"  prop="groupCode"  v-model="obj.groupCode" :disabled="route.read" :list="organize.organizeList.value.groupList"></u-select>
			<u-select required label="所属角色"  prop="roleCode"   v-model="obj.roleCode" :disabled="route.read" :list="roleList"></u-select>
			 
			 <u-select required label="状态" v-model="obj.status" :disabled="route.read" :list="SysState"></u-select>
			<template v-slot:btn>
				<u-btn size="small" v-if="!route.read&&user.permissions['sys:user:save']" formType="submit" type="primary" icon="icon-baocun">保存</u-btn>
				<u-btn size="small" v-if="!route.read" formType="reset" type="warn" icon="icon-zhongzhi">重置</u-btn>
				<u-btn width="150rpx" size="small" icon="icon-close" type="info"   @click="cancel">取消</u-btn>
			</template>
		</u-form>
	</div>
</template>

<script>
	import {ref} from 'vue';
  import store from "@/pina";
	import {toDirect} from '@/js/toDirect.js'
	import {organize} from '@/js/organize.js'
	export default {

		setup(props,context){
      const {direct,user,tag} = store()
			//路由
			const route=uni.$currentFirstRouter
			//必须携带默认属性
			const obj=ref(route.param!=undefined?route.param:
			{id:null,password:'',username:'',name:'',phone:'',email:'',birth:'',type:'',age:0,
			companyCode:[],departCode:[],stationCode:[],groupCode:[],roleCode:[],status:''});
			//保存
			const save=()=>{
				uni.request({
					service:'system',
					url:'/sysUser/save',
					data:obj.value,
					success: (e) => {
						if(e.code==200){
							uni.$msg({type:'success',message:'保存成功'}).then(()=>{
								cancel()
							})
						}else{
							uni.$msg({type:'error',message:e.message})
						}
					}
				})
			}
			const cancel=()=>{
				//关闭tag【方法，关闭地址】
        tag.delTag("/system/sysUser/edit")
				//跳转前一个页面
				uni.$router.push(tag.currentTag)
			}
			//用户组
			organize.getGroup(obj)
			//组织初始
			organize.init(obj,true,true)
			//公司选择事件
			const changeCompany=(val)=>{
			  obj.value.companyCode=val;
			  organize.change(obj,true,true)
			}
			//选择部门事件
			const changeDepart=(val)=>{
			  obj.value.departCode=val;
			  organize.change(obj,false,true)
			}
			const roleList=toDirect("system","/sysRole/listAll",{},{val:"code",label:"label"});
			 
			//状态
			const SysState=ref(direct.getDirect("SysState",Boolean));
			return {user,route,obj,save,cancel,organize,SysState,roleList,changeCompany,changeDepart}
		}
	}
</script>
<style scoped>
</style>
 