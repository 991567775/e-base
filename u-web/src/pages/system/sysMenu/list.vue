<template>
	<!-- 树表单 -->
	<u-tree-form   :data="treeList" @treeClick="treeClick" v-model="obj" @save="save">
		<!-- 表单 -->
		<u-hidden  v-model="obj.id" />
		<u-hidden     v-model="obj.pid" />
		<u-text required v-model="obj.label" size="small" label="菜单名称" prop="label"  maxlength="30"/>
		<u-radio :list="menuType" size="small"  label="菜单类型"  prop="menuType" v-model="obj.menuType"/>
		<u-text  v-if="obj.menuType!=2" v-model="obj.path" size="small" label="菜单路径"  prop="path" maxlength="150"/>
		<u-text   v-model="obj.permission" label="权限标识" prop="permission" maxlength="40"></u-text>
		<u-radio :list="yesNo" size="small"  label="是否显示"  prop="display" v-model="obj.display"/>
		<u-text v-if="obj.menuType!=2" label="图标"  prop="icon" v-model="obj.icon" maxlength="150"></u-text>
		<u-number v-model="obj.sort" size="small" label="排序"  prop="sort" :max="9999"/>
		<!-- 表单按钮 -->
		<template v-slot:btn>
			<u-btn @click="updateStore" icon="icon-xiaoqizi" width="70px" size="small" type="primary">更新缓存</u-btn>
			<u-btn @click="add"  icon="icon-xinzeng" v-if="user.permissions['sys:menu:add']" size="small" type="primary">新增</u-btn>
			<u-btn formType="submit" icon="icon-xiugai" v-if="user.permissions['sys:menu:save']" size="small" type="warn">保存</u-btn>
			<u-btn @click="clean" icon="icon-qingkong"  size="small" type="info">清空</u-btn>
			<u-btn @click="del" icon="icon-ashbin"  v-if="user.permissions['sys:menu:del']" size="small" type="danger">删除</u-btn>
		</template>
	</u-tree-form>
</template>
<script>
	import {ref,onMounted,computed} from "vue";
  import store from "@/pina";
export default {
	name: "机构配置",
	//权限
	setup(){
		//存储
    const {direct,menu,user} = store();
		// //字典
		const yesNo=direct.getDirect("yesNo",Boolean);
		const menuType=direct.getDirect("menuType",Number);
		//表单对象
		const  obj = ref({id:null,pid:0,path:'',permission:'',menuType:null,icon:'',label:"",really:true,display:true,companyId:'',sort:0})
		//查询树数据
		const treeList=ref([]);
		onMounted(()=>{
			uni.request({
				service:"system",
				url:"/sysMenu/tree",
				success: (e) => {
					treeList.value=e.data
				}
			})
		})
		//树节点点击事件
		const treeClick=(node)=>{
			//回显
			obj.value=node
		}
		//表单保存事件
		const save=()=> {
      if(obj.value.id==-1){
        uni.$msg({type:"warn",message:"顶级节点不可操作"})
        return ;
      }
      if(obj.value.pid==""&&obj.value.pid!=null){
        uni.$msg({type:"warn",message:"请选择父节点后保存"})
        return ;
      }
			uni.request({
				service:'system',
				url:"/sysMenu/save",
				data:obj.value,
				success: (e) => {
					if(e.code==200){
						uni.$msg({type:'success',message:"保存成功"})
					}else{
						uni.$msg({type:'error',message:"保存失败"})
					}
				}
			})
		}
		//清空
		const clean=()=>{
      if(obj.value.id==""&&obj.value.id!=null){
        uni.$msg({type:"error",message:"请选择节点"})
        return ;
      }
		  obj.value={pid:obj.value.pid}
		}
		//新增
		const add=()=>{
      if(obj.value.id==""&&obj.value.id!=null){
        uni.$msg({type:"warn",message:"请选择节点"})
        return ;
      }
			//将节点id赋值给子节点pid
			obj.value={pid:obj.value.id}
		}
		//删除
		const  del=()=>{
      if(obj.value.id==-1){
        uni.$msg({type:"warn",message:"顶级节点不可操作"})
        return ;
      }
      if(obj.value.id==""&&obj.value.id!=null){
        uni.$msg({type:"error",message:"请选择节点"})
        return ;
      }
      uni.$msg({lx:"confirm",type:"error",message:"确认删除？删除后不可恢复!",title:"删除",confirm:(e)=>{
				if(e){
					//执行删除
					uni.request({
						method:'GET',
						service:'system',
						url:"/sysMenu/del?ids="+obj.value.id,
						success: (e) => {
							if(e.code==200){
								uni.$msg({type:'success',message:"删除成功"})
							}else{
								uni.$msg({type:'error',message:"删除失败"})
							}
						}
					})
				}
			}})	
			
		}
		const updateStore=()=>{
      menu.deleteAllMenu().then(e=>{
        menu.getTopMenu({perm:user.userInfo.permissions,userId:user.userInfo.id}).then(o=>{
          uni.$msg({type:'success',message:"更新成功"})
        })
      })
		}
		return {user,treeList,obj,yesNo,menuType,treeClick,save,clean,del,add,updateStore}
	}
}
</script>
<style scoped>
</style>