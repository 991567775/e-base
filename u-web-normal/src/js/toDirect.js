import { customRef } from 'vue'
import utils from "./../util/util";
import store from '@/pina'
/**
 * 查询对应数据集转为select
 * @param path  请求路径
 * @param param 请求参数
 * @param obj  转为select 对象
 * @returns {*}
 */
export const toDirect = (service,path,param={},obj={val:"val",label:"label"}) => {
	const {user} = store();
    return customRef((track, trigger) => {
        let data=[];
		//vuex读取
		if(path=="/sysUser/listAll"&&user.userList!=null&&user.userList.length>0){
			data=user.userList;
			trigger() // 组件内部刷新模板
		}else{
			uni.request({
				service:service,
				url:path,
				data:param,
				success: (e) => {
					data= utils.toDirect(e.data,obj);
					if(path=="/sysUser/listAll"){
						user.setUserList(data)
					}
					trigger() 
				},
				fail: (e) => {
				}
			})
		}
		
        return {
            get () {
                track()
                return data;
            },
            set (newData) {
                trigger() // 组件内部刷新模板
            }
        }
    })
}
export  const getList = (service,path,param={}) => {
    return customRef((track, trigger) => {
        let data=[];
		uni.request({
			service:service,
			url:path,
			data:param,
			success: (e) => {
				data= e.data;
				trigger() // 组件内部刷新模板
			},
			fail: (e) => {
			}
		})
        return {
            get () {
                track()
                return data;
            },
            set (newData) {
                trigger() // 组件内部刷新模板
            }
        }
    })
}
