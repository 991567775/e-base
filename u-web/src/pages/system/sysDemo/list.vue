<template>
	<view>
		<u-query>
			<u-text size="small" v-model="query.input" label="input输入框" prop="input" span="4"></u-text>
			<u-text size="small" v-model="query.input" label="input输入框" prop="input" span="4"></u-text>
			<u-text size="small" v-model="query.input" label="input输入框" prop="input" span="4"></u-text>
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
        <!--自定义表头：head-列名称-->
        <template v-slot:head-id="data">
          这是id自定义列
        </template>
        <!--操作栏自定义-->
        <template v-slot:option="data">
          <u-tips content="查看" class="icon icon-chakan" @click="view(data)"></u-tips>
          <u-tips content="编辑" class="icon icon-xiugai" @click="edit(data)"></u-tips>
          <u-tips content="删除" class="icon icon-shanchu" @click="del(data)"></u-tips>
        </template>
        <!--自定义数据列：列名称-->
        <template v-slot:checkbox="data">
          <view @click="view(data)">列模板</view>
        </template>
      </u-table>
      <!--分页-->
      <u-page  v-model="page" @page="pageChange"></u-page>
    </div>
	</view>
</template>

<script>
	import {ref,computed} from 'vue';
  import store from "@/pina";
	import {config} from '@/config.js';
	export default {

		setup(){
      const {direct} = store();
			//表格
			const table=ref(null);
			//字段
			const field=ref([
				{prop:"id",label:"主键",width:150},
				{prop:"input",label:"输入框",width:80,sort:true},
				{prop:"radio",title:"合并",label:"单选框",width:400},
				{prop:"rate",title:"合并",label:"rate",width:600},
				{prop:"cas",label:"级联",flex:"left"},
				{prop:"tan",label:"其他",flex:"right"},
        {prop:"checkbox",label:"多选"},
				])
      const data=ref([]);
      //查询值
      const query=ref({input:''});
      //表格选中ids
      const ids=ref([]);
      //表格选中对象集合
      const objs=ref([]);
      const page=ref({});
			//查询
			const search=()=>{
        uni.request({
          service:"system",
          url:"/sysDemo/list",
          data:query.value,
          success:(res)=>{
            if(res.code=200){
              data.value=res.data.records;
              page.value={total:res.data.total,pageNo:res.data.current}
            }
          }
        })
			};
      search();
      //分页事件
      const pageChange=(val)=>{
        search();
      };
			//重置
			const reset=()=>{
        query.value={};
        search()
			};
			//新增
			const add=()=>{
				uni.$router.push({label:"新增",path:"/system/sysDemo/edit"});
			};
			//编辑
			const edit=(val)=>{
        if (objs.value.length > 1) {
        uni.$msg({ type: "warn", message: "请选择一条数据" });
        return false;
      }
        if(objs.value.length==1){
          uni.$router.push({label:"编辑",path:"/system/sysDemo/edit",param:objs.value[0]})
        }else if(val.item!=undefined){
          uni.$router.push({label:"查看",path:"/system/sysDemo/edit",param:val.item})
        }else {
          uni.$msg({message:"请选择数据",type:"warn"})
        }
			};
			//查看
			const view=(val)=>{
        if(val.item!=undefined){
          uni.$router.push({label:"查看",path:"/system/sysConfig/edit",param:data.item,read:true})
        } else {
          uni.$msg({message:"请选择数据",type:"warn"})
        }
			};
			//删除
			const del=(val)=>{
        if(objs.value.length>0||val.item!=undefined){
          uni.$msg({lx:"confirm",message:"确认删除么?",type:"warn",confirm:(e)=>{
              if(e){
                uni.request({
                  service:"system",
                  method:"GET",
                  url:"/sysDemo/del?ids="+(val.item!=undefined?val.item.id:ids.value.join(",")),
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
			};
			const exportExl=()=>{
				//pc端导出
				 window.open(config.url+"/system/sysDemo/exl?ids=1", "_blank");
			};
			return {table,field,data,query,page,ids,objs,pageChange,search,reset,add,edit,del,view,exportExl}
		}
	}
</script>

<style scoped>

</style>
