<template>
	<div class="data-edit">
		<u-form  @save="save" v-model="obj"  size="small">
      <u-hidden   prop="id" label="主键"  v-model="obj.id" />

      <u-text  left-icon="icon-xiugai" required prop="input" label="文本框"  :disabled="route.read" v-model="obj.input"
               :rule="{ required: true, message: '请输入',validator:test}"> </u-text>
			<u-text  required prop="input" label="文本框"  :disabled="route.read" v-model="obj.input" > </u-text>
			 <u-area required prop="area" label="输入框"  v-model="obj.area"></u-area>
			 <u-checkbox required prop="checkbox" label="多选框"  v-model="obj.checkbox"
			 :list="[{label:'123',val:1},{label:'456',val:2},{label:'3',val:3},{label:'4',val:4},{label:'5',val:5},{label:'6',val:6}]"></u-checkbox>
			  <edp-color-picker></edp-color-picker>
		 <u-slider required label="滑块"></u-slider>
			<u-number required prop="number" label="数字输入"   v-model="obj.number"></u-number>
			<u-radio required  prop="radio" label="单选"   v-model="obj.radio" :list="[{label:'123',val:1},{label:'456',val:2},{label:'456',val:2},{label:'456',val:2},{label:'456',val:2},{label:'456',val:2}]"></u-radio>
			<u-rate required  prop="rate" label="评分"  v-model="obj.rate"></u-rate>
			<u-switch  required prop="switch" label="开关"   v-model="obj.switch"></u-switch>
			<u-upload required
                url="/api/system/file/upload"
                down="/api/system/file/down"
                clickDown v-model="obj.path" v-model:name="obj.name" prop="upload" label="附件上传"></u-upload>
<!--      预览地址 https://static.ezeyc.cn+path    -->

<!--			  <u-select required multiple  prop="select" label="下拉选择"   v-model="obj.select"  :list="[{label:'cs',val:'3'},{label:'wz',val:'5'}]"></u-select>-->

<!--			 <u-date required format="yyyy-MM-dd" prop="date" label="日期"   v-model="obj.date"></u-date>-->
<!--			 <edp-color-picker></edp-color-picker> -->
<!--			 <u-time  prop="time" label="时间选择"   v-model="obj.time "></u-time>-->
<!--			 <u-time-select  prop="number" label="时间选择下拉"  v-model="obj.number"></u-time-select>-->
<!--			 <edp-tree></edp-tree> -->
			<template v-slot:btn>
				<u-btn size="small" v-if="!route.read&&user.permissions['sys:demo:save']" formType="submit" type="primary" icon="icon-baocun">保存</u-btn>
				<u-btn size="small" v-if="!route.read" formType="reset" type="warn" icon="icon-zhongzhi">重置</u-btn>
				<u-btn width="150rpx" type="info" size="small" icon="icon-close"   @click="cancel">取消</u-btn>
			</template>
		</u-form>
	</div>
</template>

<script>
	import {ref,computed} from 'vue';
  import store from "@/pina";
	export default {

		setup(props,context){
      const {direct,user,tag} = store()
			const route=uni.$currentFirstRouter
			//必须携带默认属性
			const obj=ref(route.param!=undefined?route.param:{input:'',path:['/2022/11/12/379384857543409664.config.js'],name:['vite.js'],sj:'',area:'',checkbox:[],date:'',number:0});
			const save=()=>{

				uni.request({
					service:'system',
					url:'/sysDemo/save',
					data:obj.value,
					success: (e) => {
						if(e.code==200){
							uni.$msg({type:'success',message:'保存成功'}).then(()=>{
								cancel()
							})
						}else{
							uni.$msg({type:'error',message:e.message})
						}
					}
				})
			}
			const cancel=()=>{
        //关闭tag【方法，关闭地址】
        tag.delTag("/system/sysDemo/edit")
				//跳转前一个页面
				uni.$router.push(tag.currentTag)
			}
      //自定义验证
      const test=(rule,val,message)=>{
        //验证错误提示 ，成功不需要管
        if(val.value=="1"){
          message("错误")
        }

      }
			return {user,route,obj,save,cancel,test}
		}
	}
</script>
	
<style>
</style>
