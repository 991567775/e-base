<template>
	<view>
		<u-query>
			<u-text   v-model="query.label" label="参数名称" prop="label" span="4"></u-text>
			<u-select   v-model="query.type" label="类型" prop="type" span="4" :list="list"></u-select>
      <template v-slot:btn>
				<u-btn type="primary"   @click="search" icon="icon-chaxun">查询</u-btn>
				<u-btn type="info"   @click="reset" icon="icon-zhongzhi">重置</u-btn>
			</template>
		</u-query>
    <div class="data-list">
      <div class="u-table-btn">
        <u-btn @click="add" icon="icon-xinzeng"  >新增</u-btn>
        <u-btn type="warn" icon="icon-xiugai" @click="edit">编辑</u-btn>
        <u-btn type="danger" icon="icon-ashbin" @click="del">删除</u-btn>
      </div>
      <u-table :field="field" :data="data" v-model:ids="ids" v-model:objs="objs">
        <template v-slot:option="data">
          <u-tips content="查看" class="icon icon-chakan" @click="view(data)"></u-tips>
          <u-tips content="编辑" class="icon icon-xiugai" @click="edit(data)"></u-tips>
          <u-tips content="删除" class="icon icon-shanchu" @click="del(data)"></u-tips>
        </template>
      </u-table>
      <u-page  v-model="page" @page="pageChange" ></u-page>
    </div>

	</view>
</template>
<script>
	import {ref} from 'vue';
	import {toDirect} from '@/js/toDirect.js'
  import store from "@/pina";
	export default {

		setup(){
			const {direct} = store();
			const list=ref(direct.getDirect("sysConfig"));
			//创建人、修改人显示
			const userList=toDirect("system","/sysUser/listAll",{},{val:"id",label:"name"});
			//字段
			const field=ref([
				{label: "参数名称", prop: "label",flex:"left"},
				{label: "参数值", prop: "val",flex:"left"},
				{label: "类型", prop: "type",isTag:true,list:list},
				{label: "备注", prop: "remark",width:150},
				{label: "创建时间", prop: "createDate",width:150,format:'yyyy-MM-dd hh:mm:ss'},
				{label: "更新时间", prop: "updateDate",width:150,format:'yyyy-MM-dd hh:mm:ss'},
				{label: "创建人", prop: "createUser",list:userList},
				{label: "更新人", prop: "updateUser",list:userList}
				])
      //数据
      const data=ref([]);
			//查询值
			const query=ref({input:''});
			//表格选中ids
			const ids=ref([])
			//表格选中对象集合
			const objs=ref([]);
      //分页
      const page=ref({})
			//查询方法
			const search=()=>{
				 uni.request({
           service:"system",
           url:"/sysConfig/list",
           data:Object.assign(query.value,page.value),
           success:(res)=>{
             if(res.code=200){
               data.value=res.data.records;
               page.value={total:res.data.total,pageNo:res.data.current}
             }
           }
         })
			}
      //分页事件
      const pageChange=(val)=>{
        search();
      }
      //页面加载执行
      search();
			//重置
			const reset=()=>{
        query.value={};
        search()
			}
			//新增按钮
			const add=()=>{
				uni.$router.push({label:"新增",path:"/system/sysConfig/edit"})
			}
			//编辑按钮
			const edit=(val)=>{
        if (objs.value.length > 1) {
        uni.$msg({ type: "warn", message: "请选择一条数据" });
        return false;
      }
        if(objs.value.length==1){
          uni.$router.push({label:"编辑",path:"/system/sysConfig/edit",param:objs.value[0]})
        }else if(val.item!=undefined){
          uni.$router.push({label:"查看",path:"/system/sysConfig/edit",param:val.item})
        }else {
          uni.$msg({message:"请选择数据",type:"warn"})
        }
			}
			//查看按钮
			const view=(val)=>{
        if(val.item!=undefined){
          uni.$router.push({label:"查看",path:"/system/sysConfig/edit",param:val.item,read:true})
        } else {
          uni.$msg({message:"请选择数据",type:"warn"})
        }
			}
			//删除按钮
			const del=(val)=>{
        if(objs.value.length>0||val.item!=undefined){
          uni.$msg({lx:"confirm",message:"确认删除么?",type:"error",confirm:(e)=>{
            if(e){
              uni.request({
                service:"system",
                method:"GET",
                url:"/sysConfig/del?ids="+(val.item!=undefined?val.item.id:ids.value.join(",")),
                success:(res)=>{
                  if(res.code==200){
                    data.value.removeObjs(val.item!=undefined?[val.item.id]:ids.value,"id")
                    objs.value.removeObjs(val.item!=undefined?[val.item.id]:ids.value,"id")
                    ids.value.removes(val.item!=undefined?[val.item.id]:ids.value);
                    uni.$msg({message:"删除成功"});
                  }
                }
              })
            }
            }})
        }else {
          uni.$msg({message:"请选择数据",type:"warn"});
        }
			}
			return {field,data,page,list,query,search,reset,add,edit,del,view,ids,objs,pageChange}
		}
	}
</script>

<style scoped>

</style>
 
 