<template>
  <u-container :top="false"   :bottom="false"   class="content"   @click="clickFatherBox"  >
    <template v-slot:title>
      <div class="bg-dark bg-title">{{ config.appBgName }}</div>
    </template>
    <template v-slot:top>
      <div class="bg-top">
        <div class="bg-top-right">
          <div class="bg-top-menus" >
            <div class="bg-top-menu">
              <div  :class="   'bg-top-menu-label ' +   (menu.currentTopMenu.id == item.id ? 'bg-top-menu-active' : '')  "
                @click="topMenuClick(item)" v-for="(item, index) in menu.topMenu"  :key="index"  >
                {{ item.label }}
              </div>
            </div>
          </div>
          <div   class="admin"  @click.stop="change" >
            <span>{{ user.userInfo.name }}</span>
            <i class="icon icon-xiala1"   style="display: inline-block; margin: 0 5px" ></i>
            <div class="layer" :style="{ display: showLayer ? 'block' : 'none' }"  >
              <p  @click.stop="showPwdD"      >  修改密码  </p>
              <p @click="loginout">退出</p>
            </div>
          </div>
        </div>
      </div>
    </template>
    <template v-slot:left>
      <div class="bg-dark">
        <u-tree   class="baseTree"  :option="{   children: 'children',  label: 'label',   show: 'show',   path: 'path',  val: 'path',   icon: 'icon',    }"
          @treeClick="treeClick"  v-model="tag.currentTag.path"  :list="menu.menu"
        ></u-tree>
      </div>
    </template>
    <template v-slot:admin>
      <div style="height: 100%">
        <div style="display: flex; align-items: center">
          <u-tabs   @tabClick="tabClick"   :tabClose="closeTag"  type="tag"   v-model="tag.currentTag.path"   :list="tag.tagList"  >  </u-tabs>
          <div   style="width: 100px; cursor: pointer; position: relative"   @click.stop="changeTabs"  >
            操作
            <i class="icon icon-xiala"></i>
            <div   class="layerTabs"   :style="{ display: showLayerTabs ? 'block' : 'none' }"    >
              <p @click.stop="closeOthersTags">关闭其他标签</p>
              <p @click.stop="closeAllTags">关闭所有标签</p>
            </div>
          </div>
        </div>
        <div class="bg-main-bk">
          <div class="bg-main">
            <first-router   :path="tag.currentTag.path"   style="overflow-y: auto"
            ></first-router>
          </div>
        </div>
        <div class="bg-bottom">
          {{ config.appBgBottom }}
        </div>
      </div>
    </template>
  </u-container>



</template>


<script>
import { ref} from "vue";
import { config } from "@/config";
import store from "@/pina";
export default {
  setup() {
    const {menu,tag,user} = store();
    //未登录跳转到登录页
    if (user.access_token == undefined || user.access_token == "" ) {
      uni.navigateTo({ url: config.loginPath });
    }


    //菜单点击事件
    const treeClick = (item) => {
      if (item.path.indexOf(menu.currentTopMenu.path) == -1) {
        store.commit("set_current_tag", item);
      }
      uni.$router.push(item);
    };

    //顶部菜单点击
    const topMenuClick = (item) => {
      menu.currentTopMenu = item;
      //移除tabs
      tag.delAll()
      //加载新菜单
      menu.getMenu({ perm: user.userInfo.permissions, userId: user.userInfo.id,pid: item.id})
    };
    //登录人点击
    const showLayer = ref(false);
    const change = () => {
      showLayer.value = !showLayer.value;
      showLayerTabs.value = false;
    };

    // 关闭标签
    const showLayerTabs = ref(false);
    const changeTabs = () => {
      showLayerTabs.value = !showLayerTabs.value;
    };
    //退出
    const loginout = () => {
      uni.request({
        service: "system",
        url: "/login/outLogin",
        success: (e) => {
          tag.delAll()
          user.quit();
          menu.deleteAllMenu();
          //跳转登录页
          uni.navigateTo({ url: config.loginPath });
          //清除路由信息(保留默认页面)
        },
      });
    };

    const clickFatherBox = () => {
      showLayer.value = false;
      showLayerTabs.value = false;
    };
    const showPwd=ref(false);
    const pwd=ref({old:'',news:''})
    const showPwdD=()=>{
      showPwd.value=true;
    }
    const  changePwd=()=>{
      uni.request({
        service:"system",
        url:"/sysUser/pwd",
        data:{ids:[store.getters.userInfo.id],pwd:pwd.value.news},
        success:(res)=>{
          if(res.code==200){
            uni.$msg({message:"修改成功"}).then(()=>{
              loginout();
            });
          }
        }
      })
    }
    return {
      tag,user,menu,
      changePwd,
      showPwdD,
      pwd,
      showPwd,
      ...tags(showLayerTabs),
      treeClick,
      change,
      loginout,
      config,
      showLayer,




      topMenuClick,

      changeTabs,
      showLayerTabs,
      clickFatherBox,

    };
  },
};
const tags = (showLayerTabs) => {
  const {tag} = store();
  //tab点击时间
  const tabClick = (item) => {
    uni.$router.push(item);
  };
  //tab 关闭
  const closeTag = (item) => {
    //关闭标签
    tag.delTag(item.path)
    //跳转前一个页面
    uni.$router.push(tag.currentTag);
  };
  //tab关闭其他
  const closeOthersTags = () => {
    tag.delOther()
    showLayerTabs.value = false;
  };
  //tab关闭全部
  const closeAllTags = () => {
    tag.delAll()
    showLayerTabs.value = false;
    //跳转默认页面
    uni.$router.push(config.tag)
  };
  return { tabClick, closeTag, closeOthersTags, closeAllTags };
};
</script>


<style scoped lang="scss">
@import "./base.scss";

::v-deep .baseTree .u-tree-active-out {
  border-left: 2px solid #c4d8ea;
  background: linear-gradient(
    to right bottom,
    rgb(38, 66, 103),
    rgb(34, 49, 66)
  );
}
::v-deep .edp-tabs-tag {
  // background-color: #fff;
  padding: 2px 0 2px 7px;
}
::v-deep .edp-tabs-tag .edp-tab {
  border: 1px solid rgb(211, 211, 211) !important;
  margin: auto 2px;
  padding: 8px 16px;
  min-width: auto;
}
::v-deep .edp-tabs-tag .edp-tab .edp-tabs-tag-close {
  padding-right: 0;
}

::v-deep .edp-tabs-tag .edp-tab.edp-tab-active {
  color: #fff !important;
  background-color: #409eff;
}
</style>
