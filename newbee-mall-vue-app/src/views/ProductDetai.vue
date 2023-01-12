<template>
  <div class="product">
    <TopNavigator>
    </TopNavigator>
    <div class="top_container" >
      <div class="search_and_cart">
        <div class="wide">
          <div class="logo" style="float: left"><img style="width: 100%" src="https://img10.360buyimg.com/img/jfs/t1/50166/9/22920/11310/63a1993cE3800216e/105f3d456f0905bb.jpg"></div>
          <div style="float:left;width: 680px;margin-top: 9px;margin-left: 92px;"><el-menu :active-text-color="fff" :default-active="activeIndex" class="el-menu-demo" mode="horizontal">
            <el-menu-item index="1">
              <el-popover
                trigger="hover">
                  <span slot="reference">
                    家电<el-divider direction="vertical"></el-divider>数码<el-divider direction="vertical"></el-divider>手机
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

                trigger="hover">
                  <span slot="reference">
                    女装<el-divider direction="vertical"></el-divider>男装<el-divider direction="vertical"></el-divider>穿搭
                  </span>
                <el-menu :active-text-color="fff" :default-active="activeIndex" class="el-menu-demo" mode="vertical">
                  <el-menu-item index="1">外套</el-menu-item>
                </el-menu>
              </el-popover>
            </el-menu-item>
            <el-menu-item index="3">
              <el-popover

                trigger="hover">
                  <span slot="reference">
                    运动<el-divider direction="vertical"></el-divider>户外<el-divider direction="vertical"></el-divider>乐器
                  </span>
                <el-menu :active-text-color="fff" :default-active="activeIndex" class="el-menu-demo" mode="vertical">
                  <el-menu-item index="1">跑鞋</el-menu-item>
                </el-menu>
              </el-popover>
            </el-menu-item>
            <el-menu-item index="4">
              <el-popover

                trigger="hover">
                  <span slot="reference">
                    家具<el-divider direction="vertical"></el-divider>家饰<el-divider direction="vertical"></el-divider>家纺
                  </span>
                <el-menu :active-text-color="fff" :default-active="activeIndex" class="el-menu-demo" mode="vertical">
                  <el-menu-item index="1">沙发</el-menu-item>
                </el-menu>
              </el-popover>
            </el-menu-item>
            <el-menu-item index="5">
              <el-popover

                trigger="hover">
                  <span slot="reference">
                    游戏<el-divider direction="vertical"></el-divider>动漫<el-divider direction="vertical"></el-divider>影视
                  </span>
                <el-menu :active-text-color="fff" :default-active="activeIndex" class="el-menu-demo" mode="vertical">
                  <el-menu-item index="1">LOL</el-menu-item>
                </el-menu>
              </el-popover>
            </el-menu-item>
            <el-menu-item index="6">
              <el-popover

                trigger="hover">
                  <span slot="reference">
                    美妆<el-divider direction="vertical"></el-divider>清洁<el-divider direction="vertical"></el-divider>宠物
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

                trigger="hover">
                  <span slot="reference">
                    工具<el-divider direction="vertical"></el-divider>装修<el-divider direction="vertical"></el-divider>建材
                  </span>
                <el-menu :active-text-color="fff" :default-active="activeIndex" class="el-menu-demo" mode="vertical">
                  <el-menu-item index="1">转换器</el-menu-item>
                </el-menu>
              </el-popover>
            </el-menu-item>
            <el-menu-item index="8">
              <el-popover

                trigger="hover">
                  <span slot="reference">
                    鞋靴<el-divider direction="vertical"></el-divider>箱包<el-divider direction="vertical"></el-divider>配件
                  </span>
                <el-menu :active-text-color="fff" :default-active="activeIndex" class="el-menu-demo" mode="vertical">
                  <el-menu-item index="1">休闲鞋</el-menu-item>
                </el-menu>
              </el-popover>
            </el-menu-item>
          </el-menu></div>
          <div class="logo" style="float: right"><img style="width: 100%" src="https://img13.360buyimg.com/babel/jfs/t1/192532/18/31574/14301/63aeb38dF5c55c212/38245c8dbdee113c.jpg.avif"></div>
        </div>
      </div>
      <div class="top_name" >
        <div class="wide">
          <h2>{{this.detail.goodsName}}</h2>
        </div>
      </div>
        <div class="wide" style="background-color: white">
          <div class="left_top">
            <el-carousel height="570px">
              <el-carousel-item v-for="(item, index) in detail.goodsCarouselList" :key="index">
                <img width="570px" :src="prefix(item)" alt="">
              </el-carousel-item>
            </el-carousel>
          </div>
          <div class="right_top">
            <el-descriptions border :column="1" :title="this.detail.goodsIntro">
              <el-descriptions-item label="现价" ><p style="color: red;font-size: 15px">¥{{this.detail.sellingPrice}}</p></el-descriptions-item>
              <el-descriptions-item label="原价"><p><del>¥{{this.detail.originalPrice}}</del></p></el-descriptions-item>
              <el-descriptions-item label="标签"><p><el-tag>{{ this.detail.tag }}</el-tag></p></el-descriptions-item>
              <el-descriptions-item label="选择数量"><p><el-input-number v-model="num" :min="1" ></el-input-number></p></el-descriptions-item>
              <el-descriptions-item label="直接购买"><p><el-button type="primary">直接购买</el-button></p></el-descriptions-item>
              <el-descriptions-item label="加入购物车"><p><el-button type="primary">加入购物车</el-button></p></el-descriptions-item>
            </el-descriptions>
          </div>
          <div class="wide" v-if="detail.goodsDetailContent!=''" style="float:left;margin-top:15px;margin-bottom: 15px" ><el-divider style="background-color:#f4f4f4;"><h3 style="font-size: 25px">商品详情</h3></el-divider></div>
          <div style="width: 1190px" class="product-intro">
            <div  class="product-content" v-html="detail.goodsDetailContent"></div>
          </div>
          <div>
            <img width="1190px" src="https://cdn.cnbj1.fds.api.mi-img.com/mi-mall/43e2954feb6d1b108438481f1d5b0bd3.png">
          </div>
        </div>
    </div>
  </div>

