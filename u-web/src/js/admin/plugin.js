 import {util} from '@wzabcd/u-plugin'
 import store from "@/pina";

 /**
  * 页面跳转拦截器
  */
export const uniInterceptor=(config,pages)=>{
	const {user}=store();
	uni.addInterceptor('navigateTo', {
	   invoke(args) {
			//登录验证拦截
			// const modules = import.meta.glob('/pages/**/*.vue')
			let obj=pages.pages.filter((item) => args.url.indexOf(item.path)!=-1);
			if(obj.length>0&&(obj[0].login==undefined||obj[0].login)&&(user.access_token==null||user.access_token=="")){
				//需要验证并且没有token跳转到登录页面
				args.url=config.loginPath;
				return
			}
			if(args.param!=undefined){
				args.url=args.url+util.urlArgs(args.param);
			}
	   },
	   success(args) {
	   }, 
	   fail(err) {
	 	  //请求失败
		  console.log(err)
		  uni.$msg({message:args.message,type:"error"})
	   }, 
	   complete(res) {
	 	  //请求完成
	   }
	 })
	/**
	 * 请求拦截器
	 */
	uni.addInterceptor('request', {
	  invoke(args) {
		//判断是否默认前缀
		if(args.prefix==undefined||args.prefix==true){
			//判断服务类型
			if(args.service==undefined||args.service==''){
				if(config.service==""||config.service==null||config.service==undefined){
					args.service="";
				}else{
					args.service=config.service;
				}
			}else{
				args.service.indexOf("/")==-1?args.service="/"+args.service:null;
			}
			args.url =config.serverPrefix+args.service+args.url;
		}	
		console.info("数据请求:"+args.url)
		if(args.header==undefined||args.header==null||args.header.Authorization==undefined||args.header.Authorization==""){

			if (user.access_token) {
			  args.header= {token:user.access_token};
			}
		}
		if(args.method==undefined){
			args.method="POST";
		}
	  },
	  success(args) {
		if(args.data!=''){
			if(args.data.code!=undefined){
				args.code=args.data.code;
				args.message=args.data.message;
				args.error=args.data.error;
				args.timestamp=args.data.timestamp;
				args.data=args.data.data;  
				//验证
				if(args.code!=200){
					console.log(args.message)
					if(typeof uni !="undefined"){
						  uni.$msg({lx:"msg",message:args.message,type:"error"})
					}else{
						window.$msg({lx:"msg",message:args.message,type:"error"})
					}
					
				}
			}
		}
	  }, 
	  fail(err) {
		  if(typeof uni !="undefined"){
			  uni.$msg({lx:"msg",message:err,type:"error"})
		  }else{
			  window.$msg({lx:"msg",message:err,type:"error"})
		  }
		  //请求失败
		  console.log(err)
	  }, 
	  complete(res) {
		  //请求完成
	  }
	})
	/**
	 * 附件上传拦截器
	 */
	uni.addInterceptor('uploadFile', {
	  invoke(args) {
	    // request 触发前拼接 url 
		if(args.url==""){
			 args.url = config.uploadPath
		}
	   
		if (user.access_token) {
		  args.header.Authorization='bearer '+user.access_token;
		}
	  },
	  success(args) {
		  if(util.isJSON(args.data) ){
			  args.data= JSON.parse(args.data);
		  }
	  }, 
	  fail(e) {
		  //请求失败
		  console.log(e.errMsg)
	  }, 
	  complete(res) {
		  //请求完成
	  }
	})
	/**
	 * 附件下载拦截器
	 */
	uni.addInterceptor('downloadFile', {
	  invoke(args) {
		if(args.url==""||args.url==undefined){
			 args.url = config.downloadPath
		}
		if(args.param!=undefined){
			 args.url=args.url+util.urlArgs(args.param);
		}
		if (user.access_token) {
		  args.header= {Authorization:'bearer '+user.access_token};
		}
	  },
	  success(args) {
	  }, 
	  fail(err) {
		  //请求失败
		  console.log(e.errMsg)
	  }, 
	  complete(res) {
		  //请求完成
	  }
	})
}