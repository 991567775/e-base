<template>
	<view>
		<u-query>
			<u-text size="small" v-model="query.label" label="字典名称" prop="label" span="4"></u-text>
			<template v-slot:btn>
				<u-btn type="primary" size="small" @click="search" icon="icon-chaxun">查询</u-btn>
				<u-btn type="info" size="small" @click="reset" icon="icon-zhongzhi">重置</u-btn>
			</template>
		</u-query>
    <div class="data-list">
      <div class="u-table-btn">
        <u-btn @click="add" icon="icon-xinzeng"  >新增</u-btn>
        <u-btn type="warn" icon="icon-xiugai" @click="edit">编辑</u-btn>
        <u-btn type="danger" icon="icon-ashbin" @click="del">删除</u-btn>
      </div>
      <!-- 表格 -->
      <u-table :field="field" :data="data" v-model:ids="ids" v-model:objs="objs">
        <template v-slot:option="data">
          <u-tips content="查看" class="icon icon-chakan" @click="view(data)"></u-tips>
          <u-tips content="编辑" class="icon icon-xiugai" @click="edit(data)"></u-tips>
          <u-tips content="删除" class="icon icon-shanchu" @click="del(data)"></u-tips>
          <u-tips content="字典" class="icon icon-shujuzidian" @click="openDialog(data)"></u-tips>
        </template>
      </u-table>
      <u-page  v-model="page" @page="pageChange"></u-page>
    </div>
	 	<!--  子表信息-->
		<u-dialog   title="字典值"   v-model="show">
			  <sys-direct-value v-if="show" :query="childQuery"></sys-direct-value>
		</u-dialog>
	</view>
 
</template>
<script>
	import {ref} from 'vue';
  import store from "@/pina";
	import {toDirect} from '@/js/toDirect.js'
	import sysDirectValue from '../sysDirectValue/list'
	export default {
		components:{
			sysDirectValue
		},

		setup(){
      const {direct} = store();
			const list=ref(direct.getDirect("sysConfig"));
			//创建人、修改人显示
			const userList=toDirect("system","/sysUser/listAll",{},{val:"id",label:"name"});
			// 表格
			const table=ref(null);
			//字段
			const field=ref([
				{label: "字典名称", prop: "label"},
				{label: "字段编码", prop: "code"},
				{label: "创建人", prop: "createUser",list:userList},
				{label: "更新人", prop: "updateUser",list:userList}
				])
      const data=ref([]);
      //查询值
      const query=ref({});
      //表格选中ids
      const ids=ref([])
      //表格选中对象集合
      const objs=ref([]);
      const page=ref({})
      //查询
      const search=()=>{
        uni.request({
          service:"system",
          url:"/sysDirect/list",
          data:Object.assign(query.value,page.value),
          success:(res)=>{
            if(res.code=200){
              data.value=res.data.records;
              page.value={total:res.data.total,pageNo:res.data.current}
            }
          }
        })
      }
      search()
      //分页事件
      const pageChange=(val)=>{
        search();
      }
			//重置
			const reset=()=>{
        query.value={};
        search()
			}
			//新增
			const add=()=>{
				uni.$router.push({label:"新增",path:"/system/sysDirect/edit"});
			}
			//编辑
			const edit=(val)=>{
        if (objs.value.length > 1) {
        uni.$msg({ type: "warn", message: "请选择一条数据" });
        return false;
      }
        if(objs.value.length==1){
          uni.$router.push({label:"编辑",path:"/system/sysDirect/edit",param:objs.value[0]})
        }else if(val.item!=undefined){
          uni.$router.push({label:"查看",path:"/system/sysDirect/edit",param:val.item})
        }else {
          uni.$msg({message:"请选择数据",type:"warn"})
        }
			}
			//查看
			const view=(val)=>{
        if(val.item!=undefined){
          uni.$router.push({label:"查看",path:"/system/sysDirect/edit",param:val.item,read:true})
        } else {
          uni.$msg({message:"请选择数据",type:"warn"})
        }
			}
			//删除
			const del=(val)=>{
        if(objs.value.length>0||val.item!=undefined){
          uni.$msg({lx:"confirm",message:"确认删除么?",type:"warn",confirm:(e)=>{
              if(e){
                uni.request({
                  method:"GET",
                  service:'system',
                  url:"/sysDirect/del?ids="+(val.item!=undefined?val.item.id:ids.value.join(",")),
                  success: (e) => {
                    if(e.code==200){
                      data.value.removeObjs(val.item!=undefined?[val.item.id]:ids.value,"id")
                      objs.value.removeObjs(val.item!=undefined?[val.item.id]:ids.value,"id")
                      ids.value.removes(val.item!=undefined?[val.item.id]:ids.value);
                      uni.$msg({message:'删除成功',type:'success'})
                    }else{
                      uni.$msg({message:e.message,type:'error'})
                    }
                  }
                })
              }
            }})
        }else {
          uni.$msg({message:"请选择数据",type:"warn"});
        }
			}
			//子表
			//弹出框显示
			const show=ref(false);
			//子表查询条件
			const childQuery=ref({})
			//打开弹出框
			const openDialog=(val)=>{
			  childQuery.value={pid:val.item.id};//主键值
			  show.value=true;
			}
			//更新缓存
			const updateStore=()=>{
        direct.delDirect().then(e=>{
          direct.getDirect().then(o=>{
            uni.$msg({type:'success',message:"更新成功"})
          })
        })
			}
			return {table,field,data,query,page,ids,objs,pageChange,search,reset,add,edit,del,view,updateStore,openDialog,
			show,childQuery}
		}
	}
</script>

<style scoped>

</style>