</template>

<script>
import TopNavigator from "@/components/TopNavigator";
import {getDetail} from "@/service/good";

export default {
  name: "ProductDetai",
  components: { TopNavigator },
  data(){
    return{
      num: 1,
      detail: {
        goodsCarouselList: []
      }
    }
  },
  async mounted() {
    const { id } = this.$router.currentRoute.query
    const { data } = await getDetail(id)
    console.log(data)
    this.detail = data
  },

}
</script>

<style scoped>
.product{
  background-color: #f4f4f4;
  overflow: scroll;
}
.wide{
  margin: auto;
  width: 1190px;
}
.search_and_cart{
  background: #fff;
  border-bottom: 1px solid #ddd;
}
.search_and_cart .wide{
  position: relative;
  z-index: 11;
  height: 140px;
}
.top_name{
  background: #fff;
  margin-bottom:1px;
  border-bottom: 1px solid #e0e0e0;
  box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04);
  height: 63px;
  width: 100%;
  margin-top: 0;
  color: #616161;
}
.top_name .wide h2{
  font-size: 18px;
  font-weight: 400;
  line-height: 34px;
  color: #424242;
  float: left;
}
.logo{
  overflow: hidden;
  display: flex;
  align-items: center;
  margin: 10px 0px 0px 0px;
  width: 190px;
  height: 120px;
}
.el-menu.el-menu--horizontal {
  border-bottom: none;
}
.left_top{
  float: left;
  width: 590px;
  height: 565px;
  padding-left: 5px;
  padding-top: 10px;
}
.right_top{
  float: left;
  width: 590px;
  height: 565px;
  padding-right: 5px;
  padding-top: 10px;
}
::v-deep .product-content p{
  width: 1190px;
}
::v-deep .product-content img{
  width: 1190px;
}
.el-divider__text{
  background-color: rgba(0,0,0,0);
}
</style>
