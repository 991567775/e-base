<template>
  <div class="data-edit">
    <u-form  @save="save" v-model="obj"  size="small" :rules="{val:{ required: true, message: '请输入参数值'}}">

      <u-hidden   prop="id" label="主键"  v-model="obj.id" />


      <template v-slot:btn>
        <u-btn   v-if="!route.read&&permissions['${table_name?replace('_',':')}:save']" formType="submit" type="primary" icon="icon-baocun">保存</u-btn>
        <u-btn   v-if="!route.read" formType="reset" type="info" icon="icon-zhongzhi">重置</u-btn>
        <u-btn     icon="icon-close"   @click="cancel">取消</u-btn>
      </template>

    </u-form>
  </div>
</template>

<script>
  import {ref,computed} from 'vue';
  import {useStore,mapGetters} from 'vuex'
  export default {
    //权限
    computed: {
      ...mapGetters([ "permissions"]),
    },
    setup(){
      const store = useStore()
      //路由
      const route=uni.$currentFirstRouter
      //obj对象
      const obj=ref(route.param!=undefined?route.param:{id:null});
      //保存
      const save=()=>{
        uni.request({
          url:'/${url}/save',
          data:obj.value,
          success: (e) => {
            if(e.code=200){
              uni.$msg({type:'success',message:'保存成功'}).then(()=>{
                cancel()
              })
            }else{
              uni.$msg({type:'error',message:e.message})
            }
          }
        })
      }

      //关闭
      const cancel=()=>{
        //关闭tag【方法，关闭地址】
        store.commit("del_tag","/${service}/${url}/edit");
        //跳转前一个页面
        uni.$router.push(store.getters.currentTag)
      }
      return {route,obj,save,cancel,list}
    }
  }
</script>
<style scoped>
</style>
