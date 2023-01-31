<template>
	<!-- 树表单 -->
	<u-tree-form   :data="treeList" @treeClick="treeClick" v-model="obj" @save="save">
		<!-- 表单 -->
		<u-hidden    v-model="obj.id" />
		<u-hidden     v-model="obj.pid" />
		<u-text required v-model="obj.label" size="small" label="公司名称" prop="label"  maxlength="30"/>
		<u-text required  v-model="obj.code" size="small" label="公司编码"  prop="code" maxlength="8"/>
		<u-radio :list="yesNo" size="small"  label="是否是公司"  prop="really" v-model="obj.really"/>
		<u-number v-model="obj.sort" size="small" label="排序"  prop="sort" :max="9999"/>
		<u-area   v-model="obj.content" size="small" label="描述" prop="content" maxlength="100"/>
		<!-- 表单按钮 -->
		<template v-slot:btn>
			<u-btn @click="add"  icon="icon-xinzeng"  v-if="permissions['sys:company:add']" size="small" type="primary">新增</u-btn>
			<u-btn formType="submit" icon="icon-xiugai" v-if="permissions['sys:company:save']" size="small" type="warn">保存</u-btn>
			<u-btn @click="del"  icon="icon-shanchu" v-if="permissions['sys:company:del']" size="small" type="danger">删除</u-btn>
			<u-btn @click="clean"  icon="icon-qingkong" size="small" type="info">清空</u-btn>
		</template>
	</u-tree-form>
</template>
<script>
import {ref,onMounted} from "vue";
import store from "@/pina";
export default {
	name: "机构配置",
	//权限
	setup(){
		//存储
    const {direct} = store();
		// //字典
		const yesNo=direct.getDirect("yesNo",Boolean);
		//表单对象
		const obj = ref({id:'',pid:'',code:'',label:"",content:"",really:true,sort:0})
		//查询树数据
		const treeList=ref([]);
		onMounted(()=>{
			uni.request({
				service:"system",
				url:"/sysCompany/tree",
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
				url:"/sysCompany/save",
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
        uni.$msg({type:"warn",message:"请选节点"})
        return ;
      }
			uni.$msg({lx:"confirm",type:"error",message:"确认删除?",title:"删除",confirm:(e)=>{
				if(e){
					uni.request({
						method:'GET',
						service:'system',
						url:"/sysCompany/del?ids="+obj.value.id,
						success: (e) => {
							if(e.code==200){
								uni.$msg({type:'success',message:"删除成功"})
							}else{
                uni.$msg({type:"error",message:"删除失败"})
							}
						}
					})
				}
				
			}})
			//执行删除
			
		}
		return {treeList,obj,yesNo,treeClick,save,clean,del,add}
	}
}
</script>
<style scoped>
</style>