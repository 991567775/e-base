<template>
	<div class="data-edit">
		<u-form  @save="save" v-model="obj"  size="small">
			<u-hidden  v-model="obj.id"></u-hidden>
			<u-hidden  v-model="obj.pid"></u-hidden>
			<u-text  required  label="字典值名称" prop="label"  :disabled="read" v-model="obj.label" > </u-text>
			<u-text  required  label="字典值"  prop="val"  :disabled="read" v-model="obj.val"  maxlength="30" > </u-text>
			<u-text  label="颜色"  prop="color"  :disabled="read" v-model="obj.color"  maxlength="30" > </u-text>
      <u-text  label="背景色"  prop="bgColor"  :disabled="read" v-model="obj.bgColor"  maxlength="30" > </u-text>

			<u-number v-model="obj.sort" size="small" label="排序"  prop="sort" :max="9999"/>
      <template v-slot:btn>
				<u-btn size="small"    formType="submit" type="primary" icon="icon-baocun">保存</u-btn>
        <u-btn size="small" v-if="!read" formType="reset" type="warn" icon="icon-zhongzhi">重置</u-btn>
      </template>
		</u-form>
	</div>
</template>
<script>
import {ref} from 'vue';
export default {
  name: "sysDirectValueEdit",

	props:{
		data:{type:Object,default:{}},
		read:{type:Boolean,default:false},
	},
	setup(props, context){
		const obj = ref(props.data)//对象实例
		//保存
		const save=()=> {
			uni.request({
				service:'system',
				url:'/sysDirectValue/save',
				data:obj.value,
				success: (e) => {
					if(e.code==200){
						uni.$msg({type:'success',message:'保存成功'})
					}
				}
			}) 
		}
		
		return {obj,save}
	},
}
</script>
<style scoped>

</style>