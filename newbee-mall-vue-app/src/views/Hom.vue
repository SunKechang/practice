<template>
  <div class="home">
    <TopNavigator>
    </TopNavigator>
    <div class="top_container">
      <div class="search_and_cart">
        <div class="wide">
          <div class="logo" style="float: left"><img style="width: 100%" src="https://img10.360buyimg.com/img/jfs/t1/50166/9/22920/11310/63a1993cE3800216e/105f3d456f0905bb.jpg"></div>
          <el-input style="width: 546px;margin-top: 65px;margin-left: 222px;" placeholder="请输入内容" v-model="input3" class="input-with-select">
            <el-button style="background-color: #f58c0c;color: white;" slot="append"  icon="el-icon-search">搜索</el-button>
          </el-input>
          <el-button style="margin-left:10px;background-color: #ED6C3E;color: white;" slot="append"  icon="el-icon-goods">购物车</el-button>
          <div class="logo" style="float: right"><img style="width: 100%" src="https://img13.360buyimg.com/babel/jfs/t1/192532/18/31574/14301/63aeb38dF5c55c212/38245c8dbdee113c.jpg.avif"></div>
        </div>
      </div>
      <div class="sort_and_images">
        <div class="wide">
          <div class="sandi_aside">
            <el-menu :active-text-color="fff" :default-active="activeIndex" class="el-menu-demo" mode="vertical">
              <el-menu-item index="1">
                <el-popover
                  placement="right"
                  trigger="hover">
                  <span slot="reference">
                    家电<el-divider direction="vertical"></el-divider>数码<el-divider direction="vertical"></el-divider>手机<i class="el-icon-arrow-right"></i>
                  </span>
                  <el-menu :active-text-color="fff" :default-active="activeIndex" class="el-menu-demo" mode="vertical">
                    <el-menu-item index="1">生活电器</el-menu-item>
                    <el-menu-item index="2">wer</el-menu-item>
                    <el-menu-item index="3">厨房神器</el-menu-item>
                    <el-menu-item index="4">扫地机器人</el-menu-item>
                    <el-menu-item index="5">空气净化器</el-menu-item>
                    <el-menu-item index="6">吸尘器</el-menu-item>
                    <el-menu-item index="7">取暖器</el-menu-item>
                    <el-menu-item index="8">豆浆机</el-menu-item>
                    <el-menu-item index="9">暖风机</el-menu-item>
                    <el-menu-item index="10">加湿器</el-menu-item>
                    <el-menu-item index="11">蓝牙音箱</el-menu-item>
                    <el-menu-item index="12">烤箱</el-menu-item>
                    <el-menu-item index="13">卷发器</el-menu-item>
                  </el-menu>
                </el-popover>
              </el-menu-item>
              <el-menu-item index="2">
                <el-popover
                  placement="right"
                  trigger="hover">
                  <span slot="reference">
                    女装<el-divider direction="vertical"></el-divider>男装<el-divider direction="vertical"></el-divider>穿搭<i class="el-icon-arrow-right"></i>
                  </span>
                  <el-menu :active-text-color="fff" :default-active="activeIndex" class="el-menu-demo" mode="vertical">
                    <el-menu-item index="1">外套</el-menu-item>
                  </el-menu>
                </el-popover>
              </el-menu-item>
              <el-menu-item index="3">
                <el-popover
                  placement="right"
                  trigger="hover">
                  <span slot="reference">
                    运动<el-divider direction="vertical"></el-divider>户外<el-divider direction="vertical"></el-divider>乐器<i class="el-icon-arrow-right"></i>
                  </span>
                  <el-menu :active-text-color="fff" :default-active="activeIndex" class="el-menu-demo" mode="vertical">
                    <el-menu-item index="1">跑鞋</el-menu-item>
                  </el-menu>
                </el-popover>
              </el-menu-item>
              <el-menu-item index="4">
                <el-popover
                  placement="right"
                  trigger="hover">
                  <span slot="reference">
                    家具<el-divider direction="vertical"></el-divider>家饰<el-divider direction="vertical"></el-divider>家纺<i class="el-icon-arrow-right"></i>
                  </span>
                  <el-menu :active-text-color="fff" :default-active="activeIndex" class="el-menu-demo" mode="vertical">
                    <el-menu-item index="1">沙发</el-menu-item>
                  </el-menu>
                </el-popover>
              </el-menu-item>
              <el-menu-item index="5">
                <el-popover
                  placement="right"
                  trigger="hover">
                  <span slot="reference">
                    游戏<el-divider direction="vertical"></el-divider>动漫<el-divider direction="vertical"></el-divider>影视<i class="el-icon-arrow-right"></i>
                  </span>
                  <el-menu :active-text-color="fff" :default-active="activeIndex" class="el-menu-demo" mode="vertical">
                    <el-menu-item index="1">LOL</el-menu-item>
                  </el-menu>
                </el-popover>
              </el-menu-item>
              <el-menu-item index="6">
                <el-popover
                  placement="right"
                  trigger="hover">
                  <span slot="reference">
                    美妆<el-divider direction="vertical"></el-divider>清洁<el-divider direction="vertical"></el-divider>宠物<i class="el-icon-arrow-right"></i>
                  </span>
                  <el-menu :active-text-color="fff" :default-active="activeIndex" class="el-menu-demo" mode="vertical">
                    <el-menu-item index="1">口红</el-menu-item>
                    <el-menu-item index="2">睫毛膏</el-menu-item>
                    <el-menu-item index="3">腮红</el-menu-item>
                    <el-menu-item index="4">粉底</el-menu-item>
                    <el-menu-item index="5">隔离霜</el-menu-item>
                    <el-menu-item index="6">美白</el-menu-item>
                    <el-menu-item index="7">气垫</el-menu-item>
                  </el-menu>
                </el-popover>
              </el-menu-item>
              <el-menu-item index="7">
                <el-popover
                  placement="right"
                  trigger="hover">
                  <span slot="reference">
                    工具<el-divider direction="vertical"></el-divider>装修<el-divider direction="vertical"></el-divider>建材<i class="el-icon-arrow-right"></i>
                  </span>
                  <el-menu :active-text-color="fff" :default-active="activeIndex" class="el-menu-demo" mode="vertical">
                    <el-menu-item index="1">转换器</el-menu-item>
                  </el-menu>
                </el-popover>
              </el-menu-item>
              <el-menu-item index="8">
                <el-popover
                  placement="right"
                  trigger="hover">
                  <span slot="reference">
                    鞋靴<el-divider direction="vertical"></el-divider>箱包<el-divider direction="vertical"></el-divider>配件<i class="el-icon-arrow-right"></i>
                  </span>
                  <el-menu :active-text-color="fff" :default-active="activeIndex" class="el-menu-demo" mode="vertical">
                    <el-menu-item index="1">休闲鞋</el-menu-item>
                  </el-menu>
                </el-popover>
              </el-menu-item>
              <el-menu-item index="9">
                <el-popover
                  placement="right"
                  trigger="hover">
                  <span slot="reference">
                    玩具<el-divider direction="vertical"></el-divider>孕产<el-divider direction="vertical"></el-divider>用品<i class="el-icon-arrow-right"></i>
                  </span>
                  <el-menu :active-text-color="fff" :default-active="activeIndex" class="el-menu-demo" mode="vertical">
                    <el-menu-item index="1">机器人</el-menu-item>
                  </el-menu>
                </el-popover>
              </el-menu-item>
            </el-menu>
          </div>
          <div class="sandi_images">
            <el-carousel height="684px" :interval="5000" arrow="always">
              <el-carousel-item height="684px"  v-for="item in swiperList" :key="item">
              <el-link :href="item.redirectUrl"><el-image style="height: 684px;width: 1404px" fit="fill" :src="item.carouselUrl"></el-image></el-link>
              </el-carousel-item>
            </el-carousel>
          </div>
        </div>
      </div>
      <div class="contents"></div>
    </div>
  </div>
