<template>
	<!-- 树表单 -->
	<u-tree-form   :data="treeList" @treeClick="treeClick" v-model="obj" @save="save">
		<!-- 树选择联动 -->
		<template v-slot:select>
			<u-select @select="changeCompany"  v-model="obj.companyCode"  :list="organize.organizeList.value.companyList"></u-select>
			<u-select @select="changeDepart"  v-model="obj.departCode"  :list="organize.organizeList.value.departList"></u-select>
		</template>
		<!-- 表单 -->
		<u-hidden  v-model="obj.id" />
		<u-hidden      v-model="obj.pid" />
		<u-text required v-model="obj.label" size="small"  label="岗位名称" prop="label"  maxlength="30"/>
		<u-text required  v-model="obj.code" size="small"  label="岗位编码"  prop="code" maxlength="8"/>
		<u-radio :list="yesNo" size="small"  label="是否是岗位"  prop="really" v-model="obj.really"/>
		<u-number v-model="obj.sort" size="small" label="排序"  prop="sort" :max="9999"/>
		<u-area   v-model="obj.content" size="small" label="描述" prop="content" maxlength="100"/>
		<!-- 表单按钮 -->
		<template v-slot:btn>
			<u-btn @click="add" icon="icon-xinzeng" v-if="user.permissions['sys:station:add']" size="small" type="primary">新增</u-btn>
			<u-btn formType="submit" icon="icon-xiugai"  v-if="user.permissions['sys:station:save']" size="small" type="warn">保存</u-btn>
			<u-btn @click="clean" icon="icon-qingkong"  size="small" type="info">清空</u-btn>
			<u-btn @click="del" icon="icon-shanchu" v-if="user.permissions['sys:station:del']" size="small" type="danger">删除</u-btn>
		</template>
	</u-tree-form>
</template>
<script>
import {ref,onMounted,computed} from "vue";
import {organize} from "@/js/organize.js";
import store from "@/pina";
export default {
	name: "岗位配置",

	setup(){
		//存储
    const {direct,user} = store();
		//字典
		const yesNo=direct.getDirect("yesNo",Boolean);
		//表单对象
		const obj = ref({id:null,really:false,pid:'',code:'',label:"",content:"",companyCode:[],departCode:[],sort:0})
		//树数据集合
		const treeList=ref([])
		//查询树数据
		const select=(val)=>{
			uni.request({
				service:"system",
				url:"/sysStation/tree",
				data:val,
				success: (e) => {
					treeList.value=e.data
				}
			})
		}
		//组织下拉框（公司）
		onMounted(()=>{
			organize.init(obj,true,false,null,(val)=>{
				//默认初始化
				obj.value.departCode=val;
				select({departCode:val})
			})
		})
		//树节点点击
		const treeClick=(node)=>{
			//设置公司值
			node.companyCode=obj.value.companyCode
			node.departCode=obj.value.departCode
			//回显
			obj.value=node
		}
		//保存
		const save=()=> {
      if(obj.value.id==-1){
        uni.$msg({type:"warn",message:"顶级节点不可操作"})
        return ;
      }
      if(obj.value.pid==""&&obj.value.pid!=null){
        uni.$msg({type:"warn",message:"请选父节点后保存"})
        return ;
      }
			uni.request({
				service:'system',
				url:"/sysStation/save",
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
        uni.$msg({type:"warn",message:"请选择节点"})
        return ;
      }
		  obj.value={pid:obj.value.pid,companyCode:obj.value.companyCode,departCode:obj.value.departCode}
		}
		//新增
		const add=()=>{
      if(obj.value.id==""&&obj.value.id!=null){
        uni.$msg({type:"warn",message:"请选择节点"})
        return ;
      }
			//将节点id赋值给子节点pid以及公司赋值
			obj.value={pid:obj.value.id,companyCode:obj.value.companyCode,departCode:obj.value.departCode}
		}
		//删除
		const  del=()=>{
      if(obj.value.id==-1){
        uni.$msg({type:"warn",message:"顶级节点不可操作"})
        return ;
      }
      if(obj.value.id==""&&obj.value.id!=null){
        uni.$msg({type:"warn",message:"请选择节点"})
        return ;
      }
			uni.$msg({lx:"confirm",type:"danger",message:"确认删除？删除后不可恢复!",title:"删除",confirm:(e)=>{
				if(e){
					//执行删除
					uni.request({
						method:'GET',
						service:'system',
						url:"/sysStation/del?ids="+obj.value.id,
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
		//选择公司
		const changeCompany=()=>{
			organize.change(obj,true,false,null,(e)=>{
				select({departCode:obj.value.departCode})
			})
		}
		//选择部门
		const changeDepart=(val)=>{
			obj.value.departCode=val;
			select({companyCode:obj.value.companyCode,departCode:val})
		}
		return {user,treeList,obj,yesNo,treeClick,save,clean,del,add,changeCompany,changeDepart,organize}
	}
}
</script>
<style scoped>
</style>