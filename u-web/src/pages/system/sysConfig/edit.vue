<template>
	<div class="data-edit">
		<u-form  @save="save" v-model="obj"  size="small" :rules="{val:{ required: true, message: '请输入参数值'}}">
			
			<u-hidden   prop="id" label="主键"  v-model="obj.id" />
			
			<u-text    required  prop="label" label="参数名" v-model="obj.label" maxlength="15" :disabled="route.read"
			 :rule="{ required: true, message: '请输入参数名'}"
			 />
			
			<u-text    required  prop="val" label="参数值" v-model="obj.val" maxlength="25" :disabled="route.read"   />
			
			<u-select   required prop="type" label="类型" v-model="obj.type" :list="list" :disabled="route.read" />
			
			<u-area  required   label="备注" v-model="obj.remark" maxlength="100" :disabled="route.read" />
      <template v-slot:btn>
				<u-btn   v-if="!route.read&&user.permissions['sys:config:save']" formType="submit" type="primary" icon="icon-baocun">保存</u-btn>
				<u-btn   v-if="!route.read" formType="reset" type="warn" icon="icon-zhongzhi">重置</u-btn>
				<u-btn     icon="icon-close" type="info"  @click="cancel">取消</u-btn>
			</template>
			
		</u-form>
	</div>
</template>

<script>
	import {ref,} from 'vue';
  import store from "@/pina";
	export default {

		setup(){
      const {direct,user,tag} = store()
			//路由
			const route=uni.$currentFirstRouter
			//获取字典
			const list=direct.getDirect("sysConfig");
			//obj对象
			const obj=ref(route.param!=undefined?route.param:{id:null,label:null,val:'',type:'',remark:'',createDate:''});
			//保存
			const save=()=>{
				uni.request({
					service:'system',
					url:'/sysConfig/save',
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

			//关闭
			const cancel=()=>{
        //关闭tag【方法，关闭地址】
        tag.delTag("/system/sysConfig/edit")
				//跳转前一个页面
				uni.$router.push(tag.currentTag)
			}
			return {user,route,obj,save,cancel,list}
		}
	}
</script>
<style scoped>
</style>
 