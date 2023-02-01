import { ref,reactive} from 'vue'

export  const organize= {
    organizeList: ref({companyList:[],departList:[],stationList:[],groupList:[]}),
    init:(form=null,isDepart=false,isStation=false,companyBack,departBack,stationBack)=>{
        uni.request({
			service:'system',
			url:'/sysCompany/selectList',
			data:{really:true},
			success: (e) => {
				if(e.data!=undefined) {
				    //集合赋值
				    organize.organizeList.value.companyList= e.data;
				    //回调判断
				    if(companyBack){
				        if(form!=null&&form.value.companyCode!=null){
				            ////单选多选
				            if(form.value.companyCode instanceof Array){
								 companyBack([e.data[0].val])
				                //todo
				            }else{//单选
				                companyBack(e.data[0].val)
				            }
				        }else{
				            companyBack(-1)
				        }
				    }
				    //默认选中设置
				    if(form!=null&&form.value.companyCode!=null) {
				        if(form.value.companyCode instanceof Array){//多选
				            //todo
							form.value.companyCode =[e.data[0].val];
				        }else{//单选
				            form.value.companyCode =e.data[0].val;
				        }
				        if(isDepart){//默认查询
				            organize.getDepartList(form,departBack,isStation,stationBack)
				        }
				    }
				}	
			}
        })
    },
    getDepartList:(form,departBack,isStation=false,stationBack)=>{
		uni.request({
			service:'system',
			url:'/sysDepart/selectList',
			data:{really:true,companyCode:form.value.companyCode?form.value.companyCode:''},
			success: (e) => {
				if(e.data!=undefined){
				    organize.organizeList.value.departList=e.data;
				    if(departBack){
				        if(form.value.departCode!=null){
				            if(form.value.departCode instanceof Array){//多选
				                                       //todo
								departBack([e.data[0].val])
				            }else{//单选
				                departBack(e.data[0].val)
				            }
				
				        }else{
				            departBack(-1)
				        }
				    }
				    //设置默认第一个选中
				    if(form.value.departCode!=null){
				        if(form.value.departCode instanceof Array){//多选
				            //todo
							 form.value.departCode =[e.data[0].val];
				        }else {//单选
				            form.value.departCode =e.data[0].val;
				        }
				        if(isStation){//默认查询
				            organize.getStationList(form,stationBack)
				        }
				    }
				}
			}
		})
        
    },
    getStationList:(form,stationBack)=>{
		uni.request({
			service:'system',
			url:'/sysStation/selectList',
			data:{really:true,departCode:form.value.departCode?form.value.departCode:''},
			success: (e) => {
				if(e.data!=undefined){
				    organize.organizeList.value.stationList=e.data;
				    //设置默认第一个选中
				    if(form.value.stationCode!=null){
				        if(form.value.stationCode instanceof Array){//多选
				            //todo
							form.value.stationCode = [e.data[0].val];
				        }else {//单选
				            form.value.stationCode = e.data[0].val;
				        }
				    }
				    if(stationBack){
				        if(form.value.stationCode!=null){
				            if(form.value.stationCode instanceof Array){//多选
				                //todo
								 stationBack([e.data[0].val]);
				            }else {//单选
				                stationBack(e.data[0].val);
				            }
				        }else{
				            stationBack(-1);
				        }
				    }
				}
			}
		})
    },
    getGroup:(form,groupBack)=>{
		uni.request({
			service:'system',
			url:'/sysGroup/selectList',
			success: (e) => {
				if(e.data!=undefined){
				    organize.organizeList.value.groupList=e.data;
				    //设置默认第一个选中
				    if(form.value.groupCode!=null){
				        if(form.value.groupCode instanceof Array){//多选
				            //todo
				        }else {//单选
				            form.value.groupCode = e.data[0].val;
				        }
				    }
				    if(groupBack){
				        if(form.value.groupCode!=null){
				            if(form.value.groupCode instanceof Array){//多选
				                //todo
				            }else {//单选
				                groupBack(e.data[0].val);
				            }
				        }else{
				            groupBack(-1);
				        }
				    }
				}
			}
		})
        
    },
    change:(form,isDepart=false,isStation=false,companyBack,departBack,stationBack)=>{
        if(form!=null&&form.value.companyCode!=null) {
            if (companyBack) {
                companyBack(form.value.companyCode);
            }
			
            if (isDepart) {
                organize.getDepartList(form,departBack,isStation,stationBack)
            }
            if(isStation){
                organize.getStationList(form,stationBack);
            }
        }
    }
}
