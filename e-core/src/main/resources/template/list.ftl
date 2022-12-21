<template>
  <view>
    <u-query>

      <template v-slot:btn>
        <u-btn type="primary"   @click="search" icon="icon-chaxun">查询</u-btn>
        <u-btn type="info"   @click="reset" icon="icon-zhongzhi">重置</u-btn>
      </template>
    </u-query>
    <div class="data-list">
      <div class="u-table-btn">
        <u-btn @click="add" icon="icon-xinzeng"   v-if="permissions['${table_name?replace('_',':')}:add']" >新增</u-btn>
        <u-btn type="warn" icon="icon-xiugai"  v-if="permissions['${table_name?replace('_',':')}:save']" @click="edit">编辑</u-btn>
        <u-btn type="danger" icon="icon-ashbin" v-if="permissions['${table_name?replace('_',':')}:del']" @click="del">删除</u-btn>
      </div>
      <u-table :field="field" :data="data" v-model:ids="ids" v-model:objs="objs">
        <template v-slot:option="data">
          <u-tips content="查看" v-if="permissions['${table_name?replace('_',':')}:view']" class="icon icon-chakan" @click="view(data)"></u-tips>
          <u-tips content="编辑" v-if="permissions['${table_name?replace('_',':')}:save']" class="icon icon-xiugai" @click="edit(data)"></u-tips>
          <u-tips content="删除" v-if="permissions['${table_name?replace('_',':')}:del']" class="icon icon-shanchu" @click="del(data)"></u-tips>
        </template>
      </u-table>
      <u-page  v-model="page" @page="pageChange" ></u-page>
    </div>

  </view>
</template>
<script>
  import {ref,watch} from 'vue';
  import {useStore,mapGetters} from 'vuex'
  export default {
    //权限
    computed: {
      ...mapGetters([ "permissions"]),
    },
    setup(){
      const store = useStore()
      //字段
      const field=ref([
        <#if column?exists>
        <#list column as model>
        {label: "${model.columnComment}", prop: "${model.changeColumnName}"},
        </#list>
        </#if>
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
          url:"/${url}/list",
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
        uni.$router.push({label:"新增",path:"/${service}/${url}/edit"})
      }
      //编辑按钮
      const edit=(val)=>{
        if(objs.value.length>0){
          uni.$router.push({label:"编辑",path:"/${service}/${url}/edit",param:objs.value[0]})
        }else if(val.item!=undefined){
          uni.$router.push({label:"查看",path:"/${service}/${url}/edit",param:val.item})
        }else {
          uni.$msg({message:"请选择数据",type:"warn"})
        }
      }
      //查看按钮
      const view=(val)=>{
        if(val.item!=undefined){
          uni.$router.push({label:"查看",path:"/${service}/${url}/edit",param:val.item,read:true})
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
                  url:"/${url}/del?ids="+(val.item!=undefined?val.item.id:ids.value.join(",")),
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
