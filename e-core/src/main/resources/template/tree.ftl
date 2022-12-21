<template>
  <!-- 树表单 -->
  <u-tree-form   :data="treeList" @treeClick="treeClick" v-model="obj" @save="save">
    <!-- 表单 -->
    <u-hidden  v-model="obj.id" />
    <u-hidden     v-model="obj.pid" />

    <!-- 表单按钮 -->
    <template v-slot:btn>
      <u-btn @click="add"  icon="icon-xinzeng" v-if="permissions['${table_name?replace('_',':')}:add']" size="small" type="primary">新增</u-btn>
      <u-btn formType="submit" icon="icon-xiugai" v-if="permissions['${table_name?replace('_',':')}:save']" size="small" type="warn">保存</u-btn>
      <u-btn @click="del" icon="icon-ashbin"  v-if="permissions['${table_name?replace('_',':')}:del']" size="small" type="danger">删除</u-btn>
    </template>
  </u-tree-form>
</template>
<script>
  import {ref,onMounted,computed} from "vue";
  import {useStore,mapGetters} from 'vuex'
  export default {
    //权限
    computed: {
      ...mapGetters([ "permissions"]),
    },
    setup(){
      //存储
      const store=useStore();
      //表单对象
      const  obj = ref({id:null})
      //查询树数据
      const treeList=ref([]);
      onMounted(()=>{
        uni.request({
          url:"/${url}/tree",
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
          url:"/${url}/save",
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
                url:"/${url}/del?ids="+obj.value.id,
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

      return {treeList,obj,treeClick,save,clean,del,add}
    }
  }
</script>
<style scoped>
</style>