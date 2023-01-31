<template>
	<div class="data-edit">
		<u-form  @save="save" v-model="data"  size="small">
			<u-hidden  v-model="data.id"></u-hidden>
			<u-text  required label="用户组名称" prop="label"  :disabled="route.read" v-model="data.label" > </u-text>
			<u-text  required label="用户组编码"  prop="code"  :disabled="route.read" v-model="data.code" :rules="[ { required: true, message: '请输入参数值'}]"> </u-text>
			<u-area  label="描述" v-model="data.content"  :disabled="route.read" maxlength="100"></u-area>
			<template v-slot:btn>
				<u-btn size="small" v-if="!route.read&&user.permissions['sys:group:save']" formType="submit" type="primary" icon="icon-baocun">保存</u-btn>
				<u-btn size="small" v-if="!route.read" formType="reset" type="warn" icon="icon-zhongzhi">重置</u-btn>
				<u-btn size="small"   icon="icon-close" type="info"  @click="cancel">取消</u-btn>
			</template>
		</u-form>
	</div>
</template>
 
 <script>
 	import {ref,computed} from 'vue';
  import store from "@/pina";

 	export default {

 		setup(props,context){
      const {user,tag} = store()
 			//必须携带默认属性
			//路由
			const route=uni.$currentFirstRouter
 			const data=ref(route.param!=undefined?route.param:{id:null,label:'',val:'',type:'',remark:''});
 			const save=()=>{
 				uni.request({
 					service:'system',
 					url:'/sysGroup/save',
 					data:data.value,
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
        tag.delTag("/system/sysGroup/edit")
				//跳转前一个页面
				uni.$router.push(tag.currentTag)
 			}
 			return {user,route,data,save,cancel}
 		}
 	}
 </script>
 <style scoped>
 </style>
