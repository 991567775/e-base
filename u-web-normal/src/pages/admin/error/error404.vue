<template>
        <view>
            <!-- <el-card class="box-card"> -->
                <view class="title">404</view>
                <view class="message">YOU&nbsp;&nbsp;PAGE&nbsp;&nbsp;LOST</view>
                <view class="btn-con">
                    <!-- <el-button @click="goHome" size="large" style="width: 200px;" type="text">返回首页</el-button> -->
                    <!-- <el-button @click="backPage" size="large" style="width: 200px;margin-left: 40px;" type="primary">返回上一页</el-button> -->
                </view>
            <!-- </el-card> -->
        </view>
</template>

<script>
    export default {
		setup(){
			// backPage() {
			//     this.$router.go(-1);
			// },
			// goHome() {
			//     this.$router.push({
			//         path: "/base/index"
			//     });
			// }
		}
    }
</script>
<style lang="scss" scoped>
    @keyframes error404animation {
        0% {
            transform: rotateZ(0deg);
        }
        20% {
            transform: rotateZ(-60deg);
        }
        40% {
            transform: rotateZ(-10deg);
        }
        60% {
            transform: rotateZ(50deg);
        }
        80% {
            transform: rotateZ(-20deg);
        }
        100% {
            transform: rotateZ(0deg);
        }
    }
    .el-card{
        height: 100% !important;
    }
    .title {
        text-align: center;
        font-size: 240px;
        font-weight: 700;
        color: #2d8cf0;
        height: 260px;
        line-height: 260px;
        span {
            display: inline-block;
            color: #19be6b;
            font-size: 230px;
            animation: error404animation 3s ease 0s infinite alternate;
        }
    }
    .message {
        display: block;
        text-align: center;
        font-size: 30px;
        font-weight: 500;
        letter-spacing: 12px;
        color: #dddde2;
    }
    .btn-con {
        text-align: center;
        padding: 20px 0;
        margin-bottom: 40px;
    }
</style>
