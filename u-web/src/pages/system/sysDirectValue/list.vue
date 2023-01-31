<template>
	<view>
		<u-query>
			<u-text size="small" v-model="query.label"  label="字典值名称" prop="label" span="4"></u-text>
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
      <u-table :field="field" :data="data" v-model:ids="ids" v-model:objs="objs">
        <template v-slot:option="data">
          <u-tips content="查看" class="icon icon-chakan" @click="view(data)"></u-tips>
          <u-tips content="编辑" class="icon icon-xiugai" @click="edit(data)"></u-tips>
          <u-tips content="删除" class="icon icon-shanchu" @click="del(data)"></u-tips>
        </template>
      </u-table>
      <u-page  v-model="page" @page="pageChange"></u-page>
    </div>

		<!--  子表信息-->
		<u-dialog   title="字典值"   v-model="show">
			<sys-direct-edit v-if="show" :data="obj" :read="read"   ></sys-direct-edit>
		</u-dialog>
	</view>
</template>
<script>
	import {ref,watch} from 'vue';
	import sysDirectEdit from './edit.vue'
	export default {
		components:{
			sysDirectEdit
		},
		props:{
			query:[Object]
		},

		setup(props){

      //表格
			const table=ref(null);
			//字段
			const field=ref([
				{label: "字典值名称", prop: "label"},
				{label: "字典值", prop: "val"},
				{label: "颜色", prop: "color"},
				{label: "排序", prop: "sort"},
				])
			//查询值
			const query=ref(Object.assign(props.query,{label: ""}));
      const data=ref([]);
      //查询值
      //表格选中ids
      const ids=ref([])
      //表格选中对象集合
      const objs=ref([]);
      const page=ref({})
      //子表查询
      const search=()=>{
        uni.request({
          service:"system",
          url:"/sysDirectValue/list",
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
			//表单对象信息
			const obj=ref({label:'',val:'',color:'',sort:0,pid:props.query.pid})
      //弹窗显示表单
			const show=ref(false);
			//新增显示表单
			const add=()=>{
				show.value=true
				read.value=false;
			}
			//编辑显示表单
			const edit=(val)=>{
        if (objs.value.length > 1) {
        uni.$msg({ type: "warn", message: "请选择一条数据" });
        return false;
      }
        if(objs.value.length==1||val.item!=undefined){
          show.value=true;
          obj.value=val.item;
          read.value=false;
        }else {
          uni.$msg({message:"请选择数据",type:"warn"})
        }
			}
			//查看显示表单
			const read=ref(false)
			const view=(val)=>{
        if(objs.value.length>0||val.item!=undefined){
          show.value=true;
          obj.value=val.item;
          read.value=true;
        }else {
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
                  url:"/sysDirectValue/del?ids="+(val.item!=undefined?val.item.id:ids.value.join(",")),
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
			return {table,field,data,query,page,ids,objs,pageChange,search,reset,add,edit,del,view,
			show,obj,read}
		}
	}
</script>

<style scoped>

</style>
 
 






 