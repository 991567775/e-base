<template>
	<view>
		<u-query>
			<u-text size="small" v-model="query.username" label="登录名" prop="username" span="4"></u-text>
			<u-text size="small" v-model="query.name" label="昵称" prop="name" span="4"></u-text>
			<template v-slot:btn>
				<u-btn type="primary" size="small" @click="search" icon="icon-chaxun">查询</u-btn>
				<u-btn type="info" size="small" @click="reset" icon="icon-zhongzhi">重置</u-btn>
			</template>
		</u-query>
    <div class="data-list">
      <!-- 表格 -->
      <div class="u-table-btn">
        <u-btn @click="add" icon="icon-xinzeng"  >新增</u-btn>
        <u-btn type="warn" icon="icon-xiugai" @click="edit">编辑</u-btn>
        <u-btn type="danger" icon="icon-ashbin" @click="del">删除</u-btn>
<!--        <u-btn type="warn" icon="icon-jinggao" width="80px" @click="changePwd('change')">修改密码</u-btn>-->
        <u-btn type="warn" icon="icon-jinggao" width="80px" @click="changePwd('reset')">重置密码</u-btn>

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

	</view>
</template>
<script>
	import {ref} from 'vue';
	import {toDirect} from '@/js/toDirect.js'
  import store from "@/pina";
	export default {
		setup(){
      const {direct} = store();
			const state=ref(direct.getDirect("SysState",Boolean));
			//创建人、修改人显示
			const userList=toDirect("system","/sysUser/listAll",{},{val:"id",label:"name"});
			//公司
			const listCompany = toDirect("system","/sysCompany/listAll",{},{val:"code",label:"label"});
			//表格
			const table=ref(null);
			//字段
			const field=ref([
				{label: "登录名", prop: "username",width:100,flex:'left'},
				{label: "昵称", prop: "name",width:100},
				{label: "年龄", prop: "age"},
				{label: "出生日期", prop: "birth",width:100},
				{label: "手机号", prop: "phone",width:150},
				{label: "邮箱", prop: "email",width:150,},
				{label: "类型", prop: "type"},
				{label: "状态", prop: "status",list:state},
				{label: "所属公司",width:180, prop: "companyCode",list: listCompany,isTag:true,tag:{color:'#409eff',bgColor:'#d9ecff'}},
				{label: "创建时间", prop: "createDate",width:150,format:'yyyy-MM-dd'},
				{label: "更新时间", prop: "updateDate",width:150,format:'yyyy-MM-dd'},
				{label: "创建人", prop: "createUser",list:userList},
				{label: "更新人", prop: "updateUser",list:userList},
				])
			//查询值
			const query=ref({username:'',name:''});
      const data=ref([]);
      //查询值
      //表格选中ids
      const ids=ref([])
      //表格选中对象集合
      const objs=ref([]);
      const page=ref({})
      //查询
      const search=()=>{
        uni.request({
          service:"system",
          url:"/sysUser/list",
          data:Object.assign(query.value,page.value),
          success:(res)=>{
            if(res.code=200){
              data.value=res.data.records;
              page.value={total:res.data.total,pageNo:res.data.current}
            }
          }
        })
      }
      search();
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
				uni.$router.push({label:"新增",path:"/system/sysUser/edit"});
			}
			//编辑
			const edit=(val)=>{
        if (objs.value.length > 1) {
        uni.$msg({ type: "warn", message: "请选择一条数据" });
        return false;
      }
        if(objs.value.length==1){
          uni.$router.push({label:"编辑",path:"/system/sysUser/edit",param:objs.value[0]})
        }else if(val.item!=undefined){
          uni.$router.push({label:"查看",path:"/system/sysUser/edit",param:val.item})
        }else {
          uni.$msg({message:"请选择数据",type:"warn"})
        }
			}
			//查看
			const view=(val)=>{
        if(val.item!=undefined){
          uni.$router.push({label:"查看",path:"/system/sysUser/edit",param:val.item,read:true})
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
                  service:"system",
                  method:"GET",
                  url:"/sysUser/del?ids="+(val.item!=undefined?val.item.id:ids.value.join(",")),
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
      const changePwd=(type)=>{
          if(type=="reset"){
            if(ids.value.length==0){
              uni.$msg({message:"请选择数据",type:"warn"});
              return
            }else {
              uni.request({
                service:"system",
                url:"/sysUser/pwd",
                data:{ids:ids.value,pwd:1234},
                success:(res)=>{
                  if(res.code==200){
                    uni.$msg({message:"重置成功,请退出后重新登录"});
                  }
                }
              })
            }
          }else if(type=="change"){
            if(ids.value.length==0){
              uni.$msg({message:"请选择数据",type:"warn"});
              return
            }else {
              uni.request({
                service:"system",
                url:"/sysUser/pwd",
                data:{ids:ids.value,pwd:1234},
                success:(res)=>{
                  if(res.code==200){
                    uni.$msg({message:"修改成功,请退出后重新登录"});
                  }
                }
              })
            }
          }
      }
			return {changePwd,table,field,query,data,page,ids,objs,pageChange,search,reset,add,edit,del,view}
		}
	}
</script>

<style scoped>

</style>
 