<template>
	<div class="data-edit">
		<u-form  @save="save" v-model="data"  size="small">
			<u-hidden   v-model="data.id"></u-hidden>
			<u-text  required prop="label" label="字典名称"  :disabled="route.read" v-model="data.label" > </u-text>
			<u-text  required prop="code" label="字典编码"  :disabled="route.read" v-model="data.code"  maxlength="30" > </u-text>
			<template v-slot:btn>
				<u-btn size="small" v-if="!route.read&&user.permissions['sys:direct:save']" formType="submit" type="primary" icon="icon-baocun">保存</u-btn>
				<u-btn size="small" v-if="!route.read" formType="reset" type="warn" icon="icon-zhongzhi">重置</u-btn>
				<u-btn width="150rpx"   size="small" icon="icon-close"  type="info"  @click="cancel">取消</u-btn>
			</template>
		</u-form>
	</div>
</template>

<script>
	import {ref,computed} from 'vue';
  import store from "@/pina";
	
	export default {

		setup(props,context){
      const {direct,user,tag} = store()
			//路由
			const route=uni.$currentFirstRouter
			//必须携带默认属性
			const data=ref(route.param!=undefined?route.param:{id:null,label:'',code:''});
			//保存
			const save=()=>{
				uni.request({
					service:'system',
					url:'/sysDirect/save',
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
			//取消
			const cancel=()=>{
				//关闭tag【方法，关闭地址】
        tag.delTag("/system/sysDirect/edit")
				//跳转前一个页面
				uni.$router.push(tag.currentTag)
			}
			return {user,route,data,save,cancel}
		}
	}
</script>
<style scoped>
  
</style>