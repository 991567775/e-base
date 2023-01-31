<template>
	<div class="data-edit">
		<u-form  @save="save" v-model="obj"  size="small">
			<u-hidden   v-model="obj.id"></u-hidden>
			<u-text  required label="角色名称" prop="label"  :disabled="route.read" v-model="obj.label" > </u-text>
			<u-text  required label="角色编码"  prop="code" :disabled="route.read" v-model="obj.code" > </u-text>
			<u-select required label="数据权限" prop="permissionId" v-model="obj.permissionId" :disabled="route.read"  :list="list"></u-select>
			<u-area  label="备注" v-model="obj.content" :disabled="route.read"  maxlength="100"></u-area>
			<u-tree :list="tree" label="菜单权限" check v-model="obj.menuId"  v-model:ids="obj.menuId" :option="{children: 'children',label: 'label',show: 'show',path: 'path',val: 'id',icon: 'icon'}"></u-tree>
			<template v-slot:btn>
				<u-btn size="small" v-if="!route.read&&user.permissions['sys:config:save']" formType="submit" type="primary" icon="icon-baocun">保存</u-btn>
				<u-btn size="small" v-if="!route.read" formType="reset" type="warn" icon="icon-zhongzhi">重置</u-btn>
				<u-btn size="small"   icon="icon-close" type="info"  @click="cancel">取消</u-btn>
			</template>
		</u-form>
	</div>
</template>

<script>
	import {ref,onMounted,computed} from "vue";
  import store from "@/pina";

  export default {

		setup(props,context){
      const {direct,user,tag} = store()
			 const list = direct.getDirect("permission",Number);
			 //路由
			 const route=uni.$currentFirstRouter
			 const tree=ref([])
			 uni.request({
				 service:'system',
			 	url:"/sysMenu/tree",
				success: (e) => {
					if(e.code==200){
						tree.value=e.data
					}
				}
			 })
			//必须携带默认属性
			const obj=ref(route.param!=undefined?route.param:{menuId:[],id:null,label:'',val:'',type:'',remark:''});
			const save=()=>{
				uni.request({
					service:'system',
					url:'/sysRole/save',
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
        tag.delTag("/system/sysRole/edit")
				//跳转前一个页面
				uni.$router.push(tag.currentTag)
			}

			return {user,route,obj,save,cancel,list,tree}
		}
	}
</script>
<style scoped>
</style>
 