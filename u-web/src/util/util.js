/**
 * by wz
 * 2021-10-28
 * 常用工具类
 * */
import { toDirect } from '@/js/toDirect.js';
const utils = {
	/**
	 *获取取水户
	 */
	getUserList:(store)=>{
		if(store.getters.userList.length==0){
			return  toDirect("water","/business/listAll",{},{val:"id",label:"name"});
		}else{
			return store.getters.userList;
		}
	},
	/**
	 * 设置取水户
	 */
	setUserList:(store)=>{
		store.commit("set_user_list",toDirect("water","/business/listAll",{},{val:"id",label:"name"}))
	},
	/**
	 * 获取公网ip
	 */
	getIp:()=>{
		return new Promise((resolve, reject)=>{
			//获取当前用户城市的ip等
			//跨域解决
			// #ifdef H5
			uni.request({
				prefix:false,
				url:'/cityjson',
				success:function(res){
					 resolve(JSON.parse(res.data.split("=")[1].replace(";",'')).cip);
				},
				fail: (e) => {
					 reject(e)
				}
			})
			// #endif
			//小程序等不走代理
			// #ifdef MP-ALIPAY
			my.request({
				dataType:'text',
				prefix:false,
				url:'http://pv.sohu.com/cityjson?ie=utf-8',
				success:function(res){
					 resolve(JSON.parse(res.data.split("=")[1].replace(";",'')).cip);
				},
				fail: (e) => {
					 reject(e)
				}
			})
			// #endif
			// #ifdef MP-WEIXIN
			wx.request({
				prefix:false,
				url:'http://pv.sohu.com/cityjson?ie=utf-8',
				success:function(res){
					 resolve(JSON.parse(res.data.split("=")[1].replace(";",'')).cip);
				},
				fail: (e) => {
					 reject(e)
				}
			})
			// #endif
			
		})
	},
    /**
     * 集合转换回显
     * @param list  集合
     * @param obj   字段参数
     * @returns {*[]}
     */
    toDirect:(list,obj={val:"id",label:"label"})=>{
        let newObj={}
        let newList=[];
        for(let key in list){
            if(key!=undefined&&key!="remove"){
                newObj={}
                newObj.val=list[key][obj.val];
                newObj.label=list[key][obj.label];
				if(newObj.val!=undefined){
					newList.push(newObj);
				}
            }
        }
        return newList;
    },
	
};
export default utils