</template>

<script>

import TopNavigator from '@/components/TopNavigator'
import {getLocal} from "@/common/js/utils";
import {Toast} from "vant";
import {getHome} from "@/service/home";

export default {
  name: 'Hom',
  components: { TopNavigator },
  data(){
    return{
      swiperList: [],
    }
  },
  async mounted() {
    const token = getLocal('token')
    if (token) {
      this.isLogin = true
    }
    window.addEventListener('scroll', this.pageScroll)
    Toast.loading({
      message: '加载中...',
      forbidClick: true
    });
    const { data } = await getHome()
    console.log(data)
    this.swiperList = data.carousels
    console.log(this.swiperList)
    Toast.clear()
  },
}
</script>
<style scoped>
.home{
  background-color: #f4f4f4;
}
.el-header, .el-footer {
  background-color: #B3C0D1;
  color: #333;
  text-align: center;
  line-height: 60px;
}

.el-aside {
  background-color: #D3DCE6;
  color: #333;
  text-align: center;
  line-height: 200px;
}

.el-main {
  background-color: #E9EEF3;
  color: #333;
  text-align: center;
  line-height: 160px;
}

body > .el-container {
  margin-bottom: 40px;
}
.wide{
  margin: auto;
  width: 1190px;
}
.search_and_cart{
  background: #fff;
  border-bottom: 1px solid #ddd;
  margin-bottom: 10px;
}
.search_and_cart .wide{
  position: relative;
  z-index: 11;
  height: 140px;
}
.logo{
  overflow: hidden;
  display: flex;
  align-items: center;
  margin: 10px 0px 0px 0px;
  width: 190px;
  height: 120px;
}
.sort_and_images{
  height: 485px;
  background-color: #f4f4f4;
  padding-top: 10px;
  padding-bottom: 10px;
}
.sandi_aside{
  position: relative;
  float: left;
  height: 475px;
  width: 190px;
  margin-right: 15px;
  background-color: #fff;
}
.sandi_aside .el-menu-item{
  height: 53px;
}
.sandi_images{
  float: left;
  height: 475px;
  width: 975px;
  background-color: #fff;
}
.el-carousel__item h3 {
  color: #475669;
  font-size: 18px;
  opacity: 0.75;
  line-height: 300px;
  margin: 0;
}

.el-carousel__item:nth-child(2n) {
  background-color: #99a9bf;
}

.el-carousel__item:nth-child(2n+1) {
  background-color: #d3dce6;
}
</style>
